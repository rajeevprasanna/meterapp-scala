import com.grizzalan.meterapp._
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.session.Database
import com.mongodb.casbah.Imports._
import org.scalatra._
import scalate.ScalateSupport
import javax.servlet.ServletContext
import _root_.akka.actor.ActorSystem
import org.scalatra.json._
import org.json4s._
import JsonDSL._
import jackson.JsonMethods._
import scala.concurrent.duration._
import org.slf4j.{Logger, LoggerFactory}
import org.scalatra.atmosphere._


class ScalatraBootstrap extends LifeCycle {

	val logger = LoggerFactory.getLogger(getClass)

	// Set up the mongo db client
	val mongoClient = MongoClient("localhost", 27017)

	// Use the Akka scheduling system to run two jobs. The first is to broadcast
	// the meter information to atmosphere-attached clients. The second is to 
	// independently update meter values based upon meter settings (normally would
	// be done in a separate process, but this is just a demo).
	val system = ActorSystem()
	import system.dispatcher

	val cancellableBroadcast = system.scheduler.schedule(1.seconds, 1.seconds) {
		val mongoColl = mongoClient("meterapp")("meter")
		val allMeters = mongoColl.find()

		import scala.collection.mutable.ArrayBuffer
		val meters = ArrayBuffer[Meter]()

		allMeters foreach { m => 
			meters += Meter(m.as[String]("name"), 
							m.as[Double]("value"), 
							m.as[String]("setting"))
		}

		val jsonObject = ("list" -> meters.toList.map { m =>
				(("name" -> m.name) ~ ("value" -> m.value) ~ ("setting" -> m.setting))
			});
		AtmosphereClient.broadcast("/atm-meters", JsonMessage(jsonObject))
	}

	val cancellableMeterDelta = system.scheduler.schedule(1.seconds, 1.seconds) {
		val mongoColl = mongoClient("meterapp")("meter")
		val allMeters = mongoColl.find()

		allMeters foreach { m => 
			val meter = Meter(m.as[String]("name"), 
							m.as[Double]("value"), 
							m.as[String]("setting"))
			val newValue = 
				if (meter.setting == "increase") {
					if (meter.value + 1.0 < 100.0) {
						meter.value + 1.0
					} else {
						100.0
					}
				} else if (meter.setting == "decrease") {
					if (meter.value - 1.0 > 0.0) {
						meter.value - 1.0
					} else {
						0.0
					}
				} else { // should be meter.setting == "hold"
					meter.value
				}
			val query = MongoDBObject("name" -> meter.name)
			val update = $set("value" -> newValue)
			val result = mongoColl.update( query, update)
		}
	}

	// Set up the sql db and data source pool
	val cpds = new ComboPooledDataSource
	logger.info("Created c3p0 connection pool")


	override def init(context: ServletContext) {
		val db = Database.forDataSource(cpds)  // create a Database which uses the DataSource
		context.mount(new MeterappServlet(db, mongoClient), "/*")
	}


	private def closeDbConnection() {
		logger.info("Closing c3po connection pool")
		cpds.close
	}


	override def destroy(context: ServletContext) {
		super.destroy(context)
		// Close the db connection and stop the jobs
		closeDbConnection
		cancellableBroadcast.cancel()
		cancellableMeterDelta.cancel()
	}

}

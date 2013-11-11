package com.grizzalan.meterapp

import org.slf4j.{Logger, LoggerFactory}
import org.scalatra._
import scalate.ScalateSupport
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import com.mongodb.casbah.Imports._
import org.json4s._
import org.scalatra.json._
import org.scalatra.atmosphere._
import JsonDSL._
import java.util.Date
import java.text.SimpleDateFormat
import scala.concurrent._
import com.mongodb.casbah.Imports._
import ExecutionContext.Implicits.global


class MeterappServlet(db: Database, mongoClient: MongoClient) extends MeterappscalaStack 
		with JValueResult with JacksonJsonSupport with AtmosphereSupport {

	val logger =  LoggerFactory.getLogger(getClass)

	// Sets up automatic case class to JSON output serialization, required by
	// the JValueResult trait.
	protected implicit val jsonFormats: Formats = DefaultFormats

	// Before every action runs, set the content type to be in JSON format.
	before() {
	  contentType = formats("json")
	}

	// Handles atmosphere/comet messaging
	atmosphere("/atm-meters") {
		new AtmosphereClient {
			def receive = {
				case Connected => {
					//logger.info("Client %s is connected" format uuid)
					broadcast(JsonMessage(("newUser" -> uuid): JValue), Everyone)
				}
				case Disconnected(disconnector, Some(error)) => {
					//logger.info("Client %s is disconnected" format uuid)
				}
				case Error(Some(error)) => {
					logger.error("Error: " + error)
				}
				case TextMessage(text) => {
					//logger.info("Text received by atmosphere server")
				}
				case JsonMessage(json) => {
					//logger.info("Json received by atmosphere server")
					broadcast(json, Everyone)
				}
			}
		}
	}

	// get index page
	get("/") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/index.jade")
	}
	
	// get admin page
	get("/admin") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/admin.jade")
	}
	
	// get page with all the meter info
	get("/main") {
		contentType="text/html"
		layoutTemplate("/WEB-INF/templates/views/meters.jade")
	}
	
	// get meter groupings for a specific page
	get("/page/:pageid") {

		db withSession {
			contentType="application/json"
			val pageName = params("pageid")

			val pageQuery = "select g.name as groupingName, m.name as meterName " +
				"from page p, grouping g, meter m, page_grouping pg, grouping_meter gm " +
				"where p.id = pg.pageid and g.id = pg.groupingid  and g.id = gm.groupingid " +
				"and m.id = gm.meterid " + "and p.name = '" + pageName + "' order by g.name, m.name";

			implicit val getGroupingMeterResult = GetResult(r => GroupingMeter(r.nextString, r.nextString))
			val q1 = Q.queryNA[GroupingMeter](pageQuery).list


			import scala.collection.mutable.ArrayBuffer
			val groupingNames = ArrayBuffer[String]()
			val meterNames = scala.collection.mutable.Map[String, ArrayBuffer[String]]()

			q1 foreach { qm => 
				val groupingName = qm.groupingName
				val meterName = qm.meterName
				if (!groupingNames.contains(groupingName)) {
					groupingNames += groupingName
					meterNames.put(groupingName, ArrayBuffer[String]())
				}
				val existingMeters = meterNames.getOrElse(groupingName, ArrayBuffer[String]())
				if (!existingMeters.contains(meterName)) {
					existingMeters += meterName
					meterNames.put(groupingName, existingMeters)
				}
			}

			val groupings = ArrayBuffer[Grouping]()
			groupingNames foreach { gn =>
				groupings += Grouping(gn, meterNames.getOrElse(gn, ArrayBuffer[String]()).toVector)
			}

			Page(pageName, groupings.toVector)

		}

	}

	// get all meter information
	get("/meters") {
		val mongoColl = mongoClient("meterapp")("meter")
		val allMeters = mongoColl.find()

		import scala.collection.mutable.ArrayBuffer
		val meters = ArrayBuffer[Meter]()

		allMeters foreach { m => 
			logger.info("meter: " + m)
			meters += Meter(m.as[String]("name"), 
							m.as[Double]("value"), 
							m.as[String]("setting"))
		}

		Map("list" -> meters.toVector)

	}

	// put a new setting for a meter
	put("/meter/:meterName") {
		val meterName = params("meterName")
		val newSetting = parsedBody.extract[MeterSetting].setting
		logger.info("Received put request for " + meterName + "; new setting: " + newSetting)

		val mongoColl = mongoClient("meterapp")("meter")
		val query = MongoDBObject("name" -> meterName)
		val update = $set("setting" -> newSetting)
		val result = mongoColl.update( query, update)

		Map("msg" -> "Put A-ok")
	}

	error {
	  case t: Throwable => t.printStackTrace()
	}

}


// The data model. Would usually go in its own package and classes, but this is a demo.
case class GroupingMeter(groupingName: String, meterName: String)
case class Grouping(name: String, meters: Vector[String])
case class Page(name: String, groupings: Vector[Grouping])
case class Meter(name: String, value: Double, setting: String)
case class MeterSetting(setting: String)

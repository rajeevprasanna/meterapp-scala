
// Get a connection to the database
var conn = new Mongo();
var db = conn.getDB("meterapp");

// Clear the meter collection
if (db.meter) {
	db.meter.remove();
}

// Insert meters
var meter01 = {
	"name": "Meter 01",
	"value": 50.0,
	"setting": "hold"
};
var meter02 = {
	"name": "Meter 02",
	"value": 50.0,
	"setting": "hold"
};
var meter03 = {
	"name": "Meter 03",
	"value": 50.0,
	"setting": "hold"
};
var meter04 = {
	"name": "Meter 04",
	"value": 50.0,
	"setting": "hold"
};
var meter05 = {
	"name": "Meter 05",
	"value": 50.0,
	"setting": "hold"
};
var meter06 = {
	"name": "Meter 06",
	"value": 50.0,
	"setting": "hold"
};
var meter07 = {
	"name": "Meter 07",
	"value": 50.0,
	"setting": "hold"
};
var meter08 = {
	"name": "Meter 08",
	"value": 50.0,
	"setting": "hold"
};
var meter09 = {
	"name": "Meter 09",
	"value": 50.0,
	"setting": "hold"
};
var meter10 = {
	"name": "Meter 10",
	"value": 50.0,
	"setting": "hold"
};

db.meter.insert(meter01);
db.meter.insert(meter02);
db.meter.insert(meter03);
db.meter.insert(meter04);
db.meter.insert(meter05);
db.meter.insert(meter06);
db.meter.insert(meter07);
db.meter.insert(meter08);
db.meter.insert(meter09);
db.meter.insert(meter10);

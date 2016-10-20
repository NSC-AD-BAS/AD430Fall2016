// our very first node program
var express = require("express");
var mysql = require("mysql");
var app = express();

app.get('/listConvo', function (req, res) {
	getAllConvos(function(data) {
		res.setHeader('Content-Type', 'application/json');
		res.json( data );
	});
})


app.get('/createConvo', function (req, res) {
	var hohUserId = req.query.hohUserId;
	var interpreterUserId = req.query.interpreterUserId;

	createConvo(hohUserId, interpreterUserId, function(data) {
		res.setHeader('Content-Type', 'application/json');
		res.json( data );
	});
})

var server = app.listen(8081, function () {
   var host = server.address().address
   var port = server.address().port

   console.log("Example app listening at http://%s:%s", host, port)
})


//Replace with relevant creds
function getConnection() {
	var con = mysql.createConnection({
		host: "localhost",
		user: "root",
		password: "dou6jKtZ2b8AmAHUtv6T",
		database: "ad430_db"
	});
	return con;
}


function createConvo(hohUserId, interpreterUserId, callback) {
	//Check your input is not null
	if(hohUserId == undefined)
	{
		callback({ "success": false, "message": "hohUserId was not supplied, but is required" });
		return;
	}
	if(interpreterUserId == undefined)
	{
		callback({ "success": false, "message": "interpreterUserId was not supplied, but is required" });
		return;
	}

	//Check your input is valid with the DB

	var result;
	var con = getConnection();
	con.connect();
	con.query('SELECT * FROM convo',function(err,rows){
		  if (err) throw err;


		con.end();
		console.log("to Preform Callback");
		callback(rows);
		return;
	});
}

function getAllConvos(callback) {
	console.log("getAllConvos Invoied");
	var result;
	var con = getConnection();
	con.connect();
	con.query('SELECT * FROM convo',function(err,rows){
		  if (err) throw err;


		con.end();
		console.log("to Preform Callback");
		callback(rows);
	});
}

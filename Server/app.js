
/**
 * Module dependencies.
 */
 // Grant's code
// Here sits the code to return data when a call is made to the endpoint
var express = require('express')
  , routes = require('./routes');

  var mysql = require("mysql");

var app = module.exports = express.createServer();

var testArray = ["Hello Friend"];

// Configuration

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

app.configure('production', function(){
  app.use(express.errorHandler());
});

// Routes

app.get('/', function (req, res) {
  // This line here is responsible for calling the next block of code
  res.json(queryDB());
});

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

app.get('/pingConvoHOH', function (req, res) {
	var hohUserId = req.query.hohUserId;
	var ConvoId = req.query.ConvoId;
	
	updateConvoHOH(hohUserId, ConvoId, function(data) {
		res.setHeader('Content-Type', 'application/json');
		res.json( data );
	});
})

app.get('/pingConvoInterpreter', function (req, res) {
	var interpreterUserId = req.query.interpreterUserId;
	var ConvoId = req.query.ConvoId;
	
	updateConvoInterpreter(interpreterUserId, ConvoId, function(data) {
		res.setHeader('Content-Type', 'application/json');
		res.json( data );
	});
})

app.get('/endConvo', function (req, res) {
	var ConvoId = req.query.ConvoId;
	
	endConvo(ConvoId, function(data) {
		res.setHeader('Content-Type', 'application/json');
		res.json( data );
	});
})

app.listen(80, function(){
  console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);
});


//Replace with relevant creds
function getConnection() {
		var con = mysql.createConnection({
  		host: "localhost",
  		user: "root",
  		password: "root",
  		database: "ad430_db"
	});
	return con;
}


// Austin's code
function queryDB() {
	var con = getConnection();

	con.connect(function(err){
  		if(err){
    			console.log('Error connecting to Db');
    			return;
  		}
  		console.log('Connection established');
	});

	con.query('SELECT * FROM user', function(err,rows){
  		if(err) throw err;

  		// Tim's code
  		var objs = { 
			people: [] 
		};
  		for (var i = 0; i < rows.length; i++) {
      			objs.people.push({ id: rows[i].user_id });
  		}
  		var result = JSON.stringify(objs);
  		console.log(result);
  	
		// parse the JSON back into readable data
  		var json = JSON.parse(result);
  		console.log(json.people[0].name);
  		//console.log('Data received from Db:\n');
  		//console.log(rows);
	});
	
	con.end(function(err) {
  	// The connection is terminated gracefully
  	// Ensures all previously enqueued queries are still
  	// before sending a COM_QUIT packet to the MySQL server.
	});
	return json;
}

//Update the convo to be over according to the db
function endConvo(ConvoId, callback)
{ 
	if(ConvoId == undefined)
	{
		callback({ "success": false, "message": "ConvoId was not supplied, but is required" });
		return;
	}

	//Get and start SQL Connection
	var con = getConnection();
	con.connect();

	//Check your input is valid with the DB
	con.query('SELECT COUNT(*) AS isGood FROM convo WHERE convo_id = ? ', ConvoId , function(err,rows){
		
		//Check interpreter user id is valid
		if(rows[0].isGood == 0)
		{
			con.end();
			callback({ "success": false, "message": "Given ConvoId cannot be found." });
			return;
		} else {
			con.query('UPDATE convo SET end_time = NOW() WHERE convo_id = ?', ConvoId, function(err,res){
			if(err) {
				callback({ "success": false, "message": "something went wrong in the db." });
			}
				console.log(err);
				console.log(res);
				callback({ "success": true, "convo_id": ConvoId });
				return;
			});
		}
	});
}



//Update the last ping time for an convo with the interpreter User
function updateConvoInterpreter(interpreterUserId, ConvoId, callback)
{ 
	//Check your input is not null
	if(interpreterUserId == undefined)
	{
		callback({ "success": false, "message": "interpreterUserId was not supplied, but is required" });
		return;
	}
	if(ConvoId == undefined)
	{
		callback({ "success": false, "message": "ConvoId was not supplied, but is required" });
		return;
	}

	//Get and start SQL Connection
	var con = getConnection();
	con.connect();

	//Check your input is valid with the DB
	con.query('SELECT COUNT(*) AS isGood FROM user WHERE user_id = ? AND is_interpreter = 1', interpreterUserId, function(err,rows){
		//Check interpreterUserId user id is valid
		if(rows[0].isGood == 0)
		{
			con.end();
			callback({ "success": false, "message": "Given interpreterUserId cannot be found or is not a interpreter user." });
			return;
		} else {
			con.query('SELECT COUNT(*) AS isGood FROM convo WHERE convo_id = ? AND interpreter_user_id = ?', [ConvoId, interpreterUserId] , function(err,rows){
							
				//Check interpreter user id is valid
				if(rows[0].isGood == 0)
				{
					con.end();
					callback({ "success": false, "message": "Given ConvoId cannot be found or did not belong to the given interpreter user." });
					return;
				} else {
					con.query('UPDATE convo SET last_updated_hoh = NOW() WHERE convo_id = ?', ConvoId, function(err,res){
						if(err) {
							callback({ "success": false, "message": "something went wrong in the db." });
						}
						console.log(err);
						console.log(res);
						callback({ "success": true, "convo_id": ConvoId });
						return;
					});
				}
			});
		}
	});
}



//Update the last ping time for an convo with the HOH User
function updateConvoHOH(hohUserId, ConvoId, callback)
{ 
	//Check your input is not null
	if(hohUserId == undefined)
	{
		callback({ "success": false, "message": "hohUserId was not supplied, but is required" });
		return;
	}
	if(ConvoId == undefined)
	{
		callback({ "success": false, "message": "ConvoId was not supplied, but is required" });
		return;
	}

	//Get and start SQL Connection
	var con = getConnection();
	con.connect();

	//Check your input is valid with the DB
	con.query('SELECT COUNT(*) AS isGood FROM user WHERE user_id = ? AND is_interpreter = 0', hohUserId, function(err,rows){
		//Check Hoh user id is valid
		if(rows[0].isGood == 0)
		{
			con.end();
			callback({ "success": false, "message": "Given hohUserId cannot be found or is not a hoh user." });
			return;
		} else {
			con.query('SELECT COUNT(*) AS isGood FROM convo WHERE convo_id = ? AND hoh_user_id = ?', [ConvoId, hohUserId] , function(err,rows){
							
				//Check interpreter user id is valid
				if(rows[0].isGood == 0)
				{
					con.end();
					callback({ "success": false, "message": "Given ConvoId cannot be found or did not belong to the given HOH user." });
					return;
				} else {
					con.query('UPDATE convo SET last_updated_hoh = NOW() WHERE convo_id = ?', ConvoId, function(err,res){
						if(err) {
							callback({ "success": false, "message": "something went wrong in the db." });
						}
						console.log(err);
						console.log(res);
						callback({ "success": true, "convo_id": ConvoId });
						return;
					});
				}
			});
		}
	});
}

//Creates a new convo and checks the input is valid
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
	
	//Get and start SQL Connection
	var con = getConnection();
	con.connect();
	
	//Check your input is valid with the DB
	con.query('SELECT COUNT(*) AS isGood FROM user WHERE user_id = ? AND is_interpreter = 0',hohUserId ,function(err,rows){

		//Check Hoh user id is valid
		if(rows[0].isGood == 0)
		{
			con.end();
			callback({ "success": false, "message": "Given hohUserId cannot be found or is not a hoh user." });
			return;
		} else {
			con.query('SELECT COUNT(*) AS isGood FROM user WHERE user_id = ? AND is_interpreter = 1',interpreterUserId ,function(err,rows){
				
				//Check interpreter user id is valid
				if(rows[0].isGood == 0)
				{
					con.end();
					callback({ "success": false, "message": "Given interpreterUserId cannot be found or is not a interpreter user." });
					return;
				} else {
					//Insert in the new convo
					var convo = { 
						start_time: moment(new Date()).format("YYYY-MM-DD HH:mm:ss"),
						hoh_user_id: hohUserId,
						interpreter_user_id: interpreterUserId,
						last_updated_hoh: moment(new Date()).format("YYYY-MM-DD HH:mm:ss"),
						last_updated_interpreter: moment(new Date()).format("YYYY-MM-DD HH:mm:ss")
						};
					con.query('UPDATE convo SET  INTO convo SET ?', convo, function(err,res){
						console.log(err);
						console.log(res);
						callback({ "success": true, "convo_id": res.insertId });
						return;
					});
				}
			});
		}
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

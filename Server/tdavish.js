var mysql = require('mysql');var conn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'ad430_db'
});conn.connect(function(err) {
    if (err) {
        console.log('Database connection error');
        return;
    }
    console.log('Database connection established');
});

// var person1 = { id: 0, name: 'Tim', age: 21, address: 'Nowhere' };
// var person2 = { id: 1, name: 'Tim2', age: 212, address: 'Nowhere2' };
// conn.query(
//     'REPLACE INTO people SET ?',
//     person2,
//     function(err, res) {
//         if (err) throw err;
//
//         console.log('Last insert id:', res.insertId);
// });

conn.query('SELECT * FROM user', function(err, rows) {
    if (err) throw err;    // create a JSON object of "people"
    var objs = {
        people: []
    };
    for (var i = 0; i < rows.length; i++) {
        objs.people.push({ id: rows[i].user_id });
    }
    var result = JSON.stringify(objs);    console.log(result);    // parse the JSON back into readable data
    var json = JSON.parse(result);
    console.log(json.people[0].name);
});conn.end(function(err) {
    // The connection is terminated gracefully.
    // Ensures all previously enqueued queries are still
    // before sending a COM_QUIT packet to the MySQL server.
    console.log('Database connection terminated');
});

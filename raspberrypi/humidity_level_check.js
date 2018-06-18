var fs = require('fs');
var admin = require('firebase-admin');
var serviceAccount = require('./ubiq-f2dd6-firebase-adminsdk-ry3uk-5efc8e7c61.json');


var exec = require('child_process').exec;
var COMMAND = 'sudo python motor_move.py';

dir = '/sys/class/gpio/'
admin.initializeApp( {
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://ubiq-f2dd6.firebaseio.com/"
} );

var db = admin.database();
var ref = db.ref("tomato"); //room1要素への参照
ref.orderByChild("time").on("value", function(snapshot) {
   var hoge = Object.keys(snapshot.val()); //取得したデータからkeyの配列を取得
   var humidity = snapshot.val()[hoge[hoge.length-1]].humidity;
    console.log(humidity); //時系列順に書き込まれたデータの末尾の湿度を取得
    if (humidity < 30){
	console.log('乾いてるゾ')
	exec(COMMAND, function (error, stdout, stderr) {
		console.log('水をやったゾ');
   		if (error !== null) {
   			 console.log('exec error: ' + error);
    			return;
		}
	}
);}
}, 
function(errorObject) {
    console.log("The read failed: " + errorObject.code);
} );

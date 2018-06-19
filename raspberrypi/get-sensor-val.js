require('date-utils');
var admin = require('firebase-admin');
var serviceAccount = require('./ubiq-f2dd6-firebase-adminsdk-ry3uk-5efc8e7c61.json'); //認証情報jsonのパス
var raspi    = require('raspi');
var gpio     = require('raspi-gpio');

const HIGH   = gpio.HIGH;
const LOW    = gpio.LOW;
const Input  = gpio.DigitalInput;
const Output = gpio.DigitalOutput;

admin.initializeApp( {
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://ubiq-f2dd6.firebaseio.com/" //データベースのurl
} );

var initHandler = function() {

    var d_in  = new Output('GPIO10'); //SPI_MOSI
    var d_out = new Input('GPIO9');   //SPI_MISO
    var clk   = new Output('GPIO11'); //SPI_CSLK
    var cs    = new Output({ 'pin' : 'GPIO8', 'pullResistor' : gpio.PULL_UP });  //SPI_CE0
    var interval = 500; //インターバル（ミリ秒）

    var clock = function(count) {
        for(var i = 0; i < count; i++) {
            clk.write(HIGH);
            clk.write(LOW);
        }
    };

    //土壌湿度センサ値読み取り
    var readHumidity = function() {
        cs.write(LOW); //start

        var i;
        var ch  = 0; // 0 ch. (0~7)
        var cmd = ( ch | 0x18 ) << 3;
        for(i = 0; i < 5; i++) {
            d_in.write( ( cmd & 0x80 ) ? HIGH : LOW );
            cmd <<= 1;
            clock(1);
        }

        var val = 0;
        for(i = 0; i < 13; i++) { // one null bit and 12 ADC bits.
            val <<= 1;
            clock(1);
            if(d_out.read()) { // === 1
                val |= 0x1;
            }
        }

        //0~100にスケール、小数点第一位まで
        var humidity = Math.floor(val / 4096 * 100 * 10) / 10;

        cs.write(HIGH); //end

        //結果出力
        console.log('raw humidity value : ', val, ', humidity : ', humidity);

        return humidity;
    };

    //水位センサ値読み取り
    var readWater = function() {
        cs.write(LOW); //start

        var i;
        var ch  = 1; // 0 ch. (0~7)
        var cmd = ( ch | 0x18 ) << 3;
        for(i = 0; i < 5; i++) {
            d_in.write( ( cmd & 0x80 ) ? HIGH : LOW );
            cmd <<= 1;
            clock(1);
        }

        var val = 0;
        for(i = 0; i < 13; i++) { // one null bit and 12 ADC bits.
            val <<= 1;
            clock(1);
            if(d_out.read()) { // === 1
                val |= 0x1;
            }
        }

        /*
        水位センサの値は、満タンのとき約700、空のとき約2100の生値を吐くのでそれを0~100にスケール、整数
        */
        if(val > 2100) val = 2100;
        else if (val < 700) val = 700;

        var water = Math.floor((-val + 2100) / 1400 * 100);

        cs.write(HIGH); //end

        //結果出力
        console.log('raw water value : ', val, ', water : ', water);

        return water;
    };

    //湿度をfirebaseに書き込み
    var writeHumidity = function() {
        var db = admin.database();
        var ref = db.ref("tomato");

        var date = new Date();
        var time = date.toFormat("YYYYMMDDHH24MI"); //時刻取得

        ref.push().set( { //push().setで一意のkeyを自動で作ってその下に各要素を追加
            "time": time,
            "humidity": readHumidity()
        } );
    }

    //水の量をfirebaseに書き込み
    var writeWater = function() {
        var db = admin.database();
        var ref = db.ref("water");

        ref.update( { //push().setで一意のkeyを自動で作ってその下に各要素を追加
            "amount": readWater()
        } );
    }

    //一定時間ごとに実行
    setInterval(writeHumidity, interval);
    setInterval(writeWater, interval);

};

raspi.init(initHandler);

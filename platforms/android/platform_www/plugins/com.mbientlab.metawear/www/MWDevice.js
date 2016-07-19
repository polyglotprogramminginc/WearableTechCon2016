cordova.define("com.mbientlab.metawear.MWDevice", function(require, exports, module) {
/**
 *
 * Created by Lance Gleason of Polyglot Programming LLC. on 10/11/2015.
 * http://www.polyglotprogramminginc.com
 * https://github.com/lgleasain
 * Twitter: @lgleasain
 *
 */

var exec = require('cordova/exec');

module.exports.initialize = function(success, failure){
    console.log("MWDevice.js: initialize");
    exec(success,  failure,  "MWDevice","initialize",[]);
}

module.exports.connect = function(macAddress, success, failure){
    console.log("MWDevice.js: connect");
    exec(success, failure, "MWDevice", "connect", [macAddress]);
}
module.exports.disconnect = function(){
    console.log("MWDevice.js: disconnect");
    exec(null, null, "MWDevice", "disconnect", []);
}

module.exports.scanForDevices = function(success, failure){
    console.log("MWDevice.js: scanForDevices");
    exec(success, failure, "MWDevice", 'scanForDevices', []);
}

module.exports.supportedModules = function(success, failure){
    console.log("MWDevice.js: supportedModules");
    exec(success, failure, "MWDevice", 'supportedModules', []);
}

module.exports.readRssi = function(success, failure){
    console.log("MWDevice.js: readRssi");
    exec(success, failure, "MWDevice", 'readRssi', []);
}

module.exports.readBatteryLevel = function(success, failure){
    console.log("MWDevice.js: readBatteryLevel");
    exec(success, failure, "MWDevice", 'readBatteryLevel', []);
}

module.exports.playLED = function(ledArguments){
    /*channel, highIntensity, lowIntensity,
      repeatCount, pulseDuration, riseTime,
      highTime, fallTime*/
    var errors = {}
    
    if((ledArguments.channel !== 'RED') &&
       (ledArguments.channel !== 'GREEN') && (ledArguments.channel !== 'BLUE')){
        errors['channel'] = "invalid channel " + ledArguments.channel;
    }
    
    if((ledArguments.highIntensity !== undefined) &&
       (isNaN(ledArguments.highIntensity)) ||
       (ledArguments.highIntensity < 0) || (ledArguments.highIntensity > 31)){
        errors['highIntensity'] = "invalid highIntensity value " + ledArguments.highIntensity;
    }
    
    if((ledArguments.lowIntensity !== undefined) &&
       (isNaN(ledArguments.lowIntensity)) ||
       (ledArguments.lowIntensity < 0) || (ledArguments.lowIntensity > 31)){
        errors['lowIntensity'] = "invalid lowIntensity value " + ledArguments.lowIntensity;
    }

    if((ledArguments.repeatCount !== undefined) &&
       (isNaN(ledArguments.repeatCount)) ||
       (ledArguments.repeatCount < -1) || (ledArguments.repeatCount > 255)){
        errors['repeatCount'] = "invalid repeatCount value " + ledArguments.repeatCount;
    }


    var millisecondParameters = {pulseDuration: ledArguments.pulseDuration,
                                 riseTime: ledArguments.riseTime,
                                 highTime: ledArguments.highTime,
                                 fallTime: ledArguments.fallTime};

    for(var parameter in millisecondParameters){
        if((millisecondParameters[parameter] !== undefined) &&
           (isNaN(millisecondParameters[parameter])) ||
           (millisecondParameters[parameter] < 0)){
            errors[parameter] = "invalid " + parameter + " riseTime value " +
                millisecondParameters[parameter];
        }
    }

    console.log("MWDevice.js: playLED");

    if(Object.keys(errors).length > 0){
        errors['status']= 'ERROR';
        return(errors);
    }else{
        exec(null, null, "MWDevice", 'playLED', [ledArguments]);
        return({status: 'ok'});
    }
};

module.exports.stopLED = function(){
    exec(null, null, "MWDevice", "stopLED", []);
};

module.exports.startAccelerometer = function(success, failure){
    console.log("MWDevice.js: start Accelerometer");
    exec(success, failure, "MWDevice", 'startAccelerometer', []);
}

module.exports.stopAccelerometer = function(){
    console.log("MWDevice.js: stopAccelerometer");
    exec(null, null, "MWDevice", 'stopAccelerometer', []);
}

module.exports.readTemperature = function(success, failure, thermistorArguments){
    console.log("MWDevice.js: read temperature");
    exec(success, failure, "MWDevice", 'readTemperature', [thermistorArguments]);
}

module.exports.startStepCounter = function(success, failure){
    console.log("MWDevice.js: start Step Counter");
    exec(success, failure, "MWDevice", 'startStepCounter', []);
}

module.exports.stopStepCounter = function(){
    console.log("MWDevice.js: stopStepCounter");
    exec(null, null, "MWDevice", 'stopStepCounter', []);
}

module.exports.gpioReadAnalogIn = function(pin, success, failure, pullup, readMode){
    console.log("MWDevice.js: gpioReadAnalogIn");
    if(pullup === undefined){
        pullup = null;
    }
    if(readMode === undefined){
        readMode = null;
    }
    console.log(pullup);
    console.log(readMode);
    exec(success, failure, "MWDevice", 'gpioReadAnalogIn', [Number(pin),pullup,readMode]);
}

module.exports.startGyroscope = function(success, failure){
    console.log("MWDevice.js: start Gyroscope");
    exec(success, failure, "MWDevice", 'startGyroscope', []);
}

module.exports.stopGyroscope = function(){
    console.log("MWDevice.js: stopGyroscope");
    exec(null, null, "MWDevice", 'stopGyroscope', []);
}

});

Cordova Plugin
==============

Cordova plugin for the MetaWear API.  This is currently a work in progress.  The API is very fluid and breaking changes are likely until things are a bit more feature complete.

##Supported Sensors
*Accelerometer
*GPIO Pins
*RSSI
*Gyroscope

##Getting Started:

Create your Cordova project
```
cordova create <your project>
```
Add in the plugin
```
cordova plugin add http://www.github.com/mbientlab-projects/cordova-plugin-metawear.html
```
Add in your platform
```
cordova platform add android
cordova platform add ios
```
Load Device.js in your javascript.

Head over to https://github.com/mbientlab-projects/metawear_cordova_example for a simple example project.

##Using The Plugin

Currently the project supports the following functions:

* Connecting and Disconnecting to the board
* Reading the RSSI for the device.
* Streaming accelerometer data.

This module currently support IOS and Android.  If you would like to see support for other platforms please open an issue.

Methods are called from the metawear.mwdevice object.  Callbacks take the form of

```Javascript
var mycallback = function(result){
   // your code here
}
```

##Supported Methods

###Connect

Connects to the specified board.  The success callback is also used for disconnect.

```Javacript
mbientlab.mwdevice.connect(macAddressOfBoard, succesCallback, failureCallback);
```

###Disconnect

Disconnects from the board.  Sends a disconnect message to the success callback of connect.

```Javascript
mbientlab.mwdevice.disconnect();
```

###supportedModules

Returns a object with the various modules with a true or false value indicating if they are supported on the board you are connected to in the success callback.

```Javascript
mbientlab.mwdevice.supportedModules(successCallback, failureCallback);

successCallback.gpio //value will be true if supported,  false if not.
successCallback.accelerometer
successCallback.gyroscope
successCallback.stepCounter
```

###readRssi

Reads the RSSI value you are currently getting from the board.  The RSSI is returned as a string in the success callback.

```Javascript
mbientlab.mwdevice.readRssi(successCallback, failureCallback);
```

###readBatteryLevel

Reads the Battery Level value you are currently getting from the board.  The Battery Level is returned as a int in the success callback.

```Javascript
mbientlab.mwdevice.readBatteryLevel(successCallback, failureCallback);
```

###startAccelerometer

Starts the accelerometer on the board and streams the data to the callback until it is stopped.  The callback result is an object with x,y and z values from the accelerometer.

```Javascript
var failure = function(result){
}

var success = function(result){
   console.log("x: " + result.x + " y: " + result.y + " z: " + result.z);
}

mbientlab.mwdevice.startAccelerometer(success, failure);
```

###stopAccelerometer

Stops the accelerometer and stops streaming data.

```Javascript
mbientlab.mwdevice.stopAccelerometer();
```

###startStepCounter

Note: BMI160 devices only

Starts the step counter on the board and streams the data to the callback until it is stopped.  The callback result is an object with text that says 'TOOK_A_STEP' when a step is detected.

```Javascript
var failure = function(result){
}

var totalSteps = 0;
var success = function(result){
   totalSteps = totalSteps + 1;
   console.log(result);
   console.log(totalSteps + " total steps");
}

mbientlab.mwdevice.startStepCounter(success, failure);
```

###stopStepCounter

Stops the step counter and stops streaming data.

```Javascript
mbientlab.mwdevice.stopStepCounter();
```

###startGyroscope

NOTE:  BMI160 devices only.

Starts the gyroscope on the board and streams the data to the callback until it is stopped.  The callback result is an object with x,y and z values from the gyroscope.

```Javascript
var failure = function(result){
}

var success = function(result){
   console.log("x: " + result.x + " y: " + result.y + " z: " + result.z);
}

mbientlab.mwdevice.startGyroscope(success, failure);
```

Note:  The failure callback will come back with a result.status of "MODULE_NOT_SUPPORTED" if the board you are connected to does not support this module.

###stopGyroscope

Stops the gyroscope and stops streaming data.

```Javascript
mbientlab.mwdevice.stopGyroscope();
```


##Roadmap

Right now the following components are on the roadmap for this plugin.  The plan is to add  these in this order,  but if there is specific functionality that you need please file an issue and we can see about moving it up on the list.

*LED
*BMI 160 Accelerometer -- advanced functions
*BMI 160 Gyro -- advanced functions
*BMP 280 Barometer-Pressure Sensor
*Haptic
*I2C
*MMA 8452Q Accelerometer -  Axis Sampling
*MultiChannel Temperature
*NeoPixel
*Logging
*Data Processor -
**accumulator
**RMS
**threshold
**counter


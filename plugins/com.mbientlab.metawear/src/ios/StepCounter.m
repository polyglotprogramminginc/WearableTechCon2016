#import "MWDevice.h"
#import "StepCounter.h"
#import "MBLAccelerometerBMI160.h"

@implementation StepCounter {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)startStepCounter:(CDVInvokedUrlCommand*)command
{
  NSLog(@"startAccelerometer called");
  MBLAccelerometerBMI160 *bmi160Accelerometer = mwDevice.connectedDevice.accelerometer;
  [bmi160Accelerometer.stepEvent startNotificationsWithHandlerAsync:^(MBLAccelerometerData *accelerometerData, NSError *error){
      CDVPluginResult* pluginResult = nil;
      NSLog(@"Accelerometer callback %@", accelerometerData);
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"TOOK_A_STEP"];
      [pluginResult setKeepCallback:[NSNumber numberWithBool:YES]];

      NSLog(@"Callback id %@", command.callbackId);
      [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)stopStepCounter:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stopStepCounter called");
  MBLAccelerometerBMI160 *bmi160Accelerometer = mwDevice.connectedDevice.accelerometer;
  [bmi160Accelerometer.stepEvent stopNotificationsAsync];
  CDVPluginResult* pluginResult = nil;
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"STEP_COUNTER_STOPPED"];

  [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
@end

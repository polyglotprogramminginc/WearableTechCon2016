#import "MWDevice.h"
#import "MWAccelerometer.h"
#import "MBLAccelerometer.h"
#import "MBLAccelerometerData.h"

@implementation MWAccelerometer {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)startAccelerometer:(CDVInvokedUrlCommand*)command
{
  NSLog(@"startAccelerometer called");
  [mwDevice.connectedDevice.accelerometer.dataReadyEvent startNotificationsWithHandlerAsync:^(MBLAccelerometerData *accelerometerData, NSError *error){
      CDVPluginResult* pluginResult = nil;
      NSLog(@"Accelerometer callback %@", accelerometerData);
      NSMutableDictionary *accelerometerReading = [NSMutableDictionary dictionaryWithDictionary:@{}];
      accelerometerReading[@"x"] = [NSNumber numberWithFloat:accelerometerData.x];
      accelerometerReading[@"y"] = [NSNumber numberWithFloat:accelerometerData.y];
      accelerometerReading[@"z"] = [NSNumber numberWithFloat:accelerometerData.z];
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:accelerometerReading];
      [pluginResult setKeepCallback:[NSNumber numberWithBool:YES]];

      NSLog(@"Callback id %@", command.callbackId);
      [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)stopAccelerometer:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stopAccelerometer called");
  [mwDevice.connectedDevice.accelerometer.dataReadyEvent stopNotificationsAsync];
  CDVPluginResult* pluginResult = nil;
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"ACCELEROMETER_STOPPED"];

  [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
@end

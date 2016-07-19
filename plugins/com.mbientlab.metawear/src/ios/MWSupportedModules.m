#import "MWDevice.h"
#import "MWSupportedModules.h"

@implementation MWSupportedModules {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)getSupportedModules:(CDVInvokedUrlCommand*)command
{
  NSLog(@"getSupportedModules called");
  NSNumber *accelerometerSupported = @(YES);
  NSNumber *gyroscopeSupported = @(YES);
  NSNumber *stepCounterSupported = @(YES);
  
  if(mwDevice.connectedDevice.accelerometer == nil){
    accelerometerSupported = @(NO);
  }

  if(mwDevice.connectedDevice.gyro == nil){
    gyroscopeSupported = @(NO);
    stepCounterSupported = @(NO);
  }

  NSMutableDictionary *supportedModules = [NSMutableDictionary dictionaryWithDictionary:@{}];
  CDVPluginResult* pluginResult = nil;
  supportedModules[@"gpio"] = @(YES);
  supportedModules[@"accelerometer"] = accelerometerSupported;
  supportedModules[@"gyroscope"] = gyroscopeSupported;
  supportedModules[@"stepCounter"] = stepCounterSupported;
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:supportedModules];
  [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end

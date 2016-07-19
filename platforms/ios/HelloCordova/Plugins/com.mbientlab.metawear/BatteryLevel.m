#import "MWDevice.h"
#import "BatteryLevel.h"

@implementation BatteryLevel {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)readBatteryLevel:(CDVInvokedUrlCommand*)command
{
  NSLog(@"readBatteryLevel called");
  [mwDevice.connectedDevice readBatteryLifeWithHandler:^(NSNumber *number, NSError *error){
      CDVPluginResult* pluginResult = nil;
      NSLog(@"BatteryLevel callback %@", number);
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:[number integerValue]];

      [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

@end

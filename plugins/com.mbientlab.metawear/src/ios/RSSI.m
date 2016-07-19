#import "MWDevice.h"
#import "RSSI.h"

@implementation RSSI {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)readRssi:(CDVInvokedUrlCommand*)command
{
  NSLog(@"readRssi called");
  [mwDevice.connectedDevice readRSSIWithHandler:^(NSNumber *number, NSError *error){
      CDVPluginResult* pluginResult = nil;
      NSLog(@"RSSI callback %@", number);
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:[number integerValue]];

      [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

@end

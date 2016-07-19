#import "MWDevice.h"
#import "LEDModule.h"
#import "MBLLED.h"
#import <UIKit/UIKit.h>

@implementation LEDModule {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)playLED:(CDVInvokedUrlCommand*)command
{
  NSLog(@"playLED called");
  NSMutableDictionary* arguments = [command.arguments objectAtIndex:0];
  NSString *stringChannel = arguments[@"channel"];
  NSNumber *highIntensity = arguments[@"highIntensity"] ? arguments[@"highIntensity"] : 0;
  NSNumber *lowIntensity = arguments[@"lowIntensity"] ? arguments[@"lowIntensity"] : 0;
  NSNumber *repeatCount = arguments[@"repeatCount"] ? arguments[@"repeatCount"] : 0;
  NSNumber *pulseDuration = arguments[@"pulseDuration"] ? arguments[@"pulseDuration"] : 0;
  NSNumber *riseTime = arguments[@"riseTime"] ? arguments[@"riseTime"] : 0;
  NSNumber *highTime = arguments[@"highTime"] ? arguments[@"highTime"] : 0;
  NSNumber *fallTime = arguments[@"fallTime"] ? arguments[@"fallTime"] : 0;

  MBLLEDColorChannel channel;
  if ([stringChannel isEqualToString:@"RED"]){
    channel = MBLLEDColorChannelRed;
  } else if ([stringChannel isEqualToString:@"GREEN"]){
    channel = MBLLEDColorChannelGreen;
  } else if ([stringChannel isEqualToString:@"BLUE"]){
    channel = MBLLEDColorChannelBlue;
  }

  NSLog(@"string channel %@", stringChannel);
  NSLog(@"highIntensity %@", highIntensity);
  NSLog(@"lowIntensity %@", lowIntensity);
  NSLog(@"riseTime %@", riseTime);
  NSLog(@"fallTime %@", fallTime);
  NSLog(@"highTime %@", highTime);
  NSLog(@"pulseDuration %@", pulseDuration);
  NSLog(@"repeatCount %@", repeatCount);

  [mwDevice.connectedDevice.led setLEDModeWithColorChannelAsync:channel onIntensity:[highIntensity unsignedShortValue]
                                                offIntensity:[lowIntensity unsignedShortValue] 
                                                       riseTime:[riseTime unsignedShortValue] 
                                                       fallTime:[fallTime unsignedShortValue] 
                                                         onTime:[highTime unsignedShortValue] 
                                                         period:[pulseDuration unsignedShortValue] 
                                                         offset:0 
                                                 repeatCount:[repeatCount unsignedShortValue]];

  CDVPluginResult* pluginResult = nil;
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"LED_STOPPED"];
  [mwDevice.connectedDevice.led setLEDOnAsync:YES withOptions:1];

  [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

  /*channel, highIntensity, lowIntensity,
    repeatCount, pulseDuration, riseTime,
    highTime, fallTime*/
}

- (void)stopLED:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stopLED called");
  [mwDevice.connectedDevice.led setLEDOnAsync:NO withOptions:1];
  CDVPluginResult* pluginResult = nil;
  pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"LED_STOPPED"];

  [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
@end

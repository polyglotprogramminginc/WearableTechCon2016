#import "MWDevice.h"
#import "MWMultiChannelTemperature.h"
#import "MBLTemperature.h"
#import "MBLData.h"
#import "MBLNumericData.h"

@implementation MWMultiChannelTemperature {
  MWDevice *mwDevice;
}

- (id)initWithDevice:(MWDevice*)device
{
  if(self = [super init]){
    mwDevice = device;
  }
  return self;
}

- (void)readTemperature:(CDVInvokedUrlCommand*)command
{
  NSMutableDictionary* arguments = [command.arguments objectAtIndex:0];
  NSString *sensor = arguments[@"sensor"];
  NSLog(@"readTemperature called for %@", sensor);

  // todo:  need to dry this code out.
  if ([sensor isEqualToString:@"PRO_NRF_DIE"] || [sensor isEqualToString:@"R_NRF_DIE"]){
    NSLog(@"reading NRF Die");
    [[mwDevice.connectedDevice.temperature.onDieThermistor readAsync] success:^(MBLNumericData * _Nonnull result) {
        CDVPluginResult* pluginResult = nil;
        NSLog(@"MWMultiChannelTemperature callback temperature is %@", result.value.stringValue);
        NSMutableDictionary *temperatureReading = [NSMutableDictionary dictionaryWithDictionary:@{}];
        temperatureReading[@"temperature"] = [NSNumber numberWithFloat:result.value.floatValue];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:temperatureReading];
        NSLog(@"Callback id %@", command.callbackId);
        [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      }];
  }else if([sensor isEqualToString:@"PRO_EXT_THERMISTOR"] || [sensor isEqualToString:@"R_EXT_THERMISTOR"]){
    // needs to be implemented
  }else if([sensor isEqualToString:@"PRO_ON_BOARD_THERMISTOR"]){
    [[mwDevice.connectedDevice.temperature.onboardThermistor readAsync] success:^(MBLNumericData * _Nonnull result) {
        CDVPluginResult* pluginResult = nil;
        NSLog(@"MWMultiChannelTemperature callback temperature is %@", result.value.floatValue);
        NSMutableDictionary *temperatureReading = [NSMutableDictionary dictionaryWithDictionary:@{}];
        temperatureReading[@"temperature"] = [NSNumber numberWithFloat:result.value.floatValue];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:temperatureReading];
        NSLog(@"Callback id %@", command.callbackId);
        [mwDevice.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      }];
  }

}
@end

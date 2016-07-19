#import "MWDevice.h"
#import "RSSI.h"
#import "BatteryLevel.h"
#import <Cordova/CDV.h>
#import "MBLMetaWear.h"
#import "MBLMetaWearManager.h"
#import "MWAccelerometer.h"
#import "LEDModule.h"
#import "MWMultiChannelTemperature.h"
#import "StepCounter.h"
#import "MWGyroscope.h"
#import "MWSupportedModules.h"

@implementation MWDevice {
  NSArray *scannedDevices;
  RSSI *rssi;
  LEDModule *ledModule;
  StepCounter *stepCounter;
  BatteryLevel *batteryLevel;
  MWAccelerometer *accelerometer;
  MWGyroscope *gyroscope;
  MWMultiChannelTemperature *mWMultiChannelTemperature;
  MWSupportedModules *supportedModules;
}

- (void)scanForDevices:(CDVInvokedUrlCommand*)command
{
  CDVPluginResult* pluginResult = nil;
  NSMutableDictionary *boards = [NSMutableDictionary dictionaryWithDictionary:@{}];

  NSLog(@"Scanning for Metawears");

  [[MBLMetaWearManager sharedManager] startScanForMetaWearsAllowDuplicates:YES handler:^(NSArray *array) {
      scannedDevices = array;
      NSLog(@"Scanning callback");
      [[MBLMetaWearManager sharedManager] stopScanForMetaWears];
      for (MBLMetaWear *device in array) {
        NSMutableDictionary *entry = [NSMutableDictionary dictionaryWithDictionary:@{}];
        entry[@"address"] = device.identifier.UUIDString;
        entry[@"rssi"] = [device.discoveryTimeRSSI stringValue];
        boards[device.identifier.UUIDString] = entry;
        NSLog(@"Found MetaWear: %@", device);
        [device rememberDevice];
      }
      CDVPluginResult* pluginResult = nil;
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:boards];

      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];

}

- (void)connect:(CDVInvokedUrlCommand*)command
{
  NSString* uUIDString = [command.arguments objectAtIndex:0];

  [[MBLMetaWearManager sharedManager] retrieveSavedMetaWearsWithHandler:^(NSArray *array) {
      __block CDVPluginResult *pluginResult = nil;

      bool foundDevice = false;
      for (MBLMetaWear *device in array){
        if([device.identifier.UUIDString isEqualToString:uUIDString]){
          foundDevice = true;
          [device connectWithTimeout:20 handler:^(NSError *error) {
              if ([error.domain isEqualToString:kMBLErrorDomain] &&
                  error.code == kMBLErrorConnectionTimeout) {
                NSLog(@"Connection Timeout");
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"ERROR"];
              }
              else {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"CONNECTED"];
                _connectedDevice = device;
              }
              [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            }];
        }
      }
      if(foundDevice == false){
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"ERROR"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      }
    }];
}

- (void)disconnect:(CDVInvokedUrlCommand*)command
{
  __block CDVPluginResult* pluginResult = nil;

  NSLog(@"disconnecting from metawear");
  [_connectedDevice disconnectWithHandler:^(NSError *error) {
      if ([error.domain isEqualToString:kMBLErrorDomain] &&
          error.code == kMBLErrorConnectionTimeout) {
        NSLog(@"Disconnect Problem");
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"ERROR"];
      }
      else {
        NSLog(@"disconnecting");
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"DISCONNECTED"];
        _connectedDevice = nil;
      }
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)supportedModules:(CDVInvokedUrlCommand*)command
{
  if(supportedModules == nil)
    {
      supportedModules = [[MWSupportedModules alloc] initWithDevice:self];
    }
  NSLog(@"read supportedModules on %@", supportedModules);
  [supportedModules getSupportedModules:command];
}

- (void)readRssi:(CDVInvokedUrlCommand*)command
{
  if(rssi == nil)
    {
      rssi = [[RSSI alloc] initWithDevice:self];
    }
  NSLog(@"read RSSI on %@", rssi);
  [rssi readRssi:command];
}

- (void)readBatteryLevel:(CDVInvokedUrlCommand*)command
{
  if(batteryLevel == nil)
    {
      batteryLevel = [[BatteryLevel alloc] initWithDevice:self];
    }
  NSLog(@"read RSSI on %@", rssi);
  [batteryLevel readBatteryLevel:command];
}

- (void)playLED:(CDVInvokedUrlCommand*)command
{
  if(ledModule == nil)
    {
      ledModule = [[LEDModule alloc] initWithDevice:self];
    }
  NSLog(@"play LED on %@", ledModule);
  [ledModule playLED:command];
}

- (void)stopLED:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stop LED on %@", ledModule);
  [ledModule stopLED:command];
}

- (void)startAccelerometer:(CDVInvokedUrlCommand*)command
{
  if(accelerometer == nil)
    {
      accelerometer = [[MWAccelerometer alloc] initWithDevice:self];
    }
  NSLog(@"read Accelerometer on %@", accelerometer);
  [accelerometer startAccelerometer:command];
}

- (void)stopAccelerometer:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stop accelerometer on %@", accelerometer);
  [accelerometer stopAccelerometer:command];
}

- (void)startStepCounter:(CDVInvokedUrlCommand*)command
{
  if(stepCounter == nil)
    {
      stepCounter = [[StepCounter alloc] initWithDevice:self];
    }
  NSLog(@"read step counter on %@", stepCounter);
  [stepCounter startStepCounter:command];
}

- (void)stopStepCounter:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stop step counter on %@", stepCounter);
  [stepCounter stopStepCounter:command];
}

- (void)readTemperature:(CDVInvokedUrlCommand*)command
{
  if(mWMultiChannelTemperature == nil)
    {
      mWMultiChannelTemperature = [[MWMultiChannelTemperature alloc] initWithDevice:self];
    }
  NSLog(@"read temperature on %@", mWMultiChannelTemperature);
  [mWMultiChannelTemperature readTemperature:command];
}

- (void)startGyroscope:(CDVInvokedUrlCommand*)command
{
  if(gyroscope == nil)
    {
      gyroscope = [[MWGyroscope alloc] initWithDevice:self];
    }
  NSLog(@"read Gyroscope on %@", gyroscope);
  [gyroscope startGyroscope:command];
}

- (void)stopGyroscope:(CDVInvokedUrlCommand*)command
{
  NSLog(@"stop accelerometer on %@", gyroscope);
  [gyroscope stopGyroscope:command];
}

@end

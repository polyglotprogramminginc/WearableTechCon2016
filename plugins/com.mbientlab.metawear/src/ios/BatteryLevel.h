
@interface BatteryLevel : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)readBatteryLevel:(CDVInvokedUrlCommand*)command;

@end

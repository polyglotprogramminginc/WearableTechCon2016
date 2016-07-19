
@interface MWMultiChannelTemperature : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)readTemperature:(CDVInvokedUrlCommand*)command;

@end

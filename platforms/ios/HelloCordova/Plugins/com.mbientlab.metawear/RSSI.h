
@interface RSSI : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)readRssi:(CDVInvokedUrlCommand*)command;

@end

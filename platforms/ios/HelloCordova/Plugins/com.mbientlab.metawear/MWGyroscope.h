@interface MWGyroscope : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)startGyroscope:(CDVInvokedUrlCommand*)command;
- (void)stopGyroscope:(CDVInvokedUrlCommand*)command;

@end

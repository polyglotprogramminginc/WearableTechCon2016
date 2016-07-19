
@interface LEDModule : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)playLED:(CDVInvokedUrlCommand*)command;
- (void)stopLED:(CDVInvokedUrlCommand*)command;

@end

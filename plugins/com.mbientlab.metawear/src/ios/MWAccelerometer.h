
@interface MWAccelerometer : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)startAccelerometer:(CDVInvokedUrlCommand*)command;
- (void)stopAccelerometer:(CDVInvokedUrlCommand*)command;

@end

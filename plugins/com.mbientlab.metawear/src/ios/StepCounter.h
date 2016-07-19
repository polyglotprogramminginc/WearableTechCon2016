
@interface StepCounter : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)startStepCounter:(CDVInvokedUrlCommand*)command;
- (void)stopStepCounter:(CDVInvokedUrlCommand*)command;

@end

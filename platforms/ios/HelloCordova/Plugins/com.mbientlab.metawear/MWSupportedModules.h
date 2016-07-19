
@interface MWSupportedModules : NSObject

- (id)initWithDevice:(MWDevice*)device;
- (void)getSupportedModules:(CDVInvokedUrlCommand*)command;

@end

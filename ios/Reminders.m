#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(Reminders, NSObject)

//RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
//                 withResolver:(RCTPromiseResolveBlock)resolve
//                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(requestPermission: (RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock))

RCT_EXTERN_METHOD(getReminders: (RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock))

RCT_EXTERN_METHOD(getReminder: (NSString *)reminderId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock))

RCT_EXTERN_METHOD(addReminder: (NSDictionary *)config resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

@end

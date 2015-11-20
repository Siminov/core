///
/// [SIMINOV FRAMEWORK]
/// Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

#import "SICLog.h"

static NSString *TAG = @"SIMINOV";

@interface SICLog (PrivateMethods)

+(NSString *)prepareMessage:(const NSString *)className methodName:(const NSString *)methodName message:(const NSString *)message;

@end

@implementation SICLog

+ (void)important:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message {
    
    if(DEPLOY == PRODUCTION) {
        return;
    }
    
    NSLog(@"Info : %@",[self prepareMessage: className methodName: methodName message: message]);
}

+ (void)error:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message {
    NSLog(@"Error : %@",[SICLog prepareMessage: className methodName: methodName message: message]);
}

+ (void)debug:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message {
    
    if(DEPLOY == PRODUCTION || DEPLOY == BETA) {
        return;
    }
    
    NSLog(@"Debug : %@",[SICLog prepareMessage: className methodName: methodName message: message]);
}

+(NSString *)prepareMessage:(const NSString *)className methodName:(const NSString *)methodName message:(const NSString *)message {
    return [NSString stringWithFormat: @"Class Name: %@, Method Name: %@, Message: %@",className,methodName,message];
}


@end

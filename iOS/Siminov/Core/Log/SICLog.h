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

#import <Foundation/Foundation.h>

static int DEVELOPMENT = 0;
static int BETA = 0;
static int PRODUCTION = 0;

static int DEPLOY = 0;


/**
 * It prints logs
 */
@interface SICLog : NSObject


/**
 * Log info messages.
 * @param className Class Name.
 * @param methodName Method Name.
 * @param message Message.
 */
+ (void)important:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message;

/**
 * Log error messages.
 * @param className Class Name.
 * @param methodName Method Name.
 * @param message Message.
 */
+ (void)error:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message;

/**
 * Log debug messages.
 * @param className Class Name.
 * @param methodName Method Name.
 * @param message Message.
 */
+ (void)debug:(NSString * const)className methodName:(NSString * const)methodName message:(NSString * const)message;

@end

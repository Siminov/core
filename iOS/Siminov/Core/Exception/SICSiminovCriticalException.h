///
/// [SIMINOV FRAMEWORK - CORE]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
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
#import "SICIException.h"

/**
 * It is a superclass of runtime exception that can be thrown during the normal operation of the Java Virtual Machine.
 */
@interface SICSiminovCriticalException : NSException<SICIException>
{
    NSString *className;
    NSString *methodName;
    NSString *message;
}

/**
 * Siminov Critical Exception Constructor
 * @param classname Name of the class
 * @param methodname Name of the method
 * @param exceptionmessage Message of exception
 */
- (id)initWithClassName:(NSString *)classname methodName:(NSString *)methodname message:(NSString *)exceptionmessage;

@end

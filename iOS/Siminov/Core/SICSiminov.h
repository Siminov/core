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
#import "SICIInitializer.h"

/** Exposes methods to deal with SIMINOV FRAMEWORK such as:-
 
 1. Initializer: Entry point to the SIMINOV.
 2. Shutdown: Exit point from the SIMINOV.
 */
@interface SICSiminov: NSObject

/** It is used to check whether SIMINOV FRAMEWORK is active or not.
 
 SIMINOV become active only when deployment of application is successful.
 @exception SICDeploymentException If SIMINOV is not active it will throw SICDeploymentException which is RuntimeException.
 */
+ (void)isActive;

+ (bool)getActive;
+ (void)setActive:(bool)active;

+ (bool)isFirstTimeInitialized;

/**
 * Returns the SICIInitializer instance.
 * @return Instance of SICIInitializer
 */
+ (id<SICIInitializer>)initializer;

/** It is the entry point to the SIMINOV FRAMEWORK.
 When application starts it should call this method to activate SIMINOV-FRAMEWORK.
 Siminov will read all descriptor defined by application, and do necessary processing.
 
 Example:
    There are two ways to make a call.
 
+ Call it from Application class.
+ Call it from LAUNCHER Activity
 */
+ (void)start;

/** It is used to stop all service started by SIMINOV.

 When application shutdown they should call this. It do following services:
    
+ Close all database's opened by SIMINOV.
+ Deallocate all resources held by SIMINOV.

 @exception SICSiminovException If any error occur while shutting down SIMINOV.
 */
+ (void)shutdown;


+ (void)process;

+ (void)processApplicationDescriptor;
+ (void)processDatabaseDescriptors;
+ (void)processLibraries;
+ (void)processEntityDescriptors;

+ (void)processDatabase;

@end

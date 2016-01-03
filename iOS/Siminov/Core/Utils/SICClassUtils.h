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
#import <objc/runtime.h>
#import "SICDatabaseException.h"
#import "SICSiminovCriticalException.h"
#import "SICLog.h"

/**
 * Exposes class util methods to SIMINOV.
 */
@interface SICClassUtils: NSObject

/**
 * Create a Class Object based on class name provided.
 * @param className Name of Class
 * @return Class Object
 */
+ (id)createClass:(NSString *)className;

/**
 * Creates class object based on full class name provided.
 * @param className Name of class.
 * @return Object of class.
 * @exception SICSiminovException If any exception occur while creating class object based on class name provided.
 */
+ (id)createClassInstance:(NSString *)className;
+ (id)createClassInstanceBasedOnConstructorParameters:(NSString *)className parameters:(id)constructorParameters;

/**
 * Create a method object.
 * @param className Name of Class
 * @param methodName Name of Method
 * @param parameterTypes Parameter Types
 * @return Method Object
 */
+ (Method)createMethodBasedOnClassName:(NSString *)className methodName:(NSString *)methodName parameterTypes:(id)parameterTypes;

/**
 * Create a method object.
 * @param classObject Class Object
 * @param methodName Name of Method
 * @param parameterTypes Parameter Types
 * @return Method Object
 */
+ (Method)createMethodBasedOnClassInstance:(id)classObject methodName:(NSString *)methodName parameterTypes:(id)parameterTypes;

/**
 * Get column values based on class object and method name provided.
 * @param classObject Class Object.
 * @param methodNames Name Of Methods.
 * @return Column Values.
 * @exception SICDatabaseException If any exception occur while getting column values.
 */
+ (NSEnumerator *)getValues:(id const)classObject methodNames:(NSEnumerator * const)methodNames;

/**
 * Get column value based on class object and method name.
 * @param classObject Class Object.
 * @param methodName Name Of Method.
 * @return Column Value.
 * @exception SICDatabaseException If any exception occur while getting column value.
 */
+ (id)getValue:(id const)classObject methodName:(NSString * const)methodName;

/**
 * Invoke method based on class object, method name and parameter provided.
 * @param classObject Class Object.
 * @param methodName Name Of Method.
 * @param parameterTypes Type of parameters.
 * @param parameters Parameters To Method.
 * @return Object
 */
+ (id)invokeMethodBasedOnMethodName:(id const)classObject methodName:(NSString * const)methodName parameterTypes:(id const)parameterTypes parameters:(id const)parameters;

/**
 * Invoke method based on class object, method object and parameter provided.
 * @param classObject Class Object.
 * @param method Method Object.
 * @param parameters Parameters to the method
 * @return Object
 */
+ (id)invokeMethodBasedOnMethod:(id const)classObject method:(Method) method parameters:(id const)parameters;

/**
 * Get new object created and filled with values provided.
 * @param className Class Name.
 * @param data Column Values.
 * @return Class Object.
 * @exception SICDatabaseException If any exception occur while create and inflating class object.
 */
+ (id)createAndInflateObject:(NSString * const)className data:(NSDictionary * const)data;
+ (id)convertToPrimitiveClasses:(id)classes;

@end


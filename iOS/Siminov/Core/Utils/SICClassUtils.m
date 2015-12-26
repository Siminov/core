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

#import "SICClassUtils.h"

@implementation SICClassUtils

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"      //Removes Warning messages of perform selector leaks.

+ (id)createClass:(NSString *)className {
    id classObject = nil;
    
    @try {
        classObject = NSClassFromString(className);
    }
    @catch (NSException *exception) {
        [SICLog error: NSStringFromClass([self class]) methodName:@"createClassObject" message:[NSString stringWithFormat:@"Exception caught while creating class object, CLASS-NAME:%@,%@",className,[exception reason]]];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"createClassObject" message:[NSString stringWithFormat:@"Exception caught while creating class object, CLASS-NAME:%@,%@",className,[exception reason]]];
    }
    
    return classObject;
}

+ (id)createClassInstance:(NSString *)className {
    
    id classObject = [self createClass:className];
    id object = nil;
    
    @try {
        object = [[classObject alloc] init];
    }
    @catch (NSException *exception) {
        [SICLog error: NSStringFromClass([self class]) methodName:@"createClassInstance" message:[NSString stringWithFormat:@"Exception caught while creating new instance of class, CLASS-NAME:%@,%@", className, [exception reason]]];
        @throw [[SICSiminovException alloc] initWithClassName: NSStringFromClass([self class]) methodName:@"createClassInstance" message:[NSString stringWithFormat:@"Exception caught while creating new instance of class, CLASS-NAME:%@,%@",className,[exception reason]]];
    }
    
    return object;
}

+ (id)createClassInstanceBasedOnConstructorParameters:(NSString *)className parameters:(id)constructorParameters {
    
    id classObject = [self createClass:className];
    id constructorParameterTypes = [[NSMutableArray alloc] initWithCapacity:[(NSMutableArray *)constructorParameters count]];
    
    for(int i = 0;i < [constructorParameters count];i++) {
        constructorParameterTypes[i] = [constructorParameters[i] class];
    }
    
    id object;
    @try {
        object = [[classObject alloc] init];
    }
    @catch (NSException *exception) {
        [SICLog error: NSStringFromClass([self class]) methodName:@"createClassInstance" message:[NSString stringWithFormat:@"Exception caught while creating new instance of class, CLASS-NAME:%@,%@",className,[exception reason]]];
        @throw [[SICSiminovException alloc] initWithClassName: NSStringFromClass([self class]) methodName:@"createClassInstance" message:[NSString stringWithFormat:@"Exception caught while creating new instance of class, CLASS-NAME: %@,%@",className,[exception reason]]];
    }
    
    return object;
}

+ (Method)createMethodBasedOnClassName:(NSString *)className methodName:(NSString *)methodName parameterTypes:(id)parameterTypes {
    id classObject = [self createClassInstance:className];
    return [self createMethodBasedOnClassInstance:classObject methodName:methodName parameterTypes:parameterTypes];
}

+ (Method)createMethodBasedOnClassInstance:(id)classObject methodName:(NSString *)methodName parameterTypes:(id)parameterTypes {
    
    Method method = nil;
    @try {
        method = class_getInstanceMethod([classObject class], NSSelectorFromString(methodName));
    }
    @catch (NSException *exception) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"createMethodBasedOnClassInstance" message:[NSString stringWithFormat:@"NoSuchMethodException caught while creating method, CLASS-NAME: %@, METHOD-NAME: %@, %@",NSStringFromClass([classObject class]),methodName,[exception reason]]];
        
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"createMethodBasedOnClassInstance" message:[NSString stringWithFormat:@"NoSuchMethodException caught while creating method, CLASS-NAME: %@, METHOD-NAME: %@, %@",NSStringFromClass([classObject class]),methodName,[exception reason]]];
    }
    
    //method setAcces
    return method;
}

+(NSEnumerator *)getValues:(id const)classObject methodNames:(NSEnumerator * const)methodNames {
    
    NSMutableArray *columnValues = [[NSMutableArray alloc] init];
    NSString *methodName = nil;
    while(methodName = [methodNames nextObject]) {
        
        Method method = [self createMethodBasedOnClassName:NSStringFromClass([classObject class]) methodName:methodName parameterTypes:nil];
        
        @try {
            id val = [classObject performSelector:method_getName(method) withObject:nil];
            [columnValues addObject: val];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"getValues" message:[NSString stringWithFormat:@"Exception caught while getting return value from method, CLASS-NAME: %@, METHOD-NAME: %s,%@",NSStringFromClass([classObject class]),sel_getName(method_getName(method)),[exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getValues" message:[NSString stringWithFormat:@"Exception caught while getting return value from method, CLASS-NAME: %@, METHOD-NAME: %s,%@",NSStringFromClass([classObject class]),sel_getName(method_getName(method)),[exception reason]]];
        }
    }
}

+ (id)getValue:(id const)classObject methodName:(NSString * const)methodName {
    
    Method method = [self createMethodBasedOnClassName:NSStringFromClass([classObject class]) methodName:methodName parameterTypes:nil];
    
    id __unsafe_unretained tempResult;
    id result;
    
    SEL methodname = method_getName(method);
    
    @try {
        
        NSMethodSignature *sign = [classObject methodSignatureForSelector:methodname];
        NSString *returnType = [NSString stringWithCString: [sign methodReturnType] encoding: NSUTF8StringEncoding];
        NSInvocation *operation = [NSInvocation invocationWithMethodSignature:sign];
        
        [operation setSelector:methodname];
        
        [operation invokeWithTarget:classObject];
        if([returnType isEqualToString:@"@"]) {
            [operation getReturnValue:&tempResult];
            result = tempResult;
        }
        
        return result;
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"getValue" message:[NSString stringWithFormat:@"Exception caught while getting return value from method, CLASS-NAME: %@, METHOD-NAME: %s, %@",NSStringFromClass([classObject class]),sel_getName(method_getName(method)),[exception reason]]];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getValue" message:[NSString stringWithFormat:@"Exception caught while getting return value from method, CLASS-NAME: %@, METHOD-NAME: %s, %@",NSStringFromClass([classObject class]),sel_getName(method_getName(method)),[exception reason]]];
    }
}

+ (id)invokeMethodBasedOnMethodName:(id const)classObject methodName:(NSString * const)methodName parameterTypes:(id const)parameterTypes parameters:(id const)parameters {
    
    SEL methodSEL = NSSelectorFromString(methodName);
    id result;
    id __unsafe_unretained tempResult;
    
    NSMethodSignature *sign = [classObject methodSignatureForSelector:methodSEL];
    NSString *returnType = [NSString stringWithCString: [sign methodReturnType] encoding: NSUTF8StringEncoding];
    NSInvocation *operation = [NSInvocation invocationWithMethodSignature:sign];
    
    [operation setSelector:methodSEL];
    
    for (int i = 0;i < [parameters count];i++) {
        id argument = [parameters objectAtIndex: i];
        [operation setArgument:&argument atIndex: i+2];
    }
    
    [operation invokeWithTarget:classObject];
    if([returnType isEqualToString:@"@"])
    {
        [operation getReturnValue:&tempResult];
        result = tempResult;
    }
    
    return result;
}


+ (id)invokeMethodBasedOnMethod:(id const)classObject method:(Method)method parameters:(id const)parameters {
    
    SEL methodSEL = method_getName(method);
    
    id result;
    id __unsafe_unretained tempResult;
    
    NSMethodSignature *sign = [classObject methodSignatureForSelector:methodSEL];
    NSString *returnType = [NSString stringWithCString: [sign methodReturnType] encoding: NSUTF8StringEncoding];
    NSInvocation *operation = [NSInvocation invocationWithMethodSignature:sign];
    
    [operation setSelector:methodSEL];
    
    for (int i = 0;i < [parameters count];i++) {
        id argument = [parameters objectAtIndex: i];
        [operation setArgument:&argument atIndex: i+2];
    }
    
    [operation invokeWithTarget:classObject];
    if([returnType isEqualToString:@"@"]) {
        [operation getReturnValue:&tempResult];
        result = tempResult;
    }
        
    return result;
}

+ (id)createAndInflateObject:(NSString * const)className data:(NSDictionary * const)data {
    
    id object = [self createClassInstance:className];
    NSArray *methodNames = [data allKeys];
    NSEnumerator *methodNamesIterate = [methodNames objectEnumerator];
    
    NSString *methodName = nil;
    while (methodName = [methodNamesIterate nextObject]) {
        id methodParameter = [data objectForKey:methodName];
        
        if (methodParameter == nil) {
            continue;
        }
        
        [self invokeMethodBasedOnMethodName:object methodName:methodName parameterTypes:[methodParameter class] parameters:[NSArray arrayWithObject:methodParameter]];
    }
    
    return object;
}

+ (id)convertToPrimitiveClasses:(id)classes {
    return nil;
}
#pragma clang diagnostic pop

@end


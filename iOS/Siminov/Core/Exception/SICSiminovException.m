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

#import "SICSiminovException.h"

@implementation SICSiminovException

- (id)initWithClassName:(NSString *)classname methodName:(NSString *)methodname message:(NSString *)exceptionmessage {
    
    if(self) {
        self.className = classname;
        self.methodName = methodname;
        self.message = exceptionmessage;
    }
    
    return self;
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

- (NSString *)getClassName {
    return className;
}

- (void)setClassName:(NSString * const)classname {
    self.className = classname;
}

- (NSString *)getMethodName {
    return methodName;
}

- (void)setMethodName:(NSString * const)methodname {
    self.methodName = methodname;
}

- (NSString *)getMessage {
    return message;
}

- (void)setMessage:(NSString * const)exceptionmessage {
    self.message = exceptionmessage;
}

@end
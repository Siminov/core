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


#import "SICInitializer.h"
#import "SICResourceManager.h"
#import "SICSiminov.h"

@implementation SICInitializer

SICResourceManager *resourceManager = nil;

-(id)init {
    
    self = [super init];
    
    if(self) {
        resourceManager = [SICResourceManager getInstance];
        parameters = [[NSMutableArray alloc] init];
        return self;
    }
    
    return self;
}

- (void)addParameter:(id)object {
    [parameters addObject: object];
}

- (void)initialize {
    [SICSiminov start];
}

@end

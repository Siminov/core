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


#import "SICEventHandler.h"
#import "SICResourceManager.h"

static SICEventHandler*eventHandler = nil;
id<SICISiminovEvents> coreEventHandler;
id<SICIDatabaseEvents> databaseEventHandler;
static SICResourceManager *resourceManager;

@implementation SICEventHandler


/**
 * Event Handler Constructor
 */
+ (void)initialize {
    resourceManager = [SICResourceManager getInstance];
    NSEnumerator *events = [[resourceManager getApplicationDescriptor] getEvents];
    NSString *event = nil;
    
    while (event = [events nextObject]) {
        
        id object = [SICClassUtils createClassInstance:event];
        if([object conformsToProtocol: @protocol(SICISiminovEvents)]) {
            coreEventHandler = (id<SICISiminovEvents>)object;
        } else if([object conformsToProtocol: @protocol(SICIDatabaseEvents)]) {
            databaseEventHandler = (id<SICIDatabaseEvents>)object;
        }
    }
}

+ (SICEventHandler *)getInstance {

    if(!eventHandler) {
        eventHandler = [[super allocWithZone:NULL] init];
    }
    return eventHandler;
}

+ (id)allocWithZone:(NSZone *)zone {
    return [self getInstance];
}

- (id<SICISiminovEvents>)getSiminovEventHandler {
    return coreEventHandler;
}

- (id<SICIDatabaseEvents>)getDatabaseEventHandler {
    return databaseEventHandler;
}

@end

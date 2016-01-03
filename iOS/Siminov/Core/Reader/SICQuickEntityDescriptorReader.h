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
#import "SICResourceManager.h"
#import "SICSiminovSAXDefaultHandler.h"

/**
 * Exposes methods to quickly parse entity descriptor defined by application.
 */
@interface SICQuickEntityDescriptorReader : SICSiminovSAXDefaultHandler {
    NSMutableString *tempValue;
    NSString *finalEntityDescriptorBasedOnClassName;
    
    SICEntityDescriptor *entityDescriptor;
    NSString *propertyName;
    bool doesMatch;
}

/**
 * SICQuickEntityDescriptorReader Constructor
 @param findEnityDescriptorBasedOnClassName Name of the entity descriptor class name
 */
- (id)initWithClassName:(NSString * const)findEntityDescriptorBasedOnClassName;

/**
 * Parse the entity descriptor defined
 */
- (void)process;

/**
 * Get entity object.
 * @return Entity Descriptor Object.
 */
-(SICEntityDescriptor *)getEntityDescriptor;

@end

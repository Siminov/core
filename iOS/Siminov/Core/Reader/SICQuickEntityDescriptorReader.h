//
//  SICQuickEntityDescriptorReader.h
//  Core
//
//  Created by user on 31/07/15.
//  Copyright (c) 2015 Siminov. All rights reserved.
//


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

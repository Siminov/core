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

#import "SICLibraryDescriptor.h"

@implementation SICLibraryDescriptor

- (id)init {
    
    self = [super init];
    
    if(self) {
        properties = [[NSMutableDictionary alloc] init];
        entityDescriptorPaths = [[NSMutableArray alloc] init];
        
        entityDescriptorsBasedOnTableName = [[NSMutableDictionary alloc] init];
        entityDescriptorsBasedOnClassName = [[NSMutableDictionary alloc] init];
        entityDescriptorsBasedOnPath = [[NSMutableDictionary alloc] init];
        
        return self;
    }
    
    return self;
}

- (NSString *)getName {
    return [properties objectForKey:LIBRARY_DESCRIPTOR_NAME];
}

- (void)setName:(NSString * const)name {
    [properties setObject:name forKey:LIBRARY_DESCRIPTOR_NAME];
}

- (NSString *)getDescription {
    return [properties objectForKey:LIBRARY_DESCRIPTOR_DESCRIPTION];
}

- (void)setDescription:(NSString * const)description {
    [properties setObject:description forKey:LIBRARY_DESCRIPTOR_DESCRIPTION];
}

- (NSEnumerator *)getProperties {
    return [[properties allKeys] objectEnumerator];
}

- (NSString *)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (bool)containProperty:(NSString *)name {
    return ([[properties allKeys] containsObject:name]);
}

- (void)addProperty:(NSString *)name value:(NSString *)value {
    [properties setValue:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
}

- (BOOL)containsEntityDescriptorBasedOnTableName:(NSString * const)tableName {
    return [[entityDescriptorsBasedOnTableName allKeys] containsObject:tableName];
}

- (BOOL)containsEntityDescriptorBasedOnClassName:(NSString * const)className {
    return [[entityDescriptorsBasedOnClassName allKeys] containsObject:className];
}

- (NSEnumerator *)getEntityDescriptorPaths {
    return [entityDescriptorPaths objectEnumerator];
}

- (void)addEntityDescriptorPath:(NSString * const)entityDescriptorPath {
    [entityDescriptorPaths addObject:entityDescriptorPath];
}

- (NSEnumerator *)getEntityDescriptors {
    return [[entityDescriptorsBasedOnClassName allValues] objectEnumerator];
}

- (SICEntityDescriptor *)getEntityDescriptorBasedOnTableName:(NSString * const)tableName {
    return [entityDescriptorsBasedOnTableName objectForKey:tableName];
}

- (SICEntityDescriptor *)getEntityDescriptorBasedOnClassName:(NSString * const)className {
    return [entityDescriptorsBasedOnClassName objectForKey:className];
}

- (SICEntityDescriptor *)getEntityDescriptorBasedOnPath:(NSString * const)libraryEntityDescriptorPath {
    return [entityDescriptorsBasedOnPath objectForKey:libraryEntityDescriptorPath];
}

- (void)addEntityDescriptor:(NSString * const)libraryEntityDescriptorPath entityDescriptor:(SICEntityDescriptor * const)entityDescriptor {
    [entityDescriptorsBasedOnPath setObject:entityDescriptor forKey:libraryEntityDescriptorPath];
    [entityDescriptorsBasedOnTableName setObject:entityDescriptor forKey:[entityDescriptor getTableName]];
    [entityDescriptorsBasedOnClassName setObject:entityDescriptor forKey:[entityDescriptor getClassName]];
}

- (void)removeEntityDescriptorBasedOnPath:(NSString *)entityDescriptorPath {
    [entityDescriptorPaths removeObject:entityDescriptorPath];
    
    SICEntityDescriptor *entityDescriptor = [entityDescriptorsBasedOnPath objectForKey:entityDescriptorPath];
    [entityDescriptorsBasedOnPath removeObjectForKey:entityDescriptorPath];
    
    [entityDescriptorsBasedOnClassName removeObjectForKey:[entityDescriptor getClassName]];
    [entityDescriptorsBasedOnTableName removeObjectForKey:[entityDescriptor getTableName]];
}

- (void)removeEntityDescriptorBasedOnClassName:(NSString *)className {
    SICEntityDescriptor *entityDescriptor = [entityDescriptorsBasedOnClassName objectForKey:className];
    NSArray *keys = [entityDescriptorsBasedOnPath allKeys];
    
    NSString *keyMatched = nil;
    BOOL found = false;
    
    for(NSString *key in keys) {
    
        SICEntityDescriptor *selfEntityDescriptor = [entityDescriptorsBasedOnPath objectForKey:key];
        if(entityDescriptor == selfEntityDescriptor) {
            keyMatched = key;
            found = true;
            break;
        }
    }
    
    if(found) {
        [self removeEntityDescriptorBasedOnPath :keyMatched];
    }
}

- (void)removeEntityDescriptorBasedOnTableName:(NSString *)tableName {
    SICEntityDescriptor *entityDescriptor = [entityDescriptorsBasedOnTableName objectForKey:tableName];
    [self removeEntityDescriptorBasedOnClassName:[entityDescriptor getClassName]];
}

- (void)removeEntityDescriptor:(SICEntityDescriptor *)entityDescriptor {
    [self removeEntityDescriptorBasedOnClassName:[entityDescriptor getClassName]];
}

- (NSEnumerator *)orderedEntityDescriptors {
    NSMutableArray *orderedEntityDescriptors = [[NSMutableArray alloc] init];
    
    for(NSString *entityDescriptorPath in entityDescriptorPaths) {
        [orderedEntityDescriptors addObject:[self getEntityDescriptorBasedOnPath:entityDescriptorPath]];
    }
    
    return [orderedEntityDescriptors objectEnumerator];
}

@end
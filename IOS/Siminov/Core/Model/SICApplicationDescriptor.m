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


#import "SICApplicationDescriptor.h"

@implementation SICApplicationDescriptor

- (id)init {
    
    self = [super init];
    
    if(self) {
        
        properties = [[NSMutableDictionary alloc] init];
        databaseDescriptorPaths = [[NSMutableArray alloc] init];
        
        databaseDescriptorsBasedOnName = [[NSMutableDictionary alloc] init];
        databaseDescriptorsBasedOnPath = [[NSMutableDictionary alloc] init];
        
        events = [[NSMutableArray alloc] init];
        libraryDescriptorPaths = [[NSMutableArray alloc] init];
        
        return self;
    }
    
    return self;
}

#pragma mark Setters & Getters

- (NSString *)getName {
    return [properties objectForKey:APPLICATION_DESCRIPTOR_NAME];
}

- (void)setName:(NSString * const)name {
    [properties setValue:name forKey:APPLICATION_DESCRIPTOR_NAME];
}

- (NSString *)getDescription {
    return [properties objectForKey:APPLICATION_DESCRIPTOR_DESCRIPTION];
}

- (void)setDescription:(NSString * const)description {
    [properties setValue:description forKey:APPLICATION_DESCRIPTOR_DESCRIPTION];
}

-(double) getVersion {
    
    NSString *version = [properties objectForKey:APPLICATION_DESCRIPTOR_VERSION];
    if(version == nil || version.length <= 0) {
        return 0.0;
    }
    
    return [version doubleValue];
}

- (void)setVersion:(double const)version {
    [properties setValue:[NSString stringWithFormat:@"%lf",version] forKey:APPLICATION_DESCRIPTOR_VERSION];
}

- (NSString *)getDeploy {
    return [properties objectForKey:APPLICATION_DESCRIPTOR_DEPLOY];
}

- (void)setDeploy:(NSString * const)deploy {
    [properties setValue:deploy forKey:APPLICATION_DESCRIPTOR_DEPLOY];
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

- (BOOL)isDatabaseNeeded {
    return databaseDescriptorPaths.count > 0 ? true : false;
}

- (BOOL)containsDatabaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor {
    return [[databaseDescriptorsBasedOnName allValues] containsObject:databaseDescriptor];
}

- (BOOL)containsDatabaseDescriptorBasedOnPath:(NSString *  const)containDatabaseDescriptorPath {
    return [[databaseDescriptorsBasedOnPath allKeys] containsObject:containDatabaseDescriptorPath];
}

- (BOOL)containsDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName {
    return [[databaseDescriptorsBasedOnName allKeys] containsObject:databaseDescriptorName];
}

-(SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName {
    return [databaseDescriptorsBasedOnName objectForKey:databaseDescriptorName];
}


-(SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnPath:(NSString * const)databaseDescriptorPath {
    return [databaseDescriptorsBasedOnPath objectForKey:databaseDescriptorPath];
}

- (NSEnumerator *)getDatabaseDescriptorPaths {
    return [databaseDescriptorPaths objectEnumerator];
}

- (NSEnumerator *)getDatabaseDescriptorNames {
    return [databaseDescriptorsBasedOnName.allKeys objectEnumerator];
}

- (void)addDatabaseDescriptorPath:(NSString * const)databaseDescriptorPath {
    [databaseDescriptorPaths addObject:databaseDescriptorPath];
}

- (NSEnumerator *)getDatabaseDescriptors {
    return [[databaseDescriptorsBasedOnName allValues] objectEnumerator];
}

- (void)addDatabaseDescriptor:(NSString * const)databaseDescriptorPath databaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor {
    [databaseDescriptorsBasedOnPath setValue:databaseDescriptor forKey:databaseDescriptorPath];
    [databaseDescriptorsBasedOnName setValue:databaseDescriptor forKey:[databaseDescriptor getDatabaseName]];
}

- (void)removeDatabaseDescriptorBasedOnPath:(NSString * const)databaseDescriptorPath {
    
    [databaseDescriptorPaths removeObject:databaseDescriptorPath];
    
    SICDatabaseDescriptor *databaseDescriptor = [databaseDescriptorsBasedOnPath objectForKey:databaseDescriptorPath];
    [databaseDescriptorsBasedOnPath removeObjectForKey:databaseDescriptorPath];
    
    [databaseDescriptorsBasedOnName removeObjectForKey:databaseDescriptor];
}

- (void)removeDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName {
    
    SICDatabaseDescriptor *databaseDescriptor = [databaseDescriptorsBasedOnName objectForKey:databaseDescriptorName];
    NSArray *keys = [databaseDescriptorsBasedOnPath allKeys];
    
    NSString *keyMatched = nil;
    BOOL found = false;
    
    for(NSString *key in keys) {
        
        SICDatabaseDescriptor *descriptor = [databaseDescriptorsBasedOnPath objectForKey:key];
        if(databaseDescriptor == descriptor) {
            keyMatched = key;
            found = true;
            break;
        }
    }
    
    if(found) {
        [self removeDatabaseDescriptorBasedOnPath :keyMatched];
    }
}

- (void)removeDatabaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor {
    [self removeDatabaseDescriptorBasedOnName:[databaseDescriptor getDatabaseName]];
}

- (void)addLibraryDescriptorPath:(NSString * const)libraryDescriptorPath {
    [libraryDescriptorPaths addObject:libraryDescriptorPath];
}

- (NSEnumerator *)getLibraryDescriptorPaths {
    return [libraryDescriptorPaths objectEnumerator];
}

- (BOOL)containLibraryDescriptorPath:(NSString * const)libraryDescriptorPath {
    return [libraryDescriptorPaths containsObject:libraryDescriptorPath];
}

- (void)removeLibraryDescriptorPath:(NSString * const)libraryDescriptorPath {
    [libraryDescriptorPaths addObject:libraryDescriptorPath];
}

- (NSEnumerator *)getEvents {
    return [events objectEnumerator];
}

- (void)addEvent:(NSString * const)event {
    [events addObject:event];
}

- (void)removeEvent:(NSString * const)event {
    [events removeObject:event];
}


@end

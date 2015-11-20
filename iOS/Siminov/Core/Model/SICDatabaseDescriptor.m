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

#import "SICDatabaseDescriptor.h"

@implementation SICDatabaseDescriptor

-(id) init {
    
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

- (NSString *)getDatabaseName {
    return [properties objectForKey:DATABASE_DESCRIPTOR_DATABASE_NAME];
}

- (void)setDatabaseName:(NSString * const)databaseName {
     [properties setValue:databaseName forKey:DATABASE_DESCRIPTOR_DATABASE_NAME];
}

- (NSString *)getType {
    return [properties objectForKey:DATABASE_DESCRIPTOR_TYPE];
}

- (void)setType:(NSString *)type {
    [properties setValue:type forKey:DATABASE_DESCRIPTOR_TYPE];
} 

-(double) getVersion {

    NSString *version = [properties objectForKey:DATABASE_DESCRIPTOR_VERSION];
    if(version == nil || version.length <= 0) {
        return 0.0;
    }
    
    return [version doubleValue];
}

- (void)setVersion:(double const)version {
    [properties setValue:[NSString stringWithFormat:@"%lf", version] forKey:DATABASE_DESCRIPTOR_VERSION];
}

- (NSString *)getDescription {
    return [properties objectForKey:DATABASE_DESCRIPTOR_DESCRIPTION];
}

- (void)setDescription:(NSString * const)description {
    [properties setValue:description forKey:DATABASE_DESCRIPTOR_DESCRIPTION];
}

- (BOOL)isExternalStorageEnable {
    NSString *externalStorage = [properties objectForKey:DATABASE_DESCRIPTOR_EXTERNAL_STORAGE];
    if(externalStorage == nil || externalStorage.length<= 0) {
        return false;
    } else if(externalStorage != nil && externalStorage.length > 0 && [externalStorage caseInsensitiveCompare:@"TRUE"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setExternalStorageEnable:(BOOL const)isExternalStorageEnable {
    [properties setValue:isExternalStorageEnable? @"TRUE" : @"FALSE" forKey:DATABASE_DESCRIPTOR_EXTERNAL_STORAGE];
}

- (BOOL)isTransactionSafe {
    NSString *transactionSafe = [properties objectForKey:DATABASE_DESCRIPTOR_TRANSACTION_SAFE];
    if(transactionSafe == nil || transactionSafe.length <= 0) {
        return false;
    } else if(transactionSafe != nil && transactionSafe.length > 0 && [transactionSafe caseInsensitiveCompare:@"TRUE"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setTransactionSafe:(BOOL const)transactionSafe {
    [properties setValue:transactionSafe? @"TRUE" : @"FALSE" forKey:DATABASE_DESCRIPTOR_TRANSACTION_SAFE];
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
    return ([entityDescriptorsBasedOnTableName objectForKey:tableName] != nil);
}

- (BOOL)containsEntityDescriptorBasedOnClassName:(NSString * const)className {
    return ([entityDescriptorsBasedOnClassName objectForKey:className] != nil);
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

- (SICEntityDescriptor *)getEntityDescriptorBasedOnPath:(NSString * const)entityDescriptorPath {
    return [entityDescriptorsBasedOnPath objectForKey:entityDescriptorPath];
}

- (void)addEntityDescriptor:(NSString * const)entityDescriptorPath entityDescriptor:(SICEntityDescriptor *)entityDescriptor {
    
    [entityDescriptorsBasedOnPath setObject:entityDescriptor forKey:entityDescriptorPath];
    [entityDescriptorsBasedOnTableName setObject:entityDescriptor forKey:[entityDescriptor getTableName]];
    [entityDescriptorsBasedOnClassName setObject:entityDescriptor forKey:[entityDescriptor getClassName]];
}

- (void)removeEntityDescriptorBasedOnPath:(NSString * const)entityDescriptorPath {
    
    [entityDescriptorPaths removeObject:entityDescriptorPath];
    
    SICEntityDescriptor *entityDescriptor = [entityDescriptorsBasedOnPath objectForKey:entityDescriptorPath];
    [entityDescriptorsBasedOnPath removeObjectForKey:entityDescriptorPath];
    
    [entityDescriptorsBasedOnClassName removeObjectForKey:[entityDescriptor getClassName]];
    [entityDescriptorsBasedOnTableName removeObjectForKey: [entityDescriptor getTableName]];
}

- (void)removeEntityDescriptorBasedOnClassName:(NSString * const)className {
    
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
        [self removeEntityDescriptorBasedOnPath:keyMatched];
    }
}

- (void)removeEntityDescriptorBasedOnTableName:(NSString * const)tableName {
    SICEntityDescriptor *entityDescriptor = [entityDescriptorsBasedOnTableName objectForKey:tableName];
    [self removeEntityDescriptorBasedOnClassName:[entityDescriptor getClassName]];
}

- (void)removeEntityDescriptor:(const SICEntityDescriptor *)entityDescriptor {
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

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



#import "SICDatabase.h"
#import "SICWhere.h"

@implementation SICDatabase

- (id)init {
    
    self = [super init];
    if (self) {
    }
    return self;
}

- (id)initWithName:(id)databaseObject {
    
    if (self = [super init]) {
        object = databaseObject;
    }
    
    return self;
}

+ (void)dropDatabase:(NSString *)databaseName {
    SICResourceManager *resourceManager = [SICResourceManager getInstance];
    [SICDatabaseHelper dropDatabase:[resourceManager getDatabaseDescriptorBasedOnName:databaseName]];
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

- (void)createTable {
    [SICDatabaseHelper createDatabase:[self getDatabaseDescriptor]];
}

- (void)dropTable {
    [SICDatabaseHelper dropTable:[self getEntityDescriptor]];
}

- (void)dropIndex:(NSString *)indexName {
    [SICDatabaseHelper dropIndex:[self getEntityDescriptor] indexName:indexName];
}

- (id<SICISelect>)select {
    return [[SICWhere alloc] initWithReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICISelect)) referObject:self];
}

- (id)select:(NSString *)query {
    
    if (object != nil) {
        return [SICDatabaseHelper select:object query:query];
    } else {
        return [SICDatabaseHelper select:self query:query];
    }
}

- (void)save {
    
    if (object != nil) {
        return [SICDatabaseHelper save:object];
    } else {
        return [SICDatabaseHelper save:self];
    }
}

- (void)update {
    
    if (object != nil) {
        return [SICDatabaseHelper update:object];
    } else {
        return [SICDatabaseHelper update:self];
    }
}

- (void)saveOrUpdate {
    
    if (object != nil) {
        return [SICDatabaseHelper saveOrUpdate:object];
    } else {
        return [SICDatabaseHelper saveOrUpdate:self];
    }
}

- (id<SICIDelete>)delete {
    
    if (object != nil) {
        return [[SICWhere alloc]initWithReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIDelete)) referObject:object];
    } else {
        return [[SICWhere alloc]initWithReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIDelete)) referObject:self];
    }
}

- (id<SICICount>)count {
    return [[SICWhere alloc]initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICICount))];
}

- (id<SICIAverage>)avg {
    return [[SICWhere alloc]initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIAverage))];
}

- (id<SICISum>)sum {
    return [[SICWhere alloc] initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICISum))];
}

- (id<SICITotal>)total {
    return [[SICWhere alloc] initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIMin))];
}

- (id<SICIMin>)min {
    return [[SICWhere alloc] initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIMin))];
}

- (id<SICIMax>)max {
    return [[SICWhere alloc] initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIMax))];
}

- (id<SICIGroupConcat>)groupConcat {
    return [[SICWhere alloc] initWithoutReferObject:[self getEntityDescriptor] interfaceName:NSStringFromProtocol(@protocol(SICIGroupConcat))];
}

- (SICDatabaseDescriptor *)getDatabaseDescriptor {
    
    if (object != nil) {
        return [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])];
    } else {
        return [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([self class])];
    }
}

- (SICEntityDescriptor *)getEntityDescriptor {
    
    if (object != nil) {
        return [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    } else {
        return [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([self class])];
    }
}

- (NSString *)getTableName {
    
    if (object != nil) {
        return [SICDatabaseHelper getTableName:object];
    } else {
        return [SICDatabaseHelper getTableName:self];
    }
}

- (NSEnumerator *)getColumnNames {
    
    if (object != nil) {
        return [SICDatabaseHelper getColumnNames:object];
    } else {
        return [SICDatabaseHelper getColumnNames:self];
    }
    
}

- (NSDictionary *)getColumnValues {
    
    if (object != nil) {
        return [SICDatabaseHelper getColumnValues:object];
    } else {
        return [SICDatabaseHelper getColumnValues:self];
    }
}

- (NSDictionary *)getColumnTypes {
    
    if (object != nil) {
        return [SICDatabaseHelper getColumnTypes:object];
    } else {
        return [SICDatabaseHelper getColumnTypes:self];
    }
}

- (NSEnumerator *)getPrimaryKeys {
    
    if (object != nil) {
        return [SICDatabaseHelper getPrimaryKeys:object];
    } else {
        return [SICDatabaseHelper getPrimaryKeys:self];
    }
}

- (NSEnumerator *)getMandatoryFields {
    
    if (object != nil) {
        return [SICDatabaseHelper getMandatoryFields:object];
    } else {
        return [SICDatabaseHelper getMandatoryFields:self];
    }
}

- (NSEnumerator *)getUniqueFields {
    
    if (object != nil) {
        return [SICDatabaseHelper getUniqueFields:object];
    } else {
        return [SICDatabaseHelper getUniqueFields:self];
    }
}

- (NSEnumerator *)getForeignKeys {
    
    if (object != nil) {
        return [SICDatabaseHelper getForeignKeys:object];
    } else {
        return [SICDatabaseHelper getForeignKeys:self];
    }
}

+ (void)beginTransaction:(SICDatabaseDescriptor * const)databaseDescriptor {
    [SICDatabaseHelper beginTransaction:databaseDescriptor];
}

+ (void)commitTransaction:(SICDatabaseDescriptor * const)databaseDescriptor {
    [SICDatabaseHelper commitTransaction:databaseDescriptor];
}


@end

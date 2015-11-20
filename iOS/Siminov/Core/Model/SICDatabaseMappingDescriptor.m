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

#import "SICDatabaseMappingDescriptor.h"

@implementation SICAttribute

- (id)init {
    
    self=[super init];
    
    if(self) {
        properties = [[NSMutableDictionary alloc] init];
        return self;
    }
    
    return self;
}

- (NSString *)getVariableName {
    return variableName;
}

- (void)setVariableName:(NSString * const)variablename {
    variableName = variablename;
}

- (NSString *)getColumnName {
    return columnName;
}

- (void)setColumnName:(NSString * const)columnname {
    columnName = columnname;
}

- (NSString *)getType {
    return [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_TYPE];
}

- (void)setType:(NSString * const)type {
    [properties setObject:type forKey:DATABASE_MAPPING_DESCRIPTOR_TYPE];
}

- (NSString *)getGetterMethodName {
    return getterMethodName;
}

- (void)setGetterMethodName:(NSString * const)getMethodName {
    getterMethodName = getMethodName;
}

- (NSString *)getSetterMethodName {
    return setterMethodName;
}

- (void)setSetterMethodName:(NSString * const)setMethodName {
    setterMethodName = setMethodName;
}

- (NSString *)getDefaultValue {
    return [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE] != nil ? [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE] :@"";
}

- (void)setDefaultValue:(NSString * const)defaultValue {
    [properties setObject:defaultValue forKey:DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE];
}

- (NSString *)getCheck {
    return [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_CHECK] != nil ? [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_CHECK] :@"";
}

- (void)setCheck:(NSString * const)check {
    [properties setObject:check forKey:DATABASE_MAPPING_DESCRIPTOR_CHECK];
}

- (BOOL)isPrimaryKey {
    
    NSString *primaryKey = [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY];
    if (primaryKey == nil || primaryKey.length <= 0) {
        return false;
    } else if (primaryKey != nil && primaryKey.length > 0 && [primaryKey caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setPrimaryKey:(BOOL const)primaryKey {
    [properties setObject:primaryKey ? @"TRUE" : @"FALSE" forKey:DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY];
}

- (BOOL)isUnique {
    
    NSString *unique = [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_UNIQUE];
    if (unique == nil || unique.length <= 0) {
        return false;
    } else if (unique != nil && unique.length > 0 && [unique caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setUnique:(BOOL const)isUnique {
    [properties setObject:isUnique ? @"TRUE" : @"FALSE" forKey:DATABASE_MAPPING_DESCRIPTOR_UNIQUE];
}

-(BOOL)isNotNull {

    NSString *notNull = [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_NOT_NULL];
    if (notNull == nil || notNull.length <= 0) {
        return false;
    } else if (notNull != nil && notNull.length > 0 && [notNull caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setNotNull:(BOOL const)isNotNull {
    [properties setObject:isNotNull ? @"TRUE" : @"FALSE" forKey:DATABASE_MAPPING_DESCRIPTOR_NOT_NULL];
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

-(NSEnumerator *)getProperties {
    return [[properties allKeys] objectEnumerator];
}

- (NSString *)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (BOOL)containProperty:(NSString *)name {
    return [[properties allKeys] containsObject:name];
}

- (void)addProperty:(NSString *)name value:(NSString *)value {
    [properties setObject:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
}

@end

@implementation SICIndex

- (id)init {

    self=[super init];
    
    if(self) {
        columns = [[NSMutableArray alloc] init];
        return self;
    }
    
    return self;
}

- (NSString *)getName {
    return indexName;
}

- (void)setName:(NSString *)name {
    indexName = name;
}

- (BOOL)isUnique {
    return uniqueIndex;
}

- (void)setUnique:(BOOL const)unique {
    uniqueIndex = unique;
}

- (BOOL)containsColumn:(NSString * const)column {
    return [columns containsObject:column];
}

- (NSEnumerator *)getColumns {
    return [columns objectEnumerator];
}

- (void)addColumn:(NSString * const)column {
    [columns addObject:column];
}

- (void)removeColumn:(NSString * const)column {
    [columns removeObject:column];
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

-(NSEnumerator *)getProperties {
    return [[NSEnumerator alloc] init];
}

- (NSString *)getProperty:(NSString *)name {
    return nil;
}

- (BOOL)containProperty:(NSString *)name {
    return false;
}

- (void)addProperty:(NSString *)name value:(NSString *)value {

}

- (void)removeProperty:(NSString *)name {

}

@end

@implementation SICRelationship

- (id)init {

    self=[super init];
    
    if(self) {
        properties = [[NSMutableDictionary alloc] init];
        return self;
    }
    
    return self;
}

- (NSString *)getRelationshipType {
    return relationshipType;
}

- (void)setRelationshipType:(NSString *)relationshiptype {
    relationshipType = relationshiptype;
}

- (NSString *)getRefer {
    return refer;
}

- (void)setRefer:(NSString *)refername {
    refer = refername;
}

- (NSString *)getReferTo {
    return referTo;
}

- (void)setReferTo:(NSString *)referto {
    referTo = referto;
}

- (NSString *)getOnUpdate {
    return onUpdate;
}

- (void)setOnUpdate:(NSString *)onupdate {
    onUpdate = onupdate;
}

- (NSString *)getOnDelete {
    return onDelete;
}

- (void)setOnDelete:(NSString *)ondelete {
    onDelete = ondelete;
}

- (NSString *)getGetterReferMethodName {
    return getterReferMethodName;
}

- (void)setGetterReferMethodName:(NSString *)getterReferMethodname {
    getterReferMethodName = getterReferMethodname;
}

- (NSString *)getSetterReferMethodName {
    return setterReferMethodName;
}

- (void)setSetterReferMethodName:(NSString *)setterReferMethodname {
    setterReferMethodName = setterReferMethodname;
}

- (BOOL)isLoad {
    
    NSString *load = [properties objectForKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD];
    if(load == nil || load.length <= 0) {
        return false;
    } else if(load != nil && load.length > 0 && [load caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setLoad:(BOOL)load {
    [properties setObject:load ? @"TRUE" : @"FALSE" forKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD];
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

- (NSEnumerator *)getProperties {
    return [[properties allKeys] objectEnumerator];
}

- (NSString *)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (BOOL)containProperty:(NSString *)name {
    return ([[properties allKeys] containsObject:name]);
}

- (void)addProperty:(NSString *)name value:(NSString *)value {
    [properties setObject:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
}

- (SICDatabaseMappingDescriptor *)getReferedDatabaseMappingDescriptor {
    return referedDatabaseMappingDescriptor;
}

- (void)setReferedDatabaseMappingDescriptor:(SICDatabaseMappingDescriptor *)referedDatabaseMappingdescriptor {
    referedDatabaseMappingDescriptor = referedDatabaseMappingdescriptor;
}

@end

@implementation SICDatabaseMappingDescriptor

- (id)init {

    self=[super init];
    
    if(self) {
        attributeBasedOnColumnNames = [[NSMutableDictionary alloc] init];
        attributeBasedOnVariableNames = [[NSMutableDictionary alloc] init];
        
        indexes = [[NSMutableDictionary alloc] init];
        
        relationshipsBasedOnRefer = [[NSMutableDictionary alloc] init];
        relationshipsBasedOnReferTo = [[NSMutableDictionary alloc] init];
    
        return self;
    }
    
    return self;
}

- (NSString *)getTableName {
    return tableName;
}

- (void)setTableName:(NSString * const)tablename {
    tableName = tablename;
}

- (NSString *)getClassName {
    return className;
}

- (void)setClassName:(NSString * const)classname {
    className = classname;
}

- (BOOL)containsAttributeBasedOnColumnName:(NSString * const)columnName {
    return [[attributeBasedOnColumnNames allKeys] containsObject:columnName] ;
}

- (BOOL)containsAttributeBasedOnVariableName:(NSString * const)variableName {
    return [[attributeBasedOnVariableNames allKeys] containsObject:variableName];
}

- (SICAttribute *)getAttributeBasedOnColumnName:(NSString * const)columnName {
    return [attributeBasedOnColumnNames objectForKey:columnName];
}

- (SICAttribute *)getAttributeBasedOnVariableName:(NSString * const)variableName {
    return [attributeBasedOnVariableNames objectForKey:variableName];
}

- (NSEnumerator *)getColumnNames {
    return [[attributeBasedOnColumnNames allKeys] objectEnumerator];
}

- (NSEnumerator *)getAttributes {
    return [[attributeBasedOnVariableNames allValues] objectEnumerator];
}

- (void)addAttribute:(SICAttribute* const)attribute {
    [attributeBasedOnVariableNames setValue:attribute forKey:[attribute getVariableName]];
    [attributeBasedOnColumnNames setValue:attribute forKey:[attribute getColumnName]];
}

- (void)removeAttributeBasedOnVariableName:(NSString * const)variableName {
    [self removeAttribute:[self getAttributeBasedOnVariableName:variableName]];
}

- (void)removeAttributeBasedOnColumnName:(NSString * const)columnName {
    [self removeAttribute:[self getAttributeBasedOnColumnName:columnName]];
}

- (void)removeAttribute:(SICAttribute* const)attribute {
    //[attributeBasedOnColumnNames ];
}

- (BOOL)containsIndex:(NSString * const)indexName {
    return [[indexes allKeys] containsObject:indexName];
}

- (SICIndex *)getIndex:(NSString * const)indexName {
    return [indexes objectForKey:indexName];
}

- (NSEnumerator *)getIndexNames {
    return [[indexes allKeys] objectEnumerator];
}

- (NSEnumerator *)getIndexes {
    return [[indexes allValues] objectEnumerator];
}

- (void)addIndex:(SICIndex* const)index {
    [indexes setValue:index forKey:[index getName]];
}

- (void)removeIndexBasedOnName:(NSString * const)indexName {
    [self removeIndex:[self getIndex:indexName]];
}

- (void)removeIndex:(SICIndex* const)index {
    [indexes removeObjectForKey:[index getName]];
}

- (NSEnumerator *)getRelationships {
    return [[relationshipsBasedOnRefer allValues] objectEnumerator];
}

- (SICRelationship *)getRelationshipBasedOnRefer:(NSString *)refer {
    return [relationshipsBasedOnRefer objectForKey:refer];
}

-(SICRelationship *)getRelationshipBasedOnReferTo:(NSString *)referTo {
    return [relationshipsBasedOnReferTo objectForKey:referTo];
}

- (NSEnumerator *)getOneToOneRelationships {
    
    NSMutableArray *oneToOneRelationships = [[NSMutableArray alloc] init];
    NSArray *relationships = [relationshipsBasedOnRefer allValues];
    
    NSEnumerator *relationshipsIterator = [relationships objectEnumerator];
    SICRelationship *relationship = nil;
    
    while (relationship = [relationshipsIterator nextObject]) {
        if ([[relationship getRelationshipType] caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE] == NSOrderedSame) {
            [oneToOneRelationships addObject:relationship];
        }
    }
	
    return [oneToOneRelationships objectEnumerator];
}

- (NSEnumerator *)getOneToManyRelationships {
    
    NSMutableArray *oneToManyRelationships = [[NSMutableArray alloc] init];
    NSArray *relationships = [relationshipsBasedOnRefer allValues];
    
    NSEnumerator *relationshipsIterator = [relationships objectEnumerator];
    SICRelationship *relationship = nil;
    
    while (relationship = [relationshipsIterator nextObject]) {
        if ([[relationship getRelationshipType] caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY] == NSOrderedSame) {
            [oneToManyRelationships addObject:relationship];
        }
    }
    
    return [oneToManyRelationships objectEnumerator];
}

- (NSEnumerator *)getManyToOneRelationships {
    
    NSMutableArray *manyToOneRelationships = [[NSMutableArray alloc] init];
    NSArray *relationships = [relationshipsBasedOnRefer allValues];
    
    NSEnumerator *relationshipsIterator = [relationships objectEnumerator];
    SICRelationship *relationship = nil;
    
    while (relationship = [relationshipsIterator nextObject]) {
        if ([[relationship getRelationshipType] caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE] == NSOrderedSame) {
            [manyToOneRelationships addObject:relationship];
        }
    }
	
    return [manyToOneRelationships objectEnumerator];
}

- (NSEnumerator *)getManyToManyRelationships {
    
    NSMutableArray *manyToManyRelationships = [[NSMutableArray alloc] init];
    NSArray *relationships = [relationshipsBasedOnRefer allValues];
    
    NSEnumerator *relationshipsIterator = [relationships objectEnumerator];
    SICRelationship *relationship = nil;
    
    while (relationship = [relationshipsIterator nextObject]) {
        if ([[relationship getRelationshipType] caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY] == NSOrderedSame) {
            [manyToManyRelationships addObject:relationship];
        }
    }
	
    return [manyToManyRelationships objectEnumerator];
}

- (void)addRelationship:(SICRelationship*)relationship {
    [relationshipsBasedOnRefer setValue:relationship forKey:[relationship getRefer]];
    [relationshipsBasedOnReferTo setValue:relationship forKey:[relationship getReferTo]];
}

///---------------------------------------------------------------------------------------
/// @name Protocol Methods
///---------------------------------------------------------------------------------------

-(NSEnumerator *)getProperties {
    return [[NSEnumerator alloc] init];
}

-(NSString *)getProperty:(NSString *)name {
    return nil;
}

- (BOOL)containProperty:(NSString *)name {
    return false;
}

- (void)addProperty:(NSString *)name value:(NSString *)value {

}

- (void)removeProperty:(NSString *)name {

}

@end


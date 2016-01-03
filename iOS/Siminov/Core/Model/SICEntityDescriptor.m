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

#import "SICEntityDescriptor.h"


@implementation SICEntityDescriptor

- (id)init {
    
    self=[super init];
    
    if(self) {
        
        properties = [[NSMutableDictionary alloc] init];
        
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
    return [properties objectForKey:ENTITY_DESCRIPTOR_TABLE_NAME];
}

- (void)setTableName:(NSString * const)tableName {
    [properties setObject:ENTITY_DESCRIPTOR_TABLE_NAME forKey:tableName];
}

- (NSString *)getClassName {
    return [properties objectForKey:ENTITY_DESCRIPTOR_CLASS_NAME];
}

- (void)setClassName:(NSString * const)className {
    [properties setObject:ENTITY_DESCRIPTOR_CLASS_NAME forKey:className];
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
    [attributeBasedOnVariableNames removeObjectForKey:[attribute getVariableName]];
    [attributeBasedOnColumnNames removeObjectForKey:[attribute getColumnName]];
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
        if ([[relationship getRelationshipType] caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE] == NSOrderedSame) {
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
        if ([[relationship getRelationshipType] caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY] == NSOrderedSame) {
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
        if ([[relationship getRelationshipType] caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE] == NSOrderedSame) {
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
        if ([[relationship getRelationshipType] caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY] == NSOrderedSame) {
            [manyToManyRelationships addObject:relationship];
        }
    }
    
    return [manyToManyRelationships objectEnumerator];
}

- (void)addRelationship:(SICRelationship*)relationship {
    [relationshipsBasedOnRefer setValue:relationship forKey:[relationship getRefer]];
    [relationshipsBasedOnReferTo setValue:relationship forKey:[relationship getReferTo]];
}


- (NSEnumerator *)getProperties {
    return [properties keyEnumerator];
}

- (id)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (bool)containProperty:(NSString *)name {
    return [[properties allKeys] containsObject:name];
}

- (void)addProperty:(NSString *)name value:(id)value {
    [properties setObject:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
}

@end



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
    return [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME];
}

- (void)setVariableName:(NSString * const)variablename {
    [properties setObject:variablename forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME];
}

- (NSString *)getColumnName {
    return [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME];
}

- (void)setColumnName:(NSString * const)columnName {
    [properties setObject:columnName forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME];
}

- (NSString *)getType {
    return [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE];
}

- (void)setType:(NSString * const)type {
    [properties setObject:type forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE];
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
    return [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE] != nil ? [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE]:@"";
}

- (void)setDefaultValue:(NSString * const)defaultValue {
    [properties setObject:defaultValue forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE];
}

- (NSString *)getCheck {
    return [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK] != nil ? [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK]:@"";
}

- (void)setCheck:(NSString * const)check {
    [properties setObject:check forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK];
}

- (BOOL)isPrimaryKey {
    
    NSString *primaryKey = [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY];
    if (primaryKey == nil || primaryKey.length <= 0) {
        return false;
    } else if (primaryKey != nil && primaryKey.length > 0 && [primaryKey caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setPrimaryKey:(BOOL const)primaryKey {
    [properties setObject:primaryKey ? @"TRUE" : @"FALSE" forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY];
}

- (BOOL)isUnique {
    
    NSString *unique = [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE];
    if (unique == nil || unique.length <= 0) {
        return false;
    } else if (unique != nil && unique.length > 0 && [unique caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setUnique:(BOOL const)isUnique {
    [properties setObject:isUnique ? @"TRUE" : @"FALSE" forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE];
}

-(BOOL)isNotNull {
    
    NSString *notNull = [properties objectForKey:ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL];
    if (notNull == nil || notNull.length <= 0) {
        return false;
    } else if (notNull != nil && notNull.length > 0 && [notNull caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setNotNull:(BOOL const)isNotNull {
    [properties setObject:isNotNull ? @"TRUE" : @"FALSE" forKey:ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL];
}


-(NSEnumerator *)getProperties {
    return [[properties allKeys] objectEnumerator];
}

- (NSString *)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (bool)containProperty:(NSString *)name {
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
        properties = [[NSMutableDictionary alloc] init];
        columns = [[NSMutableArray alloc] init];
        
        return self;
    }
    
    return self;
}

- (NSString *)getName {
    return [properties objectForKey:ENTITY_DESCRIPTOR_INDEX_NAME];
}

- (void)setName:(NSString *)name {
    [properties setObject:name forKey:ENTITY_DESCRIPTOR_INDEX_NAME];
}

- (bool)isUnique {
    return [(NSNumber *)[properties objectForKey:ENTITY_DESCRIPTOR_INDEX_UNIQUE] boolValue];
}

- (void)setUnique:(bool const)unique {
    [properties setObject:[NSNumber numberWithBool:unique] forKey:ENTITY_DESCRIPTOR_INDEX_UNIQUE];
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


-(NSEnumerator *)getProperties {
    return [[properties allKeys] objectEnumerator];
}

- (NSString *)getProperty:(NSString *)name {
    return [properties objectForKey:name];
}

- (bool)containProperty:(NSString *)name {
    return [[properties allKeys] containsObject:name];
}

- (void)addProperty:(NSString *)name value:(id)value {
    [properties setObject:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
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
    return [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE];
}

- (void)setRelationshipType:(NSString *)relationshipType {
    [properties setObject:relationshipType forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE];
}

- (NSString *)getRefer {
    return [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_REFER];
}

- (void)setRefer:(NSString *)refer {
    [properties setObject:refer forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_REFER];
}

- (NSString *)getReferTo {
    return [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO];
}

- (void)setReferTo:(NSString *)referTo {
    [properties setObject:referTo forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO];
}

- (NSString *)getOnUpdate {
    return [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE];
}

- (void)setOnUpdate:(NSString *)onUpdate {
    [properties setObject:onUpdate forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE];
}

- (NSString *)getOnDelete {
    return [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE];
}

- (void)setOnDelete:(NSString *)onDelete {
    [properties setObject:onDelete forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE];
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
    
    NSString *load = [properties objectForKey:ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD];
    if(load == nil || load.length <= 0) {
        return false;
    } else if(load != nil && load.length > 0 && [load caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        return true;
    }
    
    return false;
}

- (void)setLoad:(bool)load {
    [properties setObject:load ? @"TRUE" : @"FALSE" forKey:ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD];
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
    [properties setObject:value forKey:name];
}

- (void)removeProperty:(NSString *)name {
    [properties removeObjectForKey:name];
}

- (SICEntityDescriptor *)getReferedEntityDescriptor {
    return referedEntityDescriptor;
}

- (void)setReferedEntityDescriptor:(SICEntityDescriptor *)referedEntitydescriptor {
    referedEntityDescriptor = referedEntitydescriptor;
}

@end


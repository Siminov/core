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

#import "SICDatabaseHelper.h"
#import "SICSiminov.h"
#import "SICDatabaseUtils.h"
#import "SICConstants.h"


@implementation SICDatabaseHelper

static SICResourceManager *resourceManager = nil;
static dispatch_semaphore_t transactionSafe;

+(dispatch_semaphore_t)getSemaphore
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        transactionSafe = dispatch_semaphore_create(1);
    });
    return transactionSafe;
}

+ (void)initialize {
    resourceManager = [SICResourceManager getInstance];
}

+ (SICDatabaseBundle *)createDatabase:(SICDatabaseDescriptor * const)databaseDescriptor {
    return [[SICDatabaseFactory getInstance] getDatabaseBundle: databaseDescriptor];
}

+ (void)upgradeDatabase:(SICDatabaseDescriptor * const)databaseDescriptor {
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle: [databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if (database == nil) {
        [SICLog error: NSStringFromClass([self class]) methodName:@"upgradeDatabase" message:[NSString stringWithFormat:@"No Database Instance Found For, DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
        @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"upgradeDatabase" message:[NSString stringWithFormat:@"No Database Instance Found For, DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
    }
    
    /*
     * Fetch Database Version
     */
    NSString *fetchDatabaseVersionQuery = [queryBuilder formFetchDatabaseVersionQuery:nil];
    [SICLog debug:NSStringFromClass([self class]) methodName:@"upgradeDatabase" message:[NSString stringWithFormat:@"Fetch Database Version Query: %@",fetchDatabaseVersionQuery]];
    
    double currentDatabaseVersion = 0;
    NSEnumerator *datas = [database executeSelectQuery:databaseDescriptor entityDescriptor:nil query:fetchDatabaseVersionQuery];
    
    NSDictionary *data = nil;
    while (data = [datas nextObject]) {
        NSArray *parse = [data allValues];
        
        NSEnumerator *values = [parse objectEnumerator];
        while ((currentDatabaseVersion = [[values nextObject] doubleValue])) {
            
        }
    }
    
    if (currentDatabaseVersion == [databaseDescriptor getVersion]) {
        return;
    }
    
    NSMutableArray *allEntityDescriptor = [[NSMutableArray alloc] init];
    NSEnumerator *allEntityDescriptorIterator = [resourceManager getEntityDescriptors];
    
    SICEntityDescriptor *entityDescriptor = nil;
    while (entityDescriptor = [allEntityDescriptorIterator nextObject]) {
        [allEntityDescriptor addObject:entityDescriptor];
    }
    
    NSMutableArray *tableNames = [[NSMutableArray alloc] init];
    
    NSEnumerator *entityDescriptors = [databaseDescriptor getEntityDescriptors];
    
    /*
     * Get Table Names
     */
    
    NSString *fetchTableNamesQuery = [queryBuilder formTableNames:nil];
    [SICLog debug:NSStringFromClass([self class]) methodName:@"upgradeDatabase" message:[NSString stringWithFormat: @"Fetch Table Names, %@",fetchTableNamesQuery]];
    
    datas = [database executeSelectQuery:databaseDescriptor entityDescriptor:nil query:fetchTableNamesQuery];
    
    data = nil;
    while (data = [datas nextObject]) {
        NSEnumerator *keys = [[data allKeys] objectEnumerator];
        
        NSString *key = nil;
        while (key = [keys nextObject]) {
            [tableNames addObject:(NSString *)[data objectForKey:key]];
        }
    }
    
    
    /*
     * Create Or Upgrade Table
     */
    entityDescriptor = nil;
    while (entityDescriptor = [entityDescriptors nextObject]) {
        bool contain = FALSE;
        
        for (NSString *tableName in tableNames) {
            
            if ([tableName caseInsensitiveCompare:[entityDescriptor getTableName]] == NSOrderedSame) {
                contain = TRUE;
                break;
            }
        }
        
        if (contain) {
            [SICDatabaseHelper upgradeTable:entityDescriptor];
        } else {
            [SICDatabaseHelper createTable:entityDescriptor];
        }
    }
    
    
    /*
     * Drop Table
     */
    for (NSString *tableName in tableNames) {
        
        if ([tableName caseInsensitiveCompare:IOS_METADATA_TABLE_NAME] == NSOrderedSame) {
            continue;
        }
        
        bool contain = TRUE;
        for (SICEntityDescriptor *entityDescriptor in allEntityDescriptor) {
            if ([tableName caseInsensitiveCompare:[entityDescriptor getTableName]]) {
                contain = TRUE;
                break;
            }
        }
        
        
        if (!contain) {
            NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
            [parameters setObject:tableName forKey:FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER];
            
            [database executeQuery:databaseDescriptor entityDescriptor:nil query:[queryBuilder formDropTableQuery:parameters]];
        }
    }
    
    /*
     * Update Database Version
     */
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[NSString stringWithFormat:@"%lf",[databaseDescriptor getVersion]] forKey:FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER];
    
    NSString *updateDatabaseVersionQuery = [queryBuilder formUpdateDatabaseVersionQuery:parameters];
    [SICLog debug:NSStringFromClass([self class]) methodName:@"upgradeDatabase" message:[NSString stringWithFormat: @"Update Database Version Query: %@",updateDatabaseVersionQuery]];
    
    [database executeQuery:databaseDescriptor entityDescriptor:nil query:updateDatabaseVersionQuery];
}

+ (void)upgradeTable:(SICEntityDescriptor *)entityDescriptor {
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if (database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"upgradeTable" message:[NSString stringWithFormat:@"No Database Instance Found For, TABLE-NAME: %@",[entityDescriptor getTableName]]];
        @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"upgradeTable" message:[NSString stringWithFormat:@"No Database Instance Found For, TABLE-NAME: %@",[entityDescriptor getTableName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER];
    
    NSString *tableInfoQuery = [queryBuilder formTableInfoQuery:parameters];
    [SICLog debug:NSStringFromClass([self class]) methodName:@"upgradeTable" message:[NSString stringWithFormat:@"Table Info Query: %@",tableInfoQuery]];
    
    NSMutableArray *newAttributes = [[NSMutableArray alloc] init];
    NSMutableArray *oldAttributes = [[NSMutableArray alloc] init];
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    
    NSEnumerator *datas = [database executeSelectQuery:nil entityDescriptor:nil query:tableInfoQuery];
    NSDictionary *data;
    
    while(data = [datas nextObject]) {
        
        NSEnumerator *keys = [[data allKeys] objectEnumerator];
        NSString *key;
        
        while(key = [keys nextObject]) {
            
            if([key isEqualToString:FORM_TABLE_INFO_QUERY_NAME]) {
                [oldAttributes addObject:(NSString *)[data objectForKey:key]];
            }
        }
    }
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        bool contain = false;
        for (NSString *oldColumn in oldAttributes) {
            
            if([oldColumn caseInsensitiveCompare:[attribute getColumnName]] == NSOrderedSame) {
                contain = true;
                break;
            }
        }
        
        if(!contain) {
            [newAttributes addObject:attribute];
        }
    }
    
    for (SICAttribute *column in newAttributes) {
        
        NSString *columnName = [column getColumnName];
        
        parameters = [[NSMutableDictionary alloc] init];
        [parameters setObject:[entityDescriptor getTableName] forKey:FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER];
        [parameters setObject:columnName forKey:FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER];
        
        NSString *addColumnQuery = [queryBuilder formAlterAddColumnQuery:parameters];
        [SICLog debug:NSStringFromClass([self class]) methodName:@"upgradeTable" message:[NSString stringWithFormat:@"Add New Column Query: %@",addColumnQuery]];
        
        [database executeQuery:nil entityDescriptor:nil query:addColumnQuery];
    }
}

+ (void)createTables:(NSEnumerator *)entityDescriptors {
    
    SICEntityDescriptor *entityDescriptor = nil;
    while(entityDescriptor = [entityDescriptors nextObject]) {
        [self createTable:entityDescriptor];
    }
}

+ (void)createTable:(SICEntityDescriptor * const)entityDescriptor {
    
    /*
     * 1. Get SICIDatabase with respect to current entity descriptor class name.
     * 2. Get Table Name, and all columns.
     * 3. Get all attributes and properties from entity descriptor.
     * 		LIKE(COLUMN NAMES, COLUMN TYPES, PRIMARY KEYS, UNIQUE's, NOT NULL, DEFAULT VALUES, CHECKS, ).
     *
     * 4. If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined,
     * 		because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
     * 		Else get foreign keys.
     *
     * 5. Call QueryBuilder.formCreateTableQuery, get query to create table.
     * 	After forming create table query call executeQuery method to create table in database.
     *
     * 6. Create all triggers.
     * 7. Create Index for table if its defined.
     *		Get all attributes and properties of index.
     * 		LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
     * 		After forming index query call executeQuery method to create index.
     *
     */
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    id<SICIDataTypeHandler> dataTypeHandler = [databaseBundle getDataTypeHandler];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"createTable" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"createTable" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSString *tableName = [entityDescriptor getTableName];
    
    /*
     * Get all attributes and properties from entity descriptor.
     * LIKE(COLUMN NAMES, COLUMN TYPES, DEFAULT VALUES, CHECKS, NOT NULL, PRIMARY KEYS, UNIQUE's ).
     */
    NSMutableArray *columnNames = [[NSMutableArray alloc] init];
    NSMutableArray *columnTypes = [[NSMutableArray alloc] init];
    
    NSMutableArray *defaultValues = [[NSMutableArray alloc] init];
    NSMutableArray *checks = [[NSMutableArray alloc] init];
    
    NSMutableArray *isNotNull = [[NSMutableArray alloc] init];
    
    NSMutableArray *primaryKeys = [[NSMutableArray alloc] init];
    NSMutableArray *uniqueKeys = [[NSMutableArray alloc] init];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute = nil;
    while(attribute = [attributes nextObject]) {
        
        [columnNames addObject:[attribute getColumnName]];
        [columnTypes addObject:[dataTypeHandler convert:[attribute getType]]];
        [isNotNull addObject:[NSNumber numberWithBool:[attribute isNotNull]]];
        
        [defaultValues addObject:[attribute getDefaultValue]];
        [checks addObject:[attribute getCheck]];
        
        bool isPrimary = [attribute isPrimaryKey];
        bool isUnique = [attribute isUnique];
        
        if(isPrimary) {
            [primaryKeys addObject:[attribute getColumnName]];
        }
        
        if(isUnique) {
            [uniqueKeys addObject:[attribute getColumnName]];
        }
    }
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    NSEnumerator *oneToOneRelationships = [entityDescriptor getOneToOneRelationships];
    NSEnumerator *manyToOneRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    
    SICRelationship *oneToOneRelationship = nil;
    while(oneToOneRelationship = [oneToOneRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToOneRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToOneRelationship getReferTo]];
            [oneToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        
        NSMutableArray *foreignAttributes = [SICDatabaseHelper getForeignKeysForEntityDescriptor:referedEntityDescriptor];
        NSEnumerator *foreignAttributesIterator = [foreignAttributes objectEnumerator];
        
        SICAttribute *foreignAttribute = nil;
        while(foreignAttribute = [foreignAttributesIterator nextObject]) {
            
            [columnNames addObject:[foreignAttribute getColumnName]];
            [columnTypes addObject:[dataTypeHandler convert:[foreignAttribute getType]]];
            [isNotNull addObject:[NSNumber numberWithBool:[foreignAttribute isNotNull]]];
            
            [defaultValues addObject:[foreignAttribute getDefaultValue]];
            [checks addObject:[foreignAttribute getCheck]];
            
            bool isPrimary = [foreignAttribute isPrimaryKey];
            if(isPrimary) {
                [primaryKeys addObject:[foreignAttribute getColumnName]];
            }
            
            bool isUnique = [foreignAttribute isUnique];
            if(isUnique) {
                [uniqueKeys addObject:[foreignAttribute getColumnName]];
            }
        }
    }
    
    
    SICRelationship *manyToOneRelationship = nil;
    while(manyToOneRelationship = [manyToOneRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [manyToOneRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToOneRelationship getReferTo]];
            [manyToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSMutableArray *foreignAttributes = [SICDatabaseHelper getForeignKeysForEntityDescriptor:referedEntityDescriptor];
        NSEnumerator *foreignAttributesIterator = [foreignAttributes objectEnumerator];
        
        SICAttribute *foreignAttribute = nil;
        while(foreignAttribute = [foreignAttributesIterator nextObject]) {
            
            [columnNames addObject:[foreignAttribute getColumnName]];
            [columnTypes addObject:[dataTypeHandler convert:[foreignAttribute getType]]];
            [isNotNull addObject:[NSNumber numberWithBool:[foreignAttribute isNotNull]]];
            
            [defaultValues addObject:[foreignAttribute getDefaultValue]];
            [checks addObject:[foreignAttribute getCheck]];
            
            bool isPrimary = [foreignAttribute isPrimaryKey];
            if(isPrimary) {
                [primaryKeys addObject:[foreignAttribute getColumnName]];
            }
            
            bool isUnique = [foreignAttribute isUnique];
            if(isUnique) {
                [uniqueKeys addObject:[foreignAttribute getColumnName]];
            }
            
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToManyRelationship getReferTo]];
            [manyToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            [columnNames addObject:[attribute getColumnName]];
            [columnTypes addObject:[dataTypeHandler convert:[attribute getType]]];
            [isNotNull addObject:[NSNumber numberWithBool:[attribute isNotNull]]];
            
            [defaultValues addObject:[attribute getDefaultValue]];
            [checks addObject:[attribute getCheck]];
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                [primaryKeys addObject:[attribute getColumnName]];
            }
            
            bool isUnique = [attribute isUnique];
            if(isUnique) {
                [uniqueKeys addObject:[attribute getColumnName]];
            }
            
        }
    }
    
    /*
     * If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined,
     * because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
     * Else get foreign keys.
     */
    NSString *foreignKeys = @"";
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:entityDescriptor forKey:FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER];
    
    foreignKeys = [queryBuilder formForeignKeyQuery:parameters];
    
    
    /*
     * Call QueryBuilder.formCreateTableQuery, get query to create table.
     * After forming create table query call executeQuery method to create table in database.
     */
    
    parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:tableName forKey:FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:[columnNames objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER];
    [parameters setObject:[columnTypes objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER];
    [parameters setObject:[defaultValues objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER];
    [parameters setObject:[checks objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER];
    [parameters setObject:[primaryKeys objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER];
    [parameters setObject:[isNotNull objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER];
    [parameters setObject:[uniqueKeys objectEnumerator] forKey:FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER];
    [parameters setObject:foreignKeys forKey:FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER];
    
    NSString *query = [queryBuilder formCreateTableQuery:parameters];
    [database executeQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    
    /*
     * Create Index for table if its defined.
     */
    NSEnumerator *indexes = [entityDescriptor getIndexes];
    SICIndex *index = nil;
    
    while(index = [indexes nextObject]) {
        
        /*
         * Get all attributes and properties of index.
         * LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
         *
         * After forming index query call executeQuery method to create index.
         */
        [SICDatabaseHelper createIndexBasedonIndexObject:entityDescriptor index:index];
    }
    
    id<SICIDatabaseEvents> databaseEventHandler = [resourceManager getDatabaseEventHandler];
    if(databaseEventHandler != nil) {
        [databaseEventHandler onTableCreated:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor];
    }
}

+ (void)dropTable:(SICEntityDescriptor * const)entityDescriptor {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"dropTable" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"dropTable" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    
    NSString *tableName = [entityDescriptor getTableName];
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:tableName forKey:FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER];
    
    [database executeQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:[queryBuilder formDropTableQuery:parameters]];
    
    id<SICIDatabaseEvents> databaseEventHandler = [resourceManager getDatabaseEventHandler];
    if(databaseEventHandler != nil) {
        [databaseEventHandler onTableDropped:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor];
    }
}

+ (void)createIndexBasedonIndexObject:(SICEntityDescriptor * const)entityDescriptor index:(SICIndex* const)index {
    
    NSString *indexName = [index getName];
    NSEnumerator *columnNames = [index getColumns];
    bool isUnique = [index isUnique];
    
    [SICDatabaseHelper createIndex:entityDescriptor indexName:indexName columnNames:columnNames isUnique:isUnique];
}

+ (void)createIndex:(SICEntityDescriptor * const)entityDescriptor indexName:indexName columnNames:(NSEnumerator *)columnNames isUnique:(bool const)isUnique {
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"createIndex" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"createIndex" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableArray * const columnNamesCollection = [[NSMutableArray alloc] init];
    NSString *columnName = nil;
    
    while (columnName = [columnNames nextObject]) {
        [columnNamesCollection addObject:columnName];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:indexName forKey:FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:[columnNamesCollection objectEnumerator] forKey:FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER];
    [parameters setObject:[NSNumber numberWithBool:isUnique] forKey:FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER];
    
    NSString *query = [queryBuilder formCreateIndexQuery:parameters];
    [database executeQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    id<SICIDatabaseEvents> databaseEventHandler = [resourceManager getDatabaseEventHandler];
    if(databaseEventHandler != nil) {
        
        SICIndex *index = [[SICIndex alloc] init];
        [index setName:indexName];
        [index setUnique:isUnique];
        
        NSEnumerator *columnNamesIterator = [columnNamesCollection objectEnumerator];
        NSString *columnName = nil;
        
        while(columnName = [columnNamesIterator nextObject]) {
            [index addColumn:columnName];
        }
        
        [databaseEventHandler onIndexCreated:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor index:index];
    }
}

+ (void)dropIndex:(SICEntityDescriptor * const)entityDescriptor indexName:(NSString * const)indexName  {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"dropIndex" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"dropIndex" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:indexName forKey:FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER];
    
    NSString *query = [queryBuilder formDropIndexQuery:parameters];
    [database executeQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    id<SICIDatabaseEvents> databaseEventHandler = [resourceManager getDatabaseEventHandler];
    if(databaseEventHandler != nil) {
        [databaseEventHandler onIndexDropped:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor index:[entityDescriptor getIndex:indexName]];
    }
}

+ (void)dropDatabase:(SICDatabaseDescriptor * const)databaseDescriptor {
    [SICSiminov isActive];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"dropDatabase" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"dropDatabase" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
    }
    
    NSString *databasePath = [[[SICDatabaseUtils alloc] init] getDatabasePath:databaseDescriptor];
    
    NSFileManager *file = [NSFileManager defaultManager];
    NSString *filePath = [NSString stringWithFormat:@"%@%@",databasePath,[databaseDescriptor getDatabaseName]];
    
    [database close:databaseDescriptor];
    if ([file fileExistsAtPath:filePath]) { // Make sure it exists
        [file removeItemAtPath:filePath error:nil]; // Delete it
    }
    
    [resourceManager removeDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseEvents> databaseEventHandler = [resourceManager getDatabaseEventHandler];
    if(databaseEventHandler != nil) {
        [databaseEventHandler onDatabaseDropped:databaseDescriptor];
    }
}

+ (void)beginTransaction:(SICDatabaseDescriptor * const)databaseDescriptor {
    
    [SICSiminov isActive];
    
    if ([databaseDescriptor isTransactionSafe]) {
        dispatch_semaphore_wait([SICDatabaseHelper getSemaphore], DISPATCH_TIME_FOREVER);
    }
    
    /*
     * 1. Get entity descriptor object for mapped invoked class object.
     */
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"beginTransaction" message:[NSString stringWithFormat:@"No Database Instance Found For DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"beginTransaction" message:[NSString stringWithFormat:@"No Database Instance Found For DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
    }
    
    [database executeMethod:SQLITE_DATABASE_BEGIN_TRANSACTION parameter:nil];
}

+ (void)commitTransaction:(SICDatabaseDescriptor * const)databaseDescriptor {
    [SICSiminov isActive];
    
    if ([databaseDescriptor isTransactionSafe]) {
        dispatch_semaphore_signal([SICDatabaseHelper getSemaphore]);
    }
    
    /*
     * 1. Get entity descriptor object for mapped invoked class object.
     */
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"commitTransaction" message:[NSString stringWithFormat:@"No Database Instance Found For DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"commitTransaction" message:[NSString stringWithFormat:@"No Database Instance Found For DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
    }
    
    [database executeMethod:SQLITE_DATABASE_COMMIT_TRANSACTION parameter:nil];
    
    @try {
        [database executeMethod:SQLITE_DATABASE_END_TRANSACTION parameter:nil];
    }
    @catch (SICDatabaseException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"endTransaction" message:[NSString stringWithFormat:@"DatabaseException caught while executing end transaction method, %@",[exception getMessage]]];
    }
}


+ (id)select:(id const)object parentObject:(id)parentObject entityDescriptor:(SICEntityDescriptor *)entityDescriptor distinct:(bool)distinct where:(NSString *)whereClause columnName:(NSEnumerator *)columnNames groupBy:(NSEnumerator *)groupBy having:(NSString *)having orderBy:(NSEnumerator *)orderBy whichOrderBy:(NSString *)whichOrderBy limit:(NSString *)limit {
    
    /*
     * 1. Get entity descriptor object for mapped invoked class object.
     * 2. Traverse group by's and form a single string.
     * 3. Traverse order by'z and form a single string.
     * 4. Pass all parameters to executeFetchQuery and get cursor.
     * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
     * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
     */
    
    [SICSiminov isActive];
    
    /*
     * 1. Get entity descriptor object for mapped invoked class object.
     */
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"select" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"select" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    if(columnNames == nil) {
        NSArray *columnNamesCollection = [[NSArray alloc] init];
        columnNames = [columnNamesCollection objectEnumerator];
    }
    
    if(groupBy == nil) {
        NSArray *groupByCollection = [[NSArray alloc] init];
        groupBy = [groupByCollection objectEnumerator];
    }
    
    if(having == nil) {
        having = [[NSString alloc] init];
    }
    
    if(orderBy == nil) {
        NSArray *orderByCollection = [[NSArray alloc] init];
        orderBy = [orderByCollection objectEnumerator];
    }
    
    if(whichOrderBy == nil) {
        whichOrderBy = [[NSString alloc] init];
    }
    
    if(limit == nil) {
        limit = [[NSString alloc] init];
    }
    
    
    /*
     * 4. Pass all parameters to executeFetchQuery and get cursor.
     */
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_SELECT_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:[NSNumber numberWithBool:distinct] forKey:FORM_SELECT_QUERY_DISTINCT_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:columnNames forKey:FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER];
    [parameters setObject:groupBy forKey:FORM_SELECT_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_SELECT_QUERY_HAVING_PARAMETER];
    [parameters setObject:orderBy forKey:FORM_SELECT_QUERY_ORDER_BYS_PARAMETER];
    [parameters setObject:whichOrderBy forKey:FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER];
    [parameters setObject:limit forKey:FORM_SELECT_QUERY_LIMIT_PARAMETER];
    
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:[queryBuilder formSelectQuery:parameters]];
    
    NSMutableArray *datasBundle = [[NSMutableArray alloc] init];
    NSString *data = nil;
    
    while(data = [datas nextObject]) {
        [datasBundle addObject:data];
    }
    
    NSEnumerator *tuples =  [SICDatabaseHelper parseAndInflateData:object parentObject:parentObject entityDescriptor:entityDescriptor values:[datasBundle objectEnumerator]];
    datas = [datasBundle objectEnumerator];
    
    /*
     * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
     */
    NSMutableArray *tuplesCollection = [[NSMutableArray alloc] init];
    
    id tuple = nil;
    NSMutableDictionary *tupleData = nil;
    
    if(object != nil) {
        
        while((tuple = [tuples nextObject]) && (tupleData = [datas nextObject])) {
            
            [tuplesCollection addObject:tuple];
            
            //[SICDatabaseHelper processOneToOneRelationship:tuple];
            //[SICDatabaseHelper processOneToManyRelationship:tuple];
            
            //[SICDatabaseHelper processManyToManyRelationship:tuple data:tupleData];
            //[SICDatabaseHelper processManyToManyRelationship:tuple data:tupleData];
            
            [SICRelationshipHelper processRelationship:tuple parentObject:parentObject];
            [SICRelationshipHelper processRelationship:tuple parentObject:parentObject data:tupleData];
        }
    }
    
    return tuplesCollection;
}


+(id) select:(id const)object query:(NSString * const)query {
    [SICSiminov isActive];
    
    [query stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    NSUInteger index = 0;
    NSRange range = [query rangeOfString: DATABASE_QUERY_FROM_TABLE_INDEX];
    if (range.length == 0 && range.location > query.length) {
        index = 0;
    } else {
        index = range.location+5;
    }
    
    NSString *temp = [query substringFromIndex:index];
    
    NSUInteger spaceIndex = 0;
    NSRange spaceRange = [query rangeOfString:@" "];
    if (spaceRange.length == 0 && spaceRange.location > query.length) {
        spaceIndex = 0;
    } else {
        spaceIndex = spaceRange.location;
    }
    
    NSString *tableName = [temp substringToIndex:spaceIndex];
    
    /*
     * 1. Get entity descriptor object for mapped invoked class object.
     */
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptorBasedOnTableName:tableName];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"fetchManual" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@", NSStringFromClass(object)]];
        @throw [[SICDeploymentException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"fetchManual" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",NSStringFromClass(object)]];
    }
    
    
    /*
     * 4. Pass all parameters to executeFetchQuery and get cursor.
     */
    NSEnumerator *tuples = [SICDatabaseHelper parseAndInflateData:object parentObject:nil entityDescriptor:nil values:[database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])] entityDescriptor:nil query:query]];
    
    /*
     * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
     */
    NSMutableArray *tuplesCollection = [[NSMutableArray alloc] init];
    id tuple;
    
    while(tuple = [tuples nextObject]) {
        [tuplesCollection addObject:tuple];
    }
    
    return tuplesCollection;
}


+ (void)save:(id const)object {
    [SICDatabaseHelper save:object parentObject:nil];
}

+ (void)save:(id const)object parentObject:(id const)parentObject {
    [SICSiminov isActive];
    
    /*
     * 1. Get mapped entity descriptor object for object parameter class name.
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
     * 3. Using QueryBuilder form insert bind query.
     * 4. Pass query to executeBindQuery method for insertion.
     * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
     */
    
    if(object == nil) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"save" message:@"Invalid Object Found."];
        return;
    }
    
    /*
     * 1. Get mapped entity descriptor object for invoked class object.
     */
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    /*
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
     */
    NSString *tableName = [entityDescriptor getTableName];
    
    NSMutableArray *columnNames = [[NSMutableArray alloc] init];
    NSMutableArray *columnValues = [[NSMutableArray alloc] init];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        @try {
            id columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            if(columnValue == nil) {
                continue;
            }
            
            [columnNames addObject:[attribute getColumnName]];
            [columnValues addObject:columnValue];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME:%@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[siminovException getMessage]];
        }
    }
    
    
    [SICRelationshipHelper processRelationship:object parentObject:nil columnNames:columnNames columnValues:columnValues];
    
    
    /*
     * 3. Using QueryBuilder form insert bind query.
     */
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:tableName forKey:FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:[columnNames objectEnumerator] forKey:FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER];
    
    NSString *query = [queryBuilder formSaveBindQuery:parameters];
    
    
    /*
     * 4. Pass query to executeBindQuery method for insertion.
     */
    
    [database executeBindQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query columnValues:[columnValues objectEnumerator]];
    
    /*
     * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
     */
    NSEnumerator *relationships = [entityDescriptor getRelationships];
    SICRelationship *relationship = nil;
    
    while(relationship = [relationships nextObject]) {
        
        bool isLoad = [relationship isLoad];
        if(!isLoad) {
            continue;
        }
        
        NSString *relationshipType = [relationship getRelationshipType];
        if(relationshipType == nil || relationshipType.length <= 0) {
            continue;
        }
        
        if ([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE] == NSOrderedSame) {
            
            if(parentObject != nil && [[relationship getReferTo] caseInsensitiveCompare:NSStringFromClass([parentObject class])] == NSOrderedSame) {
                continue;
            }
            
            id referedObject = nil;
            @try {
                referedObject = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[siminovException getMessage]];
            }
            
            
            if(referedObject == nil) {
                continue;
            }
            
            /*
             * Inflate Dependent Object
             */
            SICEntityDescriptor *referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([referedObject class])];
            SICRelationship *referedRelationship = [referedEntityDescriptor getRelationshipBasedOnReferTo:NSStringFromClass([object class])];
            
            NSString *methodName = [NSString stringWithFormat:@"%@:", [referedRelationship getSetterReferMethodName]];
            
            @try {
                [SICClassUtils invokeMethodBasedOnMethodName:referedObject methodName:methodName parameterTypes:[NSArray arrayWithObject:[object class]] parameters:[NSArray arrayWithObject:object]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"SiminovException caught while setting up one to one relationship mapping, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[siminovException getMessage]];
            }
            
            
            [self saveOrUpdate:referedObject parentObject:object];
        } else if([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY] == NSOrderedSame) {
            
            id values = nil;
            @try {
                values = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[siminovException getMessage]];
            }
            
            
            if(values == nil) {
                continue;
            }
            
            id value;
            while(value = [values nextObject]) {
                [SICDatabaseHelper saveOrUpdate:value];
            }
        } else if([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY] == NSOrderedSame) {
            
            id values = nil;
            @try {
                values = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"save" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"save" message:[siminovException getMessage]];
            }
            
            
            if(values == nil) {
                continue;
            }
            
            [SICDatabaseHelper saveOrUpdate:values parentObject:object];
        }
    }
}


+ (void)update:(id const)object {
    [SICDatabaseHelper update: object parentObject:nil];
}

+ (void)update:(id const)object parentObject:(id const)parentObject {
    
    [SICSiminov isActive];
    
    /*
     * 1. Get mapped entity descriptor object for object parameter class name.
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
     * 3. Form where clause based on primary keys for updation purpose.
     * 4. Using QueryBuilder form update bind query.
     * 5. Pass query to executeBindQuery method for updation.
     * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
     */
    
    if(object == nil) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"update" message:@"Invalid Object Found."];
        return;
    }
    
    
    /*
     * 1. Get mapped entity descriptor object for invoked class object.
     */
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableString *whereClause = [[NSMutableString alloc] init];
    NSString *tableName = [entityDescriptor getTableName];
    
    /*
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
     */
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    NSMutableArray *columnNames = [[NSMutableArray alloc] init];
    NSMutableArray *columnValues = [[NSMutableArray alloc] init];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        id columnValue = nil;
        @try {
            columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            if(columnValue == nil) {
                continue;
            }
            
            [columnNames addObject:[attribute getColumnName]];
            [columnValues addObject:columnValue];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"update" message:[siminovException getMessage]];
        }
        
        if([attribute isPrimaryKey]) {
            
            if(whereClause.length == 0) {
                [whereClause appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
            } else {
                [whereClause appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
            }
        }
    }
    
    
    [SICRelationshipHelper processRelationship:object parentObject:parentObject whereClause:whereClause];
    [SICRelationshipHelper processRelationship:object parentObject:parentObject columnNames:columnNames columnValues:columnValues];
    
    
    
    /*
     * 4. Using QueryBuilder form update bind query.
     */
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:tableName forKey:FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:[columnNames objectEnumerator] forKey:FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER];
    [parameters setObject:(NSString *)whereClause forKey:FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER];
    
    NSString *query = [queryBuilder formUpdateBindQuery:parameters];
    
    /*
     * 5. Pass query to executeBindQuery method for updation.
     */
    
    NSEnumerator *values = [columnValues objectEnumerator];
    [database executeBindQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query columnValues:values];
    
    /*
     * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
     */
    NSEnumerator *relationships = [entityDescriptor getRelationships];
    SICRelationship *relationship = nil;
    
    while(relationship = [relationships nextObject]) {
        
        bool isLoad = [relationship isLoad];
        
        if(!isLoad) {
            continue;
        }
        
        NSString *relationshipType = [relationship getRelationshipType];
        if(relationshipType == nil || relationshipType.length <= 0) {
            continue;
        }
        
        if ([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE] == NSOrderedSame) {
            
            id value = nil;
            @try {
                value = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"update" message:[siminovException getMessage]];
            }
            
            
            if(value == nil) {
                continue;
            }
            
            [self saveOrUpdate:value];
        } else if([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY] == NSOrderedSame) {
            
            id relationshipValues = nil;
            @try {
                relationshipValues = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"update" message:[siminovException getMessage]];
            }
            
            
            if(relationshipValues == nil) {
                continue;
            }
            
            id value;
            while(value = [values nextObject]) {
                [SICDatabaseHelper saveOrUpdate:value];
            }
        } else if([relationshipType caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY] == NSOrderedSame) {
            
            id relationshipValues = nil;
            @try {
                relationshipValues = [SICClassUtils getValue:object methodName:[relationship getGetterReferMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"update" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"update" message:[siminovException getMessage]];
            }
            
            
            if(values == nil) {
                continue;
            }
            
            id value;
            while(value = [relationshipValues nextObject]) {
                [SICDatabaseHelper saveOrUpdate:value];
            }
        }
    }
}


+ (void)saveOrUpdate:(id const)object {
    [SICDatabaseHelper saveOrUpdate:object parentObject:nil];
}


+ (void)saveOrUpdate:(id const)object parentObject:(id const)parentObject {
    
    [SICSiminov isActive];
    
    /*
     * 1. Get mapped entity object for object class name.
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
     * 3. Form where clause based on primary keys to fetch objects from database table. IF EXISTS: call update method, ELSE: class save method.
     * 4. IF EXISTS: call update method, ELSE: call save method.
     */
    
    if(object == nil) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"saveOrUpdate" message:@"Invalid Object Found."];
        return;
    }
    
    
    /*
     * 1. Get mapped entity object for object class name.
     */
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"saveOrUpdate" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"saveOrUpdate" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    /*
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
     */
    
    NSMutableString *whereClause = [[NSMutableString alloc] init];
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        if([attribute isPrimaryKey]) {
            
            id columnValue = nil;
            @try {
                columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"saveOrUpdate" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"saveOrUpdate" message:[siminovException getMessage]];
            }
            
            if(whereClause.length <= 0) {
                [whereClause appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
            } else {
                [whereClause appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
            }
        }
    }
    
    
    [SICRelationshipHelper processRelationship:object parentObject:nil whereClause:whereClause];
    
    
    if(whereClause == nil || whereClause.length <= 0) {
        [SICDatabaseHelper save:object parentObject:parentObject];
        return;
    }
    
    /*
     * 4. IF EXISTS:call update method, ELSE:call save method.
     */
    int count = [SICDatabaseHelper count:entityDescriptor column:nil distinct:false whereClause:(NSString *)whereClause groupBys:nil having:nil];
    
    if(count <= 0) {
        [SICDatabaseHelper save:object parentObject:parentObject];
    } else {
        [SICDatabaseHelper update:object parentObject:parentObject];
    }
}

+ (void)delete:(id const)object whereClause:(NSString * const)whereClause {
    
    /*
     * 1. Get mapped entity descriptor object for object parameter class name.
     * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
     * 3. Form where clause based on primary keys for deletion purpose.
     * 4. Using QueryBuilder form update bind query.
     * 5. Pass query to executeBindQuery method for deletion.
     * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
     */
    
    if(object == nil) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"delete" message:@"Invalid Object Found."];
        return;
    }
    
    
    /*
     * 1. Get mapped entity descriptor object for object parameter class name.
     */
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:NSStringFromClass([object class])];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"delete" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"delete" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableString *where = [[NSMutableString alloc] init];
    
    if(whereClause == nil || whereClause.length <= 0) {
        NSEnumerator *attributes = [entityDescriptor getAttributes];
        
        SICAttribute *attribute;
        while(attribute = [attributes nextObject]) {
            
            id columnValue = nil;
            @try {
                columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"delete" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"delete" message:[siminovException getMessage]];
            }
            
            /*
             * 3. Form where clause based on primary keys for deletion purpose.
             */
            if([attribute isPrimaryKey]) {
                
                if(where.length == 0) {
                    [where appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
                } else {
                    [where appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
                }
            }
        }
        
        
        [SICRelationshipHelper processRelationship:object parentObject:nil whereClause:where];
    } else {
        [where appendString:whereClause];
    }
    
    
    /*
     * 4. Using QueryBuilder form update bind query.
     */
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_DELETE_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:(NSString *)where forKey:FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER];
    
    NSString *query = [queryBuilder formDeleteQuery:parameters];
    
    /*
     * 5. Pass query to executeBindQuery method for deletion.
     */
    
    [database executeQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
}

+ (int const)count:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column distinct:(bool const)distinct whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"count(%@)",whereClause] message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"count(%@)",whereClause] message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_COUNT_QUERY_TABLE_NAME_PARAMETER];
    
    if (column != nil) {
        [parameters setObject:column forKey:FORM_COUNT_QUERY_COLUMN_PARAMETER];
    }
    
    [parameters setObject:[NSNumber numberWithBool:distinct] forKey:FORM_COUNT_QUERY_DISTINCT_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER];
    
    if (groupBys != nil) {
        [parameters setObject:groupBys forKey:FORM_COUNT_QUERY_GROUP_BYS_PARAMETER];
    }
    
    if (having != nil) {
        [parameters setObject:having forKey:FORM_COUNT_QUERY_HAVING_PARAMETER];
    }
    
    
    NSString *query = [queryBuilder formCountQuery:parameters];
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+ (int const)avg:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"avg(%@)",whereClause] message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR:%@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"avg(%@)",whereClause] message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_AVG_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_AVG_QUERY_COLUMN_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_AVG_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_AVG_QUERY_HAVING_PARAMETER];
    
    
    NSString *query = [queryBuilder formAvgQuery:parameters];
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+ (int const)sum:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"sum" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"sum" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_SUM_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_SUM_QUERY_COLUMN_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_SUM_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_SUM_QUERY_HAVING_PARAMETER];
    
    
    NSString *query = [queryBuilder formSumQuery:parameters];
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+ (int const)total:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"total" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"total" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_TOTAL_QUERY_COLUMN_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_TOTAL_QUERY_HAVING_PARAMETER];
    
    
    NSString *query = [queryBuilder formTotalQuery:parameters];
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+ (int const)min:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"min" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"min" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_MIN_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_MIN_QUERY_COLUMN_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_COUNT_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_COUNT_QUERY_HAVING_PARAMETER];
    
    NSString *query = [queryBuilder formMinQuery:parameters];
    
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+ (int const)max:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"max" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"max" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_MAX_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_MAX_QUERY_COLUMN_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_MAX_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_COUNT_QUERY_HAVING_PARAMETER];
    
    NSString *query = [queryBuilder formMaxQuery:parameters];
    
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            
            if([value isKindOfClass:[NSNumber class]]) {
                const char *objectType = [value objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    return [value intValue];
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    return [value floatValue];
                }
            }
        }
    }
    
    return 0;
}

+(NSString * const)groupConcat:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column delimiter:(NSString * const)delimiter whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having {
    
    [SICSiminov isActive];
    
    SICDatabaseDescriptor *databaseDescriptor = [SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]];
    SICDatabaseBundle *databaseBundle = [resourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
    
    id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
    id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
    
    if(database == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"groupConcat" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"groupConcat" message:[NSString stringWithFormat:@"No Database Instance Found For ENTITY-DESCRIPTOR: %@",[entityDescriptor getClassName]]];
    }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    [parameters setObject:[entityDescriptor getTableName] forKey:FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER];
    [parameters setObject:column forKey:FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER];
    [parameters setObject:delimiter forKey:FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER];
    [parameters setObject:whereClause forKey:FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER];
    [parameters setObject:groupBys forKey:FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER];
    [parameters setObject:having forKey:FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER];
    
    NSString *query = [queryBuilder formGroupConcatQuery:parameters];
    
    NSEnumerator *datas = [database executeSelectQuery:[SICDatabaseHelper getDatabaseDescriptor:[entityDescriptor getClassName]] entityDescriptor:entityDescriptor query:query];
    
    NSDictionary *data;
    while (data = [datas nextObject]) {
        
        NSArray *parse = [data allValues];
        NSEnumerator *values = [parse objectEnumerator];
        
        id value;
        while (value = [values nextObject]) {
            return (NSString *)value;
        }
    }
    
    return nil;
}

+(SICDatabaseDescriptor *)getDatabaseDescriptor:(NSString * const)className {
    return [resourceManager getDatabaseDescriptorBasedOnClassName:className];
}

+(SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnTableName:(NSString * const)tableName {
    return [resourceManager getDatabaseDescriptorBasedOnTableName:tableName];
}


+(SICEntityDescriptor *)getEntityDescriptor:(NSString * const)className {
    return [resourceManager requiredEntityDescriptorBasedOnClassName:className];
}

+(NSString *)getTableName:(id const)object {
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    return [entityDescriptor getTableName];
}

+(NSEnumerator *)getColumnNames:(id const)object {
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    NSMutableArray *columnNames = [[NSMutableArray alloc] init];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        [columnNames addObject:[attribute getColumnName]];
    }
    
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                if ([attribute isNotNull]) {
                    [columnNames addObject:[attribute getColumnName]];
                }
            }
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *parentEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        NSEnumerator *parentAttributes = [parentEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                [columnNames addObject:[attribute getColumnName]];
            }
        }
    }
    
    return [columnNames objectEnumerator];
}

+(NSMutableDictionary *)getColumnValues:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSMutableDictionary *columnNameAndItsValues = [[NSMutableDictionary alloc] init];
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        @try {
            [columnNameAndItsValues setObject:[SICClassUtils getValue:object methodName:[attribute getGetterMethodName]] forKey:[attribute getColumnName]];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[siminovException getMessage]];
        }
    }
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                @try {
                    [columnNameAndItsValues setObject:[SICClassUtils getValue:object methodName:[attribute getGetterMethodName]] forKey:[attribute getColumnName]];
                } @catch(SICSiminovException *siminovException) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[siminovException getMessage]];
                }
            }
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *parentEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        NSEnumerator *parentAttributes = [parentEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                @try {
                    [columnNameAndItsValues setObject:[SICClassUtils getValue:object methodName:[attribute getGetterMethodName]] forKey:[attribute getColumnName]];
                } @catch(SICSiminovException *siminovException) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getColumnValues" message:[siminovException getMessage]];
                }
            }
        }
    }
    
    return columnNameAndItsValues;
}

+(NSMutableDictionary *)getColumnTypes:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSMutableDictionary *columnTypes = [[NSMutableDictionary alloc] init];
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        [columnTypes setObject:[attribute getType] forKey:[attribute getColumnName]];
    }
    
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                [columnTypes setObject:[attribute getType] forKey:[attribute getColumnName]];
            }
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *parentEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        NSEnumerator *parentAttributes = [parentEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                [columnTypes setObject:[attribute getType] forKey:[attribute getColumnName]];
            }
        }
    }
    
    return columnTypes;
}

+(NSEnumerator *)getPrimaryKeys:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    NSMutableArray *primaryKeys = [[NSMutableArray alloc] init];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        bool isPrimary = [attribute isPrimaryKey];
        if(isPrimary) {
            [primaryKeys addObject:[attribute getColumnName]];
        }
    }
    
    return [primaryKeys objectEnumerator];
}

+(NSEnumerator *)getMandatoryFields:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    NSMutableArray *isMandatoryFieldsVector = [[NSMutableArray alloc] init];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        if([attribute isNotNull]) {
            [isMandatoryFieldsVector addObject:[attribute getColumnName]];
        }
    }
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                if ([attribute isNotNull]) {
                    [isMandatoryFieldsVector addObject:[attribute getColumnName]];
                }
            }
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *parentEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        NSEnumerator *parentAttributes = [parentEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                if ([attribute isNotNull]) {
                    [isMandatoryFieldsVector addObject:[attribute getColumnName]];
                }
            }
        }
    }
    
    return [isMandatoryFieldsVector objectEnumerator];
}

+(NSEnumerator *)getUniqueFields:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    NSMutableArray *isUniqueFieldsVector = [[NSMutableArray alloc] init];
    
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        if([attribute isUnique]) {
            [isUniqueFieldsVector addObject:[attribute getColumnName]];
        }
    }
    
    
    /*
     * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
     */
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                if ([attribute isUnique]) {
                    [isUniqueFieldsVector addObject:[attribute getColumnName]];
                }
            }
        }
    }
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *parentEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        NSEnumerator *parentAttributes = [parentEntityDescriptor getAttributes];
        
        SICAttribute *attribute = nil;
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                if ([attribute isUnique]) {
                    [isUniqueFieldsVector addObject:[attribute getColumnName]];
                }
            }
        }
    }
    return [isUniqueFieldsVector objectEnumerator];
}

+(NSEnumerator *)getForeignKeys:(id const)object {
    
    [SICSiminov isActive];
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    
    NSMutableArray *attributes = [SICDatabaseHelper getForeignKeysForEntityDescriptor:entityDescriptor];
    NSEnumerator *attributesIterate = [attributes objectEnumerator];
    
    NSMutableArray *foreignKeys = [[NSMutableArray alloc] init];
    SICAttribute *attribute;
    
    while(attribute = [attributesIterate nextObject]) {
        [foreignKeys addObject:[attribute getColumnName]];
    }
    
    return [foreignKeys objectEnumerator];
}

+(NSMutableArray *)getForeignKeysForEntityDescriptor:(SICEntityDescriptor *)entityDescriptor {
    
    NSEnumerator *oneToManyRelationships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    NSMutableArray *foreignAttributes = [[NSMutableArray alloc]init];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    
    SICAttribute *attribute = nil;
    while(attribute = [attributes nextObject]) {
        
        if([attribute isPrimaryKey]) {
            [foreignAttributes addObject:attribute];
        }
    }
    
    SICRelationship *oneToManyRelationship = nil;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        
        NSMutableArray *referedForeignKeys = [SICDatabaseHelper getForeignKeysForEntityDescriptor:referedEntityDescriptor];
        NSEnumerator *referedForeignKeysIterate = [referedForeignKeys objectEnumerator];
        
        SICAttribute *attribute = nil;
        while(attribute = [referedForeignKeysIterate nextObject]) {
            [foreignAttributes addObject:attribute];
        }
    }
    
    
    SICRelationship *manyToManyRelationship = nil;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        
        NSMutableArray *referedForeignKeys = [SICDatabaseHelper getForeignKeysForEntityDescriptor:referedEntityDescriptor];
        NSEnumerator *referedForeignKeysIterate = [referedForeignKeys objectEnumerator];
        
        SICAttribute *attribute = nil;
        while(attribute = [referedForeignKeysIterate nextObject]) {
            [foreignAttributes addObject:attribute];
        }
    }
    
    return foreignAttributes;
}

+(NSEnumerator *)parseAndInflateData:(id const)object parentObject:parentObject entityDescriptor:(SICEntityDescriptor * const)entityDescriptor values:(NSEnumerator * const)values {
    
    [SICSiminov isActive];
    
    NSMutableArray *tuples = [[NSMutableArray alloc] init];
    NSDictionary *value = nil;
    
    while(value = [values nextObject]) {
        
        NSArray *columnNames = [value allKeys];
        NSEnumerator *columnNamesIterate = [columnNames objectEnumerator];
        
        NSMutableDictionary *data = [[NSMutableDictionary alloc] init];
        NSString *columnName = nil;
        
        while(columnName = [columnNamesIterate nextObject]) {
            
            if([entityDescriptor containsAttributeBasedOnColumnName:columnName]) {
                [data setObject:[value objectForKey:columnName] forKey:[[entityDescriptor getAttributeBasedOnColumnName:columnName] getSetterMethodName]];
            }
        }
        
        id inflateObject = nil;
        NSString *className;
        
        if(entityDescriptor == nil) {
            className = NSStringFromClass([object class]);
        } else {
            className = [entityDescriptor getClassName];
        }
        
        @try {
            inflateObject = [SICClassUtils createAndInflateObject:className data:data];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"parseAndInflateData" message:[NSString stringWithFormat:@"SiminovException caught while create and inflate object through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"parseAndInflateData" message:[siminovException getMessage]];
        }
        
        [tuples addObject:inflateObject];
    }
    
    return [tuples objectEnumerator];
}


@end



@implementation SICRelationshipHelper

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject {
    
    [SICRelationshipHelper processOneToOneRelationship:object parentObject:parentObject];
    [SICRelationshipHelper processOneToManyRelationship:object parentObject:parentObject];
    [SICRelationshipHelper processManyToOneRelationship:object parentObject:parentObject];
    [SICRelationshipHelper processManyToManyRelationship:object parentObject:parentObject];
}

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues {
    
    [SICRelationshipHelper processOneToOneRelationship:object parentObject:parentObject columnNames:columnNames columnValues:columnValues];
    [SICRelationshipHelper processOneToManyRelationship:object parentObject:parentObject columnNames:columnNames columnValues:columnValues];
    [SICRelationshipHelper processManyToOneRelationship:object parentObject:parentObject columnNames:columnNames columnValues:columnValues];
    [SICRelationshipHelper processManyToManyRelationship:object parentObject:parentObject columnNames:columnNames columnValues:columnValues];
}

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause {
    
    [SICRelationshipHelper processOneToOneRelationship:object parentObject:parentObject whereClause:whereClause];
    [SICRelationshipHelper processOneToManyRelationship:object parentObject:parentObject whereClause:whereClause];
    [SICRelationshipHelper processManyToOneRelationship:object parentObject:parentObject whereClause:whereClause];
    [SICRelationshipHelper processManyToManyRelationship:object parentObject:parentObject whereClause:whereClause];
}

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data {
    
    [SICRelationshipHelper processOneToOneRelationship:object parentObject:parentObject data:data];
    [SICRelationshipHelper processOneToManyRelationship:object parentObject:parentObject data:data];
    [SICRelationshipHelper processManyToOneRelationship:object parentObject:parentObject data:data];
    [SICRelationshipHelper processManyToManyRelationship:object parentObject:parentObject data:data];
}


+ (void)processOneToOneRelationship:(id const)object parentObject:(id const)parentObject {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *oneToOneRelationships = [entityDescriptor getOneToOneRelationships];
    
    SICRelationship *oneToOneRelationship;
    while(oneToOneRelationship = [oneToOneRelationships nextObject]) {
        
        bool isLoad = [oneToOneRelationship isLoad];
        if(!isLoad) {
            continue;
        }
        
        
        NSMutableString *whereClause = [[NSMutableString alloc] init];
        NSEnumerator *foreignKeys = [SICDatabaseHelper getPrimaryKeys:object];
        
        NSString *foreignKey;
        
        while(foreignKey = [foreignKeys nextObject]) {
            
            SICAttribute *attribute = [entityDescriptor getAttributeBasedOnColumnName:foreignKey];
            id columnValue = nil;
            
            @try {
                columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while getting column value through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[attribute getGetterMethodName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
            }
            
            if(whereClause.length <= 0) {
                [whereClause appendString:[NSString stringWithFormat:@"%@='%@'",foreignKey,(NSString *)columnValue]];
            } else {
                [whereClause appendString:[NSString stringWithFormat:@" AND %@='%@'",foreignKey,(NSString *)columnValue]];
            }
        }
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToOneRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToOneRelationship getReferTo]];
            
            [oneToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        
        if(object != nil && [NSStringFromClass([object class]) caseInsensitiveCompare:[referedEntityDescriptor getClassName]] == NSOrderedSame) {
            continue;
        }
        
        id referedObject = [SICDatabaseHelper select:nil parentObject:nil entityDescriptor:referedEntityDescriptor distinct:false where:whereClause columnName:nil groupBy:nil having:nil orderBy:nil whichOrderBy:nil limit:nil];
        id referedObjects = referedObject;
        
        if(referedObjects == nil || [(NSArray *)referedObjects count] <= 0) {
            continue;
        }
        
        if(referedObjects[0] == nil) {
            continue;
        }
        
        NSString *methodName = [NSString stringWithFormat:@"%@:", [oneToOneRelationship getSetterReferMethodName]];
        
        @try {
            [SICClassUtils invokeMethodBasedOnMethodName:object methodName:methodName parameterTypes:[NSArray arrayWithObject:[referedObjects[0] class]] parameters:[NSArray arrayWithObject:referedObjects[0]]];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[oneToOneRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
        }
        
    }
}

+ (void)processOneToManyRelationship:(id const)object parentObject:(id const)parentObject {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *oneToManyRelationships = [entityDescriptor getOneToManyRelationships];
    
    SICRelationship *oneToManyRelationship;
    while(oneToManyRelationship = [oneToManyRelationships nextObject]) {
        
        bool isLoad = [oneToManyRelationship isLoad];
        if(!isLoad) {
            continue;
        }
        
        NSMutableString *whereClause = [[NSMutableString alloc] init];
        NSEnumerator *foreignKeys = [SICDatabaseHelper getPrimaryKeys:object];
        
        NSString *foreignKey;
        
        while(foreignKey = [foreignKeys nextObject]) {
            
            SICAttribute *attribute = [entityDescriptor getAttributeBasedOnColumnName:foreignKey];
            id columnValue = nil;
            
            @try {
                columnValue = [SICClassUtils getValue:object methodName:[attribute getGetterMethodName]];
            } @catch(SICSiminovException *siminovException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while getting column value through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[attribute getGetterMethodName],[siminovException getMessage]]];
                @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[siminovException getMessage]];
            }
            
            if(whereClause.length <= 0) {
                [whereClause appendString:[NSString stringWithFormat:@"%@='%@'",foreignKey,(NSString *)columnValue]];
            } else {
                [whereClause appendString:[NSString stringWithFormat:@" AND %@='%@'",foreignKey,(NSString *)columnValue]];
            }
        }
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToManyRelationship getReferTo]];
            
            [oneToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = [SICDatabaseHelper select:nil parentObject:nil entityDescriptor:referedEntityDescriptor distinct:false where:whereClause columnName:[[[NSMutableArray alloc]init] objectEnumerator] groupBy:[[[NSMutableArray alloc] init] objectEnumerator] having:@"" orderBy:[[[NSMutableArray alloc] init] objectEnumerator] whichOrderBy:@"" limit:@""];
        
        id referedObjects = referedObject;
        
        NSMutableArray *referedCollection = [[NSMutableArray alloc] init];
        if(referedObjects != nil && [(NSArray *)referedObjects count] > 0) {
            for(int i = 0;i < [(NSArray *)referedObjects count]; i++) {
                [referedCollection addObject:referedObjects[i]];
            }
        }
        
        NSString *methodName = [NSString stringWithFormat:@"%@:", [oneToManyRelationship getSetterReferMethodName]];
        
        @try {
            [SICClassUtils invokeMethodBasedOnMethodName:object methodName:methodName parameterTypes:[NSArray arrayWithObject:[NSEnumerator class]] parameters:[NSArray arrayWithObject:[referedCollection objectEnumerator]]];
        } @catch(SICSiminovException *siminovException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[oneToManyRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[siminovException getMessage]];
        }
    }
}


+ (void)processManyToOneRelationship:(id const)object parentObject:(id const)parentObject {
    
}

+ (void)processManyToManyRelationship:(id const)object parentObject:(id const)parentObject {
    
}


+(void)processOneToOneRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *oneToOneRelationships = [entityDescriptor getOneToOneRelationships];
    
    SICRelationship *oneToOneRelationship;
    while(oneToOneRelationship = [oneToOneRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [oneToOneRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToOneRelationship getReferTo]];
            [oneToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[oneToOneRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[oneToOneRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[oneToOneRelationship getReferTo]]];
            
            continue;
            //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[oneToOneRelationship getReferTo]];
        }
        
        
        [SICRelationshipHelper processOneToManyRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        [SICRelationshipHelper processManyToOneRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        [SICRelationshipHelper processManyToManyRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                @try {
                    id columnValue = [SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]];
                    if(columnValue == nil) {
                        continue;
                    }
                    
                    [columnNames addObject:[attribute getColumnName]];
                    [columnValues addObject:columnValue];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",NSStringFromClass([referedObject class]),[attribute getGetterMethodName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
                }
            }
        }
    }
}


+ (void)processOneToManyRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues {
    
}



+(void)processManyToOneRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToOneRelationships = [entityDescriptor getManyToOneRelationships];
    
    SICRelationship *manyToOneRelationship;
    while(manyToOneRelationship = [manyToOneRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToOneRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToOneRelationship getReferTo]];
            [manyToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[manyToOneRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[manyToOneRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[manyToOneRelationship getReferTo]]];
            
            continue;
            //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[manyToOneRelationship getReferTo]];
        }
        
        
        [SICRelationshipHelper processOneToOneRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        [SICRelationshipHelper processManyToOneRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        [SICRelationshipHelper processManyToManyRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                @try {
                    id columnValue = [SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]];
                    if(columnValue == nil) {
                        continue;
                    }
                    
                    [columnNames addObject:[attribute getColumnName]];
                    [columnValues addObject:columnValue];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",NSStringFromClass([referedObject class]),[attribute getGetterMethodName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[siminovException getMessage]];
                }
            }
        }
    }
}


+ (void)processOneToOneRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *oneToOneRelationships = [entityDescriptor getOneToOneRelationships];
    
    SICRelationship *oneToOneRelationship;
    while(oneToOneRelationship = [oneToOneRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [oneToOneRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[oneToOneRelationship getReferTo]];
            [oneToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[oneToOneRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[oneToOneRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[oneToOneRelationship getReferTo]]];
            
            continue;
            //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[oneToOneRelationship getReferTo]];
        }
        
        [SICRelationshipHelper processOneToManyRelationship:referedObject parentObject:object whereClause:whereClause];
        [SICRelationshipHelper processManyToOneRelationship:referedObject parentObject:object whereClause:whereClause];
        [SICRelationshipHelper processManyToManyRelationship:referedObject parentObject:object whereClause:whereClause];
        
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                id columnValue = nil;
                @try {
                    columnValue = [SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToOneRelationship" message:[siminovException getMessage]];
                }
                
                if(NSStringFromClass([columnValue class]) == NSStringFromClass([NSNumber class])) {
                    columnValue = [NSString stringWithFormat:@"%d", [(NSNumber *)columnValue intValue]];
                }
                
                if (whereClause.length <= 0) {
                    [whereClause appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
                } else {
                    [whereClause appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
                }
            }
        }
    }
}


+ (void)processOneToManyRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause {
    
}


+ (void)processManyToOneRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToOneRelationships = [entityDescriptor getManyToOneRelationships];
    
    SICRelationship *manyToOneRelationship;
    while(manyToOneRelationship = [manyToOneRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToOneRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToOneRelationship getReferTo]];
            [manyToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[manyToOneRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[manyToOneRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[manyToOneRelationship getReferTo]]];
            
            continue;
            //return;
        }
        
        [SICRelationshipHelper processOneToOneRelationship:referedObject parentObject:object whereClause:whereClause];
        [SICRelationshipHelper processManyToOneRelationship:referedObject parentObject:object whereClause:whereClause];
        [SICRelationshipHelper processManyToManyRelationship:referedObject parentObject:object whereClause:whereClause];
        
        
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                id columnValue = nil;
                @try {
                    columnValue = [SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[siminovException getMessage]];
                }
                
                if (whereClause.length <= 0) {
                    [whereClause appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
                } else {
                    [whereClause appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
                }
            }
        }
    }
}


+ (void)processOneToOneRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data {
    
}

+ (void)processOneToManyRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data {
    
}


+ (void)processManyToOneRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToOneRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *manyToOneRelationship;
    while(manyToOneRelationship = [manyToOneRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToOneRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToOneRelationship getReferTo]];
            [manyToOneRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[manyToOneRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"Unable To Create Parent Relationship. REFER-TO: %@",[manyToOneRelationship getReferTo]]];
            
            continue;
            //return;
        }
        
        
        [SICRelationshipHelper processOneToOneRelationship:referedObject parentObject:object data:data];
        [SICRelationshipHelper processManyToOneRelationship:referedObject parentObject:object data:data];
        [SICRelationshipHelper processManyToManyRelationship:referedObject parentObject:object data:data];
        
        
        if([manyToOneRelationship isLoad]) {
            
            NSMutableString *whereClause = [[NSMutableString alloc]init];
            NSEnumerator *foreignKeys = [SICDatabaseHelper getPrimaryKeys:referedObject];
            
            NSString *foreignKey;
            
            while(foreignKey = [foreignKeys nextObject]) {
                
                SICAttribute *attribute = [referedEntityDescriptor getAttributeBasedOnColumnName:foreignKey];
                id columnValue = [data objectForKey:[attribute getColumnName]];
                
                if(whereClause.length <= 0) {
                    [whereClause appendString:[NSString stringWithFormat:@"%@='%@'",foreignKey,(NSString *)columnValue]];
                } else {
                    [whereClause appendString:[NSString stringWithFormat:@" AND %@='%@'",foreignKey,(NSString *)columnValue]];
                }
            }
            
            
            id fetchedObjects = [SICDatabaseHelper select:nil parentObject:nil entityDescriptor:referedEntityDescriptor distinct:false where:whereClause columnName:[[[NSMutableArray alloc]init] objectEnumerator] groupBy:[[[NSMutableArray alloc] init] objectEnumerator] having:@"" orderBy:[[[NSMutableArray alloc] init] objectEnumerator] whichOrderBy:@"" limit:@""];
            
            
            if(fetchedObjects == nil || [fetchedObjects length] <= 0) {
                continue;
                //return;
            }
            
            referedObject = fetchedObjects;
            
        } else {
            
            NSEnumerator *foreignKeys = [SICDatabaseHelper getPrimaryKeys:referedObject];
            NSString *foreignKey;
            
            while(foreignKey = [foreignKeys nextObject]) {
                
                SICAttribute *attribute = [referedEntityDescriptor getAttributeBasedOnColumnName:foreignKey];
                id columnValue = [data objectForKey:[attribute getColumnName]];
                
                if (columnValue == nil) {
                    continue;
                }
                
                @try {
                    [SICClassUtils invokeMethodBasedOnMethodName:referedObject methodName:[attribute getSetterMethodName] parameterTypes:[NSArray arrayWithObject:[columnValue class]] parameters:[NSArray arrayWithObject:columnValue]];
                }
                @catch (SICSiminovException *siminovException) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@",[referedEntityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processOneToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@",[referedEntityDescriptor getClassName],[siminovException getMessage]]];
                }
            }
        }
        
        if(referedObject == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"Unable To Create Parent Relationship. REFER-TO: %@",[manyToOneRelationship getReferTo]]];
            
            continue;
            //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"Unable To Create Parent Relationship. REFER-TO: %@",[manyToOneRelationship getReferTo]]];
        }
        
        
        NSString *methodName = [NSString stringWithFormat:@"%@:", [manyToOneRelationship getSetterReferMethodName]];
        
        @try {
            [SICClassUtils invokeMethodBasedOnMethodName:object methodName:methodName parameterTypes:[NSArray arrayWithObject:[referedObject class]] parameters:[NSArray arrayWithObject:referedObject]];
            
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[entityDescriptor getClassName],[manyToOneRelationship getSetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[manyToOneRelationship getSetterReferMethodName],[siminovException getMessage]]];
        }
    }
}

+ (void)processManyToManyRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *manyToManyRelationship;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToManyRelationship getReferTo]];
            [manyToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[manyToManyRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[manyToManyRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[manyToManyRelationship getReferTo]]];
            
            continue;
            //return;
        }
        
        [SICRelationshipHelper processRelationship:referedObject parentObject:object columnNames:columnNames columnValues:columnValues];
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                @try {
                    [columnNames addObject:[attribute getColumnName]];
                    [columnValues addObject:[SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]]];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method values through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[siminovException getMessage]];
                }
            }
        }
    }
}

+ (void)processManyToManyRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *manyToManyRelationship;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToManyRelationship getReferTo]];
            [manyToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = nil;
        @try {
            referedObject = [SICClassUtils getValue:object methodName:[manyToManyRelationship getGetterReferMethodName]];
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[manyToManyRelationship getGetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[siminovException getMessage]];
        }
        
        if(referedObject == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[manyToManyRelationship getReferTo]]];
            
            continue;
            //return;
        }
        
        [SICRelationshipHelper processRelationship:referedObject parentObject:parentObject whereClause:whereClause];
        
        NSEnumerator *parentAttributes = [referedEntityDescriptor getAttributes];
        SICAttribute *attribute;
        
        while(attribute = [parentAttributes nextObject]) {
            
            bool isPrimary = [attribute isPrimaryKey];
            if(isPrimary) {
                id columnValue = nil;
                @try {
                    columnValue = [SICClassUtils getValue:referedObject methodName:[attribute getGetterMethodName]];
                } @catch(SICSiminovException *siminovException) {
                    
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while get method value through reflection, CLASS-NAME: %@, %@",[entityDescriptor getClassName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[siminovException getMessage]];
                }
                
                if (whereClause.length <= 0) {
                    [whereClause appendString:[NSString stringWithFormat:@"%@= '%@'",[attribute getColumnName],columnValue]];
                } else {
                    [whereClause appendString:[NSString stringWithFormat:@" AND %@= '%@'",[attribute getColumnName],columnValue]];
                }
            }
        }
    }
}

+ (void)processManyToManyRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data {
    
    SICEntityDescriptor *entityDescriptor = [SICDatabaseHelper getEntityDescriptor:NSStringFromClass([object class])];
    NSEnumerator *manyToManyRelationships = [entityDescriptor getManyToManyRelationships];
    
    SICRelationship *manyToManyRelationship;
    while(manyToManyRelationship = [manyToManyRelationships nextObject]) {
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        if (referedEntityDescriptor == nil) {
            referedEntityDescriptor = [SICDatabaseHelper getEntityDescriptor:[manyToManyRelationship getReferTo]];
            [manyToManyRelationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        id referedObject = [SICClassUtils createClassInstance:[[manyToManyRelationship getReferedEntityDescriptor] getClassName]];
        if(referedObject == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: %@",[manyToManyRelationship getReferTo]]];
            
            continue;
            //return;
        }
        
        [SICRelationshipHelper processRelationship:referedObject parentObject:parentObject data:data];
        
        if([manyToManyRelationship isLoad]) {
            
            NSMutableString *whereClause = [[NSMutableString alloc]init];
            NSEnumerator *foreignKeys = [SICDatabaseHelper getPrimaryKeys:referedObject];
            
            NSString *foreignKey;
            
            while(foreignKey = [foreignKeys nextObject]) {
                
                SICAttribute *attribute = [referedEntityDescriptor getAttributeBasedOnColumnName:foreignKey];
                id columnValue = [data objectForKey:[attribute getColumnName]];
                
                if(whereClause.length <= 0) {
                    [whereClause appendString:[NSString stringWithFormat:@"%@='%@'",foreignKey,(NSString *)columnValue]];
                } else {
                    [whereClause appendString:[NSString stringWithFormat:@" AND %@='%@'",foreignKey,(NSString *)columnValue]];
                }
            }
            
            id fetchedObjects = [SICDatabaseHelper select:object parentObject:parentObject entityDescriptor:referedEntityDescriptor distinct:false where:whereClause columnName:[[[NSMutableArray alloc]init] objectEnumerator] groupBy:[[[NSMutableArray alloc] init] objectEnumerator] having:@"" orderBy:[[[NSMutableArray alloc] init] objectEnumerator] whichOrderBy:@"" limit:@""];
            
            referedObject = fetchedObjects;
            
        } else {
            
            NSEnumerator *primaryKeys = [SICDatabaseHelper getPrimaryKeys:referedObject];
            NSString *foreignKey;
            
            while(foreignKey = [primaryKeys nextObject]) {
                
                SICAttribute *attribute = [referedEntityDescriptor getAttributeBasedOnColumnName:foreignKey];
                id columnValue = [data objectForKey:[attribute getColumnName]];
                
                if (columnValue == nil) {
                    continue;
                }
                
                @try {
                    [SICClassUtils invokeMethodBasedOnMethodName:referedObject methodName:[attribute getSetterMethodName] parameterTypes:[NSArray arrayWithObject:[columnValue class]] parameters:[NSArray arrayWithObject:columnValue]];
                }
                @catch (SICSiminovException *siminovException) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[referedEntityDescriptor getClassName],[attribute getSetterMethodName],[siminovException getMessage]]];
                    @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME: %@, %@",[referedEntityDescriptor getClassName],[attribute getSetterMethodName],[siminovException getMessage]]];
                }
            }
        }
        
        if(referedObject == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"Unable To Create Parent Relationship. REFER-TO: %@",[manyToManyRelationship getReferTo]]];
            
            continue;
            //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToManyRelationship" message:[NSString stringWithFormat:@"Unable To Create Parent Relationship. REFER-TO: %@",[manyToManyRelationship getReferTo]]];
        }
        
        NSString *methodName = [NSString stringWithFormat:@"%@:", [manyToManyRelationship getSetterReferMethodName]];
        
        @try {
            [SICClassUtils invokeMethodBasedOnMethodName:object methodName:methodName parameterTypes:[NSArray arrayWithObject:[referedObject class]] parameters:[NSArray arrayWithObject:referedObject]];
            
        } @catch(SICSiminovException *siminovException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[manyToManyRelationship getSetterReferMethodName],[siminovException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processManyToOneRelationship" message:[NSString stringWithFormat:@"SiminovException caught while invoking method through reflection, CLASS-NAME: %@, METHOD-NAME:  %@, %@",[entityDescriptor getClassName],[manyToManyRelationship getSetterReferMethodName],[siminovException getMessage]]];
        }
    }
}

@end


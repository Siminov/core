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

#import "SICQueryBuilder.h"
#import "SICEntityDescriptor.h"
#import "SICResourceManager.h"

/**
 * Provides the SICIQueryBuilder implementation for SQLite
 */
@interface SICQueryBuilder (PrivateMethods)

- (NSString *)formSelectQuery:(NSString * const)table distinct:(BOOL const)distinct whereClause:(NSString * const)whereClause columnsNames:(NSEnumerator * const)columnsNames groupBys:(NSString * const)groupBys having:(NSString * const)having orderBys:(NSString * const)orderBys whichOrderBy:(NSString * const)whichOrderBy limit:(NSString * const)limit;
+ (void)appendClause:(NSMutableString * const)s name:(NSString * const)name clause:(NSString * const)clause;
+ (void)appendColumns:(NSMutableString * const)s columns:(NSEnumerator * const)columns;
-(NSMutableArray *)getForeignKeys:(SICEntityDescriptor *)entityDescriptor;

@end

@implementation SICQueryBuilder (PrivateMethods)

- (NSString *)formSelectQuery:(NSString * const)table distinct:(BOOL const)distinct whereClause:(NSString * const)whereClause columnsNames:(NSEnumerator * const)columnsNames groupBys:(NSString * const)groupBys having:(NSString * const)having orderBys:(NSString * const)orderBys whichOrderBy:(NSString * const)whichOrderBy limit:(NSString * const)limit {
    
    if (groupBys != nil && groupBys.length <=0 && having != nil && having.length <= 0) {
        //@throw [[NSException alloc]initWithName:NSStringFromClass([self class]) reason:@"HAVING clauses are only permitted when using a groupBy clause" userInfo:nil];
    }
    
    /*Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
     if (!TextUtils.isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
     throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
     }*/
    
    NSMutableString *query = [[NSMutableString alloc]initWithCapacity:120];
    
    [query appendString:@"SELECT "];
    if(distinct) {
        [query appendString:@"DISTINCT "];
    } else {
        
        int columnNamesCount = 0;
        if(columnsNames != nil) {
            
            NSString *columnsName;
            if (columnsName = [columnsNames nextObject]) {
                [SICQueryBuilder appendColumns:query columns:columnsNames];
            }
        }
        
        if(columnNamesCount <= 0) {
            [query appendString:@"* "];
        }
    }
    
    
    [query appendString:@" FROM "];
    [query appendString:table];
    [SICQueryBuilder appendClause:query name:@" WHERE " clause:whereClause];
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:groupBys];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    if(whichOrderBy != nil && whichOrderBy.length > 0) {
        [SICQueryBuilder appendClause:query name:[NSString stringWithFormat:@" ORDER BY %@",orderBys] clause:having];
    } else {
        [SICQueryBuilder appendClause:query name:@" ORDER BY " clause:orderBys];
    }
    
    [SICQueryBuilder appendClause:query name:@" LIMIT " clause:limit];
    
    return (NSString *)query;
}

+ (void)appendClause:(NSMutableString * const)s name:(NSString * const)name clause:(NSString * const)clause {
    
    if (clause != nil && clause.length >0) {
        [s appendString:name];
        [s appendString:clause];
        
    }
}

+ (void)appendColumns:(NSMutableString * const)s columns:(NSEnumerator * const)columns {
    
    int index = 0;
    NSString *column;
    
    while(column = [columns nextObject]) {
        
        if (column != nil) {
            if (index > 0) {
                [s appendString:@", "];
            }
            
            [s appendString:column];
            index++;
        }
    }
    
    [s appendString:@" "];
}

- (NSMutableArray *)getForeignKeys:(SICEntityDescriptor *)entityDescriptor {
    
    NSEnumerator *oneToManyRealtionships = [entityDescriptor getManyToOneRelationships];
    NSEnumerator *manyToManyRealtionships = [entityDescriptor getManyToManyRelationships];
    
    NSMutableArray *foreignAttributes = [[NSMutableArray alloc]init];
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    SICAttribute *attribute;
    while(attribute = [attributes nextObject]) {
        
        if([attribute isPrimaryKey]) {
            [foreignAttributes addObject:attribute];
        }
    }
    
    SICRelationship *oneToManyRelationship;
    while(oneToManyRelationship = [oneToManyRealtionships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [oneToManyRelationship getReferedEntityDescriptor];
        
        NSMutableArray *referedForeignKeys = [self getForeignKeys:referedEntityDescriptor];
        NSEnumerator *referedForeignKeysIterate = [referedForeignKeys objectEnumerator];
        
        NSString *referedForeignKey;
        while(referedForeignKey = [referedForeignKeysIterate nextObject]) {
            [foreignAttributes addObject:referedForeignKey];
        }
    }
    
    SICRelationship *manyToManyRelationship;
    while(manyToManyRelationship = [manyToManyRealtionships nextObject]) {
        
        SICEntityDescriptor *referedEntityDescriptor = [manyToManyRelationship getReferedEntityDescriptor];
        
        NSMutableArray *referedForeignKeys = [self getForeignKeys:referedEntityDescriptor];
        NSEnumerator *referedForeignKeysIterate = [referedForeignKeys objectEnumerator];
        
        NSString *referedForeignKey;
        while(referedForeignKey = [referedForeignKeysIterate nextObject]) {
            [foreignAttributes addObject:referedForeignKey];
        }
    }
    
    return foreignAttributes;
}

@end

@implementation SICQueryBuilder

- (NSString *)formTableInfoQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER];
    return [NSString stringWithFormat:@"pragma table_info(%@)",tableName];
}

- (NSString *)formFetchDatabaseVersionQuery:(NSMutableDictionary * const)parameters {
    return @"PRAGMA user_version;";
}

- (NSString *)formUpdateDatabaseVersionQuery:(NSMutableDictionary * const)parameters {
    
    double const databaseVersion = [[parameters objectForKey:FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER] doubleValue];
    return [NSString stringWithFormat:@"PRAGMA user_version=%lf",databaseVersion];
}

- (NSString *)formAlterAddColumnQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER];
    NSString * const columnName = (NSString *)[parameters objectForKey:FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER];
    
    return [NSString stringWithFormat:@"ALTER TABLE %@ ADD COLUMN %@ TEXT",tableName,columnName];
}

-(NSString *)formTableNames:(NSMutableDictionary * const)parameters {
    return @"SELECT * FROM sqlite_master WHERE type='table'";
}

- (NSString *)formCreateTableQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER];
    NSEnumerator * const columnNames = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER];
    NSEnumerator * const columnTypes = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER];
    NSEnumerator * const defaultValues = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER];
    NSEnumerator * const checks = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER];
    NSEnumerator * const primaryKeys = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER];
    NSEnumerator * const isNotNull = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER];
    NSEnumerator * const uniqueColumns = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER];
    NSString * const foreignKeys = (NSString *)[parameters objectForKey:FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER];
    
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (",tableName]];
    
    int index = 0;
    
    NSString *columnName;
    NSString *columnType;
    
    id notNull;
    
    NSString *defaultValue;
    NSString *check;
    
    while((columnName = [columnNames nextObject]) && (columnType = [columnTypes nextObject]) && (notNull= [isNotNull nextObject]) && (defaultValue = [defaultValues nextObject]) && (check = [checks nextObject])) {
        
        if(index == 0) {
            [query appendString:[NSString stringWithFormat:@"%@ %@",columnName,columnType]];
        } else {
            [query appendString:[NSString stringWithFormat:@", %@ %@",columnName,columnType]];
        }
        
        if([notNull boolValue]) {
            [query appendString:[NSString stringWithFormat:@" NOT NULL"]];
        }
        
        if(defaultValue != nil && defaultValue.length > 0) {
            [query appendString:[NSString stringWithFormat:@" DEFAULT '%@'",defaultValue]];
        }
        
        if(check != nil && check.length > 0) {
            [query appendString:[NSString stringWithFormat:@" CHECK('%@)",check]];
        }
        
        index++;
    }
    
    NSMutableString *primaryKey = [[NSMutableString alloc]init];
    
    index = 0;
    
    BOOL isPrimaryKeysPresent = false;
    [primaryKey appendString:@"PRIMARY KEY("];
    
    NSString *primary;
    
    while(primary = [primaryKeys nextObject]) {
        
        if(index == 0) {
            [primaryKey appendString:primary];
            isPrimaryKeysPresent = true;
        } else {
            [primaryKey appendString:[NSString stringWithFormat:@", %@",primary]];
        }
        
        index++;
    }
    [primaryKey appendString:@")"];
    
    NSMutableString *uniqueColumn = [[NSMutableString alloc]init];
    
    index = 0;
    
    BOOL isUniqueKeysPresent = false;
    [uniqueColumn appendString:@"UNIQUE ("];
    
    NSString *column;
    while(column = [uniqueColumns nextObject]) {
        
        if(index == 0) {
            [uniqueColumn appendString:column];
            isUniqueKeysPresent = true;
        } else {
            [uniqueColumn appendString:[NSString stringWithFormat:@", %@",column]];
        }
        
        index++;
    }
    
    [uniqueColumn appendString:@")"];
    
    if(isPrimaryKeysPresent) {
        [query appendString:[NSString stringWithFormat:@", %@",primaryKey]];
    }
    
    if(isUniqueKeysPresent) {
        [query appendString:[NSString stringWithFormat:@", %@",uniqueColumn]];
    }
    
    if(foreignKeys.length > 0) {
        [query appendString:[NSString stringWithFormat:@", %@",foreignKeys]];
    }
    
    [query appendString:@")"];
    
    return (NSString *)query;
}

- (NSString *)formCreateIndexQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const indexName = (NSString *)[parameters objectForKey:FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER];
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER];
    NSEnumerator * const columnNames = (NSEnumerator *)[parameters objectForKey:FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER];
    BOOL const isUnique = (BOOL) [parameters objectForKey:FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    
    if(isUnique) {
        [query appendString:[NSString stringWithFormat:@"CREATE UNIQUE INDEX IF NOT EXISTS %@ ON %@(",indexName,tableName]];
    } else {
        [query appendString:[NSString stringWithFormat:@"CREATE INDEX IF NOT EXISTS %@ ON %@(",indexName,tableName]];
    }
    
    int index = 0;
    NSString *columnName;
    
    while(columnName= [columnNames nextObject]) {
        if(index == 0) {
            [query appendString:columnName];
        } else {
            [query appendString:[NSString stringWithFormat:@", %@",columnName]];
        }
        
        index++;
    }
    
    [query appendString:@")"];
    return (NSString *)query;
}

- (NSString *)formDropTableQuery:(NSMutableDictionary * const)parameters {
    
    const NSString *tableName = (NSString *)[parameters objectForKey:FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"DROP TABLE IF EXISTS %@",tableName]];
    
    return (NSString *)query;
}

- (NSString *)formDropIndexQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER];
    NSString * const indexName = (NSString *)[parameters objectForKey:FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"DROP INDEX IF EXISTS %@ ON %@",indexName,tableName]];
    
    return (NSString *)query;
}

- (NSString *)formSelectQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_SELECT_QUERY_TABLE_NAME_PARAMETER];
    BOOL const distinct = [[parameters objectForKey:FORM_SELECT_QUERY_DISTINCT_PARAMETER] boolValue];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const columnNames = (NSEnumerator *)[parameters objectForKey:FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_SELECT_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_SELECT_QUERY_HAVING_PARAMETER];
    NSEnumerator * const orderBy = (NSEnumerator *)[parameters objectForKey:FORM_SELECT_QUERY_ORDER_BYS_PARAMETER];
    NSString * const whichOrderBy = (NSString *)[parameters objectForKey:FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER];
    NSString * const limit = (NSString *)[parameters objectForKey:FORM_SELECT_QUERY_LIMIT_PARAMETER];
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    NSMutableString *orderBysBuilder = [[NSMutableString alloc]init];
    
    index = 0;
    if(orderBy != nil) {
        
        NSString *orderByString;
        while(orderByString = [orderBy nextObject]) {
            if(index == 0) {
                [orderBysBuilder appendString:orderByString];
            } else {
                [orderBysBuilder appendString:[NSString stringWithFormat:@", %@",orderByString]];
            }
            index++;
        }
    }
    
    return [self formSelectQuery:tableName distinct:distinct whereClause:whereClause columnsNames: columnNames groupBys:(NSString *)groupBysBuilder having:having orderBys:(NSString *)orderBysBuilder whichOrderBy:whichOrderBy limit:limit];
    
}

- (NSString *)formSaveBindQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER];
    NSEnumerator * const columnNames = (NSEnumerator *)[parameters objectForKey:FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"INSERT INTO %@(",tableName]];
    
    int index = 0;
    NSString *columnName;
    
    while(columnName = [columnNames nextObject]) {
        if(index == 0) {
            [query appendString:columnName];
        } else {
            [query appendString:[NSString stringWithFormat:@", %@",columnName]];
        }
        
        index++;
    }
    
    [query appendString:@") VALUES("];
    
    for(int i = 0;i < index;i++) {
        if(i == 0) {
            [query appendString:@"?"];
        } else {
            [query appendString:@", ?"];
        }
    }
    
    [query appendString:@")"];
    return (NSString *)query;
}

- (NSString *)formUpdateBindQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER];
    NSEnumerator * const columnNames = (NSEnumerator *)[parameters objectForKey:FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    
    [query appendString:[NSString stringWithFormat:@"UPDATE %@ SET ",tableName]];
    
    int index = 0;
    NSString *columnName;
    
    while(columnName = [columnNames nextObject]) {
        if(index == 0) {
            [query appendString:[NSString stringWithFormat:@"%@= ?",columnName]];
        } else {
            [query appendString:[NSString stringWithFormat:@", %@= ?",columnName]];
        }
        
        index++;
    }
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@",whereClause]];
    }
    
    return (NSString *)query;
}

-(NSString *)formDeleteQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_DELETE_QUERY_TABLE_NAME_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    
    [query appendString:[NSString stringWithFormat:@"DELETE FROM %@",tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@",whereClause]];
    }
    
    return (NSString *)query;
}


- (NSString *)formCountQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_COUNT_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_COUNT_QUERY_COLUMN_PARAMETER];
    BOOL const distinct = [[parameters objectForKey:FORM_COUNT_QUERY_DISTINCT_PARAMETER] boolValue];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_COUNT_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_COUNT_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    
    if(column != nil && column.length > 0) {
        if(distinct) {
            [query appendString:[NSString stringWithFormat:@"SELECT COUNT(DISTINCT %@ ) FROM %@",column,tableName]];
        } else {
            [query appendString:[NSString stringWithFormat:@"SELECT COUNT(%@) FROM %@",column,tableName]];
        }
    } else {
        [query appendString:[NSString stringWithFormat:@"SELECT COUNT(*) FROM %@",tableName]];
    }
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@",whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formAvgQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_AVG_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_AVG_QUERY_COLUMN_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_AVG_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_AVG_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"SELECT AVG(%@) FROM %@",column,tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formSumQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_SUM_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_SUM_QUERY_COLUMN_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_SUM_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_SUM_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"SELECT SUM(%@) FROM %@",column,tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formTotalQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_TOTAL_QUERY_COLUMN_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_TOTAL_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"SELECT TOTAL(%@) FROM %@",column,tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formMaxQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_MAX_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_MAX_QUERY_COLUMN_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_MAX_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_MAX_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"SELECT MAX(%@) FROM %@",column,tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formMinQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_MIN_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_MIN_QUERY_COLUMN_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_MIN_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_MIN_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    [query appendString:[NSString stringWithFormat:@"SELECT MIN(%@) FROM %@",column,tableName]];
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formGroupConcatQuery:(NSMutableDictionary * const)parameters {
    
    NSString * const tableName = (NSString *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER];
    NSString * const column = (NSString *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER];
    NSString * const delimiter = (NSString *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER];
    NSString * const whereClause = (NSString *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER];
    NSEnumerator * const groupBys = (NSEnumerator *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER];
    NSString * const having = (NSString *)[parameters objectForKey:FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER];
    
    NSMutableString *query = [[NSMutableString alloc]init];
    if (delimiter == nil || delimiter.length <= 0) {
        [query appendString:[NSString stringWithFormat:@"SELECT GROUP_CONCAT(%@) FROM %@",column,tableName]];
    } else {
        [query appendString:[NSString stringWithFormat:@"SELECT GROUP_CONCAT(%@) FROM %@",column,tableName]];
    }
    
    if(whereClause != nil && whereClause.length > 0) {
        [query appendString:[NSString stringWithFormat:@" WHERE %@", whereClause]];
    }
    
    NSMutableString *groupBysBuilder = [[NSMutableString alloc]init];
    
    int index = 0;
    if(groupBys != nil) {
        
        NSString *groupBy;
        while(groupBy = [groupBys nextObject]) {
            if(index == 0) {
                [groupBysBuilder appendString:groupBy];
            } else {
                [groupBysBuilder appendString:[NSString stringWithFormat:@", %@",groupBy]];
            }
            
            index++;
        }
    }
    
    [SICQueryBuilder appendClause:query name:@" GROUP BY " clause:(NSString *)groupBysBuilder];
    [SICQueryBuilder appendClause:query name:@" HAVING " clause:having];
    
    return (NSString *)query;
}

- (NSString *)formForeignKeyQuery:(NSMutableDictionary * const)parameters {
    
    const SICEntityDescriptor *child = (SICEntityDescriptor *)[parameters objectForKey:FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER];
    
    NSEnumerator *oneToManyRealtionships = [child getManyToOneRelationships];
    NSEnumerator *manyToManyRealtionships = [child getManyToManyRelationships];
    
    NSMutableArray *relationships = [[NSMutableArray alloc]init];
    
    SICRelationship *oneToManyRelationship;
    while(oneToManyRelationship = [oneToManyRealtionships nextObject]) {
        [relationships addObject:oneToManyRelationship];
    }
    
    SICRelationship *manyToManyRelationship;
    while(manyToManyRelationship = [manyToManyRealtionships nextObject]) {
        [relationships addObject:manyToManyRelationship];
    }
    
    NSMutableString *foreignKeysQuery = [[NSMutableString alloc]init];
    
    NSEnumerator *relationshipsIterator = [relationships objectEnumerator];
    SICRelationship *relationship;
    
    while(relationship = [relationshipsIterator nextObject]) {
        
        NSMutableString *foreignKeyQuery = [[NSMutableString alloc]init];
        
        SICEntityDescriptor *referedEntityDescriptor = [relationship getReferedEntityDescriptor];
        if(referedEntityDescriptor == nil) {
            
            referedEntityDescriptor = [[SICResourceManager getInstance] requiredEntityDescriptorBasedOnClassName:[relationship getReferTo]];
            [relationship setReferedEntityDescriptor:referedEntityDescriptor];
            [relationship setReferedEntityDescriptor:referedEntityDescriptor];
        }
        
        
        NSString *parentTable = [referedEntityDescriptor getTableName];
        NSMutableArray *foreignAttributes = nil;
        @try {
            foreignAttributes = [self getForeignKeys:referedEntityDescriptor];
        } @catch(SICDatabaseException *databaseException) {
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"formForeignKeys" message:[NSString stringWithFormat:@"Database Exception caught while getting foreign columns, %@",[databaseException getMessage]]];
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"formForeignKeys" message:[databaseException getMessage]];
        }
        
        NSEnumerator *foreignAttributesIterate = [foreignAttributes objectEnumerator];
        
        NSMutableArray *foreignKeys = [[NSMutableArray alloc]init];
        SICAttribute *foreignAttribute;
        
        while(foreignAttribute = [foreignAttributesIterate nextObject]) {
            [foreignKeys addObject:[foreignAttribute getColumnName]];
        }
        
        NSEnumerator *foreignKeysIterate = [foreignKeys objectEnumerator];
        
        [foreignKeyQuery appendString:@"FOREIGN KEY("];
        
        int index = 0;
        NSString *foreignKey;
        while(foreignKey = [foreignKeysIterate nextObject]) {
            if(index == 0) {
                [foreignKeyQuery appendString:foreignKey];
            } else {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@", %@",foreignKey]];
            }
            
            index++;
        }
        
        [foreignKeyQuery appendString:[NSString stringWithFormat:@") REFERENCES %@(",parentTable]];
        foreignKeysIterate = [foreignKeys objectEnumerator];
        
        index = 0;
        NSString *foreignKeyIterate;
        while(foreignKeyIterate = [foreignKeysIterate nextObject]) {
            if(index == 0) {
                [foreignKeyQuery appendString:foreignKeyIterate];
            } else {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@", %@",foreignKeyIterate]];
            }
            
            index++;
        }
        
        [foreignKeyQuery appendString:@")"];
        
        NSString *onDeleteAction = [relationship getOnDelete];
        NSString *onUpdateAction = [relationship getOnUpdate];
        
        if(onDeleteAction != nil && onDeleteAction.length > 0) {
            if([onDeleteAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_DELETE,QUERY_BUILDER_CASCADE]];
            } else if([onDeleteAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_DELETE,QUERY_BUILDER_RESTRICT]];
            } else if([onDeleteAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_DELETE,QUERY_BUILDER_NO_ACTION]];
            } else if([onDeleteAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_DELETE,QUERY_BUILDER_SET_NULL]];
            } else if([onDeleteAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_DELETE,QUERY_BUILDER_SET_DEFAULT]];
            }
        }
        
        if(onUpdateAction != nil && onUpdateAction.length > 0) {
            if([onUpdateAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_UPDATE,QUERY_BUILDER_CASCADE]];
            } else if([onUpdateAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_UPDATE,QUERY_BUILDER_RESTRICT]];
            } else if([onUpdateAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_UPDATE,QUERY_BUILDER_NO_ACTION]];
            } else if([onUpdateAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_UPDATE,QUERY_BUILDER_SET_NULL]];
            } else if([onUpdateAction caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT] == NSOrderedSame) {
                [foreignKeyQuery appendString:[NSString stringWithFormat:@" %@ %@",QUERY_BUILDER_ON_UPDATE,QUERY_BUILDER_SET_DEFAULT]];
            }
        }
        
        if(foreignKeyQuery.length > 0) {
            if(foreignKeysQuery.length <= 0) {
                [foreignKeysQuery appendString:[NSString stringWithFormat:@" %@",foreignKeyQuery]];
            } else {
                [foreignKeysQuery appendString:[NSString stringWithFormat:@", %@",foreignKeyQuery]];
            }
        }
    }
    
    return (NSString *)foreignKeysQuery;
}

@end

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


#import "SICDatabaseImpl.h"
#import "SICDatabaseUtils.h"
#import "SICIDataTypeHandler.h"
#import "SICLog.h"
#import "SICDeploymentException.h"

@implementation SICDatabaseImpl

- (void)openOrCreate:(SICDatabaseDescriptor * const)databaseDescriptor {
    [SICLog debug:NSStringFromClass([self class]) methodName:@"openOrCreate" message:@"open or create database"];
    
    [UIApplication sharedApplication];
    
    NSString *databasePath = [[[SICDatabaseUtils alloc] init] getDatabasePath:databaseDescriptor];
    
    NSFileManager *file = [NSFileManager defaultManager];
	NSError *theError = nil;
    
    if (![file fileExistsAtPath:databasePath]) {
    	@try {
            [file createDirectoryAtPath:databasePath withIntermediateDirectories:YES attributes:nil error:&theError];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"Exception caught while creating database directories, DATABASE-PATH:%@, DATABASE-DESCRIPTOR: %@, %@",databasePath,[databaseDescriptor getDatabaseName],[exception reason]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"Exception caught while creating database directories, DATABASE-PATH:%@, DATABASE-DESCRIPTOR: %@, %@",databasePath,[databaseDescriptor getDatabaseName],[exception reason]]];
        }
	}
    
    NSString *databaseName = [databaseDescriptor getDatabaseName];
    if (databaseName == nil || databaseName.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: %@",[databaseDescriptor getDatabaseName]]];
    }
    
    if(![databaseName hasSuffix:@".db"]) {
        databaseName = [NSString stringWithFormat:@"%@.db",databaseName];
    }
    
    @try {
        NSString *dbPath = [NSString stringWithFormat:@"%@%@",databasePath,databaseName];
        [SICLog debug:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"db path: %@", dbPath]];
        
        sqlite3_shutdown();
        sqlite3_config(SQLITE_CONFIG_SERIALIZED);
        sqlite3_initialize();
        
        if (sqlite3_open([dbPath UTF8String], &sqliteDatabase) != SQLITE_OK) {
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"SQLiteException caught while opening database"]];
        }
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"SQLiteException caught while opening database, %@",[exception reason]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"openOrCreate" message:[NSString stringWithFormat:@"SQLiteException caught while opening database, %@",[exception reason]]];
    }
    
    [SICLog debug:NSStringFromClass([self class]) methodName:@"openOrCreate" message:@"Database created"];
}

- (void)close:(id)databaseDescriptor {
    sqlite3_close(sqliteDatabase);
}

- (void)executeQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * const)query {
    [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeQuery(%@)",query] message:[NSString stringWithFormat:@"QUERY: %@",query]];

    @try {
        int result = sqlite3_exec(sqliteDatabase, [query UTF8String],NULL,NULL,NULL);
        if (result != SQLITE_OK) {
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while executing query, QUERY: %@",query]];
        }
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while executing query, QUERY: %@,%@",query,[exception reason]]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while executing query, QUERY: %@,%@",query,[exception reason]]];
    }
}

- (void)executeBindQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * const)query columnValues:(NSEnumerator * const)columnValues {
    
    [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:[NSString stringWithFormat:@"QUERY: %@",query]];
    
    sqlite3_stmt *statement = nil;
    int databasePrepareResult;
    
    @try {
        databasePrepareResult = sqlite3_prepare_v2(sqliteDatabase,[query UTF8String],-1,&statement,NULL);
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while compiling statement, %@",[exception reason]]];
        
        int index =0;
        id columnValue;
        
        while (columnValue = [columnValues nextObject]) {
            
            if ([columnValue isKindOfClass:[NSString class]]) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %@",index,columnValue]];
            } else if([columnValue isKindOfClass:[NSNumber class]]) {
                const char *objectType = [columnValue objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %d",index,[columnValue intValue]]];
                    
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %ld",index,[columnValue longValue]]];
                    
                } else if(strcmp(objectType, @encode(double)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %f",index,[columnValue doubleValue]]];
                    
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %f",index,[columnValue floatValue]]];
                    
                } else if(strcmp(objectType, @encode(BOOL)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %@",index,[(NSString *)columnValue boolValue]? @"TRUE" : @"FALSE"]];
                }
            } else if ([columnValue isKindOfClass:[NSData class]]) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %@",index,[columnValue bytes]]];
            } else {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-INDEX: %d, VALUE: %@",index,columnValue]];
            }
            
            index++;
        }
        //@throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while compiling statement, %@",[exception reason]]];
        return;
    }
    
    [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:@"After preparing statement"];
    
    NSMutableArray *duplicateColumnValues = [[NSMutableArray alloc]init];
    
    int index =0;
    id object;
    while (object = [columnValues nextObject]) {
        
        if ([object isKindOfClass:[NSString class]])
        {
            sqlite3_bind_text(statement, index+1, [object UTF8String], -1, SQLITE_TRANSIENT);
        }
        else if([object isKindOfClass:[NSNumber class]])
        {
            const char *objectType = [object objCType];
            
            if (strcmp(objectType, @encode(int)) == 0)
            {
                sqlite3_bind_int(statement, index+1, [object intValue]);
                
            }else if(strcmp(objectType, @encode(float)) == 0)
            {
                sqlite3_bind_double(statement, index+1, [object floatValue]);
            }
            else if(strcmp(objectType, @encode(double)) == 0)
            {
                sqlite3_bind_double(statement, index+1, [object doubleValue]);
            }
            else if(strcmp(objectType, @encode(long)) == 0)
            {
                sqlite3_bind_int64(statement, index+1, [object longValue]);
            }
        }
        else if([object isKindOfClass:[NSData class]])
        {
            // The fourth argument should return the number of bytes to write to
            sqlite3_bind_blob(statement, index+1, [object bytes], 0, SQLITE_TRANSIENT);
            
        }else{
            
            sqlite3_bind_null(statement, index+1);
        }
         [duplicateColumnValues addObject:object];
        index++;
    }
    
    [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:@"Before execute statement"];
    
    @try {
        int databaseResult = sqlite3_step(statement);
        if(databaseResult != SQLITE_DONE) 	{
            @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"SQLiteException caught while executing statement, QUERY: %@", query]];
        }
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while compiling statement, %@",[exception reason]]];
        
        index = 0;
        NSEnumerator *newColumnValues = [duplicateColumnValues objectEnumerator];
        id columnValue;
        
        while (columnValue = [newColumnValues nextObject]) {
            
            if ([columnValue isKindOfClass:[NSString class]]) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %@",index,columnValue]];
            } else if([columnValue isKindOfClass:[NSNumber class]]) {
                const char *objectType = [columnValue objCType];
                
                if (strcmp(objectType, @encode(int)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %d",index,[columnValue intValue]]];
                    
                } else if(strcmp(objectType, @encode(long)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %ld",index,[columnValue longValue]]];
                    
                } else if(strcmp(objectType, @encode(double)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %f",index,[columnValue doubleValue]]];
                    
                } else if(strcmp(objectType, @encode(float)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %f",index,[columnValue floatValue]]];
                    
                } else if(strcmp(objectType, @encode(bool)) == 0) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %@",index,[(NSString *)columnValue boolValue]? @"TRUE" : @"FALSE"]];
                }
            } else if ([columnValue isKindOfClass:[NSData class]]) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %@",index,[columnValue bytes]]];
            } else {
                [SICLog error:NSStringFromClass([self class]) methodName:@"executeBindQuery" message:[NSString stringWithFormat:@"COLUMN-NAME: %d, VALUE: %@",index,columnValue]];
            }
            
            index++;
        }
        
        sqlite3_finalize(statement);
        
        @throw [[SICDatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:[NSString stringWithFormat:@"SQLiteException caught while executing statement, %@",[exception reason]]];
    }
    
    [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"executeBindQuery(%@)",query] message:@"After execute statement"];
    
    sqlite3_finalize(statement);
}

- (NSEnumerator *)executeSelectQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * )query {
    
    NSMutableArray *tuples = [[NSMutableArray alloc]init];
    
    sqlite3_stmt *statement = nil;
    sqlite3_prepare_v2(sqliteDatabase, [query UTF8String], -1, &statement, NULL);
    
    int columnCount = sqlite3_column_count(statement);
    if(statement) {
        
        while (sqlite3_step(statement) == SQLITE_ROW) {
            
            NSMutableDictionary *tuple = [[NSMutableDictionary alloc]init];
            
            for (int i = 0; i < columnCount; i++)
            {
                NSString *columnName = [NSString stringWithUTF8String:(char *)sqlite3_column_name(statement, i)];
                
                switch (sqlite3_column_type(statement, i))
                {
                    case SQLITE_TEXT:
                    {
                        NSString *columnValue = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, i)];
                        if (entityDescriptor != nil) {
                            SICAttribute *attribute = [entityDescriptor getAttributeBasedOnColumnName:columnName];
                            if (attribute != nil) {
                                if ([[attribute getType] caseInsensitiveCompare:STRING_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:columnValue forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:PRIMITIVE_BOOLEAN_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:BOOLEAN_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:STRING_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:(NSString *)columnValue forKey:columnName];
                                }
                            } else {
                                [tuple setObject:columnValue forKey:columnName];
                            }
                        } else {
                            [tuple setObject:columnValue forKey:columnName];
                        }
                    }
                        break;
                    
                    case SQLITE_INTEGER:
                    {
                        NSNumber *columnValue = [NSNumber numberWithInt:(int)sqlite3_column_int(statement, i)];
                        if (entityDescriptor != nil) {
                            SICAttribute *attribute = [entityDescriptor getAttributeBasedOnColumnName:columnName];
                            if (attribute != nil) {
                                if ([[attribute getType] caseInsensitiveCompare:PRIMITIVE_INTEGER_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:columnValue forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:INTEGER_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:PRIMITIVE_LONG_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:LONG_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:(NSString *)columnValue forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:LONG_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:(NSString *)columnValue forKey:columnName];
                                }
                            } else {
                                [tuple setObject:columnValue forKey:columnName];
                            }
                        } else {
                            [tuple setObject:columnValue forKey:columnName];
                        }
                    }
                        break;
                        
                    case SQLITE_FLOAT:
                    {
                        NSNumber *columnValue = [NSNumber numberWithDouble:sqlite3_column_double(statement, i)];
                        if (entityDescriptor != nil) {
                            SICAttribute *attribute = [entityDescriptor getAttributeBasedOnColumnName:columnName];
                            if (attribute != nil) {
                                if ([[attribute getType] caseInsensitiveCompare:PRIMITIVE_DOUBLE_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:columnValue forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:DOUBLE_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:PRIMITIVE_FLOAT_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:[columnValue boolValue]? @"true": @"false" forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:FLOAT_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:(NSString *)columnValue forKey:columnName];
                                } else if ([[attribute getType] caseInsensitiveCompare:FLOAT_DATA_TYPE] == NSOrderedSame) {
                                    [tuple setObject:(NSString *)columnValue forKey:columnName];
                                }
                            } else {
                                [tuple setObject:columnValue forKey:columnName];
                            }
                        } else {
                            [tuple setObject:columnValue forKey:columnName];
                        }
                    }
                        break;
                        
                    case SQLITE_BLOB:
                    {
                        BOOL const *blob = sqlite3_column_blob(statement, i);
                        int bytesLength = sqlite3_column_bytes(statement, i);
                        
                        NSData *columnValue = [NSData dataWithBytes:blob length:bytesLength];
                        [tuple setObject:columnValue forKey:columnName];
                    }
                        break;
                        
                    case SQLITE_NULL: // This null value does not return any value
                        break;
                        
                    default:
                        break;
                }
            }
            [tuples addObject:tuple];
        }
    }
    
    return [tuples objectEnumerator];
}

- (void)executeMethod:(NSString * const)methodName parameter:(id const)parameter {
    
    /*Method method  = nil;
    if ([parameter isKindOfClass:[NSNumber class]]) {
        
        const char *objectType = [parameter objCType];
        
        if (strcmp(objectType, @encode(BOOL)) == 0) {
            @try {
                method = class_getInstanceMethod([sqliteDatabase class],NSSelectorFromString(methodName));
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
                @throw [[DatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
            }
        } else {
            if (parameter == nil) {
                @try {
                    method =
                }
                @catch (NSException *exception) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
                    @throw [[DatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
                }
            } else {
                @try {
                    method =
                }
                @catch (NSException *exception) {
                    [SICLog error:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
                    @throw [[DatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
                }
            }
        }
    }
    
    if (parameter == nil) {
        @try {
            [sqliteDatabase performSelector:method_getName(method)];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
            @throw [[DatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
        }
    } else {
        @try {
           [sqliteDatabase performSelector:method_getName(method)];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString stringWithFormat:@"Exception caught while getting method, METHOD-NAME: %@, %@",methodName,[exception description]]];
            @throw [[DatabaseException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"invokeMethod" message:[NSString
    }*/
}

@end

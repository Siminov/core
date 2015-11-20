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

#import "SICDatabaseFactory.h"
#import "SICClassUtils.h"

@interface SICDatabaseFactory()

-(SICDatabaseBundle *)getDatabaseBundleForPackageName:(NSString *)packageName;

@end


@implementation SICDatabaseFactory

static SICDatabaseFactory *databaseFactory = nil;

NSString *DATABASE_PACKAGE_NAME = @"Siminov.Core.Database";
NSString *DATABASE_CLASS_NAME = @"DatabaseImpl";
NSString *DATABASE_QUERY_BUILDER = @"QueryBuilder";
NSString *DATABASE_DATA_TYPE_HANDLER = @"DataTypeHandler";

- (id)init {
    
    self = [super init];
    
    if (self) {
        databaseBundles = [[NSMutableDictionary alloc] init];
    }
    
    return self;
}

+ (SICDatabaseFactory *)getInstance {

    if(!databaseFactory) {
        databaseFactory = [[super allocWithZone:NULL] init];
    }
    
    return databaseFactory;
}

+ (id)allocWithZone:(NSZone *)zone {
    return [self getInstance];
}

- (SICDatabaseBundle *)getDatabaseBundle:(SICDatabaseDescriptor * const)databaseDescriptor {
    
    if([[databaseBundles allKeys] containsObject:[databaseDescriptor getDatabaseName]]) {
        return [databaseBundles objectForKey:[databaseDescriptor getDatabaseName]];
    }
    
    NSString *type = [databaseDescriptor getType];
    NSString *packageName;
    
    if ([type isEqualToString:@"Sqlite"]) {
        packageName = [NSString stringWithFormat:@"SIC"];
    }  else {
        packageName = [NSString stringWithFormat:@"%@",[type uppercaseString]];
    }
    
    SICDatabaseBundle *databaseBundle = [self getDatabaseBundleForPackageName:packageName];
    [databaseBundles setObject:databaseBundle forKey:[databaseDescriptor getDatabaseName]];
    
    return databaseBundle;
}

- (NSEnumerator *)getDatabaseBundles {
    return [[databaseBundles allValues] objectEnumerator];
}

- (void)removeDatabaseBundle:(SICDatabaseDescriptor * const)databaseDescriptor {
    [databaseBundles removeObjectForKey:[databaseDescriptor getDatabaseName]];
}

///---------------------------------------------------------------------------------------
/// @name Private Methods
///---------------------------------------------------------------------------------------

/**
 * Returns database bundle instance.
 * @param packageName Name of the package.
 * @return SICDatabaseBundle Instance of database bundle.
 */
- (SICDatabaseBundle *)getDatabaseBundleForPackageName:(NSString *)packageName {
    
    id<SICIDatabaseImpl> database = (id<SICIDatabaseImpl>)([SICClassUtils createClassInstance:[NSString stringWithFormat:@"%@%@",packageName,DATABASE_CLASS_NAME]]);
    id<SICIQueryBuilder> queryBuilder = (id<SICIQueryBuilder>)([SICClassUtils createClassInstance:[NSString stringWithFormat:@"%@%@",packageName,DATABASE_QUERY_BUILDER]]);
    id<SICIDataTypeHandler> dataTypeHandler = (id<SICIDataTypeHandler>)([SICClassUtils createClassInstance:[NSString stringWithFormat:@"%@%@",packageName,DATABASE_DATA_TYPE_HANDLER]]);
    
    SICDatabaseBundle *databaseBundle = [[SICDatabaseBundle alloc] init];
    [databaseBundle setDatabase:database];
    [databaseBundle setQueryBuilder:queryBuilder];
    [databaseBundle setDataTypeHandler:dataTypeHandler];
    
    return databaseBundle;
}

@end

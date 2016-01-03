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

#import <Foundation/Foundation.h>
#import "SICIDatabaseImpl.h"
#import "SICIDataTypeHandler.h"
#import "SICIQueryBuilder.h"

/** It is a collection of below database items:

 1. Database Instance
 2. Query Builder Instance
 3. Data Type Handler Instance
 */
@interface SICDatabaseBundle : NSObject {
    id<SICIDatabaseImpl> database;
    id<SICIQueryBuilder> queryBuilder;
    id<SICIDataTypeHandler> dataTypeHandler;
}

/**
 * It returns the database instance.
 * @return SICIDatabaseImpl instance object.
 */
- (id<SICIDatabaseImpl>)getDatabase;

/**
 * It sets the database instance
 * @param database SICIDatabaseImpl instance object.
 */
- (void)setDatabase:(id<SICIDatabaseImpl>)database;

/**
 * It returns the query builder instance
 * @return SICIQueryBuilder SICIQueryBuilder instance object.
 */
- (id<SICIQueryBuilder>)getQueryBuilder;

/**
 * It sets the query builder instance
 * @param queryBuilder SICIQueryBuilder instance object.
 */
- (void)setQueryBuilder:(id<SICIQueryBuilder>)queryBuilder;

/**
 * It returns the data type handler instance.
 * @return SICIDataTypeHandler SICIDataTypeHandlerDataTypeHandler instance object.
 */
- (id<SICIDataTypeHandler>)getDataTypeHandler;

/**
 * It sets the data type handler instance.
 * @param dataTypeHandler SICIDataTypeHandler instance object.
 */
- (void)setDataTypeHandler:(id<SICIDataTypeHandler>)dataTypeHandler;

@end

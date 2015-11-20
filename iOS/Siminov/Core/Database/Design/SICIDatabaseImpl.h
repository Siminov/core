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


#import <Foundation/Foundation.h>
#import "SICDatabaseDescriptor.h"
#import "SICDatabaseException.h"

/**
 * Exposes methods to deal with actual database object.
 * It has methods to open, create, close, and execute query's.
 */
@protocol SICIDatabaseImpl <NSObject>

/** Open/Create the database through Database Descriptor.
 
 By default add CREATE_IF_NECESSARY flag so that if database does not exist it will create.
 
 @param databaseDescriptor Database-Descriptor object which defines the schema of database.
 @exception SICDatabaseException If the database cannot be opened or create.
 */
- (void)openOrCreate:(SICDatabaseDescriptor * const)databaseDescriptor;

/** Close the existing opened database through Database Descriptor.

 @param databaseDescriptor Database-Descriptor object which defines the schema of database.
 @exception SICDatabaseException If the database cannot be closed.
 */
- (void)close:(SICDatabaseDescriptor * const)databaseDescriptor;

/** Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
 
 It has no means to return any data (such as the number of affected rows). Instead, you're encouraged to use insert, update, delete, when possible.
 
 @param databaseDescriptor Database-Descriptor object which defines the schema of database.
 @param entityDescriptor Entity-Descriptor object which defines the structure of table.
 @param query Query which needs to be executed.
 @exception SICDatabaseException If any error occur while executing query provided.
 */
- (void)executeQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * const)query;

/** A pre-compiled statement that can be reused. The statement cannot return multiple rows, but 1x1 result sets are allowed.
 
 @param databaseDescriptor Database-Descriptor object which defines the schema of database.
 @param entityDescriptor Entity-Descriptor object which defines the structure of table.
 @param query A pre-compiled statement.
 @param columnValues Column values
 @exception SICDatabaseException If any error occur while inserting or updating tuple.
 */
- (void)executeBindQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * const)query columnValues:(NSEnumerator * const)columnValues;

/** Query the given table, returning a Cursor over the result set.
 
 @param databaseDescriptor Database-Descriptor object which defines the schema of database.
 @param entityDescriptor Entity-Descriptor object which defines the structure of table.
 @param query Query based on which tuples will be fetched from database.
 @return A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.
 @exception SICDatabaseException If any error occur while getting tuples from a single table.
 */
-(NSEnumerator *)executeSelectQuery:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor query:(NSString * const)query;

/** Executes the method on database object.

 @param methodName Name Of Database Method.
 @param parameters Parameters Needed By Database Method.
 @exception SICDatabaseException If any exeception occur which invoking method in database object.
 */
- (void)executeMethod:(NSString * const)methodName parameter:(id const)parameter;

@end

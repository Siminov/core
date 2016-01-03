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


/**
* Exposes API's to build database queries.
*/


#import <Foundation/Foundation.h>

@protocol SICIQueryBuilder <NSObject>

/** Form Table Info Query
 
 Build query to get table info.
 
 @param parameters Required to build query.
 @return Table Info Query.
 */
- (NSString *)formTableInfoQuery:(NSMutableDictionary * const)parameters;

/** Form Fetch Database Version Query
 
 Build query to get database version.
 
 @param parameters Required to build query.
 @return Fetch Database Version Query.
 */
- (NSString *)formFetchDatabaseVersionQuery:(NSMutableDictionary * const)parameters;

/** Form Update Database Version Query.
 
 Build query to update database version.
 
 @param parameters Required to build query.
 @return Update Database Version Query.
 */
- (NSString *)formUpdateDatabaseVersionQuery:(NSMutableDictionary * const)parameters;

/** Form Alter Add Column Query
 
 Build query to alter add new column to table.
 
 @param parameters Required to build query.
 @return Alter Add New Column Query.
 */
- (NSString *)formAlterAddColumnQuery:(NSMutableDictionary * const)parameters;

/** Form Table Names
 
 Build query to get all table names exists in database.
 
 @param parameters Required to build query.
 @return Table Names Query.
 */
- (NSString *)formTableNames:(NSMutableDictionary * const)parameters;

/** Form Create Table Query
 
 Build query to create table.
 
 @param parameters Required to build query.
 @return Create Table Query.
 */
- (NSString *)formCreateTableQuery:(NSMutableDictionary * const)parameters;

/** Form Create Index Query
 
 Build query to create index.
 
 @param parameters Required to build query.
 @return Create Index Query.
 */
- (NSString *)formCreateIndexQuery:(NSMutableDictionary * const)parameters;

/** Form Drop Table Query
 
 Build query to drop table.
 
 @param parameters Required to build query.
 @return Drop Table Query.
 */
- (NSString *)formDropTableQuery:(NSMutableDictionary * const)parameters;

/** Form Drop Index Query
 
 Build query to drop index from table.
 
 @param parameters Required to build query.
 @return Drop Index Query.
 */
- (NSString *)formDropIndexQuery:(NSMutableDictionary * const)parameters;

/** Form Select Query
 
 Build query to fetch tuples from table.
 
 @param parameters Required to build query.
 @return Select Query.
 */
- (NSString *)formSelectQuery:(NSMutableDictionary * const)parameters;

/** Form Save Bind Query
 
 Build query to insert data into table.
 
 @param parameters Required to build query.
 @return Save Query.
 */
- (NSString *)formSaveBindQuery:(NSMutableDictionary * const)parameters;

/** Form Update Bind Query
 
 Build query to update tuples.
 
 @param parameters Required to build query.
 @return Update Query.
 */
- (NSString *)formUpdateBindQuery:(NSMutableDictionary * const)parameters;

/** Form Delete Query
 
 Build query to delete tuples.
 
 @param parameters Required to build query.
 @return Delete Query.
 */
- (NSString *)formDeleteQuery:(NSMutableDictionary * const)parameters;

/** Form Count Query
 
 Build query to get count of tuples.
 
 @param parameters Required to build query.
 @return Count Query.
 */
- (NSString *)formCountQuery:(NSMutableDictionary * const)parameters;

/** Form Average Query.
 
 Build query to get Average.
 
 @param parameters Required to build query.
 @return Average Query.
 */
- (NSString *)formAvgQuery:(NSMutableDictionary * const)parameters;

/** Form Sum Query.
 
 Build query to get Sum.
 
 @param parameters Required to build query.
 @return Sum Query
 */
- (NSString *)formSumQuery:(NSMutableDictionary * const)parameters;

/** Form Total Query
 
 Build query to get total.
 
 @param parameters Required to build query.
 @return Total Query.
 */
- (NSString *)formTotalQuery:(NSMutableDictionary * const)parameters;

/** Form Max Query.
 
 Build query to get Max.
 
 @param parameters Required to build query.
 @return Max Query.
*/
- (NSString *)formMaxQuery:(NSMutableDictionary * const)parameters;

/** Form Minimum Query
 
 Build query to get Minimum Query.
 
 @param parameters Required to build query.
 @return Minimum Query.
 */
- (NSString *)formMinQuery:(NSMutableDictionary * const)parameters;

/** Form Group Concat Query
 
 Build query to get group concat.
 
 @param parameters Required to build query.
 @return Group Concat Query.
 */
- (NSString *)formGroupConcatQuery:(NSMutableDictionary * const)parameters;

/** Form Foreign Keys Query
 
 Build query to generate foreign keys of table.
 
 @param entityDescriptor Database Descriptor Object.
 @return Generated Query.
 */
- (NSString *)formForeignKeyQuery:(NSMutableDictionary * const)parameters;

@end

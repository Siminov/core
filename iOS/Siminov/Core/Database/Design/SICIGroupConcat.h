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
 * Exposes API's to get group concat that returns a string which is the concatenation of all non-NULL values of X.
 * If parameter Y is present then it is used as the separator between instances of X. A comma (",") is used as the separator if Y is omitted.
 * The order of the concatenated elements is arbitrary.
 */


#import <Foundation/Foundation.h>

@protocol SICIGroupConcatClause;

@protocol SICIGroupConcat <NSObject>

/**
 * Used to specify separator if Y is omitted.
 * @param delimiter Delimiter.
 * @return SICIGroupConcat Interface.
 */
- (id<SICIGroupConcat>)delimiter:(NSString *)delimiter;

/**
 * Column name of which condition will be specified.
 * @param column Name of column.
 * @return SICIGroupConcatClause Interface.
 */
- (id<SICIGroupConcatClause>)where:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereClause Manually created where clause.
 * @return SICIGroupConcat Interface.
 */
- (id<SICIGroupConcat>)whereClause:(NSString *)whereClause;

/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIGroupConcatClause Interface.
 */
- (id<SICIGroupConcatClause>)and:(NSString *)column;

/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIGroupConcatClause Interface.
 */
- (id<SICIGroupConcatClause>)or:(NSString *)column;

/**
 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
 * @param columns Name of columns.
 * @return SICIGroupConcat Interface.
 */
- (id<SICIGroupConcat>)groupBy:(NSArray *)columns;

/**
 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
 * @param column Name of column on which condition need to be applied.
 * @return SICIGroupConcatClause Interface.
 */
- (id<SICIGroupConcatClause>)having:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param havingClause Where clause.
 * @return SICIGroupConcat Interface.
 */
- (id<SICIGroupConcat>)havingClause:(NSString *)havingClause;

/**
 * Used to provide name of column for which average will be calculated.
 * @param column Name of column.
 * @return SICIGroupConcat Interface.
 */
- (id<SICIGroupConcat>)column:(NSString *)column;

/**
 * Used to get average, this method should be called in last to calculate group concat.
 * @return Return group concat.
 * @exception SICDatabaseException Throws exception if any error occur while calculating group concat.
 */
- (id) execute;

@end

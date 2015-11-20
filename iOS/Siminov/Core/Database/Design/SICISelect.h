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

/**
 * Exposes API's to get tuples from table based on information provided.
 */

#import <Foundation/Foundation.h>
#import "SICDatabaseException.h"

@protocol SICISelectClause;

@protocol SICISelect <NSObject>

/**
 * Used to specify DISTINCT condition.
 * @return SICICount Interface.
 */
- (id<SICISelect>)distinct;

/**
 * Column name of which condition will be specified.
 * @param column Name of column.
 * @return SICISelectClause Interface.
 */
- (id<SICISelectClause>)where:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereClause Manually created where clause.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)whereClause:(NSString *)whereClause;

/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICISelectClause Interface.
 */
- (id<SICISelectClause>)and:(NSString *)column;

/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICISelectClause Interface.
 */
- (id<SICISelectClause>)or:(NSString *)column;

/**
 * Used to specify ORDER BY keyword to sort the result-set.
 * @param columns Name of columns which need to be sorted.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)orderBy:(NSArray *)columns;

/**
 * Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
 * @param columns Name of columns which need to be sorted.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)ascendingOrderBy:(NSArray *)columns;

/**
 * Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
 * @param columns Name of columns which need to be sorted.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)descendingOrderBy:(NSArray *)columns;

/**
 * Used to specify the range of data need to fetch from table.
 * @param limit LIMIT of data.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)limit:(int) limit;

/**
 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
 * @param columns Name of columns.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)groupBy:(NSArray *)columns;

/**
 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
 * @param column Name of column on which condition need to be applied.
 * @return SICISelectClause Interface.
 */
- (id<SICISelectClause>)having:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param havingClause Where clause.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)havingClause:(NSString *)havingClause;

/**
 * Used to provide name of column for which average will be calculated.
 * @param column Name of column.
 * @return SICISelect Interface.
 */
- (id<SICISelect>)columns:(NSArray *)column;

/**
 * Used to get average, this method should be called in last to calculate average.
 * @return Return average.
 * @exception SICDatabaseException Throws exception if any error occur while calculating average.
 */
- (id)execute;

@end

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
 * Exposes API's to returns the maximum value of all values in the group.
 * The maximum value is the value that would be returned last in an ORDER BY on the same column.
 * Aggregate max() returns NULL if and only if there are no non-NULL values in the group.
 */


#import <Foundation/Foundation.h>
#import "SICDatabaseException.h"

@protocol SICIMaxClause;

@protocol SICIMax <NSObject>

/**
 * Column name of which condition will be specified.
 * @param column Name of column.
 * @return SICIMaxClause Interface.
 */
- (id<SICIMaxClause>)where:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereClause Manually created where clause.
 * @return SICIMax Interface.
 */
- (id<SICIMax>)whereClause:(NSString *)whereClause;

/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIMaxClause Interface.
 */
- (id<SICIMaxClause>)and:(NSString *)column;

/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIMaxClause Interface.
 */
- (id<SICIMaxClause>)or:(NSString *)column;

/**
 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
 * @param columns Name of columns.
 * @return SICIMax Interface.
 */
- (id<SICIMax>)groupBy:(NSArray *)columns;

/**
 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
 * @param column Name of column on which condition need to be applied.
 * @return SICIMaxClause Interface.
 */
- (id<SICIMaxClause>)having:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param havingClause Where clause.
 * @return SICIMax Interface.
 */
- (id<SICIMax>)havingClause:(NSString *)havingClause;

/**
 * Used to provide name of column for which maximum will be calculated.
 * @param column Name of column.
 * @return SICIMax Interface.
 */
- (id<SICIMax>)column:(NSString *)column;

/**
 * Used to get maximum, this method should be called in last to calculate maximum.
 * @return Return maximum.
 * @exception SICDatabaseException Throws exception if any error occur while calculating maximum.
 */
- (id)execute;

@end

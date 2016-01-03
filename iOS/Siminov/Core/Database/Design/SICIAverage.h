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
#import "SICDatabaseException.h"

@protocol SICIAverageClause;

/** Exposes API's to get average value of all non-NULL X within a group.
 
 String and BLOB values that do not look like numbers are interpreted as 0.
 The result of avg()is always a floating point value as long as at there is at least one non-NULL input even if all inputs are integers.
 The result of avg()is NULL if and only if there are no non-NULL inputs.
 */
@protocol SICIAverage <NSObject>

/**
 * Column name of which condition will be specified.
 * @param column Name of column.
 * @return SICIAverageClause Interface.
 */
- (id<SICIAverageClause>)where:(NSString *)column;
	
/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereClause Manually created where clause.
 * @return SICIAverage Interface.
 */
- (id<SICIAverage>)whereClause:(NSString *)whereClause;
	
/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIAverageClause Interface.
 */
- (id<SICIAverageClause>)and:(NSString *)column;
	
/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIAverageClause Interface.
 */
- (id<SICIAverageClause>)or:(NSString *)column;

/**
 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
 * @param columns Name of columns.
 * @return SICIAverage Interface.
 */
- (id<SICIAverage>)groupBy:(NSArray *)columns;

/**
 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
 * @param column Name of column on which condition need to be applied.
 * @return SICIAverageClause Interface.
 */
- (id<SICIAverageClause>)having:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param havingClause Where clause.
 * @return SICIAverage Interface.
 */
- (id<SICIAverage>)havingClause:(NSString *)havingClause;

/**
 * Used to provide name of column for which average will be calculated.
 * @param column Name of column.
 * @return SICIAverage Interface.
 */
- (id<SICIAverage>)column:(NSString *)column;

/**
 * Used to get average, this method should be called in last to calculate average.
 * @return Return average.
 * @exception SICDatabaseException Throws exception if any error occur while calculating average.
 */
- (id)execute;

@end

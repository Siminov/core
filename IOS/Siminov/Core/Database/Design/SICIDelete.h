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
 * Exposes API's to delete tuples from table.
 */

#import <Foundation/Foundation.h>
#import "SICDatabaseException.h"

@protocol SICIDeleteClause;

@protocol SICIDelete <NSObject>

/**
 * Column name of which condition will be specified.
 * @param column Name of column.
 * @return SICIDeleteClause Interface.
 */
- (id<SICIDeleteClause>)where:(NSString *)column;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereClause Manually created where clause.
 * @return SICIDelete Interface.
 */
- (id<SICIDelete>)whereClause:(NSString *)whereClause;

/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIDeleteClause Interface.
 */
- (id<SICIDeleteClause>)and:(NSString *)column;

/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 * @return SICIDeleteClause Interface.
 */
- (id<SICIDeleteClause>)or:(NSString *)column;

/**
 * Used to get delete, this method should be called in last to delete tuples from table.
 * @exception SICDatabaseException Throws exception while deleting tuples from table.
 */
- (id)execute;

@end

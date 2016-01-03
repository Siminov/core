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
#import "SICISelectClause.h"
#import "SICIDeleteClause.h"
#import "SICICountClause.h"
#import "SICISumClause.h"
#import "SICITotalClause.h"
#import "SICIAverageClause.h"
#import "SICIMaxClause.h"
#import "SICIMinClause.h"
#import "SICIGroupConcatClause.h"

@class SICWhere;

static NSString * const EQUAL_TO = @"=";
static NSString * const NOT_EQUAL_TO = @"!=";
static NSString * const GREATER_THAN = @">";
static NSString * const GREATER_THAN_EQUAL = @">=";
static NSString * const LESS_THAN = @"<";
static NSString * const LESS_THAN_EQUAL = @"<=";
static NSString * const BETWEEN = @"BETWEEN";
static NSString * const LIKE = @"LIKE";
static NSString * const IN = @"IN";
static NSString * const AND = @"AND";
static NSString * const OR = @"OR";

static NSString * const ASC_ORDER_BY = @"ASC";
static NSString * const DESC_ORDER_BY = @"DESC";

/**
 * It is used to create where clause used in database query.
 * It implements all the Clauses which are used to in the where clause.
 */
@interface SICClause : NSObject <SICISelectClause, SICIDeleteClause, SICICountClause, SICISumClause, SICITotalClause, SICIAverageClause, SICIMaxClause, SICIMinClause, SICIGroupConcatClause> {
    NSMutableString *whereClause;
    SICWhere *where;
}

/**
 * Clause Constructor
 * @param whereClass Where class
 */
- (id)initWithWhereClass:(SICWhere*)whereClass;

/**
 * Add column
 * @param column Name of column
 */
- (void)addCol:(NSString *)column;

/**
 * Used to specify EQUAL TO (=)condition.
 * @param value Value for which EQUAL TO (=)condition will be applied.
 * @return Where object.
 */
- (SICWhere *)equalTo:(id)value;

/**
 * Used to specify NOT EQUAL TO (!=)condition.
 * @param value Value for which NOT EQUAL TO (=)condition will be applied.
 * @return Where object.
 */
- (SICWhere *)notEqualTo:(id)value;

/**
 * Used to specify GREATER THAN (>)condition.
 * @param value Value for while GREATER THAN (>)condition will be specified.
 * @return Where object.
 */
- (SICWhere *)greaterThan:(id)value;

/**
 * Used to specify GREATER THAN EQUAL (>=)condition.
 * @param value Value for which GREATER THAN EQUAL (>=)condition will be specified.
 * @return Where object.
 */
- (SICWhere *)greaterThanEqual:(id)value;

/**
 * Used to specify LESS THAN (<)condition.
 * @param value Value for which LESS THAN (<)condition will be specified.
 * @return Where object.
 */
- (SICWhere *)lessThan:(id)value;

/**
 * Used to specify LESS THAN EQUAL (<=)condition.
 * @param value Value for which LESS THAN EQUAL (<=)condition will be specified.
 * @return Where object.
 */
- (SICWhere *)lessThanEqual:(id)value;

/**
 * Used to specify BETWEEN condition.
 * @param start Start Range.
 * @param end End Range.
 * @return Where object.
 */
- (SICWhere *)between:(id)start end:(id)end;

/**
 * Used to specify LIKE condition.
 * @param like LIKE condition.
 * @return Where object.
 */
- (SICWhere *)like:(id)like;

/**
 * Used to specify IN condition.
 * @param values Values for IN condition.
 * @return Where object.
 */
- (SICWhere *)in:(NSArray *)values;

/**
 * Used to specify AND condition between where clause.
 * @param column Name of column on which condition need to be specified.
 */
- (void)and:(NSString *)column;

/**
 * Used to specify OR condition between where clause.
 * @param column Name of column on which condition need to be specified.
 */
- (void)or:(NSString *)column;

/**
 * It returns the where clause.
 * @return String where clause.
 */
- (NSString *)toString;

@end

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
#import "SICISelect.h"
#import "SICIDelete.h"
#import "SICICount.h"
#import "SICISum.h"
#import "SICITotal.h"
#import "SICIAverage.h"
#import "SICIMax.h"
#import "SICIMin.h"
#import "SICIGroupConcat.h"
#import "SICEntityDescriptor.h"

@class SICClause;

/**
 * It is used to provide condition between where clause.
 */
@interface SICWhere : NSObject <SICISelect, SICIDelete, SICICount, SICISum, SICITotal, SICIAverage, SICIMax, SICIMin, SICIGroupConcat> {
    SICEntityDescriptor *entityDescriptor;
    NSString *interfaceName;
    id referObject;
    
    NSString *column;
    NSArray *columns;
    
    SICClause *clause;
    NSString *whereClause;
    
    NSArray *orderBy;
    NSString *whichOrderBy;
    
    NSArray *groupBy;
    
    SICClause *having;
    NSString *havingClause;
    
    int limit;
    
    NSString *delimiter;
}

/**
 * Where Constructor
 * @param entitydescriptor Entity Descriptor instance
 * @param interfacename Name of interface
 */
- (id)initWithoutReferObject:(SICEntityDescriptor * const)entitydescriptor interfaceName:(NSString * const)interfacename;

/**
 * Where Constructor
 * @param entitydescriptor Entity Descriptor instance
 * @param interfacename Name of interface
 * @param referobject Refered Object instance
 */
- (id)initWithReferObject:(SICEntityDescriptor * const)entitydescriptor interfaceName:(NSString * const)interfacename referObject:(id const)referobject;

/**
 * Used to specify DISTINCT condition.
 * @return Where Where instance object.
 */
- (SICWhere *)distinct;

/**
 * Column name of which condition will be specified.
 * @param columnName Name of column.
 * @return Clause Clause instance object.
 */
- (SICClause *)where:(NSString *)columnName;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param whereclause Manually created where clause.
 * @return Where Where instance object.
 */
- (SICWhere *)whereClause:(NSString *)whereclause;


/**
 * Used to specify AND condition between where clause.
 * @param columnName Name of column on which condition need to be specified.
 * @return Clause instance object.
 */
- (SICClause *)and:(NSString *)columnName;

/**
 * Used to specify OR condition between where clause.
 * @param columnName Name of column on which condition need to be specified.
 * @return Clause Clause instance object.
 */
- (SICClause *)or:(NSString *)columnName;

/**
 * Used to specify ORDER BY keyword to sort the result-set.
 * @param columnsArray Name of columns which need to be sorted.
 * @return Where Where instance object.
 */
- (SICWhere *)orderBy:(NSArray *)columnsArray;

/**
 * Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
 * @param columnsArray Name of columns which need to be sorted.
 * @return Where Where instance object.
 */
- (SICWhere *)ascendingOrderBy:(NSArray *)columnsArray;

/**
 * Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
 * @param columnsArray Name of columns which need to be sorted.
 * @return Where Where instance object.
 */
- (SICWhere *)descendingOrderBy:(NSArray *)columnsArray;

/**
 * Used to specify the range of data need to fetch from table.
 * @param limitValue LIMIT of data.
 * @return Where Where instance object.
 */
- (SICWhere *)limit:(int) limitValue;

/**
 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
 * @param columnsArray Name of columns.
 * @return Where Where instance object.
 */
- (SICClause *)groupBy:(NSArray *)columnsArray;

/**
 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
 * @param columnName Name of column on which condition need to be applied.
 * @return Clause Clause instance object.
 */
- (SICWhere *)having:(NSString *)columnName;

/**
 * Used to provide manually created Where clause, instead of using API's.
 * @param havingclause Where clause.
 * @return Where Where instance object.
 */
- (SICWhere *)havingClause:(NSString *)havingclause;

/**
 * Used to add column
 * @param columnName Name of the column
 * @return Where Where instance object
 */
- (SICWhere *)column:(NSString *)columnName;

/**
 * Used to provide name of columns only for which data will be fetched.
 * @param columnsArray Name of columns.
 * @return Where Where instance object.
 */
- (SICWhere *)columns:(NSArray *)columnsArray;

/**
 * Used to provide name of column for which average will be calculated.
 * @param delimiterString Name of Delimiter.
 * @return Where Where instance object.
 */
- (SICWhere *)delimiter:(NSString *)delimiterString;

/**
 * Executes the request
 * @return Request Output
 */
- (id)execute;


@end

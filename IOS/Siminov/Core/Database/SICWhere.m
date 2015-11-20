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



#import "SICWhere.h"
#import "SICClause.h"
#import "SICDatabaseHelper.h"

@implementation SICWhere

BOOL distinct = false;

- (id)init {
    
    self = [super init];
    
    if(self) {
        return self;
    }
    
    return self;
}

- (id)initWithoutReferObject:(SICEntityDescriptor * const)entitydescriptor interfaceName:(NSString * const)interfacename {
    
    if(self) {
        entityDescriptor = entitydescriptor;
        interfaceName = interfacename;
    }
    
    return self;
}

- (id)initWithReferObject:(SICEntityDescriptor * const)entitydescriptor interfaceName:(NSString * const)interfacename referObject:(id const)referobject {
    
    if(self) {
        entityDescriptor = entitydescriptor;
        interfaceName = interfacename;
        referObject = referobject;
    }
    
    return self;
}

- (SICWhere *)distinct {
    distinct = true;
    return self;
}

- (SICClause *)where:(NSString *)columnName {
    clause = [[SICClause alloc] initWithWhereClass: self];
    [clause addCol: columnName];
    
    return clause;
}

- (SICWhere *)whereClause:(NSString *)whereClauseString {
    whereClause = whereClauseString;
    return self;
}

- (SICClause *)and:(NSString *)columnName {
    [clause and: columnName];
    return clause;
}

- (SICClause *)or:(NSString *)columnName {
    [clause or: columnName];
    return clause;
}

- (SICWhere *)orderBy:(NSArray *)columnsArray {
    orderBy = columnsArray;
    return  self;
}

- (SICWhere *)ascendingOrderBy:(NSArray *)columnsArray {
    orderBy = columnsArray;
    whichOrderBy = ASC_ORDER_BY;
    
    return self;
}

- (SICWhere *)descendingOrderBy:(NSArray *)columnsArray {
    orderBy = columnsArray;
    whichOrderBy = DESC_ORDER_BY;
    
    return self;
}

- (SICWhere *)limit:(int) limitValue {
    limit = limitValue;
    return self;
}

- (SICWhere *)groupBy:(NSArray *)columnsArray {
    groupBy = columnsArray;
    return self;
}

- (SICClause *)having:(NSString *)columnName {
    having = [[SICClause alloc] initWithWhereClass: self];
    [having addCol: column];
    
    return having;
}

- (SICWhere *)havingClause:(NSString *)havingclause {
    havingClause = havingclause;
    return self;
}

- (SICWhere *)column:(NSString *)columnName {
    column = columnName;
    return self;
}

- (SICWhere *)columns:(NSArray *)columnsArray {
    columns = columnsArray;
    return self;
}

- (SICWhere *)delimiter:(NSString *)delimiterString {
    delimiter = delimiterString;
    return self;
}

- (id)execute {
   
    if (column == nil) {
        column = @"";
    }
    
    NSString *whereString = @"";
    if (whereClause != nil && whereClause.length > 0) {
        whereString = whereClause;
    } else {
    
        if (clause != nil) {
            whereString = [clause toString];
        }
    }
    
    NSString *havingString = @"";
    if (havingClause != nil && havingClause.length > 0) {
        havingString = havingClause;
    } else {
        
        if (having != nil) {
            havingString = (NSString *)having;
        }
    }
    
    if (columns == nil) {
        columns = [[NSMutableArray alloc] init];
    }
    
    if (orderBy == nil) {
        orderBy = [[NSMutableArray alloc] init];
    }
    
    if(groupBy == nil) {
        groupBy = [[NSMutableArray alloc] init];
    }
    
    if(whichOrderBy == nil) {
        whichOrderBy = @"";
    }
    
    NSString *limiT = nil;
    if (limit != 0) {
        limiT = [NSString stringWithFormat: @"%i", limit];
    } else {
        limiT = @"";
    }
    
    if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICIDelete))] == NSOrderedSame) {
        [SICDatabaseHelper delete: referObject whereClause: whereString];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICICount))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper count:entityDescriptor column: column distinct: distinct whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICIAverage))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper avg: entityDescriptor column: column whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICISum))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper sum: entityDescriptor column: column whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICITotal))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper total: entityDescriptor column: column whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICIMax))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper max: entityDescriptor column: column whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICIMin))] == NSOrderedSame) {
        return [NSNumber numberWithInt: [SICDatabaseHelper min: entityDescriptor column: column whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString]];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICIGroupConcat))] == NSOrderedSame) {
        return [SICDatabaseHelper groupConcat: entityDescriptor column: column delimiter: delimiter whereClause: whereString groupBys: [groupBy objectEnumerator] having: havingString];
    } else if ([interfaceName caseInsensitiveCompare: NSStringFromProtocol(@protocol(SICISelect))] == NSOrderedSame) {
        return [SICDatabaseHelper select:referObject parentObject:nil entityDescriptor:entityDescriptor distinct: distinct where: whereString columnName: [columns objectEnumerator] groupBy: [groupBy objectEnumerator] having: havingString orderBy: [orderBy objectEnumerator] whichOrderBy: whichOrderBy limit: limiT];
    }
    return nil;
}

@end

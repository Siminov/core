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


#import "SICClause.h"

@implementation SICClause

- (id)init {
    
    self = [super init];
    
    if(self) {
        return self;
    }
    
    return self;
}

- (id)initWithWhereClass:(SICWhere*)whereClass {
   
    if (self = [super init]) {
    
        where = whereClass;
        whereClause = [[NSMutableString alloc] init];
    }
    
    return self;
}

- (void)addCol:(NSString *)column {
    [whereClause appendString:column];
}

- (SICWhere *)equalTo:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",EQUAL_TO,(NSString *)value]];
    return where;
}

- (SICWhere *)notEqualTo:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",NOT_EQUAL_TO,(NSString *)value]];
    return where;
}

- (SICWhere *)greaterThan:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",GREATER_THAN,(NSString *)value]];
    return where;
}

- (SICWhere *)greaterThanEqual:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",GREATER_THAN_EQUAL,(NSString *)value]];
    return where;
}

- (SICWhere *)lessThan:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",LESS_THAN,(NSString *)value]];
    return where;
}

- (SICWhere *)lessThanEqual:(id)value {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",LESS_THAN_EQUAL,(NSString *)value]];
    return where;
}

- (SICWhere *)between:(id)start end:(id)end {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@' AND '%@'",BETWEEN,start,end]];
    return where;
}

- (SICWhere *)like:(id)like {
    [whereClause appendString:[NSString stringWithFormat:@"%@ '%@'",LIKE,like]];
    return where;
}

- (SICWhere *)in:(NSArray *)values {
    [whereClause appendString:[NSString stringWithFormat:@"%@(",IN]];
    
    if(values != nil && values.count > 0) {
    
        for(int i=0;i < values.count;i++) {
        
            if(i==0) {
                [whereClause appendString:[NSString stringWithFormat:@"'%@'",values[i]]];
                continue;
            }
            
            [whereClause appendString:[NSString stringWithFormat:@" ,'%@'",values[i]]];
        }
    }
    
    [whereClause appendString:@")"];
    return where;
}

- (void)and:(NSString *)column {
    [whereClause appendString:[NSString stringWithFormat:@" AND %@",column]];
}

- (void)or:(NSString *)column {
    [whereClause appendString:[NSString stringWithFormat:@" OR %@",column]];
}

- (NSString *)toString {
    return (NSString *)whereClause;
}

@end

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


#import "SICDataTypeHandler.h"
#import "SICConstants.h"
#import "SICDeploymentException.h"

@implementation SICDataTypeHandler

static NSString * const SQLITE_DATA_TYPE_INTEGER = @"INTEGER";
static NSString * const SQLITE_DATA_TYPE_TEXT = @"TEXT";
static NSString * const SQLITE_DATA_TYPE_REAL = @"REAL";
static NSString * const SQLITE_DATA_TYPE_NONE = @"NONE";
static NSString * const SQLITE_DATA_TYPE_NUMERIC = @"NUMERIC";


- (NSString *)convert:(NSString *)dataType {
    
    if([dataType caseInsensitiveCompare:INTEGER_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_INTEGER_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:PRIMITIVE_INTEGER_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_INTEGER_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:LONG_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_INTEGER_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:PRIMITIVE_LONG_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_INTEGER_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:FLOAT_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_REAL_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:PRIMITIVE_FLOAT_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_REAL_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:DOUBLE_DATA_TYPE] == NSOrderedSame ) {
        return SQLITE_REAL_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:PRIMITIVE_DOUBLE_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_REAL_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:BOOLEAN_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_TEXT_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:PRIMITIVE_BOOLEAN_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_TEXT_DATA_TYPE;
    } else if([dataType caseInsensitiveCompare:STRING_DATA_TYPE] == NSOrderedSame) {
        return SQLITE_TEXT_DATA_TYPE;
    }
    
    /*
     * Other Data Type
     */
    else {
         @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([SICDataTypeHandler class]) methodName:@"convert" message:[NSString stringWithFormat:@"%@ Data Type Not Supported On iOS", dataType]];
    }
}

@end

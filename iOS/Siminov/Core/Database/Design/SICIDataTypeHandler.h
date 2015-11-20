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
 * Exposes convert API which is responsible to provide column data type based on java variable data type.
 */

#import <Foundation/Foundation.h>

/*
 * SQLite Data Types
 */

/** SQLite Integer Data Type */
static NSString * const SQLITE_INTEGER_DATA_TYPE = @"INTEGER";

/**
 * SQLite Text Data Type
 */
static NSString * const SQLITE_TEXT_DATA_TYPE = @"TEXT";

/**
 * SQLite Real Data Type
 */
static NSString * const SQLITE_REAL_DATA_TYPE = @"REAL";

/**
 * SQLite None Data Type
 */
static NSString * const SQLITE_NONE_DATA_TYPE = @"NONE";

/**
 * SQLite Numeric Data Type
 */
static NSString * const SQLITE_NUMERIC_DATA_TYPE = @"NUMERIC";


@protocol SICIDataTypeHandler <NSObject>

/** Converts objective c variable data type to database column data type.
 
 @param dataType Java variable data type.
 @return column data type.
 */
- (NSString *)convert:(NSString *)dataType;

@end

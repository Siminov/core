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


/**
 * Exposes file storage apis, it helps to easily handle file system.
 */
@interface SICFileUtils: NSObject

/**
 * Get file path based on name and directory
 * @param name Name of the file
 * @param directory Directory of the file
 * @return File Path
 */
- (NSString *)getFilePath:(NSString *)name inDirectory:(NSString *)directory;

@end

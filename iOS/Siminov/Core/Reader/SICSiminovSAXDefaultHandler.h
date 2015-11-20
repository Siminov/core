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
 * Exposes common methods which are required by every SIMINOV parsers.
 */

#import <Foundation/Foundation.h>
#import "SICSiminovException.h"

@interface SICSiminovSAXDefaultHandler : NSObject <NSXMLParserDelegate>

/** Call this method to parse input stream.
 
 @param inputStream Input Stream.
 @exception SICSiminovException If any exception occur while parsing input stream.
 */
- (void)parseMessage:(NSData * const)inputStream;

@end

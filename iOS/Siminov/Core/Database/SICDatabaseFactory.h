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
#import "SICIDatabase.h"
#import "SICDatabaseDescriptor.h"
#import "SICDatabaseBundle.h"
#import "SICClassUtils.h"

/**
 * Exposes methods to create database instance based on Database Type needed.
 * It has methods to GET DatabaseFactory, GET Database.
 */
@interface SICDatabaseFactory : NSObject {
    NSMutableDictionary *databaseBundles;
}

/**
 * It provides an instance of DatabaseFactory class.
 *
 * @return SICDatabaseFactory instance.
 */
+ (SICDatabaseFactory *)getInstance;

/**
 * Get SICIDatabase Instance.
 * @param databaseDescriptor DatabaseDescriptor Object.
 * @return SICIDatabase Object.
 */
- (SICDatabaseBundle *)getDatabaseBundle:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Returns all database bundles.
 * @return Iterator<DatabaseHandler> All Database Bundle instances.
 */
- (NSEnumerator *)getDatabaseBundles;

/**
 * Removes database bundle instance.
 * @param databaseDescriptor Database descriptor instance object.
 */
- (void)removeDatabaseBundle:(SICDatabaseDescriptor * const)databaseDescriptor;

@end

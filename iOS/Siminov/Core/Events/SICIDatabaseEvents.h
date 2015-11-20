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

#import <Foundation/Foundation.h>
#import "SICDatabaseDescriptor.h"


/**
 * Exposes methods which deal with events associated with database operation's.
 * It has methods such as (onDatabaseCreated, onDatabaseDroped, onTableCreated, onTableDroped, onIndexCreated).
 */
@protocol SICIDatabaseEvents <NSObject>

/**
 * This event is fired when database gets created as per database descriptor.
 * @param databaseDescriptor contains meta data associated with database.
 */
- (void)onDatabaseCreated:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * This event is fired when database is dropped.
 * @param databaseDescriptor contains meta data associated with dropped database.
 */
- (void)onDatabaseDropped:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * This event is fired when a table is created.
 * @param databaseDescriptor contains meta data associated with created table.
 * @param entityDescriptor contains meta data associated with created table.
 */
- (void)onTableCreated:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor;

/**
 * This event is fired when a table is dropped.
 * @param databaseDescriptor contains meta data associated with dropped table.
 * @param entityDescriptor contains meta data associated with dropped table.
 */
- (void)onTableDropped:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor;

/**
 * This event is fired when a index is created on table.
 * @param databaseDescriptor contains meta data associated with table on which index is created.
 * @param entityDescriptor contains meta data associated with table on which index is created.
 * @param index meta data about index got created.
 */
- (void)onIndexCreated:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor index:(SICIndex*)index;

/**
 * This event is fired when a index is dropped.
 * @param databaseDescriptor contains meta data associated with table on which index is dropped.
 * @param entityDescriptor contains meta data associated with table on which index is dropped.
 * @param index meta data about index got dropped.
 */
- (void)onIndexDropped:(SICDatabaseDescriptor * const)databaseDescriptor entityDescriptor:(SICEntityDescriptor * const)entityDescriptor index:(SICIndex*)index;

@end

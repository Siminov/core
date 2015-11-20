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
#import "SICIAverage.h"
#import "SICICount.h"
#import "SICIDelete.h"
#import "SICIGroupConcat.h"
#import "SICIMax.h"
#import "SICIMin.h"
#import "SICISelect.h"
#import "SICISum.h"
#import "SICITotal.h"
#import "SICIDatabaseEvents.h"
#import "SICDatabaseDescriptor.h"
#import "SICDatabaseHelper.h"

/**
 * Exposes methods to interact with database.
 * It has methods to create, delete, and perform other common database management tasks.
 */
@interface SICDatabase : NSObject <SICIDatabase> {
    id object;
}

- (id)initWithName:(id)databaseObject;

/** It drops the whole database based on database name.
 
 Drop the Liquor table.
 
 SICDatabaseDescriptor *databaseDescriptor = [[[Liquor alloc]init] getDatabaseDescriptor];
 
 @try {
 [SICDatabase dropDatabase:[databaseDescriptor getDatabaseName]];
 } @catch(SICDatabaseException *databaseException) {
 //Log It.
 }
 
 @param databaseName Entity-Descriptor object which defines the structure of table.
 */
+ (void)dropDatabase:(NSString *)databaseName;

/** Begins a transaction in EXCLUSIVE mode.
 
 Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
 The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
 
 Example: Make Beer Object
 
	Liquor *beer = [[Liquor alloc] init];
	[beer setLiquorType: LIQUOR_TYPE_BEER];
	[beer setDescription: @"beer_description"];
	[beer setHistory: @"beer_history"];
	[beer setLink: @"beer_link"];
	[beer setAlcholContent: @"beer_alchol_content"];
 
	SICDatabaseDescriptor *databaseDescriptor = [beer getDatabaseDescriptor];
 
	@try {
 [SICDatabase beginTransaction:databaseDescriptor];
 [beer save];
 [SICDatabase commitTransaction:databaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
 //Log it.
	} @finally {
 [SICDatabase endTransaction:databaseDescriptor];
	}
 
 @param databaseDescriptor DatabaseDescriptor object.
 */
+ (void)beginTransaction:(SICDatabaseDescriptor * const)databaseDescriptor;

/** Marks the current transaction as successful.
 
 Finally it will end a transaction.
 
 Example: Make Beer Object
	
 Liquor *beer = [[Liquor alloc] init];
	[beer setLiquorType: LIQUOR_TYPE_BEER];
	[beer setDescription: @"beer_description"];
	[beer setHistory: @"beer_history"];
	[beer setLink: @"beer_link"];
	[beer setAlcholContent: @"beer_alchol_content"];
 
	DatabaseDescriptor *databaseDescriptor = [beer getDatabaseDescriptor];
 
	@try {
 [SICDatabase beginTransaction:databaseDescriptor];
 [beer save];
 [SICDatabase commitTransaction:databaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
 //Log it.
	} @finally {
 [SICDatabase endTransaction:databaseDescriptor];
	}
	
 @param databaseDescriptor Database Descriptor Object.
 */
+ (void)commitTransaction:(SICDatabaseDescriptor * const)databaseDescriptor;


@end

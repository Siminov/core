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
#import "SICDatabaseDescriptor.h"
#import "SICISelect.h"
#import "SICICount.h"
#import "SICIAverage.h"
#import "SICISum.h"
#import "SICITotal.h"
#import "SICIDelete.h"
#import "SICIMin.h"
#import "SICIMax.h"
#import "SICIGroupConcat.h"

/**
 * Exposes methods to deal with actual database object.
 * It has methods to open, create, close, and execute query's.
 */
@protocol SICIDatabase <NSObject>


/** Is used to create a new table in an database.
 
 Using SIMINOV there are three ways to create table in database.
 
 - Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
 
 SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.

 Example:
	 
 
            <!-- Design Of EntityDescriptor.xml -->
 
    <entity-descriptor>
 
        <!-- General Properties Of Table And Class -->
 
                <!-- Mandatory Field -->
                <!-- NAME OF TABLE -->
        <property name="table_name">name_of_table</property>
 
                <!-- Mandatory Field -->
                <!-- MAPPED CLASS NAME -->
        <property name="class_name">mapped_class_name</property>
 
 
        <!-- Optional Field -->
        <attributes>
 
            <!-- Column Properties Required Under This Table -->
 
                <!-- Optional Field -->
            <attribute>
 
                    <!-- Mandatory Field -->
                    <!-- COLUMN_NAME: Mandatory Field -->
                <property name="column_name">column_name_of_table</property>
 
                    <!-- Mandatory Field -->
                    <!-- VARIABLE_NAME: Mandatory Field -->
                <property name="variable_name">class_variable_name</property>
 
                    <!-- Mandatory Field -->
                <property name="type">java_variable_data_type</property>
 
                    <!-- Optional Field (Default is false) -->
                <property name="primary_key">true/false</property>
 
                    <!-- Optional Field (Default is false) -->
                <property name="not_null">true/false</property>
 
                    <!-- Optional Field (Default is false) -->
                <property name="unique">true/false</property>
 
                    <!-- Optional Field -->
                <property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
 
                    <!-- Optional Field -->
                <property name="default">default_value_of_column (Eg: 0.1)</property>
 
            </attribute>
 
        </attributes>
 
 
        <!-- Optional Field -->
        <indexes>
 
            <!-- Index Properties -->
            <index>
 
                    <!-- Mandatory Field -->
                    <!-- NAME OF INDEX -->
                <property name="name">name_of_index</property>
 
                    <!-- Mandatory Field -->
                    <!-- UNIQUE: Optional Field (Default is false) -->
                <property name="unique">true/false</property>
 
                    <!-- Optional Field -->
                    <!-- Name of the column -->
                <property name="column">column_name_needs_to_add</property>
 
            </index>
 
        </indexes>
 
 
        <!-- Map Relationship Properties -->
 
            <!-- Optional Field's -->
        <relationships>
 
            <relationship>
 
                    <!-- Mandatory Field -->
                    <!-- Type of Relationship -->
                <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
 
                    <!-- Mandatory Field -->
                    <!-- REFER -->
                <property name="refer">class_variable_name</property>
 
                    <!-- Mandatory Field -->
                    <!-- REFER TO -->
                <property name="refer_to">map_to_class_name</property>
 
                    <!-- Optional Field -->
                <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>
 
                    <!-- Optional Field -->
                <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>
 
                    <!-- Optional Field (Default is false) -->
                <property name="load">true/false</property>
 
            </relationship>
 
        </relationships>
 
    </entity-descriptor>
 
*/
- (void)createTable;

/** It drop's the table from database based on entity-descriptor.
 
 Drop the Book table.
	
	Book *book = [[Book alloc] init];
	
	@try {
        [book dropTable];
    } @catch(SICDatabaseException *databaseException) {
        //Log It.
	}
 */
- (void)dropTable;

/** Is used to drop a index on a table in database.
 
 Create Index On Book table.

	NSString * indexName = @"BOOK_INDEX_BASED_ON_AUDITOR";
	Book *book = [[Book alloc] init];
	
	@try {
        [book dropIndex:indexName];
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}

 @param indexName Name of a index needs to be drop.
 */
- (void)dropIndex:(NSString *)indexName;

/** Returns all tuples based on query from mapped table for invoked class object.
 
 Example:
 
    NSArray *books;
    @try {
        books = [[[[Book alloc] init] select] execute];
     } @catch(SICDatabaseException de) {
        //Log it.
     }
 
 @return SICISelect object.
 */
- (id<SICISelect>)select;

/** Returns all tuples based on manual query from mapped table for invoked class object.

 Example:

     NSString *query = @"SELECT * FROM BOOK";
     NSArray *books;
        
     @try {
        books = [[[Book alloc] init] select:query];
     } @catch(SICDatabaseException *de) {
        //Log it.
     }
 
 @param query Query to get tuples from database.
 @return SICISelect object.
 */

- (id)select:(NSString *)query;

/** It adds a record to any single table in a relational database.
 
 Example: Make Book Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     @try {
        [cBook save];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (void)save;

/** It updates a record to any single table in a relational database.
 
 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"beer_link"];
 
     @try {
        [cBook update];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (void)update;

/** It finds out whether tuple exists in table or not.
	IF NOT EXISTS:
 adds a record to any single table in a relational database.
	ELSE:
 updates a record to any single table in a relational database.

 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setHistory: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     @try {
        [cBook saveOrUpdate];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (void)saveOrUpdate;

/** It deletes a record from single table in a relational database.

 Example: Make Beer Object

     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     @try {
        [cBook delete];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICIDelete>)delete;

/** Returns the count of rows based on where clause provided.
 
 Example: Make Beer Object

     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int noOfBooks = 0;
     
     @try {
        noOfBooks = [[cBook count] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICICount>)count;

/** Returns the average based on where clause provided.
 
 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int noOfBooks = 0;
     
     @try {
        noOfBooks = [[beer avg] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICIAverage>)avg;

/** Returns the sum based on where clause provided.
 
 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int noOfBooks = 0;
     
     @try {
        noOfBooks = [[cBook sum] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICISum>)sum;

/** Returns the total based on where clause provided.
 
 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle:BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int totalBooks = 0;
     
     @try {
        totalBooks = [[cBook avg] execute];
     }
     @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICITotal>)total;

/** Returns the min based on where clause provided.

 Example: Make Book Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int minBooks = 0;
     
     @try {
        minBooks = [[cBook min] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICIMin>)min;

/** Returns the max based on where clause provided.
 
 Example: Make Beer Object

     Book *cBook = [[Book alloc] init];
     [cBook setTitle: Book.BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int maxBooks = 0;
     
     @try {
        maxBooks = [[cBook max] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICIMax>)max;

/** Returns the group concat based on where clause provided.
 
 Example: Make Beer Object
 
     Book *cBook = [[Book alloc] init];
     [cBook setTitle: BOOK_TYPE_C];
     [cBook setDescription: @"c_description"];
     [cBook setAuditor: @"c_auditor"];
     [cBook setLink: @"c_link"];
 
     int groupConcatBooks = 0;
     
     @try {
        groupConcatBooks = [[cBook groupConcat] execute];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 */
- (id<SICIGroupConcat>)groupConcat;

/** Returns database descriptor object based on the mapped class called.

 Example:
 
	@try {
        SICDatabaseDescriptor *databaseDescriptor = [[[Book alloc] init] getDatabaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}
	
 @return Database Descriptor Object.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptor;

/** Returns the actual entity descriptor object mapped for invoked class object.
 
 Example:

     SICEntityDescriptor *entityDescriptor = nil;
     @try {
        entityDescriptor = [[[Book alloc] init] getEntityDescriptor];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return Entity Descriptor Object
 */
- (SICEntityDescriptor *)getEntityDescriptor;

/** Returns the mapped table name for invoked class object.

 Example:

     NSString *tableName = nil;
     @try {
        tableName = [[[Book alloc] init] getTableName];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
     
 @return Mapped Table name.
 */
- (NSString *)getTableName;

/** Returns all column names of mapped table.

 Example:

     NSEnumerator *columnNames = nil;
     @try {
        columnNames = [[[Book alloc] init] getColumnNames];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return All column names of mapped table.
 */
- (NSEnumerator *)getColumnNames;

/** Returns all column values in the same order of column names for invoked class object.

 Example:
 
     NSDictionary *values = nil;
     @try {
        values = [[[Book alloc] init] getColumnValues];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }

 @return All column values for invoked object.
 */
- (NSDictionary *)getColumnValues;

/** Returns all columns with there data types for invoked class object.

 Example:
 
 NSDictionary *columnTypes = nil;
 @try {
	columnTypes = [[[Book alloc] init] getColumnTypes];
 } @catch(SICDatabaseException *databaseException) {
	//Log it.
 }

 @return All columns with there data types.
 */
- (NSDictionary *)getColumnTypes;

/** Returns all primary keys of mapped table for invoked class object.
 
 Example:
 
     NSEnumerator *primaryKeys = nil;
     @try {
        primaryKeys = [[[Book alloc] init] getPrimaryKeys];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return All primary keys.
 */
- (NSEnumerator *)getPrimaryKeys;

/** Returns all mandatory fields which are associated with mapped table for invoked class object.

 Example:

     NSEnumerator *mandatoryFields = nil;
     @try {
        mandatoryFields = [[[Book alloc] init] getMandatoryFields];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return All mandatory fields for mapped table.
 */
- (NSEnumerator *)getMandatoryFields;

/** Returns all unique fields which are associated with mapped table for invoked class object.

 Example:

     NSEnumerator *uniqueFields = null;
     @try {
        uniqueFields = [[[Book alloc] init] getUniqueFields];
     }
     @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return All unique fields for mapped table.
 */
- (NSEnumerator *)getUniqueFields;

/** Returns all foreign keys of mapped table for invoked class object.
 
 Example:
 
     NSEnumerator *foreignKeys = nil;
     @try {
        foreignKeys = [[[Book alloc] init] getForeignKeys];
     } @catch(SICDatabaseException *databaseException) {
        //Log it.
     }
 
 @return All foreign keys of mapped table.
 */
- (NSEnumerator *)getForeignKeys;

@end

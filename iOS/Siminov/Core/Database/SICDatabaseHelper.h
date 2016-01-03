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
#import "SICResourceManager.h"

/**
 * It provides utility methods to deal with database.
 * It has methods to create, delete, and perform other common database management tasks.
 */
@interface SICDatabaseHelper : NSObject


/**
 * It is used to create instance of SICIDatabase implementation.
 * @param databaseDescriptor Database Descriptor object to create database.
 * @return SICDatabaseBundle Database Bundle instance object.
 */
+ (SICDatabaseBundle *)createDatabase:(SICDatabaseDescriptor * const)databaseDescriptor;

/** It drops the whole database based on database-descriptor.
 
 Drop the book table.
    
    SICDatabaseDescriptor *databaseDescriptor = [[[Book alloc] init] getDatabaseDescriptor];
	
	@try {
        [SICDatabase dropDatabase:databaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}
 
 @param databaseDescriptor Entity Descriptor object which defines the structure of table.
 */
+ (void)dropDatabase:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Upgrade Existing Database.
 * @param databaseDescriptor Database Descriptor object.
 */
+ (void)upgradeDatabase:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Upgrade Table.
 * @param entityDescriptor object related to table.
 * @exception DatabaseException If any exception thrown while upgrading table.
 */
+ (void)upgradeTable:(SICEntityDescriptor *)entityDescriptor;

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
 

 @param entityDescriptors Entity Descriptor objects which defines the structure of each table.
 */
+ (void)createTables:(NSEnumerator *)entityDescriptors;


/** Is used to create a new table in an database.
 
 Manually creating table structure using Entity Descriptor mapped class.
 
 Example:
	
     Book *book = [[Book alloc] init];
 
     @try {
        [SICDatabase createTables:[book getEntityDescriptor]];
     } @catch(SICDatabaseException *databaseException) {
        //Log It.
     }
 
 @param entityDescriptor Entity Descriptor object which defines the structure of table.
 */
+ (void)createTable:(SICEntityDescriptor * const)entityDescriptor;

/** It drops the table from database based on entity descriptor.
 
 Drop the book table.
	
    EntityDescriptor *entityDescriptor = [[[Book alloc] init] getEntityDescriptor];
        
    @try {
        [SICDatabase dropTable:entityDescriptor];
    } @catch(SICDatabaseException *databaseException) {
        //Log It.
    }
 
 @param entityDescriptor Entity Descriptor object which defines the structure of table.
 */
+ (void)dropTable:(SICEntityDescriptor * const)entityDescriptor;


/** Is used to create a new index on a table in database.
 
 Create Index On book table.
	
	Index *indexOnBook = [[Index alloc] init];
	[indexOnBook setName: @"BOOK_INDEX_BASED_ON_AUDITOR"];
	[indexOnBook setUnique: true];
	
	//Add Columns on which we need index.
	[indexOnBook addColumn: @"LINK"];
 
	SICEntityDescriptor *entityDescriptor = [[[Book alloc] init] getEntityDescriptor];
 
	@try {
        [SICDatabase createIndexBasedonIndexObject:entityDescriptor index:indexOnBook);
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}
	
 @param entityDescriptor Entity Descriptor object which defines the structure of table.
 @param index Index object which defines the structure of index needs to create.
 */
+ (void)createIndexBasedonIndexObject:(SICEntityDescriptor * const)entityDescriptor index:(SICIndex* const)index;


/** Is used to create a new index on a table in database.
 
 Create Index On book table.
 
	NSString *indexName = @"BOOK_INDEX_BASED_ON_AUDITOR";
	BOOL isUnique = true;
	
	NSMutableArray *columnNames = [NSMutableArray alloc] init];
	[columnNames add:@"LINK"];
	
	@try {
        [[Book alloc] init] createIndex;
        new Book().createIndex(indexName, columnNames.iterator(), isUnique);
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}

 @param entityDescriptor Entity Descriptor object which defines the structure of table.
 @param indexName Name of index.
 @param columnNames Iterator over column names.
 @param isUnique true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
 */
+ (void)createIndex:(SICEntityDescriptor * const)entityDescriptor indexName:indexName columnNames:(NSEnumerator *)columnNames isUnique:(bool const)isUnique;

/** Is used to drop a index on a table in database.

 Create Index On Book table.
 
	NSString *indexName = @"BOOK_INDEX_BASED_ON_AUDITOR";
	SICEntityDescriptor *entityDescriptor = [[[Book alloc] init] getEntityDescriptor];
	
	@try {
        [SICDatabase dropIndex:entityDescriptor indexName:indexName];
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}

 @param entityDescriptor Entity Descriptor object which defines the structure of table.
 @param indexName Name of a index needs to be drop.
 */
+ (void)dropIndex:(SICEntityDescriptor * const)entityDescriptor indexName:(NSString * const)indexName;

/** Begins a transaction in EXCLUSIVE mode.

 Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
 The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
 
 Example: Make Book Object
 
    Book *cBook = [[Book alloc] init];
	[cBook setTitle:BOOK_TYPE_C];
	[cBook setDescription: @"c_description"];
	[cBook setAuditor: @"c_auditor"];
	[cBook setLink: @"c_link"];
 
	SICDatabaseDescriptor *databaseDescriptor = [cBook getDatabaseDescriptor];
 
	@try {
        [SICDatabase beginTransaction:databaseDescriptor];
        [cBook save];
        [SICDatabase commitTransaction:databaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	} @finally {
        [SICDatabase endTransaction: atabaseDescriptor];
	}
 
 @param databaseDescriptor DatabaseDescriptor object.
 */
+ (void)beginTransaction:(SICDatabaseDescriptor * const)databaseDescriptor;

/** Marks the current transaction as successful.

 Finally it will End a transaction.
 
 Example: Make Beer Object
	
	Book *cBook = [[Book alloc] init];
	[cBook setTitle:BOOK_TYPE_C];
	[cBook setDescription: @"c_description"];
	[cBook setAuditor: @"c_auditor"];
	[cBook setLink: @"c_link"];
 
	SICDatabaseDescriptor *databaseDescriptor = [cBook getDatabaseDescriptor];
 
	@try {
        [SICDatabase beginTransaction: databaseDescriptor];
        [cBook save];
        [SICDatabase commitTransaction: databaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	} @finally {
        [SICDatabase endTransaction: databaseDescriptor];
	}

 @param databaseDescriptor DatabaseDescriptor object.
 */
+ (void)commitTransaction:(SICDatabaseDescriptor * const)databaseDescriptor;


+ (id)select:(id const)object parentObject:(id const)parentObject entityDescriptor:(SICEntityDescriptor * const)entityDescriptor distinct:(bool const)distinct where:(NSString * const)whereClause columnName:(NSEnumerator * const)columnNames groupBy:(NSEnumerator * const)groupBy having:(NSString * const) having orderBy:(NSEnumerator * const)orderBy whichOrderBy:(NSString * const)whichOrderBy limit:(NSString * const)limit;


/** Returns all tuples based on manual query from mapped table for invoked class object.
 
 Example:

	NSString *query = @"SELECT * FROM BOOK";
	NSArray *books;
	
    @try {
        books = [[[[Book alloc] init] select] execute];
	} @catch(SICDatabaseException *de) {
        //Log it.
	}

 @param object Class object.
 @param query Manual query on which tuples need to be fetched.
 @return Array Of Objects.
 */

+ (id)select:(id const)object query:(NSString * const)query;

/** It adds a record to any single table in a relational database.

 Example: Make Book Object
 
	Book *cBook = [[Book alloc] init];
	[cBook setTitle: BOOK_TYPE_C];
	[beer setDescription: @"c_description"];
	[beer setAuditor: @"c_auditor"];
	[beer setLink: @"beer_link"];
 
	@try {
        [cBook save];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
 
 @param object Class object.
 */
+ (void)save:(id const)object;

+ (void)save:(id const)object parentObject:(id const)parentObject;


/** It updates a record to any single table in a relational database.
	
 Example: Make Beer Object

	Book *cBook = [[Book alloc] init];
	[cBook setTitle:BOOK_TYPE_C];
	[cBook setDescription: @"c_description"];
	[cBook setAuditor: @"c_auditor"];
	[cBook setLink: @"c_link"];
 
	@try {
        [cBook update];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
 
 @param object Class object.
 */
+ (void)update:(id const)object;

+ (void)update:(id const)object parentObject:(id const)parentObject;


/** It finds out whether tuple exists in table or not.
 IF NOT EXISTS:
 adds a record to any single table in a relational database.
 ELSE:
 updates a record to any single table in a relational database.
 
 Example: Make Beer Object

	Book *cBook = [[Book alloc] init];
	[cBook setTitle:BOOK_TYPE_C];
	[cBook setDescription: @"c_description"];
	[cBook setAuditor: @"c_auditor"];
	[cBook setLink: @"c_link"];
 
	@try {
        [cBook saveOrUpdate];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}

 @param object Class object.
 */
+ (void)saveOrUpdate:(id const)object;

+ (void)saveOrUpdate:(id const)object parentObject:(id const)parentObject;



+ (void)delete:(id const)object whereClause:(NSString * const)whereClause;

+ (int const)count:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column distinct:(bool const)distinct whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;
+ (int const)avg:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;
+ (int const)sum:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;
+ (int const)total:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;
+ (int const)min:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;
+ (int const)max:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;

+ (NSString * const)groupConcat:(SICEntityDescriptor * const)entityDescriptor column:(NSString * const)column delimiter:(NSString * const)delimiter whereClause:(NSString * const)whereClause groupBys:(NSEnumerator * const)groupBys having:(NSString * const)having;

/** Returns database descriptor object based on the POJO class called.

 Example:

	@try {
        SICDatabaseDescriptor *databaseDescriptor = [[[Book alloc] init] getDatabaseDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log It.
	}
  
  @param className Name of class.
  @return Database Descriptor Object.
 */
+ (SICDatabaseDescriptor *)getDatabaseDescriptor:(NSString * const)className;

/** Returns the actual entity descriptor object mapped for invoked class object.

 Example:
 
	SICEntityDescriptor *entityDescriptor = nil;
	@try {
        entityDescriptor = [[[Book alloc] init] getEntityDescriptor];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}

 @param className Name of class.
 @return Entity Descriptor Object
 */
+ (SICEntityDescriptor *)getEntityDescriptor:(NSString * const)className;


/** Returns the mapped table name for invoked class object.

 Example:
	
	NSString *tableName = nil;
	@try {
        tableName = [[[Book alloc] init] getTableName];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
	
 @param object Object of class.
 @return Mapped Table name.
 */
+ (NSString *)getTableName:(id const)object;


/** Returns all column names of mapped table.
 
 Example:
	
	NSEnumerator *columnNames = nil;
	@try {
        columnNames = [[[Book alloc] init] getColumnNames];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
 
 @param object Object of class.
 @return All column names of mapped table.
 */
+ (NSEnumerator *)getColumnNames:(id const)object;


/** Returns all column values in the same order of column names for invoked class object.
 
 Example:
	
	NSMutableDictionary *values = nil;
	@try {
        values = [[[Book alloc] init] getColumnValues];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}

 @param object Object of class.
 @return All column values for invoked object.
 */
+ (NSMutableDictionary *)getColumnValues:(id const)object;


/** Returns all columns with there data types for invoked class object.

 Example:
 
	NSMutableDictionary *columnTypes = nil;
	@try {
        columnTypes = [[[Book alloc] init] getColumnTypes];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
	
 @param object Object of class.
 @return All columns with there data types.
 */
+ (NSMutableDictionary *)getColumnTypes:(id const)object;


/** Returns all primary keys of mapped table for invoked class object.

 Example:
 
	NSEnumerator *primaryKeys = nil;
	@try {
        primaryKeys = [[[Book alloc] init] getPrimeryKeys();
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
	
 @param object Object of class.
 @return All primary keys.
 */
+ (NSEnumerator *)getPrimaryKeys:(id const)object;


/** Returns all mandatory fields which are associated with mapped table for invoked class object.

 Example:
 
	NSEnumerator *mandatoryFields = null;
	@try {
        mandatoryFields = [[[Book alloc] init] getMandatoryFields];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
 
 @param object Object of class.
 @return All mandatory fields for mapped table.
 */
+ (NSEnumerator *)getMandatoryFields:(id const)object;


/** Returns all unique fields which are associated with mapped table for invoked class object.

 Example:

	NSEnumerator *uniqueFields = null;
	@try {
        uniqueFields = [[[Book alloc] init] getUniqueFields];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
 
 @param object Object of class.
 @return All unique fields for mapped table.
 */
+ (NSEnumerator *)getUniqueFields:(id const)object;


/** Returns all foreign keys of mapped table for invoked class object.
 
 Example:
 
	NSEnumerator *foreignKeys = nil;
	@try {
        foreignKeys = [[[Book alloc] init] getForeignKeys];
	} @catch(SICDatabaseException *databaseException) {
        //Log it.
	}
	
 @param object Object of class.
 @return All foreign keys of mapped table.
 */
+ (NSEnumerator *)getForeignKeys:(id const)object;
+ (NSMutableArray *)getForeignKeysForEntityDescriptor:(SICEntityDescriptor *)entityDescriptor;


/**
 Iterates the provided cursor, and returns tuples in form of actual objects.
 @param entityDescriptor object related to table.
 @param values Values to parse.
 */
+ (NSEnumerator *)parseAndInflateData:(id const)object parentObject:parentObject entityDescriptor:(SICEntityDescriptor * const)entityDescriptor values:(NSEnumerator * const)values;

@end


@interface SICRelationshipHelper : NSObject

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject;

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject columnNames:(NSMutableArray * const)columnNames columnValues:(NSMutableArray * const)columnValues;

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject whereClause:(NSMutableString * const)whereClause;

+ (void)processRelationship:(id const)object parentObject:(id const)parentObject data:(NSMutableDictionary * const)data;

@end

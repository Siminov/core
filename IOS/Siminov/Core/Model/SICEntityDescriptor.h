//
//  SICEntityDescriptor.h
//  Core
//
//  Created by user on 29/07/15.
//  Copyright (c) 2015 Siminov. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SICConstants.h"
#import "SICIDescriptor.h"

@class SICAttribute, SICRelationship, SICIndex, Column;

/** Exposes methods to GET and SET Library Descriptor information as per define in DatabaseDescriptor.xml or LibraryDescriptor.xml  file by application.
 
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
@interface SICEntityDescriptor : NSObject <SICIDescriptor> {
@private
    
    NSMutableDictionary *properties;
    
    NSMutableDictionary *attributeBasedOnColumnNames;
    NSMutableDictionary *attributeBasedOnVariableNames;
    
    NSMutableDictionary *indexes;
    
    NSMutableDictionary *relationshipsBasedOnRefer;
    NSMutableDictionary *relationshipsBasedOnReferTo;
}

/**
 * Get table name.
 * @return Name of table.
 */
- (NSString *)getTableName;

/**
 * Set table name as per defined in EntityDescriptor.xml file.
 * @param tableName Name of table.
 */
- (void)setTableName:(NSString * const)tableName;

/**
 * Get mapped class name.
 * @return mapped class name.
 */
- (NSString *)getClassName;

/**
 * Set mapped class name as per defined in EntityDescriptor.xml file.
 * @param className mapped class name.
 */
- (void)setClassName:(NSString * const)className;

/**
 * Check whether column exists based on column name.
 * @param columnName Name of column.
 * @return TRUE: If column exists, FALSE: If column do not exists.
 */
- (BOOL)containsAttributeBasedOnColumnName:(NSString * const)columnName;

/**
 * Check whether column exists based on variable name.
 * @param variableName Name of variable.
 * @return TRUE: If column exists, FALSE: If column do not exists.
 */
- (BOOL)containsAttributeBasedOnVariableName:(NSString * const)variableName;

/**
 * Get column based on column name.
 * @param columnName Name of column name.
 * @return Column object.
 */
- (SICAttribute *)getAttributeBasedOnColumnName:(NSString * const)columnName;

/**
 * Get column based on variable name.
 * @param variableName Name of variable.
 * @return Column object.
 */
- (SICAttribute *)getAttributeBasedOnVariableName:(NSString * const)variableName;

/**
 * Get all column names.
 * @return Iterator of all column names.
 */
- (NSEnumerator *)getColumnNames;

/**
 * Get all column names.
 * @return Iterator of all columns.
 */
- (NSEnumerator *)getAttributes;

/**
 * Add column to Entity Descriptor object.
 * @param attribute Column object.
 */
- (void)addAttribute:(SICAttribute * const)attribute;

/**
 * Remove column based on variable name.
 * @param variableName Name of variable.
 */
- (void)removeAttributeBasedOnVariableName:(NSString * const)variableName;

/**
 * Remove column based on column name.
 * @param columnName Name of column.
 */
- (void)removeAttributeBasedOnColumnName:(NSString * const)columnName;

/**
 * Remove column based on column object.
 * @param attribute Column object which need to be removed.
 */
- (void)removeAttribute:(SICAttribute * const)attribute;

/**
 * Check whether index exists based in index name.
 * @param indexName Name of index.
 * @return TRUE: If index exists, FALSE: If index do not exists.
 */
- (BOOL)containsIndex:(NSString * const)indexName;

/**
 * Get index object based on index name.
 * @param indexName Name of index.
 * @return Index object.
 */
- (SICIndex *)getIndex:(NSString * const)indexName;

/**
 * Get all index names.
 * @return Iterator which contains all index names.
 */
- (NSEnumerator *)getIndexNames;

/**
 * Get all indexs.
 * @return Iterator which contain all indexs.
 */
- (NSEnumerator *)getIndexes;

/**
 * Add index to EntityDescriptor object.
 * @param index Index object.
 */
- (void)addIndex:(SICIndex* const)index;

/**
 * Remove index object.
 * @param indexName Index object.
 */
- (void)removeIndexBasedOnName:(NSString * const)indexName;

/**
 * Remove index object.
 * @param index Index object.
 */
- (void)removeIndex:(SICIndex* const)index;

/**
 * Get iterator of relationship objects.
 * @return Relationship objects.
 */
- (NSEnumerator *)getRelationships;

/**
 * Get iterator of relationship objects based on refer.
 * @param refer Name of refer.
 * @return Relationship object based on refer.
 */
- (SICRelationship *)getRelationshipBasedOnRefer:(NSString *)refer;

/**
 * Get relationship object based on refer to.
 * @param referTo Name of refer to.
 * @return Relationship object based on refer to.
 */
- (SICRelationship *)getRelationshipBasedOnReferTo:(NSString *)referTo;

/**
 * Get one to one relationship object.
 * @return Iterator of relationship objects.
 */
- (NSEnumerator *)getOneToOneRelationships;

/**
 * Get one to many relationship object.
 * @return Iterator of relationship objects.
 */
- (NSEnumerator *)getOneToManyRelationships;

/**
 * Get many to one relationship object.
 * @return Iterator of relationship objects.
 */
- (NSEnumerator *)getManyToOneRelationships;

/**
 * Get many to many relationship object.
 * @return Iterator of relationship objects.
 */
- (NSEnumerator *)getManyToManyRelationships;

/**
 * Add relationship object.
 * @param relationship Relationship object.
 */
- (void)addRelationship:(SICRelationship*)relationship;

@end

/** Exposes methods to GET and SET Column information as per define in EntityDescriptor.xml file by application.
	
 Example:
 
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
 

 */
@interface SICAttribute : NSObject <SICIDescriptor> {
    
    NSString *getterMethodName;
    NSString *setterMethodName;
    
    NSMutableDictionary *properties;
}

/**
 * Get variable name.
 */
- (NSString *)getVariableName;

/**
 * Set variable name as per defined in EntityDescriptor.xml file.
 * @param variablename Name of variable.
 */
- (void)setVariableName:(NSString * const)variablename;

/**
 * Get column name.
 * @return Name Of Column.
 */
- (NSString *)getColumnName;

/**
 * Set column name as per defined in EntityDescriptor.xml file.
 * @param columnname Name of column name.
 */
- (void)setColumnName:(NSString * const)columnname;

/**
 * Get type of column.
 * @return Type of column.
 */
- (NSString *)getType;

/**
 * Set type of column as per defined in EntityDescriptor.xml file.
 * @param type Type of column.
 */
- (void)setType:(NSString * const)type;

/**
 * Get POJO class column getter method name.
 * @return POJO class column getter method name.
 */
- (NSString *)getGetterMethodName;

/**
 * Set POJO class column getter method name.
 * @param getMethodName POJO class coumn getter method name.
 */
- (void)setGetterMethodName:(NSString * const)getMethodName;

/**
 * Get POJO class column setter method name.
 * @return POJO class column setter method name.
 */
- (NSString *)getSetterMethodName;

/**
 * Set POJO class column setter method name.
 * @param setMethodName POJO class column setter method name.
 */
- (void)setSetterMethodName:(NSString * const)setMethodName;

/**
 * Get default value of column.
 * @return Default value of column.
 */
- (NSString *)getDefaultValue;

/**
 * Set default value of column as per defined in EntityDescriptor.xml file.
 * @param defaultValue Default value of column.
 */
- (void)setDefaultValue:(NSString * const)defaultValue;

/**
 * Get check constraint of column.
 * @return Check constraint of column.
 */
- (NSString *)getCheck;

/**
 * Set check constraint of column as per defined in EntityDescriptor.xml file.
 * @param check Check constraint.
 */
- (void)setCheck:(NSString * const)check;

/**
 * Check whether column is primary key.
 * @return TRUE: If column is primary key, FALSE: If column is not primary key.
 */
- (BOOL)isPrimaryKey;

/**
 * Set column as primary key or not.
 * @param primaryKey TRUE: If column is primary key, FALSE: If column is not primary key.
 */
- (void)setPrimaryKey:(BOOL const)primaryKey;

/**
 * Check whether column is unique or not.
 * @return TRUE: If column is unique, FALSE: If column is not unique.
 */
- (BOOL)isUnique;

/**
 * Set whether column is unique or not.
 * @param isUnique TRUE: If column is unique, FALSE: If column is not unique
 */
- (void)setUnique:(BOOL const)isUnique;

/**
 * Check whether column value can be not or not.
 * @return TRUE: If column value can be null, FALSE: If column value can not be null.
 */
- (BOOL)isNotNull;

/**
 * Set whether column can be null or not.
 * @param isNotNull TRUE: If column value can be null, FALSE: If column value can not be null.
 */
- (void)setNotNull:(BOOL const)isNotNull;

@end

/**
 * Contains relationship details.
 */
@interface SICRelationship : NSObject <SICIDescriptor> {
    
    NSString *getterReferMethodName;
    NSString *setterReferMethodName;
    
    NSMutableDictionary *properties;
    
    SICEntityDescriptor *referedEntityDescriptor;
}

/**
 * Get relationship type.
 * @return Type of relationship.
 */
- (NSString *)getRelationshipType;

/**
 * Set relationship type.
 * @param relationshiptype Type of relationship.
 */
- (void)setRelationshipType:(NSString *)relationshiptype;

/**
 * Get refer.
 * @return Name of refer.
 */
- (NSString *)getRefer;

/**
 * Set refer.
 * @param refername Name of refer.
 */
- (void)setRefer:(NSString *)refername;

/**
 * Get refer to.
 * @return Name of refer to.
 */
- (NSString *)getReferTo;

/**
 * Set refer to.
 * @param referto Name of refer to.
 */
- (void)setReferTo:(NSString *)referto;

/**
 * Get on update.
 * @return Action on update.
 */
- (NSString *)getOnUpdate;

/**
 * Set on update.
 * @param onupdate Action on update.
 */
- (void)setOnUpdate:(NSString *)onupdate;

/**
 * Get on delete.
 * @return Action on delete.
 */
- (NSString *)getOnDelete;

/**
 * Set on delete.
 * @param ondelete Action on delete.
 */
- (void)setOnDelete:(NSString *)ondelete;

/**
 * Get getter refer method name.
 * @return Getter refer method name.
 */
- (NSString *)getGetterReferMethodName;

/**
 * Set getter refer method name.
 * @param getterReferMethodname Name of getter refer method name.
 */
- (void)setGetterReferMethodName:(NSString *)getterReferMethodname;

/**
 * Get setter refer method name.
 * @return Name of setter refer method name.
 */
- (NSString *)getSetterReferMethodName;

/**
 * Set setter refer method name.
 * @param setterReferMethodname Name of setter refer name.
 */
- (void)setSetterReferMethodName:(NSString *)setterReferMethodname;

/**
 * Check whether load property value is set to TRUE/FASLE.
 * @return TRUE: If load property value is set to true; FALSE: If load property value is set to false.
 */
- (BOOL)isLoad;

/**
 * Set load property value.
 * @param load TRUE: If load property value is true; FALSE: If load property value is false.
 */
- (void)setLoad:(bool)load;

/**
 * Get entity descriptor object.
 * @return EntityDescriptor object.
 */
- (SICEntityDescriptor *)getReferedEntityDescriptor;

/**
 * Set refered entity descriptor object.
 * @param referedEntitydescriptor EntityDescriptor object.
 */
- (void)setReferedEntityDescriptor:(SICEntityDescriptor *)referedEntitydescriptor;

@end

/** Exposes methods to GET and SET Reference Map information as per define in EntityDescriptor.xml file by application.
	
 Example:
	
	<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
 <column>HISTORY</column>
	</index>
 */
@interface SICIndex : NSObject <SICIDescriptor> {
    
    NSMutableDictionary *properties;
    NSMutableArray *columns;
}

/**
 * Get index name.
 * @return Index Name.
 */
- (NSString *)getName;

/**
 * Set index name as per defined in EntityDescriptor.xml file.
 * @param name Index Name.
 */
- (void)setName:(NSString *)name;

/**
 * Check whether index should be unique or not.
 * @return TRUE: If index is unique, FALSE: If index is not unqiue.
 */
- (bool)isUnique;

/**
 * Set whether unqiue is unique or not.
 * @param unique TRUE: If index is unique, FALSE: If index is not unique.
 */
- (void)setUnique:(bool const)unique;

/**
 * Check whether index contain column or not.
 * @param column Name of column.
 * @return TRUE: If index contains column, FALSE: If index does not contain column.
 */
- (BOOL)containsColumn:(NSString * const)column;

/**
 * Get all columns.
 * @return Iterator which contain all columns.
 */
- (NSEnumerator *)getColumns;

/**
 * Add column to index.
 * @param column Name of column.
 */
- (void)addColumn:(NSString * const)column;

/**
 * Remove column from index.
 * @param column Name of column.
 */
- (void)removeColumn:(NSString * const)column;

@end

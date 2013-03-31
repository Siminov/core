/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.core;

import siminov.orm.annotation.Column;
import siminov.orm.annotation.Index;
import siminov.orm.annotation.IndexColumn;
import siminov.orm.annotation.Indexes;
import siminov.orm.annotation.Map;
import siminov.orm.annotation.Property;
import siminov.orm.annotation.Reference;
import siminov.orm.annotation.Table;
import siminov.orm.annotation.ColumnProperty;
import siminov.orm.annotation.OneToMany;
import siminov.orm.annotation.RelationshipProperty;

@Table(tableName="name_of_table")
@Indexes({
	@Index(name="name_of_index", unique="true/false", value={
		@IndexColumn(column="column_name_needs_to_add")
	}), 
})
public class DatabaseMappingDescriptor {

	@Column(columnName="column_name_of_table",
			properties={
				@Property(name="primary_key", value="true/false"),
				@Property(name="not_null", value="true/false"), 
				@Property(name="unique", value="true/false")
				@Property(name="check", value="condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)")
				@Property(name="default", value="default_value_of_column (Eg: 0.1)")
				})
	@OneToOne(onUpdate="cascade/restrict/no_action/set_null/set_default", onDelete="cascade/restrict/no_action/set_null/set_default", 
	properties={
		@RelationshipProperty(name=RelationshipProperty.LOAD, value="true")
		})
	@OneToMany(onUpdate="cascade/restrict/no_action/set_null/set_default", onDelete="cascade/restrict/no_action/set_null/set_default", 
	properties={
		@RelationshipProperty(name=RelationshipProperty.LOAD, value="true")
		})
	@ManyToOne(onUpdate="cascade/restrict/no_action/set_null/set_default", onDelete="cascade/restrict/no_action/set_null/set_default", 
	properties={
		@RelationshipProperty(name=RelationshipProperty.LOAD, value="true")
		})
	@ManyToMany(onUpdate="cascade/restrict/no_action/set_null/set_default", onDelete="cascade/restrict/no_action/set_null/set_default", 
	properties={
		@RelationshipProperty(name=RelationshipProperty.LOAD, value="true")
		})
	private String variableName = null;
	
}


/**
Type Of Annotation Tags

	i. @Table: This tag defines the relationship between database table and its mapped POJO class name.
		
			-> tableName*: Name of table. It is mandatory field. 

	ii. @Indexes: It contain all index needed in table.
	
	iii. @Index: This tag defines the index's needed by table.
			-> name*: Name of the index.
		
			-> unique: TRUE/FALSE. It defines whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
					Default value is false.
					
			COLUMN'S TAG
				
				-> column: Name of column's included in index.

	iv. @Column: This tag defines the relationship between database table column and its mapped POJO class variable name.
	
			-> columnName*: Name of database table column name which it defines.

			COLUMN PROPERTIES: @Properties

				It contain all properties needed by column.

			
			COLUMN PROPERTY: @Property: This tag defines a perticular property of column.

				-> primary_key | ColumnProperty.PRIMARY_KEY: TRUE/FALSE. It defines whether the field is primary key to table or not. It is optional field. Default value is false.
				
				-> not_null | ColumnProperty.NOT_NULL: TRUE/FALSE. It defines whether the field can be empty or not. It is optional field. Default value is false.
				
				-> unique | ColumnProperty.UNIQUE: TRUE/FALSE. It defines whether the field should be unique or not. It is optional field. Default value is false.
				
				-> default | ColumnProperty.DEFAULT: It define the default value of that column in table. It is optional field.
				
				-> check | ColumnProperty.CHECK: It define the condition needs to be satisfied for column value. It is optinal field.

	vii. @OneToOne: This tag defines one-to-one relationship, where each row in one database table is linked to 1 and only 1 other row in another table.

			-> onUpdate: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when update occur.
			
			-> onDelete: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when delete occur.
			
	viii. @OneToMany: This tag defines one-to-many relationship, where each row in the related to table can be related to many rows in the relating table.
				This effectively save storage as the related record does not need to be stored multiple times in the relating table.
	
			-> onUpdate: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when update occur.
			
			-> onDelete: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when delete occur.

	ix. @ManyToOne: This tag defines one-to-many relationship, where one entity (typically a column or set of columns) contains values that refer to another entity.
				(a column or set of columns) that has unique values.
	
			-> onUpdate: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when update occur.
			
			-> onDelete: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when delete occur.

	x. @ManyToMany: This tag defines one-to-many relationship, where one or more rows in a table can be related to 0, 1 or many rows in another table. 
				A mapping table is required in order to implement such a relationship.
	
			-> onUpdate: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when update occur.
			
			-> onDelete: cascade/restrict/no_action/set_null/set_default. It defines action needs to be done, when delete occur.

	
		RELATIONSHIP PROPERTY TAG: @RelationshipProperty
		
			-> load: TRUE/FALSE, It defines whether it need to be load or not.


-->
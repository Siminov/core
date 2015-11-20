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
#import "SICResourceManager.h"
#import "SICSiminovSAXDefaultHandler.h"

/** Exposes methods to parse Library Descriptor information as per define in DatabaseDescriptor.si.xml or LibraryDescriptor.si.xml  file by application.
	
 Example:
 
     <database-mapping-descriptor>
     
        <!-- General Properties Of Table And Class -->
     
            <!-- TABLE_NAME: Mandatory Field -->
            <!-- CLASS_NAME: Mandatory Field -->
        <entity table_name="name_of_table" class_name="mapped_pojo_class_name">
     
            <!-- Column Properties Required Under This Table -->
         
            <!-- Optional Field -->
         
                <!-- VARIABLE_NAME: Mandatory Field -->
                <!-- COLUMN_NAME: Mandatory Field -->
            <attribute column_name="column_name_of_table" variable_name="class_variable_name">
         
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
         
         
         
             <!-- Index Properties -->
             
                <!-- Optional Field -->
                    <!-- NAME: Mandatory Field -->
                    <!-- UNIQUE: Optional Field (Default is false) -->
                <index name="name_of_index" unique="true/false">
                    <column>column_name_needs_to_add</column>
                </index>
         
         
         
            <!-- Map Relationship Properties -->
         
            <!-- Optional Field's -->
            <relationships>
         
                    <!-- REFER: Mandatory Field -->
                    <!-- REFER_TO: Mandatory Field -->
                <one-to-one refer="class_variable_name" refer_to="map_to_pojo_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
         
                        <!-- Optional Field (Default is false) -->
                    <property name="load">true/false</property>
                </one-to-one>
         
                    <!-- REFER: Mandatory Field -->
                    <!-- REFER_TO: Mandatory Field -->
                <one-to-many refer="class_variable_name" refer_to="map_to_pojo_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
         
                        <!-- Optional Field (Default is false) -->
                    <property name="load">true/false</property>
                </one-to-many>
         
                    <!-- REFER: Mandatory Field -->
                    <!-- REFER_TO: Mandatory Field -->
                <many-to-one refer="class_variable_name" refer_to="map_to_pojo_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
         
                        <!-- Optional Field (Default is false) -->
                    <property name="load">true/false</property>
                </many-to-one>
         
                    <!-- REFER: Mandatory Field -->
                    <!-- REFER_TO: Mandatory Field -->
                <many-to-many refer="class_variable_name" refer_to="map_to_pojo_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
         
                        <!-- Optional Field (Default is false) -->
                    <property name="load">true/false</property>
                </many-to-many>
         
            </relationships>
 
        </entity>
     
     </database-mapping-descriptor>
 */
@interface SICDatabaseMappingDescriptorReader : SICSiminovSAXDefaultHandler {

	NSMutableString *tempValue;
    NSString *propertyName;
    
    NSString *databaseMappingName;
    
    SICResourceManager *resourceManager;
    
    SICDatabaseMappingDescriptor *databaseMappingDescriptor;
    
    SICAttribute *currentAttribute;
    SICIndex *currentIndex;
    SICRelationship *currectRelationship;
    
    BOOL isColumn;
    BOOL isIndex;
    BOOL isRelationship;
}


/**
 * DatabaseMappingDescriptor Constructor
 * @param databaseMappingDescriptorName Name of the database mapping descriptor name
 */
- (id)initWithClassName:(NSString * const)databaseMappingDescriptorName;
- (SICDatabaseMappingDescriptor *)getDatabaseMappingDescriptor;

@end

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
#import "SICSiminovSAXDefaultHandler.h"

/** Exposes methods to parse Entity Descriptor information as per define in DatabaseDescriptor.xml or LibraryDescriptor.xml  file by application.
	
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
@interface SICEntityDescriptorReader : SICSiminovSAXDefaultHandler {
    
    NSMutableString *tempValue;
    NSString *propertyName;
    
    NSString *entityDescriptorName;
    
    SICResourceManager *resourceManager;
    
    SICEntityDescriptor *entityDescriptor;
    
    SICAttribute *currentAttribute;
    SICIndex *currentIndex;
    SICRelationship *currectRelationship;
    
    bool isAttribute;
    bool isIndex;
    bool isRelationship;
}


/**
 * EntityDescriptor Constructor
 * @param entityDescriptorName Name of the entity descriptor name
 */
- (id)initWithClassName:(NSString * const)entityDescriptorName;


- (SICEntityDescriptor *)getEntityDescriptor;

@end

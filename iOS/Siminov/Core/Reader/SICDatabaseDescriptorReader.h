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

/** Exposes methods to parse Database Descriptor information as per define in DatabaseDescriptor.xml file by application.
	
 Example:
	
     <database-descriptor>
     
         <!-- General Database Descriptor Properties -->
         
            <!-- Mandatory Field -->
        <property name="database_name">name_of_database_file</property>
         
            <!-- Optional Field (Default is sqlite)-->
        <property name="type">type_of_database</property>
         
            <!-- Mandatory Field -->
        <property name="version">database_version</property>
         
            <!-- Optional Field -->
        <property name="description">database_description</property>
         
            <!-- Optional Field (Default is false) -->
        <property name="transaction_safe">true/false</property>
            
            <!-- Optional Field (Default is false) -->
        <property name="external_storage">true/false</property>
         
         
         
        <!-- Entity Descriptor Paths Needed Under This Database Descriptor -->
            
            <!-- Optional Field -->
        <entity-descriptors>
            <entity-descriptor>full_path_of_entity_descriptor_file</entity-descriptor>
        </entity-descriptors>
        
     </database-descriptor>
 */
@interface SICDatabaseDescriptorReader : SICSiminovSAXDefaultHandler {
    NSString *databaseDescriptorPath;
    
    SICDatabaseDescriptor *databaseDescriptor;
    
    SICResourceManager *resourceManager;
    
    NSMutableString *tempValue;
    NSString *propertyName;
}

/**
 * DatabaseDescriptorReader Constructor
   @param databaseDescriptorpath Path of the database descriptor.
 */
- (id)initWithPath:(NSString * const)databaseDescriptorpath;

/**
 * Get database descriptor object.
 * @return Database Descriptor Object.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptor;

@end

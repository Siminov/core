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
#import "SICEntityDescriptor.h"

/** Exposes methods to GET and SET Database Descriptor information as per define in DatabaseDescriptor.xml file by application.
	
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
@interface SICDatabaseDescriptor: NSObject <SICIDescriptor> {

	NSMutableDictionary *properties;
    NSMutableArray *entityDescriptorPaths;
    
    NSMutableDictionary *entityDescriptorsBasedOnTableName;
    NSMutableDictionary *entityDescriptorsBasedOnClassName;
    NSMutableDictionary *entityDescriptorsBasedOnPath;
}

/**
 * Get database descriptor name as defined in DatabaseDescriptor.core.xml file.
 * @return Database Descriptor Name.
 */
- (NSString *)getDatabaseName;

/**
 * Set database descriptor name as per defined in DatabaseDescriptor.core.xml file.
 * @param databaseName Database Descriptor Name.
 */
- (void)setDatabaseName:(NSString * const)databaseName;

/**
 * Get type of database
 * @return Type of database
 */
- (NSString *)getType;

/**
 * Set type of database
 * @param type Type of database
 */
- (void)setType:(NSString *)type;

/**
 * Get Version of Application as per defined in ApplicationDescriptor.core.xml file.
 * Version of application.
 */
- (double) getVersion;

/**
 * Set Version of Application as per defined in ApplicationDescriptor.core.xml file.
 * @param version Version of application.
 */
- (void)setVersion:(double const)version;

/**
 * Get description as per defined in DatabaseDescriptor.core.xml file.
 * @return Description defined in DatabaseDescriptor.core.xml file.
 */
- (NSString *)getDescription;

/**
 * Set description as per defined in DatabaseDescritor.xml file.
 * @param description Description defined in DatabaseDescriptor.core.xml file.
 */
- (void)setDescription:(NSString * const)description;

/**
 * Check whether database needs to be stored on SDCard or not.
 * @return TRUE:If external_storage defined as true in DatabaseDescriptor.core.xml file, FALSE:If external_storage defined as false in DatabaseDescritor.xml file.
 */
- (BOOL)isExternalStorageEnable;

/**
 * Set the external storage value as per defined in DatabaseDescriptor.xml file.
 * @param isExternalStorageEnable (true/false) External Storage Enable Or Not.
 */
- (void)setExternalStorageEnable:(BOOL const) isExternalStorageEnable;

/**
 * Check whether database transactions to make multi-threading safe or not.
 * @return TRUE: If locking is required as per defined in DatabaseDescriptor.xml file, FALSE: If locking is not required as per defined in DatabaseDescriptor.xml file.
 */
- (BOOL)isTransactionSafe;

/**
 * Set database locking as per defined in DatabaseDescriptor.xml file.
 * @param transactionSafe (true/false) database locking as per defined in DatabaseDescriptor.xml file.
 */
- (void)setTransactionSafe:(BOOL const) transactionSafe;

/**
 * Check whether entity descriptor object exists or not, based on table name.
 * @param tableName Name of table.
 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
 */
- (BOOL)containsEntityDescriptorBasedOnTableName:(NSString * const)tableName;

/**
 * Check whether entity descriptor object exists or not, based on mapped class name.
 * @param className Mapped class name.
 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
 */
- (BOOL)containsEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Get all entity descriptor paths as per defined in DatabaseDescriptor.core.xml file.
 * @return Iterator which contain all entity descriptor paths.
 */
- (NSEnumerator *)getEntityDescriptorPaths;

/** Add entity descriptor path as per defined in DatabaseDescriptor.xml file.
 
 EXAMPLE:

    <database-descriptor>
        <entity-descriptors>
            <entity-descriptor>Entity-Descriptors/Liquor.xml</entity-descriptor>
            <entity-descriptor>Entity-Descriptors/LiquorBrand.xml</entity-descriptor>
        </entity-descriptors>
	</database-descriptor>
 
 @param entityDescriptorPath Entity Descriptor Path.
 */
- (void)addEntityDescriptorPath:(NSString * const)entityDescriptorPath;

/**
 * Get all entity descriptor objects contained.
 * @return All entity descriptor objects.
 */
- (NSEnumerator *)getEntityDescriptors;

/**
 * Get entity descriptor object based on table name.
 * @param tableName Name of table.
 * @return EntityDescriptor object based on table name.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnTableName:(NSString * const)tableName;

/**
 * Get entity descriptor object based on mapped class name.
 * @param className Mapped class name.
 * @return Entity Descriptor object.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Get entity descriptor object based on path.
 * @param entityDescriptorPath Entity Descriptor path as per defined in Database Descriptor.xml file.
 * @return Entity Descriptor object.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnPath:(NSString * const)entityDescriptorPath;

/**
 * Add entity descriptor object in respect to entity descriptor path.
 * @param entityDescriptorPath Entity Descriptor Path.
 * @param entityDescriptor Entity Descriptor object.
 */
- (void)addEntityDescriptor:(NSString * const)entityDescriptorPath entityDescriptor:(SICEntityDescriptor *)entityDescriptor;

/**
 * Remove entity descriptor object based on entity descriptor path.
 * @param entityDescriptorPath Entity Descriptor Path.
 */
- (void)removeEntityDescriptorBasedOnPath:(NSString * const)entityDescriptorPath;

/**
 * Remove entity descriptor object based on mapped class name.
 * @param className Mapped class name.
 */
- (void)removeEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Remove entity descriptor object based on table name.
 * @param tableName Name of table.
 */
- (void)removeEntityDescriptorBasedOnTableName:(NSString * const)tableName;

/**
 * Remove entity descriptor object based on entity descriptor object.
 * @param entityDescriptor Entity Descriptor object which needs to be removed.
 */
- (void)removeEntityDescriptor:(SICEntityDescriptor * const)entityDescriptor;

/**
 * Get all entity descriptor objects in sorted order. The order will be as per defined in DatabaseDescriptor.core.xml file.
 * @return Iterator which contains all entity descriptor objects.
 */
- (NSEnumerator *)orderedEntityDescriptors;

@end

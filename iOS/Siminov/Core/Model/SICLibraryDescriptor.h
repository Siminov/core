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
#import "SICEntityDescriptor.h"

/** Exposes methods to GET and SET Library Descriptor information as per define in LibraryDescriptor.core.xml file by application.
 
 Example:
 
     <library-descriptor>
        
         <!-- General Properties Of Library -->
         
         <!-- Mandatory Field -->
         <property name="name">name_of_library</property>
         
         <!-- Optional Field -->
         <property name="description">description_of_library</property>
            
         
         
         <!-- Entity Descriptor Needed Under This Library Descriptor -->
         
         <!-- Optional Field -->
            <!-- Entity Descriptor Descriptors -->
         <entity-descriptors>
            <entity-descriptor>name_of_database_descriptor.full_path_of_entity_descriptor_file</entity-descriptor>
         </entity-descriptors>
     
    </library-descriptor>
 */
@interface SICLibraryDescriptor : NSObject <SICIDescriptor> {
    NSMutableDictionary *properties;
    
    NSMutableArray *entityDescriptorPaths;
    
    NSMutableDictionary *entityDescriptorsBasedOnTableName;
    NSMutableDictionary *entityDescriptorsBasedOnClassName;
    NSMutableDictionary *entityDescriptorsBasedOnPath;
}

/**
 * Get library name.
 */
- (NSString *)getName;

/**
 * Set library name as per defined in LibraryDescriptor.core.xml
 * @param name Name of Library.
 */
- (void)setName:(NSString * const)name;

/**
 * Get descriptor as per defined in LibraryDescriptor.core.xml
 * @return
 */
- (NSString *)getDescription;

/**
 * Set description as per defined in LibraryDescritor.core.xml
 * @param description Description of Library.
 */
- (void)setDescription:(NSString * const)description;

/**
 * Check whether entity descriptor object exists or not, based on table name.
 * @param tableName Name of table.
 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
 */
- (BOOL)containsEntityDescriptorBasedOnTableName:(NSString * const)tableName;

/**
 * Check whether entity descriptor object exists or not, based on POJO class name.
 * @param className Mapped class name.
 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
 */
- (BOOL)containsEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Get all entity descriptor paths as per defined in DatabaseDescriptor.xml file.
 * @return Iterator which contain all entity descriptor paths.
 */
- (NSEnumerator *)getEntityDescriptorPaths;

/** Add entity descriptor path as per defined in DatabaseDescriptor.core.xml file.

 EXAMPLE:
 
     <database-descriptor>
        <entity-descriptors>
            <entity-descriptor>Entity-Descriptors/Book.xml</entity-descriptor>
            <entity-descriptor>Entity-Descriptors/Lession.xml</entity-descriptor>
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
 * @param libraryEntityDescriptorPath Library Database path as per defined in Database Descriptor.xml file.
 * @return Entity Descriptor object.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnPath:(NSString * const)libraryEntityDescriptorPath;

/**
 * Add entity descriptor object in respect to entity descriptor path.
 * @param libraryEntityDescriptorPath Library Entity Descriptor Path.
 * @param entityDescriptor Entity Descriptor object.
 */
- (void)addEntityDescriptor:(NSString * const)libraryEntityDescriptorPath entityDescriptor:(SICEntityDescriptor * const)entityDescriptor;

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
 * Get all entity descriptor objects in sorted order. The order will be as per defined in DatabaseDescriptor.xml file.
 * @return Iterator which contains all entity descriptor objects.
 */
- (NSEnumerator *)orderedEntityDescriptors;

@end

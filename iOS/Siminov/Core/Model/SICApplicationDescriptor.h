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

/** Exposes methods to GET and SET Application Descriptor information as per define in ApplicationDescriptor.xml file by application.
 
 Example:
 
	<siminov>
 
         <!-- General Application Description Properties -->
         
         <!-- Mandatory Field -->
         <property name="name">application_name</property>
         
         <!-- Optional Field -->
         <property name="description">application_description</property>
         
         <!-- Mandatory Field (Default is 0.0) -->
         <property name="version">application_version</property>
         
         
         <!-- Database Descriptors Used By Application (zero-to-many) -->
             <!-- Optional Field's -->
         <database-descriptors>
             <database-descriptor>full_path_of_database_descriptor_file</database-descriptor>
         </database-descriptors>
         
         
         <!-- Library Descriptors Used By Application (zero-to-many) -->
             <!-- Optional Field's -->
         <library-descriptors>
             <library-descriptor>full_path_of_library_descriptor_file</library-descriptor>
         </library-descriptors>
         
         
         <!-- Event Handlers Implemented By Application (zero-to-many) -->
         
             <!-- Optional Field's -->
         <event-handlers>
             <event-handler>full_class_path_of_event_handler_(ISiminovHandler/IDatabaseHandler)</event-handler>
         </event-handlers>
	
	</siminov>
 */
@interface SICApplicationDescriptor: NSObject <SICIDescriptor> {
    NSMutableDictionary *properties;
    
    NSMutableArray *databaseDescriptorPaths;
    NSMutableDictionary *databaseDescriptorsBasedOnName;
    NSMutableDictionary *databaseDescriptorsBasedOnPath;
    
    NSMutableArray *events;
    
    NSMutableArray *libraryDescriptorPaths;
}

/**
 * Get Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
 * @return Application Descriptor Name.
 */
- (NSString *)getName;

/**
 * Set Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
 * @param name Name of Application Descriptor.
 */
- (void)setName:(NSString * const)name;

/**
 * Set Description of Application as per defined in ApplicationDescriptor.xml file.
 * @return Description of application.
 */
- (NSString *)getDescription;

/**
 * Set Description of Application as per defined in ApplicationDescriptor.xml file.
 * @param description Description of application.
 */
- (void)setDescription:(NSString * const)description;

/**
 * Get Version of Application as per defined in ApplicationDescriptor.xml file.
 * @return Version of application.
 */
- (double)getVersion;

/**
 * Set Version of Application as per defined in ApplicationDescriptor.xml file.
 * @param version Version of application.
 */
- (void)setVersion:(double const)version;

- (NSString *)getDeploy;

- (void)setDeploy:(NSString *)deploy;


/**
 * Check whether database needed by application or not.
 * @return TRUE: If database needed by application, FALSE: If database is not needed by application.
 */
- (BOOL)isDatabaseNeeded;

/**
 * Check whether database descriptor exists in Resources or not.
 * @param databaseDescriptor Database Descriptor object.
 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
 */
- (BOOL)containsDatabaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Check whether database descriptor exists in Resources or not, based on database descriptor path.
 * @param containDatabaseDescriptorPath Database Descriptor path.
 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
 */
- (BOOL)containsDatabaseDescriptorBasedOnPath:(NSString * const)containDatabaseDescriptorPath;

/**
 * Check whether database descriptor exists in Resources or not, based on Database Descriptor name.
 * @param databaseDescriptorName Database Descriptor Name.
 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
 */
- (BOOL)containsDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName;

/**
 * Get Database Descriptor based on Database Descriptor Name.
 * @param databaseDescriptorName Database Desciptor Name.
 * @return Database Descriptor Object.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName;

/**
 * Get Database Descriptor based on Database Descriptor Path.
 * @param databaseDescriptorPath Database Descriptor Path.
 * @return Database Descriptor Object.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnPath:(NSString * const)databaseDescriptorPath;

/**
 * Get all database descriptor paths as per contained in ApplicationDescriptor.xml file.
 * @return Iterator which contains all database descriptor paths.
 */
- (NSEnumerator *)getDatabaseDescriptorPaths;

/**
 * Get all database descriptor names as per needed by application.
 * @return Iterator which contains all database descriptor names.
 */
- (NSEnumerator *)getDatabaseDescriptorNames;

/**
 * Add Database Descriptor path as per contained in ApplicationDescriptor.xml file.
 * @param databaseDescriptorPath DatabaseDescriptor path.
 */
- (void)addDatabaseDescriptorPath:(NSString * const)databaseDescriptorPath;

/**
 * Get all database descriptor objects contains by Siminov.
 * @return Iterator which contains all database descriptor objects.
 */
- (NSEnumerator *)getDatabaseDescriptors;

/**
 * Add Database Descriptor object in respect to database descriptor path.
 * @param databaseDescriptorPath Database Descriptor Path.
 * @param databaseDescriptor Database Descriptor Object.
 */
- (void)addDatabaseDescriptor:(NSString * const)databaseDescriptorPath databaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Remove Database Descriptor from Resources based on database path provided, as per defined in ApplicationDescriptor.xml file
 * @param databaseDescriptorPath Database Descriptor Path.
 */
- (void)removeDatabaseDescriptorBasedOnPath:(NSString * const)databaseDescriptorPath;

/**
 * Remove Database Descriptor from Resources based in database name provided, as per defined in DatabaseDescriptor.xml file
 * @param databaseDescriptorName DatabaseDescriptor Name.
 */
- (void)removeDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName;

/**
 * Remove DatabaseDescriptor object from Resources.
 * @param databaseDescriptor DatabaseDescriptor object which needs to be removed.
 */
- (void)removeDatabaseDescriptor:(SICDatabaseDescriptor * const)databaseDescriptor;

/**
 * Add library descriptor path
 * @param libraryDescriptorPath Library Descriptor Path
 */
- (void)addLibraryDescriptorPath:(NSString * const)libraryDescriptorPath;

/**
 * Get all library descriptor paths
 * @return Library Descriptor Paths
 */
- (NSEnumerator *)getLibraryDescriptorPaths;

/**
 * Check whether it contains library descriptor path or not
 * @param libraryDescriptorPath Path of Library Descriptor
 * @return (true/false) TRUE: If library descriptor path exists | FALSE: If library descriptor path does not exists.
 */
- (BOOL)containLibraryDescriptorPath:(NSString * const)libraryDescriptorPath;

/**
 * Remove library descriptor path
 * @param libraryDescriptorPath Path of library descriptor
 */
- (void)removeLibraryDescriptorPath:(NSString * const)libraryDescriptorPath;

/**
 * Get all event handlers as per defined in ApplicationDescriptor.xml file.
 * @return All event handlers defined in ApplicationDescriptor.xml file
 */
- (NSEnumerator *)getEvents;

/**
 * Add event as per defined in ApplicationDescriptor.xml file.
 * @param event Event Handler class name.
 */
- (void)addEvent:(NSString * const)event;

/**
 * Remove event as per defined event name
 * @param event Name of the event
 */
- (void)removeEvent:(NSString * const)event;

@end

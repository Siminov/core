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
#import <UIKit/UIKit.h>
#import "SICApplicationDescriptor.h"
#import "SICDeploymentException.h"
#import "SICISiminovEvents.h"
#import "SICDatabaseFactory.h"
#import "SICIDatabaseEvents.h"
#import "SICDatabaseDescriptor.h"
#import "SICConstants.h"

/** It handles and provides all resources needed by SIMINOV such as:
 <p>
 Provides Application Descriptor, Database Descriptor, Library Descriptor, Entity Descriptor.
 */
@interface SICResourceManager : NSObject {
    SICApplicationDescriptor *applicationDescriptor;
    SICDatabaseFactory *databaseFactory;
    
    NSMutableDictionary *databaseBasedOnDatabaseDescriptorName;
    NSMutableDictionary *databaseBasedOnEntityDescriptorClassName;
    NSMutableDictionary *databaseBasedOnEntityDescriptorTableName;
}

/**
 * It provides an instance of Resources class
 * @return Resources instance.
 */
+ (SICResourceManager*)getInstance;

/**
 * Get Application Descriptor object of application.
 * @return Application Descriptor.
 */
- (SICApplicationDescriptor *)getApplicationDescriptor;

/**
 * Set Application Descriptor of application.
 * @param applicationDescriptorInstance Application Descriptor object.
 */
- (void)setApplicationDescriptor:(SICApplicationDescriptor * const)applicationDescriptorInstance;

/** Get iterator of all database descriptors provided in Application Descriptor file.
 
 Example: ApplicationDescriptor.xml
	
	<siminov>
	
        <database-descriptors>
            <database-descriptor>DatabaseDescriptor.xml</database-descriptor>
        </database-descriptors>
 
	</siminov>

 @return Iterator which contains all database descriptor paths provided.
 */
- (NSEnumerator *)getDatabaseDescriptorPaths;

/** Get DatabaseDescriptor based on path provided as per defined in Application Descriptor file.

 Example: ApplicationDescriptor.xml
	
	<siminov>
	
        <database-descriptors>
            <database-descriptor>DatabaseDescriptor.xml</database-descriptor>
        </database-descriptors>
 
	</siminov>
 
 @param databaseDescriptorPath Iterator which contains all database descriptor paths provided.
 @return Database Descriptor
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnPath:(NSString *)databaseDescriptorPath;

/** Get Database Descriptor based on database descriptor name provided as per defined in Database Descriptor file.
 
 Example: DatabaseDescriptor.xml
	
	<database-descriptor>
	
        <property name="database_name">SIMINOV-CORE-SAMPLE</property>
 
	</database-descriptor>
	
 @param databaseDescriptorName Database Descriptor object based on database descriptor name provided.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnName:(NSString *)databaseDescriptorName;

/**
 * Get all Database Descriptors object.
 * @return Iterator which contains all Database Descriptors.
 */
- (NSEnumerator *)getDatabaseDescriptors;

/**
 * Get Database Descriptor based on POJO class name provided.
 *
 * @param className POJO class name.
 * @return Database Descriptor object in respect to POJO class name.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnClassName:(NSString *)className;

/**
 * Get database descriptor name based on class name
 * @param className Name of Class
 * @return Database Descriptor Name
 */
- (NSString *)getDatabaseDescriptorNameBasedOnClassName:(NSString *)className;

/**
 * Get Database Descriptor based on table name provided.
 *
 * @param tableName Name of table.
 * @return Database Descriptor object in respect to table name.
 */
- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnTableName:(NSString *)tableName;

/**
 * Get database descriptor name based on table name
 * @param tableName Name of Table
 * @return Database Descriptor Name
 */
- (NSString *)getDatabaseDescriptorNameBasedOnTableName:(NSString *)tableName;

/**
 * Get Entity Descriptor based on mapped class name provided.
 *
 * @param className Mapped class name.
 * @return Entity Descriptor object in respect to class name.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Get Entity Descriptor based on table name provided.
 *
 * @param tableName Name of table.
 * @return Database Descriptor object in respect to table name.
 */
- (SICEntityDescriptor *)getEntityDescriptorBasedOnTableName:(NSString * const)tableName;

/**
 * Get all entity descriptors
 * @return Entity Descriptors
 */
- (NSEnumerator *)getEntityDescriptors;

/**
 * Get entity descriptor Object based on class name provided. If entity descriptor object not present in resource layer, it will parse EntityDescriptor.xml file defined by application and will place it in resource layer.
 * @param className Full name of class.
 * @return EntityDescriptor object.
 * @exception SICSiminovException If any exception occur while getting entity descriptor object.
 */
- (SICEntityDescriptor *)requiredEntityDescriptorBasedOnClassName:(NSString * const)className;

/**
 * Get IDatabase object based on Database Descriptor name.
 * @param databaseName Name of Database Descriptor.
 * @return IDatabase object.
 */
- (SICDatabaseBundle *)getDatabaseBundle:(NSString * const)databaseName;

/**
 * Get all IDatabase objects contain by application.
 * @return Iterator which contains all IDatabase objects.
 */
- (NSEnumerator *)getDatabaseBundles;

/**
 * Remove IDatabase object from Resources based on Database Descriptor name.
 * @param databaseDescriptorName Database Descriptor name.
 */
- (void)removeDatabaseBundle:(NSString * const)databaseDescriptorName;

/**
 * Get SIMINOV-EVENT Handler
 * @return ISiminovEvents implementation object as per defined by application.
 */
- (id<SICISiminovEvents>)getSiminovEventHandler;

/**
 * Get DATABASE-EVENT Handler
 * @return IDatabaseEvents implementation object as per defined by application.
 */
- (id<SICIDatabaseEvents>)getDatabaseEventHandler;


@end

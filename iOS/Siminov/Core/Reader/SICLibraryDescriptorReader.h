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
#import "SICLibraryDescriptor.h"
#import "SICResourceManager.h"
#import "SICSiminovSAXDefaultHandler.h"

/** Exposes methods to parse Library Descriptor information as per define in LibraryDescriptor.xml file by application.
	
 Example:
	
	<library-descriptor>
	
        <!-- General Properties Of Library -->
 
        <!-- Mandatory Field -->
        <property name="name">name_of_library</property>
 
        <!-- Optional Field -->
        <property name="description">description_of_library</property>
	
 
 
        <!-- Entity Descriptor Needed Under This Library Descriptor -->
 
        <!-- Optional Field -->
            <!-- Entity Descriptors -->
        <entity-descriptors>
            <entity-descriptor>name_of_database_descriptor.full_path_of_entity_descriptor_file</entity-descriptor>
        </entity-descriptors>
 
	</library-descriptor>

 */
@interface SICLibraryDescriptorReader: SICSiminovSAXDefaultHandler {
    NSString *libraryName;
    
    SICLibraryDescriptor *libraryDescriptor;
    
    NSMutableString *tempValue;
    NSString *propertyName;
}

/**
 * LibraryDescriptorReader Constructor
 * @param libraryname Name of the library
 */
- (id)initWithLibraryName:(NSString * const)libraryname;

/**
 * Get library descriptor object.
 * @return Library Descriptor Object.
 */
- (SICLibraryDescriptor *)getLibraryDescriptor;

@end

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

#import "SICDatabaseMappingDescriptorReader.h"
#import "SICFileUtils.h"

@implementation SICDatabaseMappingDescriptorReader

- (id)init {
    self = [super init];
    
    if(self) {
        tempValue = [[NSMutableString alloc] init];
        resourceManager = [SICResourceManager getInstance];
        
        isColumn = false;
        isIndex = false;
        isRelationship = false;
        
        return self;
    }
    
    return self;
}


- (id)initWithClassName:(NSString * const)databaseMappingDescriptorName {
    
    if (self = [super init]) {
        databaseMappingName = databaseMappingDescriptorName;
        
        if (databaseMappingDescriptorName == nil || databaseMappingDescriptorName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Invalid name found. DATABASE-MAPPING-MODEL: %@", databaseMappingName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Invalid name found. DATABASE-MAPPING-MODEL: %@", databaseMappingName]];
        }
        
        NSData *databaseMappingStream = nil;
        
        @try {
            
            NSString *databaseDescriptorPathName;
            
            if ([databaseMappingDescriptorName hasSuffix:FILE_TYPE]) {
                
                NSUInteger index = 0;
                NSRange range = [databaseMappingDescriptorName rangeOfString:FILE_TYPE];
                if (range.length == 0 && range.location > databaseMappingDescriptorName.length) {
                    index = 0;
                } else {
                    index = range.location;
                }
                
                databaseDescriptorPathName = [databaseMappingDescriptorName substringToIndex:index];
            }
            
            NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:databaseDescriptorPathName inDirectory:DIRECTORY_NAME];
            databaseMappingStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            
            if(databaseMappingStream == nil) {
                
                NSUInteger index = 0;
                NSRange range = [databaseDescriptorPathName rangeOfString:LIBRARY_DESCRIPTOR_DATABASE_MAPPING_SEPRATOR options:NSBackwardsSearch];
                if (range.length == 0 && range.location > databaseDescriptorPathName.length) {
                    index = 0;
                } else {
                    index = range.location;
                }
                
                NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[databaseDescriptorPathName componentsSeparatedByString:@"."]];
                NSString *filename = [directoryParts lastObject];
                
                [directoryParts removeLastObject];
                
                NSString *directoryPartsJoined = [directoryParts componentsJoinedByString:@"."];
                databaseDescriptorPathName = [NSString stringWithFormat:@"%@/%@", directoryPartsJoined, filename];
               
                NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:databaseDescriptorPathName inDirectory:@"include"];
                databaseMappingStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            }
            
            if(databaseMappingStream == nil) {
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of database mapping descriptor,  DATABASE-MAPPING-MODEL: %@", databaseMappingName]];
            }
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of database mapping descriptor, DATABASE-MAPPING-MODEL: %@, %@", databaseMappingName, [exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of database mapping descriptor,  DATABASE-MAPPING-MODEL: %@, %@", databaseMappingName, [exception reason]]];
        }
        
        @try {
            [self parseMessage:databaseMappingStream];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-MAPPING:  %@, %@", databaseMappingName, [exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-MAPPING: %@, %@", databaseMappingName, [exception reason]]];
        }
        
        [self doValidation];
    }
    
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_DATABASE_MAPPING_DESCRIPTOR] == NSOrderedSame) {
        databaseMappingDescriptor = [[SICDatabaseMappingDescriptor alloc] init];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_ENTITY] == NSOrderedSame) {
        [self initializeEntity:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_ATTRIBUTE] == NSOrderedSame) {
        if (!isIndex) {
            [self initializeAttribute:attributeDict];
        }
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self initializeProperty:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_INDEX] == NSOrderedSame) {
        [self initalizeIndex:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS] == NSOrderedSame) {
        isRelationship = true;
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE] == NSOrderedSame) {
        currectRelationship = [[SICRelationship alloc] init];
        [currectRelationship setRelationshipType:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE];
        
        [self initializeRelationship:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY] == NSOrderedSame) {
        currectRelationship = [[SICRelationship alloc] init];
        [currectRelationship setRelationshipType:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY];
        
        [self initializeRelationship:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE] == NSOrderedSame) {
        currectRelationship = [[SICRelationship alloc] init];
        [currectRelationship setRelationshipType:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE];
        
        [self initializeRelationship:attributeDict];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY] == NSOrderedSame) {
        currectRelationship = [[SICRelationship alloc] init];
        [currectRelationship setRelationshipType:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY];
        
        [self initializeRelationship:attributeDict];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    [tempValue appendString:(NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {

    if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self processProperty];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_ATTRIBUTE] == NSOrderedSame) {
        if (currentIndex != nil) {
            [currentIndex addColumn:(NSString *)tempValue];
            return;
        }
        
        [databaseMappingDescriptor addAttribute:currentAttribute];
        isColumn = false;
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_INDEX] == NSOrderedSame) {
        [databaseMappingDescriptor addIndex:currentIndex];
        isIndex = false;
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE] == NSOrderedSame) {
        [self processRelationship];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY] == NSOrderedSame) {
        [self processRelationship];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE] == NSOrderedSame) {
        [self processRelationship];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY] == NSOrderedSame) {
        [self processRelationship];
    } else if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS] == NSOrderedSame) {
        isRelationship = false;
    }
}


- (SICDatabaseMappingDescriptor *)getDatabaseMappingDescriptor {
    return databaseMappingDescriptor;
}


- (void)initializeEntity:(NSDictionary *)attributes {
    NSString *tableName = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_ENTITY_TABLE_NAME];
    NSString *className = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_ENTITY_CLASS_NAME];
    
    [databaseMappingDescriptor setTableName:tableName];
    [databaseMappingDescriptor setClassName:className];
}


- (void)initializeAttribute:(NSDictionary *)attributes {
    
    NSString *variableName = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME];
    NSString *columnName = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME];
    
    NSString *charArray = [NSString stringWithFormat:@"%@%@", [[variableName substringToIndex:1] uppercaseString], [variableName substringFromIndex:1]];
    NSString *getterMethodName = [NSString stringWithFormat:@"get%@", charArray];
    NSString *setterMethodName = [NSString stringWithFormat:@"set%@:", charArray];
    
    currentAttribute = [[SICAttribute alloc] init];
    
    [currentAttribute setVariableName:variableName];
    [currentAttribute setColumnName:columnName];
    [currentAttribute setGetterMethodName:getterMethodName];
    [currentAttribute setSetterMethodName:setterMethodName];
    
    isColumn = true;
}


- (void)initializeProperty:(NSDictionary *)attributes {
    propertyName = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_NAME];
}


- (void)initalizeIndex:(NSDictionary *)attributes {
    NSString *name = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_NAME];
    NSString *unique = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_UNIQUE];
    
    currentIndex = [[SICIndex alloc] init];
    
    [currentIndex setName:name];
    
    if(unique != nil && unique.length > 0 && [unique caseInsensitiveCompare:@"true"] == NSOrderedSame) {
        [currentIndex setUnique:true];
    } else {
        [currentIndex setUnique:false];
    }
    
    isIndex = true;
}


- (void)initializeRelationship:(NSDictionary * const)attributes {
    
    NSString *refer = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER];
    NSString *referTo = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER_TO];
    
    NSString *onUpdate = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_UPDATE];
    NSString *onDelete = [attributes objectForKey:DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_DELETE];
    
    [currectRelationship setRefer:refer];
    [currectRelationship setReferTo:referTo];
    
    NSString *charArray = [NSString stringWithFormat:@"%@%@", [[refer substringToIndex:1] uppercaseString], [refer substringFromIndex:1]];
    NSString *getterReferMethodName = [NSString stringWithFormat:@"get%@", charArray];
    NSString *setterReferMethodName = [NSString stringWithFormat:@"set%@", charArray];

    [currectRelationship setGetterReferMethodName:getterReferMethodName];
    [currectRelationship setSetterReferMethodName:setterReferMethodName];
    
    [currectRelationship setOnUpdate:onUpdate];
    [currectRelationship setOnDelete:onDelete];
    
}


- (void)processProperty {
    
    if (isRelationship) {
        [currectRelationship addProperty:propertyName value:(NSString *)tempValue];
    } else if (isColumn) {
        [currentAttribute addProperty:propertyName value:(NSString *)tempValue];
    }
}


- (void)processRelationship {
    [databaseMappingDescriptor addRelationship:currectRelationship];
}


- (void)doValidation {
    
    /*
     * Validate Table Name field.
     */
    
    NSString *tableName = [databaseMappingDescriptor getTableName];
    if (tableName == nil || tableName.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"TABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING: %@", databaseMappingName]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"TABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING: %@", databaseMappingName]];
    }
    
    /*
     * Validate Class Name field.
     */
    NSString *className = [databaseMappingDescriptor getClassName];
    if(className == nil || className.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"CLASS-NAME IS MANDATORY FIELD - DATABASE-MAPPING: %@", databaseMappingName]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"CLASS-NAME IS MANDATORY FIELD - DATABASE-MAPPING: %@", databaseMappingName]];
    }
    
    NSEnumerator *attributes = [databaseMappingDescriptor getAttributes];
    SICAttribute *attribute;
    
    while (attribute = [attributes nextObject]) {
        
        /*
         * Validate Variable Name field.
         */
        NSString *variableName = [attribute getVariableName];
        if(variableName == nil || variableName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"VARIABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"VARIABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
        }
        
        /*
         * Validate Column Name filed.
         */
        NSString *columnName = [attribute getColumnName];
        if(columnName == nil || columnName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
        }
        
        /*
         * Validate Type field.
         */
        NSString *type = [attribute getType];
        if(type == nil || type.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-TYPE IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-TYPE IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: %@", databaseMappingName]];
        }
    }
}

@end

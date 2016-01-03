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


#import "SICEntityDescriptorReader.h"
#import "SICFileUtils.h"

@implementation SICEntityDescriptorReader

- (id)init {
    self = [super init];
    
    if(self) {
        tempValue = [[NSMutableString alloc] init];
        resourceManager = [SICResourceManager getInstance];
        
        isAttribute = false;
        isIndex = false;
        isRelationship = false;
        
        return self;
    }
    
    return self;
}


- (id)initWithClassName:(NSString * const)entityDescriptorname {
    
    if (self = [super init]) {
        entityDescriptorName = entityDescriptorname;
        
        if (entityDescriptorName == nil || entityDescriptorName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Invalid name found. ENTITY-DESCRIPTOR-MODEL: %@", entityDescriptorName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Invalid name found. ENTITY-DESCRIPTOR-MODEL: %@", entityDescriptorName]];
        }
        
        NSData *entityDescriptorStream = nil;
        
        @try {
            
            NSString *databaseDescriptorPathName;
            
            if ([entityDescriptorName hasSuffix:FILE_TYPE]) {
                
                NSUInteger index = 0;
                NSRange range = [entityDescriptorName rangeOfString:FILE_TYPE];
                if (range.length == 0 && range.location > entityDescriptorName.length) {
                    index = 0;
                } else {
                    index = range.location;
                }
                
                databaseDescriptorPathName = [entityDescriptorName substringToIndex:index];
            }
            
            NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:databaseDescriptorPathName inDirectory:DIRECTORY_NAME];
            entityDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            
            if(entityDescriptorStream == nil) {
                
                NSUInteger index = 0;
                NSRange range = [databaseDescriptorPathName rangeOfString:LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR options:NSBackwardsSearch];
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
                entityDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            }
            
            if(entityDescriptorStream == nil) {
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: %@", entityDescriptorName]];
            }
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of entity descriptor, ENTITY-DESCRIPTOR-MODEL: %@, %@", entityDescriptorName, [exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: %@, %@", entityDescriptorName, [exception reason]]];
        }
        
        @try {
            [self parseMessage:entityDescriptorStream];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing ENTITY-DESCRIPTOR:  %@, %@", entityDescriptorName, [exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing ENTITY-DESCRIPTOR: %@, %@", entityDescriptorName, [exception reason]]];
        }
        
        [self doValidation];
    }
    
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR] == NSOrderedSame) {
        entityDescriptor = [[SICEntityDescriptor alloc] init];
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        propertyName = [attributeDict objectForKey:ENTITY_DESCRIPTOR_NAME];
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_ATTRIBUTE] == NSOrderedSame) {
        currentAttribute = [[SICAttribute alloc] init];
        isAttribute = true;
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_INDEX] == NSOrderedSame) {
        currentIndex = [[SICIndex alloc] init];
        isIndex = true;
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP] == NSOrderedSame) {
        currectRelationship = [[SICRelationship alloc] init];
        isRelationship = true;
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    [tempValue appendString:(NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    
    if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self processProperty];
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_ATTRIBUTE] == NSOrderedSame) {
        [entityDescriptor addAttribute:currentAttribute];
        isAttribute = false;
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_INDEX] == NSOrderedSame) {
        [entityDescriptor addIndex:currentIndex];
        isIndex = false;
    } else if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP] == NSOrderedSame) {
        [entityDescriptor addRelationship:currectRelationship];
        isRelationship = false;
    }
}


- (SICEntityDescriptor *)getEntityDescriptor {
    return entityDescriptor;
}


- (void)processProperty {
    
    if(isAttribute) {
        [currentAttribute addProperty:propertyName value:(NSString *)tempValue];
        
        if([propertyName caseInsensitiveCompare:ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME] == NSOrderedSame) {
            
            NSString *charArray = [NSString stringWithFormat:@"%@%@", [[(NSString *)tempValue substringToIndex:1] uppercaseString], [(NSString *)tempValue substringFromIndex:1]];
            
            NSString *getterMethodName = [NSString stringWithFormat:@"get%@", charArray];
            NSString *setterMethodName = [NSString stringWithFormat:@"set%@:", charArray];
            
            [currentAttribute setGetterMethodName:getterMethodName];
            [currentAttribute setSetterMethodName:setterMethodName];
        }
    } else if(isIndex) {
        
        if([propertyName caseInsensitiveCompare:ENTITY_DESCRIPTOR_INDEX_COLUMN] == NSOrderedSame) {
            [currentIndex addColumn:(NSString *)tempValue];
        } else {
            [currentIndex addProperty:propertyName value:(NSString *)tempValue];
        }
    } else if(isRelationship) {
        [currectRelationship addProperty:propertyName value:(NSString *)tempValue];
        
        if([propertyName caseInsensitiveCompare:ENTITY_DESCRIPTOR_RELATIONSHIP_REFER] == NSOrderedSame) {
            
            NSString *charArray = [NSString stringWithFormat:@"%@%@", [[(NSString *)tempValue substringToIndex:1] uppercaseString], [(NSString *)tempValue substringFromIndex:1]];
            NSString *getterReferMethodName = [NSString stringWithFormat:@"get%@", charArray];
            NSString *setterReferMethodName = [NSString stringWithFormat:@"set%@", charArray];
            
            [currectRelationship setGetterReferMethodName:getterReferMethodName];
            [currectRelationship setSetterReferMethodName:setterReferMethodName];
        }
    } else {
        [entityDescriptor addProperty:propertyName value:(NSString *)tempValue];
    }
}

- (void)doValidation {
    
    /*
     * Validate Table Name field.
     */
    
    NSString *tableName = [entityDescriptor getTableName];
    if (tableName == nil || tableName.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: %@", entityDescriptorName]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: %@", entityDescriptorName]];
    }
    
    /*
     * Validate Class Name field.
     */
    NSString *className = [entityDescriptor getClassName];
    if(className == nil || className.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: %@", entityDescriptorName]];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: %@", entityDescriptorName]];
    }
    
    NSEnumerator *attributes = [entityDescriptor getAttributes];
    SICAttribute *attribute;
    
    while (attribute = [attributes nextObject]) {
        
        /*
         * Validate Variable Name field.
         */
        NSString *variableName = [attribute getVariableName];
        if(variableName == nil || variableName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
        }
        
        /*
         * Validate Column Name filed.
         */
        NSString *columnName = [attribute getColumnName];
        if(columnName == nil || columnName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
        }
        
        /*
         * Validate Type field.
         */
        NSString *type = [attribute getType];
        if(type == nil || type.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:[NSString stringWithFormat:@"COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: %@", entityDescriptorName]];
        }
    }
}

@end

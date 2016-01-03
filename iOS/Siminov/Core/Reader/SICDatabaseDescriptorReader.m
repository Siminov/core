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

#import "SICDatabaseDescriptorReader.h"
#import "SICFileUtils.h"

@implementation SICDatabaseDescriptorReader

- (id)init {
    self = [super init];
    
    if(self) {
        tempValue = [[NSMutableString alloc] init];
        resourceManager = [SICResourceManager getInstance];
        
        return self;
    }
    
    return self;
}

- (id)initWithPath:(NSString * const)databaseDescriptorpath {
    
    if (self = [super init]) {
        if (databaseDescriptorpath == nil || databaseDescriptorpath.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Database Descriptor path found."];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Database Descriptor path found."];
        }
        
        databaseDescriptorPath = databaseDescriptorpath;
        
        NSData *databaseDescriptorStream = nil;
        
        @try {
        
            NSString *databaseDescriptorPathName;
            
            if ([databaseDescriptorpath hasSuffix:FILE_TYPE]) {
                
                NSUInteger index = 0;
                NSRange range = [databaseDescriptorPath rangeOfString:FILE_TYPE];
                if (range.length == 0 && range.location > databaseDescriptorPath.length) {
                    index = 0;
                } else {
                    index = range.location;
                }

                databaseDescriptorPathName = [databaseDescriptorpath substringToIndex:index];
            }
            
            NSString *filePath = [[[SICFileUtils alloc]init] getFilePath:databaseDescriptorPathName inDirectory:DIRECTORY_NAME];
            NSLog([NSString stringWithFormat:@"Database Descriptor Path: %@", filePath], __PRETTY_FUNCTION__);
            
            databaseDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            
            if(databaseDescriptorStream == nil) {
                
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
                NSLog([NSString stringWithFormat:@"Database Descriptor Path: %@", filePath], __PRETTY_FUNCTION__);
                
                databaseDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            }
            
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting database descriptor file stream, %@", [exception reason]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[exception reason]];
        }
        
        @try {
            [self parseMessage:databaseDescriptorStream];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-DESCRIPTOR: %@, %@", databaseDescriptorPath, [exception reason]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-DESCRIPTOR: %@, %@", databaseDescriptorPath,[exception reason]]];
        }
        
        [self doValidation];
    }
    
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if ([elementName caseInsensitiveCompare:DATABASE_DESCRIPTOR] == NSOrderedSame) {
        databaseDescriptor = [[SICDatabaseDescriptor alloc] init];
    } else if ([elementName caseInsensitiveCompare:DATABASE_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self initializeProperty:attributeDict];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    [tempValue appendString:(NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    
    if ([elementName caseInsensitiveCompare:DATABASE_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [databaseDescriptor addProperty:propertyName value:(NSString *)tempValue];
    } else if ([elementName caseInsensitiveCompare:DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR] == NSOrderedSame) {
        [databaseDescriptor addEntityDescriptorPath:(NSString *)tempValue];
    }
}

- (void)initializeProperty:(NSDictionary *)attributes {
    propertyName = [attributes objectForKey:DATABASE_DESCRIPTOR_PROPERTY_NAME];
}


- (void)doValidation {
    
    /*
     * Validate Database Name field.
    */
    
}

- (SICDatabaseDescriptor *)getDatabaseDescriptor {
    
    if ([databaseDescriptor getType] == nil || [databaseDescriptor getType].length <= 0) {
        [databaseDescriptor setType:SQLITE_DATABASE];
    }
    
    return databaseDescriptor;
}

@end

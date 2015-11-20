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

#import "SICQuickDatabaseMappingDescriptorReader.h"
#import "SICFileUtils.h"
#import "SICDatabaseMappingDescriptorReader.h"
#import "SICPrematureEndOfParseException.h"

static SICResourceManager *resourceManager;

@implementation SICQuickDatabaseMappingDescriptorReader

+ (void)initialize {
    resourceManager = [SICResourceManager getInstance];
}


- (id)initWithClassName:(NSString * const)findDatabaseMappingDescriptorBasedOnClassName {
  
    if (self = [super init]) {
        
        if (findDatabaseMappingDescriptorBasedOnClassName == nil || findDatabaseMappingDescriptorBasedOnClassName.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Database Mapping Class Name Which Needs To Be Searched."];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Database Mapping Class Name Which Needs To Be Searched."];
        }

        finalDatabaseMappingBasedOnClassName = findDatabaseMappingDescriptorBasedOnClassName;
    }
    
    return self;
}

- (void)process {
    
    SICApplicationDescriptor *applicationDescriptor = [resourceManager getApplicationDescriptor];
    if (applicationDescriptor == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:@"Invalid Application Context found."];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:@"Invalid Application Context found."];
    }
    
    if (![applicationDescriptor isDatabaseNeeded]) {
        doesMatch = false;
        return;
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
       
        NSEnumerator *databaseMappingDescriptors = [databaseDescriptor getDatabaseMappingDescriptorPaths];
        NSString *databaseMappingDescriptorPath;
        
        while (databaseMappingDescriptorPath = [databaseMappingDescriptors nextObject]) {
            
            NSData *databaseMappingDescriptorStream = nil;
            
            @try {
                
                NSString *databaseDescriptorPathName;
                
                if ([databaseMappingDescriptorPath hasSuffix:FILE_TYPE]) {
                    
                    NSUInteger index = 0;
                    NSRange range = [databaseMappingDescriptorPath rangeOfString:FILE_TYPE];
                    if (range.length == 0 && range.location > databaseMappingDescriptorPath.length) {
                        index = 0;
                    } else {
                        index = range.location;
                    }
                    
                    databaseDescriptorPathName = [databaseMappingDescriptorPath substringToIndex:index];
                }
                
                NSString *filePath = [[[SICFileUtils alloc]init] getFilePath:databaseDescriptorPathName inDirectory:DIRECTORY_NAME];
                databaseMappingDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];

                if(databaseMappingDescriptorStream == nil) {
                    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[databaseDescriptorPathName componentsSeparatedByString:@"."]];
                    NSString *filename = [directoryParts lastObject];
                    
                    [directoryParts removeLastObject];
                    
                    NSString *directoryPartsJoined = [directoryParts componentsJoinedByString:@"."];
                    databaseDescriptorPathName = [NSString stringWithFormat:@"%@/%@", directoryPartsJoined, filename];
                    
                    NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:databaseDescriptorPathName inDirectory:@"include"];
                    databaseMappingDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
                }
                if(databaseMappingDescriptorStream == nil) {
                    @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of database mapping descriptor, DATABASE-MAPPING-MODEL:%@", databaseMappingDescriptorPath]];
                }
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while getting input stream of DATABASE-MAPPING: %@, %@", databaseMappingDescriptorPath, [exception reason]]];
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while getting input stream of DATABASE-MAPPING: %@, %@", databaseMappingDescriptorPath, [exception reason]]];
            }
            
            @try {
                [self parseMessage:databaseMappingDescriptorStream];
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-DESCRIPTOR: %@, %@", databaseMappingDescriptorPath, [exception reason]]];
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-DESCRIPTOR:  %@, %@", databaseMappingDescriptorPath, [exception reason]]];
            }
            
            if (doesMatch) {
                SICDatabaseMappingDescriptorReader *databaseMappingParser = [[SICDatabaseMappingDescriptorReader alloc] initWithClassName:databaseMappingDescriptorPath];
                
                databaseMappingDescriptor = [databaseMappingParser getDatabaseMappingDescriptor];
                [databaseDescriptor addDatabaseMappingDescriptor:databaseMappingDescriptorPath databaseMappingDescriptor:databaseMappingDescriptor];
                
                return;
            }
        }
    }
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if ([elementName caseInsensitiveCompare:DATABASE_MAPPING_DESCRIPTOR_ENTITY] == NSOrderedSame) {
        NSString *className = [attributeDict objectForKey:DATABASE_MAPPING_DESCRIPTOR_ENTITY_CLASS_NAME];
        
        NSUInteger lastIndex = 0;
        NSRange range = [className rangeOfString:@"." options:NSBackwardsSearch];
        if (range.length == 0 && range.location > className.length) {
            lastIndex = 0;
        } else {
            lastIndex = range.location+1;
        }

        if ([className caseInsensitiveCompare:finalDatabaseMappingBasedOnClassName] == NSOrderedSame) {
            doesMatch = true;
        } else if ([[className substringFromIndex:lastIndex] caseInsensitiveCompare:finalDatabaseMappingBasedOnClassName] == NSOrderedSame) {
            doesMatch = true;
        }
        
        [parser setDelegate:nil];
        [parser abortParsing];
        parser = nil;
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
     tempValue = (NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    
}

-(SICDatabaseMappingDescriptor *)getDatabaseMapping {
    return databaseMappingDescriptor;
}

@end

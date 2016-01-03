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

#import "SICQuickEntityDescriptorReader.h"
#import "SICFileUtils.h"
#import "SICEntityDescriptorReader.h"
#import "SICPrematureEndOfParseException.h"

static SICResourceManager *resourceManager;

@implementation SICQuickEntityDescriptorReader

+ (void)initialize {
    resourceManager = [SICResourceManager getInstance];
}


- (id)initWithClassName:(NSString * const)findEntityDescriptorBasedOnClassname {
    
    if (self = [super init]) {
        
        if (findEntityDescriptorBasedOnClassname == nil || findEntityDescriptorBasedOnClassname.length <= 0) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Entity Descriptor Class Name Which Needs To Be Searched."];
            @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:@"Invalid Entity Descriptor Class Name Which Needs To Be Searched."];
        }
        
        finalEntityDescriptorBasedOnClassName = findEntityDescriptorBasedOnClassname;
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
        
        NSEnumerator *entityDescriptors = [databaseDescriptor getEntityDescriptorPaths];
        NSString *entityDescriptorPath;
        
        while (entityDescriptorPath = [entityDescriptors nextObject]) {
            
            NSData *entityDescriptorStream = nil;
            
            @try {
                
                NSString *databaseDescriptorPathName;
                
                if ([entityDescriptorPath hasSuffix:FILE_TYPE]) {
                    
                    NSUInteger index = 0;
                    NSRange range = [entityDescriptorPath rangeOfString:FILE_TYPE];
                    if (range.length == 0 && range.location > entityDescriptorPath.length) {
                        index = 0;
                    } else {
                        index = range.location;
                    }
                    
                    databaseDescriptorPathName = [entityDescriptorPath substringToIndex:index];
                }
                
                NSString *filePath = [[[SICFileUtils alloc]init] getFilePath:databaseDescriptorPathName inDirectory:DIRECTORY_NAME];
                entityDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
                
                if(entityDescriptorStream == nil) {
                    NSMutableArray *directoryParts = [NSMutableArray arrayWithArray:[databaseDescriptorPathName componentsSeparatedByString:@"."]];
                    NSString *filename = [directoryParts lastObject];
                    
                    [directoryParts removeLastObject];
                    
                    NSString *directoryPartsJoined = [directoryParts componentsJoinedByString:@"."];
                    databaseDescriptorPathName = [NSString stringWithFormat:@"%@/%@", directoryPartsJoined, filename];
                    
                    NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:databaseDescriptorPathName inDirectory:@"include"];
                    entityDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
                }
                if(entityDescriptorStream == nil) {
                    @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"Exception caught while getting input stream of entity descriptor, ENTITY-DESCRIPTOR-MODEL:%@", entityDescriptorPath]];
                }
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while getting input stream of ENTITY-DESCRIPTOR: %@, %@", entityDescriptorPath, [exception reason]]];
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while getting input stream of ENTITY-DESCRIPTOR: %@, %@", entityDescriptorPath, [exception reason]]];
            }
            
            @try {
                [self parseMessage:entityDescriptorStream];
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing ENTITY-DESCRIPTOR: %@, %@", entityDescriptorPath, [exception reason]]];
                @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing DATABASE-DESCRIPTOR:  %@, %@", entityDescriptorPath, [exception reason]]];
            }
            
            if (doesMatch) {
                SICEntityDescriptorReader *entityDescriptorParser = [[SICEntityDescriptorReader alloc] initWithClassName:entityDescriptorPath];
                
                entityDescriptor = [entityDescriptorParser getEntityDescriptor];
                [databaseDescriptor addEntityDescriptor:entityDescriptorPath entityDescriptor
                                                       :entityDescriptor];
                
                return;
            }
        }
    }
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        propertyName = [attributeDict objectForKey:ENTITY_DESCRIPTOR_NAME];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    tempValue = (NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    
    if([elementName caseInsensitiveCompare:ENTITY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        
        if([propertyName caseInsensitiveCompare:ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME] == NSOrderedSame) {
            
            if([(NSString *)tempValue caseInsensitiveCompare:finalEntityDescriptorBasedOnClassName] == NSOrderedSame) {
                doesMatch = true;
            }
            
            [parser setDelegate:nil];
            [parser abortParsing];
            parser = nil;
        }
    }
}

-(SICEntityDescriptor *)getEntityDescriptor {
    return entityDescriptor;
}

@end
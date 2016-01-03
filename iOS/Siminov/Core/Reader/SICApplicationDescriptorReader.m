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


#import "SICApplicationDescriptorReader.h"
#import "SICFileUtils.h"

@implementation SICApplicationDescriptorReader


/**
 * ApplicationDescriptorReader Constructor
 */
- (id)init {
    self = [super init];
    
    if(self) {
        tempValue = [[NSMutableString alloc] init];
        resourceManager = [SICResourceManager getInstance];
        
        NSData *applicationDescriptorStream = nil;
        
        @try {
            
            NSString *filePath = [[[SICFileUtils alloc] init] getFilePath:APPLICATION_DESCRIPTOR_FILE_NAME inDirectory:DIRECTORY_NAME];
            applicationDescriptorStream = [[NSFileManager defaultManager] contentsAtPath:filePath];
            
            if (applicationDescriptorStream == nil) {
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"IOException caught while getting input stream of application descriptor"]];
            }
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"IOException caught while getting input stream of application descriptor %@", [exception reason]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"Constructor" message:[NSString stringWithFormat:@"IOException caught while getting input stream of application descriptor %@", [exception reason]]];
        }
        
        @try {
            [self parseMessage:applicationDescriptorStream];
        }
        @catch (NSException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing APPLICATION-DESCRIPTOR, %@", [exception reason]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"process" message:[NSString stringWithFormat:@"Exception caught while parsing APPLICATION-DESCRIPTOR %@", [exception reason]]];
        }
        
        [self doValidation];
    }
    
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    tempValue = [[NSMutableString alloc] init];
    
    if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_SIMINOV] == NSOrderedSame) {
        applicationDescriptor = [[SICApplicationDescriptor alloc] init];
    } else if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self initializeProperty:attributeDict];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    [tempValue appendString:(NSMutableString *)[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {

    if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [applicationDescriptor addProperty:propertyName value:(NSString *)tempValue];
    } else if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR] == NSOrderedSame) {
        [applicationDescriptor addDatabaseDescriptorPath:(NSString *)tempValue];
    } else if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_EVENT_HANDLER] == NSOrderedSame) {
        if (tempValue == nil || tempValue.length <= 0) {
            return;
        }
        [applicationDescriptor addEvent:(NSString *)tempValue];
    } else if ([elementName caseInsensitiveCompare:APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR] == NSOrderedSame) {
        if (tempValue == nil || tempValue.length <= 0) {
            return;
        }
        [applicationDescriptor addLibraryDescriptorPath:(NSString *)tempValue];
    }
}

- (void)initializeProperty:(NSDictionary *)attributes {
    propertyName = [attributes objectForKey:APPLICATION_DESCRIPTOR_NAME];
}

- (void)doValidation {
    
    /*
     * Validate Application Name field.
     */
    NSString *name = [applicationDescriptor getName];
    if (name == nil || name.length <= 0) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"doValidation" message:@"NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR"];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"doValidation" message:@"NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR"];
    }
}

-(SICApplicationDescriptor *)getApplicationDescriptor {
    return applicationDescriptor;
}


@end

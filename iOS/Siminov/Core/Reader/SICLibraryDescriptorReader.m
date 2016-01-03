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



#import "SICLibraryDescriptorReader.h"
#import "SICFileUtils.h"

@implementation SICLibraryDescriptorReader

- (id)init {
    self = [super init];
    
    if(self) {
        tempValue = [[NSMutableString alloc] init];
        return self;
    }
    
    return self;
}

- (id)initWithLibraryName:(NSString * const)libraryname {
    
    if (self = [super init]) {
        
        if (libraryname == nil || libraryname.length <= 0) {
            [SICLog error: NSStringFromClass([self class]) methodName: @"Constructor" message: @"Invalid Library Name Found."];
            @throw [[SICSiminovException alloc] initWithClassName: NSStringFromClass([self class]) methodName: @"Constructor" message: @"Invalid Library Name Found."];
        }
        
        libraryName = libraryname;
        NSData *libraryDescriptorStream = nil;
        
        NSUInteger index = 0;
        NSRange range = [LIBRARY_DESCRIPTOR_FILE_NAME rangeOfString: LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR];
        if (range.length == 0 && range.location > LIBRARY_DESCRIPTOR_FILE_NAME.length) {
            index = 0;
        } else {
            index = range.location;
        }
        
        NSString *libraryDescriptorPathName = [NSString stringWithFormat: @"%@/%@", libraryName, [LIBRARY_DESCRIPTOR_FILE_NAME substringWithRange: NSMakeRange(0,index)]];
        
        NSString *filePath = [[[SICFileUtils alloc] init] getFilePath: libraryDescriptorPathName inDirectory: @"include"];
        libraryDescriptorStream = [[NSFileManager defaultManager] contentsAtPath: filePath];
        
        if(libraryDescriptorStream == nil) {
            [SICLog error: NSStringFromClass([self class]) methodName: @"Constructor" message: [NSString stringWithFormat:@"Invalid Library Descriptor Stream Found, LIBRARY-NAME: %@", filePath]];
            @throw [[SICSiminovException alloc] initWithClassName: NSStringFromClass([self class]) methodName: @"Constructor" message: [NSString stringWithFormat: @"Invalid Library Descriptor Stream Found, LIBRARY-NAME: %@", filePath]];

        }
        
        @try {
            [self parseMessage: libraryDescriptorStream];
        }
        @catch (NSException *exception) {
            [SICLog error: NSStringFromClass([self class]) methodName: @"Constructor" message: [NSString stringWithFormat: @"Exception caught while parsing LIBRARY-DESCRIPTOR: %@, %@", libraryName, [exception reason]]];
            @throw [[SICSiminovException alloc] initWithClassName: NSStringFromClass([self class]) methodName: @"Constructor" message: [NSString stringWithFormat: @"Exception caught while parsing LIBRARY-DESCRIPTOR: %@, %@", libraryName, [exception reason]]];
        }
        
        [self doValidation];
    }
    
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    
    tempValue = [[NSMutableString alloc] init];
    
    if ([elementName caseInsensitiveCompare: LIBRARY_DESCRIPTOR_LIBRARY_DESCRIPTOR] == NSOrderedSame) {
        libraryDescriptor = [[SICLibraryDescriptor alloc] init];
    } else if ([elementName caseInsensitiveCompare: LIBRARY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [self initializeProperty: attributeDict];
    }
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    [tempValue appendString:(NSMutableString *)[string stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceAndNewlineCharacterSet]]];
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    
    if ([elementName caseInsensitiveCompare: LIBRARY_DESCRIPTOR_PROPERTY] == NSOrderedSame) {
        [libraryDescriptor addProperty: propertyName value:(NSString *)tempValue];
    } else if ([elementName caseInsensitiveCompare: LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR] == NSOrderedSame) {
        [libraryDescriptor addEntityDescriptorPath:(NSString *)tempValue];
    }
}

- (void)initializeProperty:(NSDictionary *)attributeDict {
    propertyName = [attributeDict objectForKey: LIBRARY_DESCRIPTOR_NAME];
}

- (void)doValidation {

    /*
     * Validation for name field.
     */
    
    NSString *name = [libraryDescriptor getName];
    if (name == nil || name.length <= 0) {
        [SICLog error: NSStringFromClass([self class]) methodName: @"doValidation" message:[NSString stringWithFormat: @"LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: %@", libraryName]];
        @throw [[SICDeploymentException alloc] initWithClassName: NSStringFromClass([self class]) methodName: @"doValidation" message: [NSString stringWithFormat: @"LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: %@", libraryName]];

    }
    
}

-(SICLibraryDescriptor *)getLibraryDescriptor {
    return libraryDescriptor;
}

@end

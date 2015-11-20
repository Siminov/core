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


#import "SICSiminovSAXDefaultHandler.h"
#import "SICLog.h"
#import "SICPrematureEndOfParseException.h"
#import "SICSiminovException.h"

@implementation SICSiminovSAXDefaultHandler

- (void)parseMessage:(NSData * const)inputStream {
    
    if (inputStream == nil) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"parseMessage" message:@"Invalid InputStream Found."];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"parseMessage" message:@"Invalid InputStream Found."];
    }
    
    NSXMLParser *parser;
    
    @try {
        parser = [[NSXMLParser alloc] initWithData:inputStream];
        [parser setDelegate:self];
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"parseMessage" message:@"Exception caught while creating new instance of NSXML parser."];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"parseMessage" message:[NSString stringWithFormat:@"Exception caught while creating new instance of NSXML parser, %@",[exception reason]]];
    }
    
    @try {
        [parser parse];
    }
    @catch (SICPrematureEndOfParseException *prematureEndOfParseException) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"parserMessage" message:[NSString stringWithFormat:@"PrematureEndOfParseException caught while parsing input stream, %@",[prematureEndOfParseException getMessage]]];
    }
    @catch (NSException *exception) {
        [SICLog error:NSStringFromClass([self class]) methodName:@"parseMessage" message:[NSString stringWithFormat:@"Exception caught while parsing input stream, %@",[exception reason]]];
        @throw [[SICSiminovException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"parseMessage" message:[NSString stringWithFormat:@"Exception Caught While parsing input stream, %@",[exception reason]]];
    }
}

@end

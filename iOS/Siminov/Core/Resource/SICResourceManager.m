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


#import "SICResourceManager.h"
#import <objc/runtime.h>
#import "SICQuickEntityDescriptorReader.h"
#import "SICEventHandler.h"

@implementation SICResourceManager

static SICResourceManager *resources = nil;


/**
 * Resource Private Constructor
 */
-(id)init {
    
    self = [super init];
    
    if (self) {
        databaseFactory = [SICDatabaseFactory getInstance];
    }
    
    return self;
}

+ (SICResourceManager *)getInstance {

    if(!resources) {
        resources = [[super allocWithZone:NULL] init];
    }
    
    return resources;
}

+ (id)allocWithZone:(NSZone *)zone {
    return [self getInstance];
}

- (SICApplicationDescriptor *)getApplicationDescriptor {
    return applicationDescriptor;
}

- (void)setApplicationDescriptor:(SICApplicationDescriptor * const)applicationDescriptorInstance {
    applicationDescriptor = applicationDescriptorInstance;
}

- (NSEnumerator *)getDatabaseDescriptorPaths {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"databaseDescriptorsPaths" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    return [applicationDescriptor getDatabaseDescriptorPaths];
}

- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnPath:(NSString *)databaseDescriptorPath {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorBasedOnPath" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    return [applicationDescriptor getDatabaseDescriptorBasedOnPath: databaseDescriptorPath];
}

- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnName:(NSString * const)databaseDescriptorName {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorBasedOnName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    return [applicationDescriptor getDatabaseDescriptorBasedOnName: databaseDescriptorName];
}

- (NSEnumerator *)getDatabaseDescriptors {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptors" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    return [applicationDescriptor getDatabaseDescriptors];
}

- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnClassName:(NSString *)className {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorBasedOnClassName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnClassName:className];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return databaseDescriptor;
        }
    }
    return nil;
}

- (NSString *)getDatabaseDescriptorNameBasedOnClassName:(NSString * const)className {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorNameBasedOnClassName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnClassName:className];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return [databaseDescriptor getDatabaseName];
        }
    }
    return nil;
}

- (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnTableName:(NSString *)tableName {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorBasedOnTableName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnTableName:tableName];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return databaseDescriptor;
        }
    }
    
    return nil;
}

- (NSString *)getDatabaseDescriptorNameBasedOnTableName:(NSString *)tableName {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getDatabaseDescriptorBasedOnTableName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnTableName:tableName];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return [databaseDescriptor getDatabaseName];
        }
    }
    return nil;
}

- (SICEntityDescriptor *)getEntityDescriptorBasedOnClassName:(NSString * const)className {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getEntityDescriptorBasedOnClassName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnClassName:className];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return [databaseDescriptor getEntityDescriptorBasedOnClassName:className];
        }
    }
    
    return nil;
}

- (SICEntityDescriptor *)getEntityDescriptorBasedOnTableName:(NSString * const)tableName {
    
    if(applicationDescriptor == nil) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"getEntityDescriptorBasedOnTableName" message:@"Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND."];
    }
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = NULL;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        BOOL containsEntityDescriptorInDatabaseDescriptor = [databaseDescriptor containsEntityDescriptorBasedOnTableName:tableName];
        if(containsEntityDescriptorInDatabaseDescriptor) {
            return [databaseDescriptor getEntityDescriptorBasedOnTableName:tableName];
        }
    }
    
    return nil;
}

- (NSEnumerator *)getEntityDescriptors {
    
    NSMutableArray *entityDescriptors = [[NSMutableArray alloc] init];
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = nil;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        NSEnumerator *selfEntityDescriptors = [databaseDescriptor getEntityDescriptors];
        SICEntityDescriptor *entityDescriptor = nil;
        
        while (entityDescriptor = [selfEntityDescriptors nextObject]) {
            [entityDescriptors addObject: entityDescriptor];
        }
    }
    
    return [entityDescriptors objectEnumerator];
}

- (SICEntityDescriptor *)requiredEntityDescriptorBasedOnClassName:(NSString * const)className {
    
    SICEntityDescriptor *entityDescriptor = [self getEntityDescriptorBasedOnClassName: className];
    if(entityDescriptor == nil) {
        
        [SICLog debug:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"requiredEntityDescriptorBasedOnClassName(%@)",className] message:[NSString stringWithFormat:@"Entity Descriptor Model Not registered With Siminov, MODEL: %@",className]];
        
        SICQuickEntityDescriptorReader *quickEntityDescriptorParser = nil;
        
        @try {
            quickEntityDescriptorParser = [[SICQuickEntityDescriptorReader alloc] initWithClassName:className];
            [quickEntityDescriptorParser process];
        }
        @catch (SICSiminovException *exception) {
            [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"requiredEntityDescriptorBasedOnClassName(%@)",className] message:[NSString stringWithFormat:@"SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: %@, %@",className,[exception reason]]];
            @throw [[SICSiminovCriticalException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"requiredEntityDescriptorBasedOnClassName(%@)",className] message:[NSString stringWithFormat:@"SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: %@, %@",className,[exception reason]]];
        }
        
        SICEntityDescriptor *foundEntityDescriptor = [quickEntityDescriptorParser getEntityDescriptor];
        if(foundEntityDescriptor == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"requiredEntityDescriptorBasedOnClassName(%@)",className] message:[NSString stringWithFormat:@"Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: %@",className]];
            @throw [[SICSiminovCriticalException alloc] initWithClassName:NSStringFromClass([self class]) methodName:[NSString stringWithFormat:@"requiredEntityDescriptorBasedOnClassName(%@)",className] message:[NSString stringWithFormat:@"Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: %@",className]];
        }
        
        return foundEntityDescriptor;
    }
    
    return entityDescriptor;
}

- (SICDatabaseBundle *)getDatabaseBundle:(NSString * const)databaseName {
    
    SICDatabaseDescriptor *databaseDescriptor = [self getDatabaseDescriptorBasedOnName:databaseName];
    return [databaseFactory getDatabaseBundle:databaseDescriptor];
}

- (NSEnumerator *)getDatabaseBundles {
    return [databaseFactory getDatabaseBundles];
}

- (void)removeDatabaseBundle:(NSString * const)databaseDescriptorName {
    SICDatabaseDescriptor *databaseDescriptor = [self getDatabaseDescriptorBasedOnName:databaseDescriptorName];
    [databaseFactory removeDatabaseBundle:databaseDescriptor];
}

- (id<SICISiminovEvents>)getSiminovEventHandler {
    return [[SICEventHandler getInstance] getSiminovEventHandler];
}

- (id<SICIDatabaseEvents>)getDatabaseEventHandler {
    return [[SICEventHandler getInstance] getDatabaseEventHandler];
}

@end
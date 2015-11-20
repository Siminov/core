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


#import "SICSiminov.h"
#import "SICDatabaseUtils.h"
#import "SICApplicationDescriptorReader.h"
#import "SICEntityDescriptorReader.h"
#import "SICDatabaseDescriptorReader.h"
#import "SICLibraryDescriptorReader.h"
#import "SICDatabaseHelper.h"
#import "SICInitializer.h"
#import "SICLog.h"



@implementation SICSiminov

static BOOL isCoreActive;

static BOOL firstTimeProcessed = false;
static SICResourceManager *coreResourceManager;

+ (void)initialize {
    coreResourceManager = [SICResourceManager getInstance];
}

+ (void)isActive {
    if(!isCoreActive) {
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"isActive" message:@"isActive"];
    }
}

+ (bool)getActive {
    return isCoreActive;
}

+ (void)setActive:(bool)active {
    isCoreActive = active;
}

+ (bool)isFirstTimeInitialized {
    return firstTimeProcessed;
}

+ (id<SICIInitializer>)initializer {
    return [[SICInitializer alloc] init];
}

+ (void)start {
    if (isCoreActive) {
        return;
    }
    
    [self process];
    isCoreActive = true;
    
    id<SICISiminovEvents> coreEventHandler = [coreResourceManager getSiminovEventHandler];
    if (coreEventHandler != nil) {
        if (firstTimeProcessed) {
            [coreEventHandler onFirstTimeSiminovInitialized];
        } else {
            [coreEventHandler onSiminovInitialized];
        }
    }
}

+ (void)shutdown {
    [self isActive];
    
    NSEnumerator *databaseDescriptors = [coreResourceManager getDatabaseDescriptors];
    BOOL failed = false;
    
    SICDatabaseDescriptor *databaseDescriptor = nil;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        SICDatabaseBundle *databaseBundle = [coreResourceManager getDatabaseBundle:[databaseDescriptor getDatabaseName]];
        id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
        
        @try {
            [database close:databaseDescriptor];
        }
        @catch (SICDatabaseException *databaseException) {
            failed = true;
            
            [SICLog error:NSStringFromClass([self class]) methodName:@"shutdown" message:[NSString stringWithFormat:@"DatabaseException caught while closing database, %@",[databaseException getMessage]]];
            continue;
        }
    }
    
    if (failed) {
        @throw [[SICSiminovCriticalException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"shutdown" message:@"DatabaseException caught while closing database"];
    }
    
    id<SICISiminovEvents> coreEventHandler = [coreResourceManager getSiminovEventHandler];
    if (coreEventHandler != nil) {
        [coreEventHandler onSiminovStopped];
    }
}


+ (void)process {
    [self processApplicationDescriptor];
    [self processDatabaseDescriptors];
    [self processLibraries];
    [self processEntityDescriptors];
    
    [self processDatabase];
}


/**
 * It process ApplicationDescriptor.xml file defined in Application, and stores in Resource Manager.
 */
+ (void)processApplicationDescriptor {
    SICApplicationDescriptorReader *applicationDescriptorParser = [[SICApplicationDescriptorReader alloc] init];
    
    SICApplicationDescriptor *applicationDescriptor = [applicationDescriptorParser getApplicationDescriptor];
    if (applicationDescriptor == nil) {
        [SICLog debug:NSStringFromClass([self class]) methodName:@"processApplicationDescriptor" message:@"Invalid Application Descriptor Found."];
        @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processApplicationDescriptor" message:@"Invalid Application Descriptor Found."];
    }
    
    [coreResourceManager setApplicationDescriptor:applicationDescriptor];
    
    /*
     * Setup Deploy Mode
     */
    NSString *deploy = [applicationDescriptor getDeploy];
    if(deploy != nil && [deploy caseInsensitiveCompare:APPLICATION_DESCRIPTOR_DEPLOY_DEVELOPMENT] == NSOrderedSame) {
        DEPLOY = DEVELOPMENT;
    } else if(deploy != nil && [deploy caseInsensitiveCompare:APPLICATION_DESCRIPTOR_DEPLOY_BETA] == NSOrderedSame) {
        DEPLOY = BETA;
    } else if(deploy != nil && [deploy caseInsensitiveCompare:APPLICATION_DESCRIPTOR_DEPLOY_PRODUCTION] == NSOrderedSame) {
        DEPLOY = PRODUCTION;
    }
}


/**
 * It process all DatabaseDescriptor.xml files defined by Application and stores in Resource Manager.
 */
+ (void)processDatabaseDescriptors {
    NSEnumerator *databaseDescriptorPaths = [[coreResourceManager getApplicationDescriptor] getDatabaseDescriptorPaths];
    
    NSString *databaseDescriptorPath = nil;
    while (databaseDescriptorPath = [databaseDescriptorPaths nextObject]) {
        SICDatabaseDescriptorReader *databaseDescriptorParser = [[SICDatabaseDescriptorReader alloc] initWithPath:databaseDescriptorPath];
        
        SICDatabaseDescriptor *databaseDescriptor = [databaseDescriptorParser getDatabaseDescriptor];
        if (databaseDescriptor == nil) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabaseDescriptors" message:[NSString stringWithFormat:@"Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR:%@",databaseDescriptorPath]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabaseDescriptors" message:[NSString stringWithFormat:@"Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR:%@",databaseDescriptorPath]];
        }
        
        [[coreResourceManager getApplicationDescriptor] addDatabaseDescriptor:databaseDescriptorPath databaseDescriptor:databaseDescriptor];
    }
}


/**
 * It process all LibraryDescriptor.xml files defined by application, and stores in Resource Manager.
 */
+ (void)processLibraries {
    SICApplicationDescriptor *applicationDescriptor = [coreResourceManager getApplicationDescriptor];
    
    NSEnumerator *libraries = [applicationDescriptor getLibraryDescriptorPaths];
    
    NSMutableString *library = nil;
    while (library = [libraries nextObject]) {
        /*
         * Parse LibraryDescriptor.
         */
        SICLibraryDescriptorReader *libraryDescriptorReader = [[SICLibraryDescriptorReader alloc] initWithLibraryName:library];
        SICLibraryDescriptor *libraryDescriptor = [libraryDescriptorReader getLibraryDescriptor];
        
        /*
         * Map Entity Descriptors
         */
        NSEnumerator *entityDescriptors = [libraryDescriptor getEntityDescriptorPaths];
        NSString *libraryEntityDescriptorPath = nil;
        
        while (libraryEntityDescriptorPath = [entityDescriptors nextObject]) {
            
            NSUInteger index = 0;
            NSRange range = [libraryEntityDescriptorPath rangeOfString:LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR];
            if (range.length == 0 && range.location > libraryEntityDescriptorPath.length) {
                index = 0;
            } else {
                index = range.location;
            }
            
            NSString *databaseDescriptorName = [libraryEntityDescriptorPath substringWithRange:NSMakeRange(0,index)];
            NSString *entityDescriptor = [libraryEntityDescriptorPath substringFromIndex:index+1];
            
            NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
            SICDatabaseDescriptor *databaseDescriptor = nil;
            
            while (databaseDescriptor = [databaseDescriptors nextObject]) {
                if ([[databaseDescriptor getDatabaseName] caseInsensitiveCompare:databaseDescriptorName] == NSOrderedSame) {
                    [databaseDescriptor addEntityDescriptorPath:[NSString stringWithFormat:@"%@%@%@",library,LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR, entityDescriptor]];
                }
            }
        }
    }
}


/**
 * It process all EntityDescriptor.xml file defined in Application, and stores in Resource Manager.
 */
+ (void)processEntityDescriptors {
    [self doesDatabaseExists];
    
    SICApplicationDescriptor *applicationDescriptor = [coreResourceManager getApplicationDescriptor];
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = nil;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        NSEnumerator *entityDescriptorPaths = [databaseDescriptor getEntityDescriptorPaths];
        
        NSString *entityDescriptorPath = nil;
        while (entityDescriptorPath = [entityDescriptorPaths nextObject]) {
            SICEntityDescriptorReader *entityDescriptorParser = [[SICEntityDescriptorReader alloc] initWithClassName:entityDescriptorPath];
            [databaseDescriptor addEntityDescriptor:entityDescriptorPath entityDescriptor:[entityDescriptorParser getEntityDescriptor]];
        }
    }
}


/**
 * It process all DatabaseDescriptor.xml and initialize Database and stores in Resource Manager.
 */
+ (void)processDatabase {
    SICApplicationDescriptor *applicationDescriptor = [coreResourceManager getApplicationDescriptor];
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = nil;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        
        NSString *databasePath = [[[SICDatabaseUtils alloc] init] getDatabasePath:databaseDescriptor];
        SICDatabaseBundle *databaseBundle = nil;
        
        @try {
            databaseBundle = [SICDatabaseHelper createDatabase:databaseDescriptor];
        }
        @catch (SICDatabaseException *databaseException) {
            [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while getting database instance from database factory, DATABASE-DESCRIPTOR:%@, %@",[databaseDescriptor getDatabaseName],[databaseException getMessage]]];
            @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
        }
        
        id<SICIDatabaseImpl> database = [databaseBundle getDatabase];
        id<SICIQueryBuilder> queryBuilder = [databaseBundle getQueryBuilder];
        
        /*
         * If Database exists then open and return.
         * If Database does not exists create the database.
         */
        
        NSString *databaseName = [databaseDescriptor getDatabaseName];
        if (![databaseName hasSuffix:@".db"]) {
            databaseName = [NSString stringWithFormat:@"%@.db",databaseName];
        }
        
        NSString *path = [NSString stringWithFormat:@"%@%@",databasePath,databaseName];
        NSFileManager *file = [NSFileManager defaultManager];
        
        if ([file fileExistsAtPath:path]) {
            
            /*
             * Open Database
             */
            @try {
                [database openOrCreate:databaseDescriptor];
            }
            @catch (SICDatabaseException *databaseException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while opening database, %@", [databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            /*
             * Enable Foreign Key Constraints
             */
            @try {
                [database executeQuery:databaseDescriptor entityDescriptor:nil query:SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING];
            }
            @catch (SICDatabaseException *databaseException) {
                
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while executing query to enable foreign keys, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            /*
             * Safe MultiThread Transaction
             */
            @try {
                [database executeMethod:SQLITE_DATABASE_ENABLE_LOCKING parameter:[NSNumber numberWithBool:[databaseDescriptor isTransactionSafe]]];
            }
            @catch (SICDatabaseException *databaseException) {
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while enabling locking on database, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            /*
             * Upgrade Database
             */
            @try {
                //[DatabaseHelper upgradeDatabase:databaseDescriptor];
            }
            @catch (SICDatabaseException *databaseException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while upgrading database, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
        } else {
            
            /*
             * Create Database Directory
             */
            NSFileManager *file = [NSFileManager defaultManager];
            NSError *theError = nil;
            
            @try {
                [file createDirectoryAtPath:databasePath withIntermediateDirectories:YES attributes:nil error:&theError];
            }
            @catch (NSException *exception) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"Exception caught while creating database directories, DATABASE-PATH:%@, DATABASE-DESCRIPTOR:%@, %@", databasePath,[databaseDescriptor getDatabaseName],[exception reason]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[exception reason]];
            }
            
            /*
             * Create Database File.
             */
            @try {
                [database openOrCreate:databaseDescriptor];
            }
            @catch (SICDatabaseException *databaseException) {
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while creating database, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
                
            }
            
            /*
             * Set Database Version
             */
            
            NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
            [parameters setObject:[NSNumber numberWithDouble:[databaseDescriptor getVersion]] forKey:FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER];
            
            @try {
                NSString *updateDatabaseVersionQuery = [queryBuilder formUpdateDatabaseVersionQuery:parameters];
                [database executeQuery:databaseDescriptor entityDescriptor:NULL query:updateDatabaseVersionQuery];
            }
            @catch (SICDatabaseException *databaseException) {
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while updating database version, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            id<SICIDatabaseEvents> databaseEventHandler = [coreResourceManager getDatabaseEventHandler];
            if (databaseEventHandler != nil) {
                [databaseEventHandler onDatabaseCreated:databaseDescriptor];
            }
            
            /*
             * Enable Foreign Key Constraints
             */
            @try {
                [database executeQuery:databaseDescriptor entityDescriptor:nil query:SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING];
            }
            @catch (SICDatabaseException *databaseException) {
                
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while executing query to enable foreign keys, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            /*
             * Safe MultiThread Transaction
             */
            @try {
                [database executeMethod:SQLITE_DATABASE_ENABLE_LOCKING parameter:[NSNumber numberWithBool:[databaseDescriptor isTransactionSafe]]];
            }
            @catch (SICDatabaseException *databaseException) {
                
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while enabling locking on database, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc] initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
            
            /*
             * Create Tables
             */
            @try {
                [SICDatabaseHelper createTables:[databaseDescriptor orderedEntityDescriptors]];
            }
            @catch (SICDatabaseException *databaseException) {
                NSError *error;
                if ([[NSFileManager defaultManager] isDeletableFileAtPath:path]) {
                    BOOL success = [[NSFileManager defaultManager] removeItemAtPath:path error:&error];
                    if (!success) {
                        NSLog(@"Error removing file at path:%@", error.localizedDescription);
                    }
                }
                
                [SICLog error:NSStringFromClass([self class]) methodName:@"processDatabase" message:[NSString stringWithFormat:@"DatabaseException caught while creating tables, %@",[databaseException getMessage]]];
                @throw [[SICDeploymentException alloc]initWithClassName:NSStringFromClass([self class]) methodName:@"processDatabase" message:[databaseException getMessage]];
            }
        }
    }
}

/**
 * It is used to check whether database exists or not.
 */

+ (void)doesDatabaseExists {
    SICApplicationDescriptor *applicationDescriptor = [coreResourceManager getApplicationDescriptor];
    
    NSEnumerator *databaseDescriptors = [applicationDescriptor getDatabaseDescriptors];
    SICDatabaseDescriptor *databaseDescriptor = nil;
    BOOL databaseExists = true;
    
    while (databaseDescriptor = [databaseDescriptors nextObject]) {
        NSString *databasePath = [[[SICDatabaseUtils alloc]init] getDatabasePath:databaseDescriptor];
        NSString *databaseName = [databaseDescriptor getDatabaseName];
        
        if (![databaseName hasSuffix:@".db"]) {
            databaseName = [NSString stringWithFormat:@"%@.db",databaseName];
        }
        
        NSString *path = [NSString stringWithFormat:@"%@%@",databasePath,databaseName];
        NSFileManager *file = [NSFileManager defaultManager];
        
        if (![file fileExistsAtPath:path]) {
            databaseExists = false;
        }
    }
    
    if (!databaseExists) {
        firstTimeProcessed = true;
    } else {
        firstTimeProcessed = false;
    }
}

@end

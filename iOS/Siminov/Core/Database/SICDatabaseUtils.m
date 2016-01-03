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



#import "SICDatabaseUtils.h"
#import "SICResourceManager.h"

@implementation SICDatabaseUtils

- (NSString *)getDatabasePath:(SICDatabaseDescriptor * const)databaseDescriptor {
    
    SICResourceManager *resourceManager = [SICResourceManager getInstance];
    SICApplicationDescriptor *applicationDescriptor = [resourceManager getApplicationDescriptor];
    
    // Get the Documents directory path
    NSString *databasePath = [NSString stringWithFormat: @"%@/%@/%@/%@/",[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex: 0],[applicationDescriptor getName],DATABASE_PATH_DATABASE,[databaseDescriptor getDatabaseName]];
    
    return databasePath;
}

@end

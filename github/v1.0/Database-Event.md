Core provides this event handler which automatically gets triggered when action happen at database level. Application have to provide implementation for this event notifier and register them with Core.

#### Android Sample: Database Events

```java

    public interface IDatabaseEvents {

        public void onDatabaseCreated(final DatabaseDescriptor databaseDescriptor);
	
        public void onDatabaseDropped(final DatabaseDescriptor databaseDescriptor);
	
        public void onTableCreated(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMapping);
	
        public void onTableDropped(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMapping);
	
        public void onIndexCreated(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMapping, Index index);
	
        public void onIndexDropped(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMapping, Index index);
	
    }

```

#### iOS Sample: Database Events

```objective-c

    @protocol SICIDatabaseEvents <NSObject>

    - (void)onDatabaseCreated:(SICDatabaseDescriptor* const)databaseDescriptor;

    - (void)onDatabaseDropped:(SICDatabaseDescriptor* const)databaseDescriptor;

    - (void)onTableCreated:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor;

     - (void)onTableDropped:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor;

     - (void)onIndexCreated:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor * const)databaseMappingDescriptor index:(SICIndex *)index;

     - (void)onIndexDropped:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor index:(SICIndex *)index;

    @end

```

#### Windows Sample: Database Events

```c#

    public interface IDatabaseEvents 
    {

        void OnDatabaseCreated(DatabaseDescriptor databaseDescriptor);
	
        void OnDatabaseDropped(DatabaseDescriptor databaseDescriptor);
	
        void OnTableCreated(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMapping);
	
        void OnTableDropped(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMapping);
	
        void OnIndexCreated(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMapping, Index index);
	
        void OnIndexDropped(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMapping, Index index);
	
    }

```


###### 1. On Database Created - [Android:onDatabaseCreated(DatabaseDescriptor) | iOS:onDatabaseCreated:DatabaseDescriptor | Windows:OnDatabaseCreated(DatabaseDescriptor)]

It is triggered when database is created based on schema defined in DatabaseDescriptor.si.xml file. This API provides DatabaseDescriptor object for which database is created.

###### 2. On Database Dropped - [Android:onDatabaseDropped(DatabaseDescriptor) |  iOS:onDatabaseDropped:DatabaseDescriptor | Windows:OnDatabaseDropped(DatabaseDescriptor)]

It is triggered when database is dropped. This API provides DatabaseDescriptor object for which database is dropped.

###### 3. On Table Created - [Android:onTableCreated(DatabaseDescriptor, DatabaseMappingDescriptor) | iOS:onTableCreated:DatabaseDescriptor databaseMappingDescriptor:DatabaseMappingDescriptor | Windows:OnTableCreated(DatabaseDescriptor, DatabaseMappingDescriptor)]

It is triggered when a table is created in database. This API provides Database descriptor object and Database mapping descriptor object which describes table structure.

###### 4. On Table Dropped - [Android:onTableDropped(DatabaseDescriptor, DatabaseMappingDescriptor) | iOS:onTableDropped:DatabaseDescriptor databaseMappingDescriptor:DatabaseMappingDescriptor | Windows:OnTableDropped(DatabaseDescriptor, DatabaseMappingDescriptor)]

It is triggered when a table is deleted from database. This API provides Database descriptor object and Database mapping descriptor object for which table is dropped.

###### 5. On Index Created - [Android:onIndexCreated(DatabaseDescriptor, DatabaseMappingDescriptor, Index) | iOS:onIndexCreated:DatabaseDescriptor databaseMappingDescriptor:DatabaseMappingDescriptor index:Index | Windows:OnIndexCreated(DatabaseDescriptor, DatabaseMappingDescriptor, Index)]

It is triggered when a index is created on table. This API provides DatabaseMappingDescriptor and Index object which defines table and index structure.

###### 6. On Index Dropped - [Android:onIndexDropped(DatabaseDescriptor, DatabaseMappingDescriptor, Index) | iOS:onIndexDropped:DatabaseDescriptor databaseMappingDescriptor:DatabaseMappingDescriptor index:Index | Windows:OnIndexDropped(DatabaseDescriptor, DatabaseMappingDescriptor, Index)]

It is triggered when a index is dropped from table. This API provides Database descriptor object, Database mapping descriptor object and Index object which defines table and index for which index is dropped.

## Android Sample: IDatabase Event Handler

***

![Android Sample: IDatabase Event Handler] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/idatabase_application_sample_example.png "Android Sample: IDatabase Event Handler")

***

## iOS Sample: SICIDatabase Event Handler

***

![iOS Sample: SICIDatabase Event Handler] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/idatabase_application_sample_example.png "iOS Sample: SICIDatabase Event Handler")

***


## Windows Sample: IDatabase Event Handler

***

![Windows Sample: IDatabase Event Handler] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/idatabase_application_sample_example.png "Windows Sample: IDatabase Event Handler")

***
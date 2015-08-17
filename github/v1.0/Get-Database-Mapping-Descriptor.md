## Get Database Mapping Descriptor

#### Android API: Get Database Mapping Descriptor

```java

    public final DatabaseMappingDescriptor getDatabaseMappingDescriptor() throws DatabaseException;

```

- _Android Sample: Get Database Mapping Descriptor_

```java

    DatabaseMappingDescriptor databaseMappingDescriptor = null;
    try {
        databaseMappingDescriptor = new Liquor().getDatabaseMappingDescriptor();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Database Mapping Descriptor

```objective-c

    - (SICDatabaseMappingDescriptor *)getDatabaseMappingDescriptor;

```

- _iOS Sample: Get Database Mapping Descriptor_

```objective-c

    SICDatabaseMappingDescriptor *databaseMappingDescriptor = nil;
    @try {
        databaseMappingDescriptor = [[[Liquor alloc] init] getDatabaseMappingDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Get Database Mapping Descriptor

```c#

    public DatabaseMappingDescriptor GetDatabaseMappingDescriptor();

```

- _Windows Sample: Get Database Mapping Descriptor_

```c#

    DatabaseMappingDescriptor databaseMappingDescriptor = null;
    try 
    {
        databaseMappingDescriptor = new Liquor().GetDatabaseMappingDescriptor();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
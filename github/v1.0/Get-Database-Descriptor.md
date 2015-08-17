## Get Database Descriptor


#### Android API: Get Database Descriptor

```java

    public final DatabaseDescriptor getDatabaseDescriptor() throws DatabaseException;

```

- _Android Sample: Get Database Descriptor_

```java

    try {
        DatabaseDescriptor databaseDescriptor = new Liquor().getDatabaseDescriptor();
    } catch(DatabaseException databaseException) {
		//Log It.
    }

```

#### iOS API: Get Database Descriptor

```objective-c

    - (SICDatabaseDescriptor *)getDatabaseDescriptor;

```

- _iOS Sample: Get Database Descriptor_

```objective-c

    @try {
        SICDatabaseDescriptor *databaseDescriptor = [[[Liquor alloc] init] getDatabaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }

```

#### Windows API: Get Database Descriptor

```c#

    public DatabaseDescriptor GetDatabaseDescriptor();

```

- _Windows Sample: Get Database Descriptor_

```c#

    try 
    {
        DatabaseDescriptor databaseDescriptor = new Liquor().GetDatabaseDescriptor();
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }

```
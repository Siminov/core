## Drop Database

Database class provides API to drop complete database from an application.

#### Android - Drop Database API

```java

    public class DatabaseHepler {
        public static void dropDatabase(DatabaseDesriptor) throws DatabaseException;
    }

```

###### _**Android Sample**_: Drop Database

```java

    DatabaseDescriptor databaseDescriptor = new Liquor().getDatabaseDescriptor();
	
    try {
        DatabaseHelper.dropDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }
 
```


#### iOS - Drop Database API

```objective-c

    + (void)dropDatabase:(SICDatabaseDesriptor* const)databaseDescriptor;

```


###### _**iOS Sample**_: Drop Database

```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [[[Liquor alloc] init] getDatabaseDescriptor];
	
    @try {
        [SICDatabaseHelper dropDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }
 
```


#### Windows - Drop Database API

```c#

    public class DatabaseHepler 
    {
        static void DropDatabase(DatabaseDesriptor);
    }

```


###### _**Windows  Sample**_: Drop Database

```c#

    DatabaseDescriptor databaseDescriptor = new Liquor().GetDatabaseDescriptor();
	
    try 
    {
        DatabaseHelper.DropDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }
 
```

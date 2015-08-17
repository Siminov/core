## Drop Table

Database class provides following API to drop a table.

#### Android API: Drop Table
```java

    public void dropTable() throws DatabaseException;

```

###### Android Sample: Drop Table

```java

    try {
        new Liquor().dropTable();
    } catch(DatabaseException databaseException) {
		//Log It.
    }

```

#### iOS API: Drop Table

```objective-c

    + (void)dropTable;

```

###### iOS Sample: Drop Table

```objective-c

    @try {
        [[[Liquor alloc] init] dropTable];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }

```

###### Windows API: Drop Table

```c#

    public void DropTable();

```

#### Windows Sample: Drop Table

```c#

    try 
    {
        new Liquor().DropTable();
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }

```


## Get Table Name

#### Android API: Get Table Name

```java

    public final String getTableName() throws DatabaseException;

```

- _Android Sample: Get Table Name_

```java

    String tableName = null;
    try {
        tableName = new Liquor().getTableName();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Table Name

```objective-c

    - (NSString *)getTableName;

```

- _iOS Sample: Get Table Name_

```objective-c

    NSString *tableName = nil;
    @try {
        tableName = [[[Liquor alloc] init] getTableName];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```


#### Windows API: Get Table Name

```c#

    public String GetTableName();

```

- _Windows Sample: Get Table Name_

```c#

    String tableName = null;
    try 
    {
        tableName = new Liquor().GetTableName();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```

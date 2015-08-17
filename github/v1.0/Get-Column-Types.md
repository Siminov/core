## Get Column Types

#### Android API: Get Column Types

```java

    public final Map<String, String> getColumnTypes() throws DatabaseException;

```

- _Android Sample: Get Column Types_

```java

    Map<String, String> columnTypes = null;
    try {
        columnTypes = new Liquor().getColumnTypes();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Column Types

```objective-c

    - (NSMutableDictionary *)getColumnTypes;

```


- _iOS Sample: Get Column Types_

```objective-c

    NSMutableDictionary *columnTypes = nil;
    @try {
        columnTypes = [[[Liquor alloc] init] getColumnTypes];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Get Column Types

```c#

    public IDictionary<String, String> GetColumnTypes();

```

- _Windows Sample: Get Column Types_

```c#

    IDictionary<String, String> columnTypes = null;
    try 
    {
        columnTypes = new Liquor().GetColumnTypes();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```


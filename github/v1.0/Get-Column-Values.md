## Get Column Values

#### Android API: Get Column Values

```java

    public final Map<String, Object> getColumnValues() throws DatabaseException;

```

- _Android Sample: Get Column Values_

```java

    Map<String, Object> values = null;
    try {
        values = new Liquor().getColumnValues();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Column Values

```objective-c

    - (NSMutableDictionary *)getColumnValues;

```

- _iOS Sample: Get Column Values_

```objective-c

    NSMutableDictionary *values = nil;
    @try {
        values = [[[Liquor alloc] init] getColumnValues];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```


#### Windows API: Get Column Values

```c#

    public IDictionary<String, Object> GetColumnValues();

```

- _Windows Sample: Get Column Values_

```c#

    Dictionary<String, Object> values = null;
    try 
    {
        values = new Liquor().GetColumnValues();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
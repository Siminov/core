## Get Column Names

#### Android API: Get Column Names

```java

    public final Iterator<String> getColumnNames() throws DatabaseException;

```

- _Android Sample: Get Column Names_

```java

    String[] columnNames = null;
    try {
        columnNames = new Liquor().getColumnNames();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Column Names

```objective-c

    - (NSEnumerator *)getColumnNames;

```

- _iOS Sample: Get Column Names_

```objective-c

    NSArray *columnNames = nil;
    @try {
        columnNames = [[[Liquor alloc] init] getColumnNames];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```


#### Windows API: Get Column Names

```c#

    public IEnumerator<String> GetColumnNames();

```

- _Windows Sample: Get Column Names_

```c#

    IEnumerator<String> columnNames = null;
    try 
    {
        columnNames = new Liquor().GetColumnNames();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
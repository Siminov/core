## Get Primary Keys

#### Android API: Get Primary Keys

```java

    public final Iterator<String> getPrimaryKeys() throws DatabaseException;

```

- _Android Sample: Get Primary Keys_

```java

    Iterator<String> primaryKeys = null;
    try {
        primaryKeys = new Liquor().getPrimeryKeys();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Primary Keys

```objective-c

    - (NSArray *)getPrimaryKeys;

```

- _iOS Sample: Get Primary Keys_

```objective-c

    NSArray *primaryKeys = nil;
    @try {
        primaryKeys = [[[Liquor alloc] init] getPrimeryKeys];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Get Primary Keys

```c#

    public IEnumerator<String> GetPrimaryKeys();

```

- _Windows Sample: Get Primary Keys_

```c#

    IEnumerator<String> primaryKeys = null;
    try 
    {
        primaryKeys = new Liquor().GetPrimeryKeys();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```

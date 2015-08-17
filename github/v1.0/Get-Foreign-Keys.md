## Get Foreign Keys

#### Android API: Get Foreign Keys

```java

    public final Iterator<String> getForeignKeys() throws DatabaseException;

```

- _Android Sample: Get Foreign Keys_

```java

    Iterator<String> foreignKeys = null;
    try {
        foreignKeys = new Liquor().getForeignKeys();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Foreign Keys

```objective-c

    - (NSArray *)getForeignKeys;

```

- _iOS Sample: Get Foreign Keys_

```objective-c

    NSArray *foreignKeys = nil;
    @try {
        foreignKeys = [[[Liquor alloc] init] getForeignKeys];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Get Foreign Keys

```c#

    public IEnumerator<String> GetForeignKeys();

```

- _Windows Sample: Get Foreign Keys_

```c#

    IEnumerator<String> foreignKeys = null;
    try 
    {
        foreignKeys = new Liquor().GetForeignKeys();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
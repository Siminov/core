## Drop Index

#### Android API: Drop Index

```java

    public void dropIndex(final String indexName) throws DatabaseException { }

```

###### Android Sample: Drop Index

```java

    String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
	
    try {
        new Liquor().dropIndex(indexName);
    } catch(DatabaseException databaseException) {
		//Log It.
    }

```

#### iOS API: Drop Table

```objective-c

    - (void)dropIndex:(NSString *)indexName;

```

###### iOS Sample: Drop Table

```objective-c

    NSString *indexName = @"LIQUOR_INDEX_BASED_ON_LINK";
	
    @try {
        [[[Liquor alloc] init] dropIndex:indexName];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }

```

#### Windows API: Drop Index

```c#

    public void DropIndex(String indexName) { }

```

###### Windows Sample: Drop Index

```c#

    String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
	
    try 
    {
        new Liquor().DropIndex(indexName);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }
```

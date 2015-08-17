## Commit Transaction

Marks the current transaction as successful. Finally it will End a transaction.

#### Android API: Commit Transaction

```java

    public static final void commitTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;

```

###### Android Sample: Commit Transaction

```java

    Liquor beer = new Liquor();
    beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.setDescription(applicationContext.getString(R.string.beer_description));
    beer.setHistory(applicationContext.getString(R.string.beer_history));
    beer.setLink(applicationContext.getString(R.string.beer_link));
    beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));

    DatabaseDescriptor databaseDescriptor = beer.getDatabaseDescriptor();
  
    try {
        Database.beginTransaction(databaseDescriptor);
  		
        beer.save();
  
        Database.commitTransaction(databaseDescriptor);
    } catch(DatabaseException de) {
		//Log it.
    } finally {
        Database.endTransaction(databaseDescriptor);
    }

```

#### iOS API: Commit Transaction

```objective-c

    + (void)commitTransaction:(SICDatabaseDescriptor *)databaseDescriptor;

```

###### iOS Sample: Commit Transaction

```objective-c

    Liquor *beer = [[Liquor alloc] init];
    [beer setLiquorType:[Liquor LIQUOR_TYPE_BEER]];
    [beer setDescription:@"beer_description"];
    [beer setHistory:@"beer_history"];
    [beer setLink:@"beer_link"];
    [beer setAlcholContent:@"beer_alchol_content"];

    SICDatabaseDescriptor databaseDescriptor = [beer getDatabaseDescriptor];
  
    @try {
        [SICDatabase beginTransaction:databaseDescriptor];
  		
        [beer save];
  
        [SICDatabase commitTransaction:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    } @finally {
        [SICDatabase endTransaction:databaseDescriptor];
    }

```

#### Windows API: Commit Transaction

```c#

    public static void CommitTransaction(DatabaseDescriptor databaseDescriptor);

```

###### Windows Sample: Commit Transaction

```c#

    Liquor beer = new Liquor();
    beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.SetDescription("beer_description");
    beer.SetHistory("beer_history");
    beer.SetLink("beer_link");
    beer.SetAlcholContent("beer_alchol_content");

    DatabaseDescriptor databaseDescriptor = beer.GetDatabaseDescriptor();
  
    try 
    {
        Database.BeginTransaction(databaseDescriptor);
  		
        beer.Save();
  
        Database.CommitTransaction(databaseDescriptor);
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    } 
    finally 
    {
        Database.EndTransaction(databaseDescriptor);
    }

```


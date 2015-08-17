## End Transaction

End the current transaction.

#### Android API: End Transaction

```java

    public static final void endTransaction(final DatabaseDescriptor databaseDescriptor);

```


###### Android Sample: End Transaction

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

#### iOS API: End Transaction

```objective-c

    + (void)endTransaction:(SICDatabaseDescriptor *)databaseDescriptor;

```

###### iOS Sample: End Transaction

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

#### Windows API: End Transaction

```c#

    public static void EndTransaction(DatabaseDescriptor databaseDescriptor);

```

###### Windows Sample: End Transaction

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

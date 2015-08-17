## Save

#### Android API: Save

```java

    public final void save() throws DatabaseException;

```

###### Android Sample: Save

```java

    Liquor beer = new Liquor();
    beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.setDescription(applicationContext.getString(R.string.beer_description));
    beer.setHistory(applicationContext.getString(R.string.beer_history));
    beer.setLink(applicationContext.getString(R.string.beer_link));
    beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
  
    try {
        beer.save();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Save

```objective-c

    - (void)save;

```

###### iOS Sample: Save

```objective-c

    Liquor *beer = [[Liquor alloc] init];
    [beer setLiquorType:[Liquor LIQUOR_TYPE_BEER]];
    [beer setDescription:@"beer_description"];
    [beer setHistory:@"beer_history"];
    [beer setLink:@"beer_link"];
    [beer setAlcholContent:@"beer_alchol_content"];
  
    @try {
        [beer save];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```


#### Windows API: Save

```c#

    public void Save();

```

###### Windows Sample: Save

```c#

    Liquor beer = new Liquor();
    beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.SetDescription("beer_description");
    beer.SetHistory("beer_history");
    beer.SetLink("beer_link");
    beer.SetAlcholContent("beer_alchol_content");
  
    try 
    {
        beer.Save();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
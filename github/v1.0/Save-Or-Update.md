## Save Or Update

#### Android API: Save

```java

    public final void saveOrUpdate() throws DatabaseException;

```

> **Note**
>
> - If tuple is not present in table then it will insert it into table else it will update the tuple.

###### Android Sample: Save Or Update

```java

    Liquor beer = new Liquor();
    beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.setDescription(applicationContext.getString(R.string.beer_description));
    beer.setHistory(applicationContext.getString(R.string.beer_history));
    beer.setLink(applicationContext.getString(R.string.beer_link));
    beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
  
    try {
        beer.saveOrUpdate();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Save Or Update

```objective-c

    - (void)saveOrUpdate;

```

> **Note**
>
> - If tuple is not present in table then it will insert it into table else it will update the tuple.

###### iOS Sample: Save Or Update

```objective-c

    Liquor *beer = [[Liquor alloc] init];
    [beer setLiquorType:[Liquor LIQUOR_TYPE_BEER]];
    [beer setDescription:@"beer_description"];
    [beer setHistory:@"beer_history"];
    [beer setLink:@"beer_link"];
    [beer setAlcholContent:@"beer_alchol_content"];
  
    @try {
        [beer saveOrUpdate];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Save Or Update

```c#

    public void SaveOrUpdate();

```

> **Note**
>
> - If tuple is not present in table then it will insert it into table else it will update the tuple.

###### Windows Sample: Save Or Update

```c#

    Liquor beer = new Liquor();
    beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.SetDescription("beer_description");
    beer.SetHistory("beer_history");
    beer.SetLink("beer_link");
    beer.SetAlcholContent("beer_alchol_content");
  
    try 
    {
        beer.SaveOrUpdate();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```

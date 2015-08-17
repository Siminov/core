## Select

Database class provides following API's to fetch tuples from table.

### 1. Select API

###### Android API: Select

```java

    public ISelect select() throws DatabaseException;

```

- _**ISelect**_: Exposes API's to provide information based on which tuples will be fetched from table.

```java

    public interface ISelect {

        public ISelect distinct();
	
        public ISelectClause where(String column);
	
        public ISelect whereClause(String whereClause);
	
        public ISelectClause and(String column);
	
        public ISelectClause or(String column);
	
        public ISelect orderBy(String...columns);
	
        public ISelect ascendingOrderBy(String...columns);
	
        public ISelect descendingOrderBy(String...columns);

        public ISelect limit(int limit);
	
        public ISelect groupBy(String...columns);
	
        public ISelectClause having(String column);
	
        public ISelect havingClause(String havingClause);
	
        public ISelect columns(String...columns);
	
        public Object[] execute() throws DatabaseException;
	
    }

```

- _**ISelectClause**_: Exposes API's to provide condition to where clause based on which tuples will be fetched from table.

```java

    public interface ISelectClause {

        public ISelect equalTo(String value);

        public ISelect notEqualTo(String value);
	
        public ISelect greaterThan(String value);

        public ISelect greaterThanEqual(String value);
	
        public ISelect lessThan(String value);
	
        public ISelect lessThanEqual(String value);
	
        public ISelect between(String start, String end);
	
        public ISelect like(String like);
	
        public ISelect in(String...values);
	
    }

```

- **Android Sample: Select**

```java

     LiquorBrand[] liquorBrands = new LiquorBrand().select().where(LiquorBrand.LIQUOR_TYPE).equalTo(liquorType).execute();
```


###### iOS API: Select

```objective-c

    - (id<SICISelect>)select;

```

- _**SICISelect**_: Exposes API's to provide information based on which tuples will be fetched from table.


```objective-c

    @protocol SICISelect <NSObject>

    - (id<SICISelect>)distinct;

    - (id<SICISelectClause>)where:(NSString *)column;

    - (id<ISelect>)whereClause:(NSString *)whereClause;

    - (id<SICISelectClause>)and:(NSString *)column;

    - (id<SICISelectClause>)or:(NSString *)column;

    - (id<SICISelect>)orderBy:(NSArray *)columns;

    - (id<SICISelect>)ascendingOrderBy:(NSArray *)columns;

    - (id<SICISelect>)descendingOrderBy:(NSArray *)columns;

    - (id<SICISelect>)limit:(int)limit;

    - (id<SICISelect>)groupBy:(NSArray *)columns;

    - (id<SICISelectClause>)having:(NSString *)column;

    - (id<SICISelect>)havingClause:(NSString *)havingClause;

    - (id<SICISelect>)columns:(NSArray *)column;

    - (id)execute;

    @end

```

- _**SICISelectClause**_: Exposes API's to provide condition to where clause based on which tuples will be fetched from table.


```objective-c

    @protocol SICISelectClause <NSObject>

    - (id<SICISelect>)equalTo:(id)value;

    - (id<SICISelect>)notEqualTo:(id)value;

    - (id<SICISelect>)greaterThan:(id)value;

    - (id<SICISelect>)greaterThanEqual:(id)value;

    - (id<SICISelect>)lessThan:(id)value;
 
    - (id<SICISelect>)lessThanEqual:(id)value;

    - (id<SICISelect>)between:(id)start end:(id)end;

    - (id<SICISelect>)like:(id)like;

    - (id<SICISelect>)in:(id)values;
 
    @end

```

- **iOS Sample: Select**

```objective-c

    NSArray *liquorBrands = [[[[[[LiquorBrand alloc] init] select] where:[LiquorBrand LIQUOR_TYPE]] equalTo:liquorType] execute];
```

###### Windows API: Select

```c#

    public ISelect Select() throws DatabaseException;

```

- _**ISelect**_: Exposes API's to provide information based on which tuples will be fetched from table.

```c#

    public interface ISelect 
    {

        ISelect Distinct();
	
        ISelectClause Where(String column);
	
        ISelect whereClause(String whereClause);
	
        ISelectClause And(String column);
	
        ISelectClause Or(String column);
	
        ISelect OrderBy(String[] columns);
	
        ISelect AscendingOrderBy(String[] columns);
	
        ISelect DescendingOrderBy(String[] columns);

        ISelect Limit(int limit);
	
        ISelect GroupBy(String[] columns);
	
        ISelectClause Having(String column);
	
        ISelect HavingClause(String havingClause);
	
        ISelect Columns(String...columns);
	
        Object[] Execute();
	
    }

```


- _**ISelectClause**_: Exposes API's to provide condition to where clause based on which tuples will be fetched from table.

```c#

    public interface ISelectClause 
    {

        ISelect EqualTo(String value);

        ISelect NotEqualTo(String value);
	
        ISelect GreaterThan(String value);
	
        ISelect GreaterThanEqual(String value);
	
        ISelect LessThan(String value);
	
        ISelect LessThanEqual(String value);
	
        ISelect Between(String start, String end);
	
        ISelect Like(String like);
	
        ISelect In(String...values);
	
    }

```

- **Windows Sample: Select**

```c#

    LiquorBrand[] liquorBrands = new LiquorBrand().Select().Where(LiquorBrand.LIQUOR_TYPE).EqualTo(liquorType).Execute();
```

## 2. Select Manually API

###### Android API: Select Manually

```java

    public Object[] select(String query) throws DatabaseException;

```


- **Android Sample: Select Manually**

```java

       String query = "SELECT * FROM LIQUOR";
	
       Liquor[] liquors = null;
       try {
           liquors = new Liquor().select(query);
       } catch(DatabaseException de) {
		//Log it.
       }

```

###### iOS API: Select Manually

```objective-c

    - (NSArray *)select:(NSString *)query;

```

- **iOS Sample: Select Manually**

```objective-c

    NSString *query = @"SELECT * FROM LIQUOR";
	
    NSArray *liquors = nil;
    @try {
        liquors = [[[Liquor alloc] init] select:query];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```


###### Windows API: Select Manually

```c#

    public Object[] Select(String query) throws DatabaseException;

```


- **Windows Sample: Select Manually**

```c#

       String query = "SELECT * FROM LIQUOR";
	
       Liquor[] liquors = null;
       try {
           liquors = new Liquor().Select(query);
       } catch(DatabaseException de) {
		//Log it.
       }

```
## Maximum

Exposes API's to returns the maximum value of all values in the group. The maximum value is the value that would be returned last in an ORDER BY on the same column. Aggregate max() returns NULL if and only if there are no non-NULL values in the group.


#### Android API: Maximum

```java

    public IMax max() throws DatabaseException;

```

- _Android: IMax Interface_

```java

    public interface IMax {

        public IMaxClause where(String column);
	
        public IMax whereClause(String whereClause);
	
        public IMaxClause and(String column);
	
        public IMaxClause or(String column);

        public IMax groupBy(String...columns);
	
        public IMaxClause having(String column);

        public IMax havingClause(String havingClause);
	
        public IMax column(String column);
	
        public Object execute() throws DatabaseException;
	
    }

```

- _Android: IMaxClause Interface_

```java

    public interface IMaxClause {

        public IMax equalTo(String value);

        public IMax notEqualTo(String value);
	
        public IMax greaterThan(String value);
	
        public IMax greaterThanEqual(String value);
	
        public IMax lessThan(String value);
	
        public IMax lessThanEqual(String value);
	
        public IMax between(String start, String end);
	
        public IMax like(String like);
	
        public IMax in(String...values);
	
    }

```

###### Android Sample: Get Maximum 

```java

    int maximum = 0;
	
    try {
        maximum = new Liquor().max().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }
	
```

#### iOS API: Maximum

```objective-c

    - (id<SICIMax>)max;

```

- _iOS: IMax Interface_

```objective-c

    @protocol SICIMax <NSObject>

    - (id<SICIMaxClause>)where:(NSString *)column;

    - (id<SICIMax>)whereClause:(NSString *)whereClause;

    - (id<SICIMaxClause>)and:(NSString*)column;

    - (id<SICIMaxClause>)or:(NSString *)column;

    - (id<SICIMax>)groupBy:(NSArray *)columns;

    - (id<SICIMaxClause>)having:(NSString *)column;

    - (id<SICIMax>)havingClause:(NSString *)havingClause;

    - (id<SICIMax>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICIMaxClause Interface_

```objective-c

    @protocol SICIMaxClause <NSObject>

    - (id<SICIMax>)equalTo:(id)value;

    - (id<SICIMax>)notEqualTo:(id)value;

    - (id<SICIMax>)greaterThan:(id)value;

    - (id<SICIMax>)greaterThan:(id)value;

    - (id<SICIMax>)lessThan:(id)value;

    - (id<SICIMax>)lessThanEqual:(id)value;

    - (id<SICIMax>)between:(id)start end:(id)end;

    - (id<SICIMax>)like:(id)like;

    - (id<SICIMax>)in:(id)values;

    @end

```

###### iOS Sample: Get Maximum 

```objective-c

    double maximum = 0;
	
    @try {
        maximum = [[[[[[[Liquor alloc] init] max] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }
	
```

#### Windows API: Maximum

```c#

    public IMax Max();

```

- _Windows: IMax Interface_

```c#

    public interface IMax 
    {

        IMaxClause Where(String column);
	
        IMax WhereClause(String whereClause);
	
        IMaxClause And(String column);
	
        IMaxClause Or(String column);

        IMax GroupBy(String[] columns);
	
        IMaxClause Having(String column);

        IMax HavingClause(String havingClause);
	
        IMax Column(String column);
	
        double Execute();
	 
    }

```

- _Windows: IMaxClause Interface_

```c#

    public interface IMaxClause 
    {
 
        IMax EqualTo(String value);

        IMax NotEqualTo(String value);
	
        IMax GreaterThan(String value);
	
        IMax GreaterThanEqual(String value);
	
        IMax LessThan(String value);
	
        IMax LessThanEqual(String value);
	
        IMax Between(String start, String end);
	
        IMax Like(String like);
	
        IMax In(String[] values);
	
    }

```

###### Windows Sample: Get Maximum 

```c#

    double maximum = 0;
	
    try 
    {
        maximum = new Liquor().Max().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }
	
```
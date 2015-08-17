## Average

Exposes API's to get average value of all non-NULL X within a group. String and BLOB values that do not look like numbers are interpreted as 0. The result of avg() is always a floating point value as long as at there is at least one non-NULL input even if all inputs are integers. The result of avg() is NULL if and only if there are no non-NULL inputs.


#### Android API: Average

```java

    public IAverage avg() throws DatabaseException;

```

- _Android: IAverage Interface_

```java

    public interface IAverage {

        public IAverageClause where(String column);
	
        public IAverage whereClause(String whereClause);
	
        public IAverageClause and(String column);
	
        public IAverageClause or(String column);

        public IAverage groupBy(String...columns);

        public IAverageClause having(String column);

        public IAverage havingClause(String havingClause);
	
        public IAverage column(String column);
	
        public Object execute() throws DatabaseException;

    }

```

- _Android: IAverageClause Interface_

```java

    public interface IAverageClause {

        public IAverage equalTo(String value);

        public IAverage notEqualTo(String value);
	
        public IAverage greaterThan(String value);
	
        public IAverage greaterThanEqual(String value);
	
        public IAverage lessThan(String value);
	
        public IAverage lessThanEqual(String value);
	
        public IAverage between(String start, String end);
	
        public IAverage like(String like);

        public IAverage in(String...values);

    }

```

###### Android Sample: Get Average

```java

    int average = 0;
	
    try {
        average = new Liquor().avg().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }
	
```

#### iOS API: Average

```objective-c

    - (id<SICIAverage>)avg;

```

- _iOS: SICIAverage Interface_

```objective-c

    @protocol SICIAverage <NSObject>

    - (id<SICIAverageClause>)where:(NSString *)column;
	
    - (id<SICIAverage>)whereClause:(NSString *)whereClause;
	
    - (id<SICIAverageClause>)and:(NSString *)column;
	
    - (id<SICIAverageClause>)or:(NSString *)column;

    - (id<SICIAverage>)groupBy:(NSArray *)columns;

    - (id<SICIAverageClause>)having:(NSString *)column;

    - (id<SICIAverage>)havingClause:(NSString *)havingClause;

    - (id<SICIAverage>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICIAverageClause Interface_

```objective-c

    @protocol SICIAverageClause <NSObject>

    - (id<SICIAverage>)equalTo:(id)value;

    - (id<SICIAverage>)notEqualTo:(id)value;

    - (id<SICIAverage>)greaterThan:(id)value;

    - (id<SICIAverage>)greaterThanEqual:(id)value;

    - (id<SICIAverage>)lessThan:(id)value;

    - (id<SICIAverage>)lessThanEqual:(id)value;

    - (id<SICIAverage>)between:(id)start end:(id)end;

    - (id<SICIAverage>)like:(id)like;

    - (id<SICIAverage>)in:(id)values;

    @end

```

###### iOS Sample: Get Average 

```objective-c

    double average = 0;
	
    @try {
        average = [[[[[[[Liquor alloc] init] avg] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }
	
```

#### Windows API: Average

```c#

    public IAverage Avg();

```

- _Windows: IAverage Interface_

```c#

    public interface IAverage 
    {

        IAverageClause Where(String column);

        IAverage WhereClause(String whereClause);
	
        IAverageClause And(String column);
	
        IAverageClause Or(String column);

        IAverage GroupBy(String[] columns);

        IAverageClause Having(String column);

        IAverage HavingClause(String havingClause);
	
        IAverage Column(String column);
	
        double Execute();

    }

```

- _Windows: IAverageClause Interface_

```c#

    public interface IAverageClause 
    {

        IAverage EqualTo(String value);

        IAverage NotEqualTo(String value);
	
        IAverage GreaterThan(String value);
	
        IAverage GreaterThanEqual(String value);
	
        IAverage LessThan(String value);
	
        IAverage LessThanEqual(String value);
	
        IAverage Between(String start, String end);
	
        IAverage Like(String like);
	
        IAverage In(String[] values);
 
    }

```

###### Windows Sample: Get Average 

```java

    double average = 0;

    try 
    {
        average = new Liquor().Avg().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }
	
```


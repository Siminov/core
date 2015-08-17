## Delete

Database class provides following API's to delete tuples from table.

#### Android API: Delete

```java

    public IDelete delete() throws DatabaseException { }

```

- Android API: IDelete Interface

```java

    public interface IDelete {

        public IDeleteClause where(String column);
	
        public IDelete whereClause(String whereClause); 

        public IDeleteClause and(String column);
	
        public IDeleteClause or(String column);

        public Object execute() throws DatabaseException;

    }

```

- Android API: IDeleteClause Interface

```java

    public interface IDeleteClause {

        public IDelete equalTo(String value);

        public IDelete notEqualTo(String value);
	
        public IDelete greaterThan(String value);
	
        public IDelete greaterThanEqual(String value);
	
        public IDelete lessThan(String value);
	
        public IDelete lessThanEqual(String value);
	
        public IDelete between(String start, String end);
	
        public IDelete like(String like);
	
        public IDelete in(String...values);

    }

```

###### Android Sample: Delete 

```java

    Liquor liquor = new Liquor();
    liquor.delete().execute();

```

#### iOS API: Delete

```objective-c

    - (id<SICIDelete>)delete { }

```

- iOS API: SICIDelete

```objective-c

    @protocol SICIDelete <NSObject>

    - (id<SICIDeleteClause>)where:(NSString *)column;

    - (id<SICIDelete>)whereClause:(NSString *)whereClause;

    - (id<SICIDeleteClause>)and:(NSString *)column;
 
    - (id<SICIDeleteClause>)or:(NSString *)column;

    - (id)execute;

    @end

```

- iOS API: SICIDeleteClause

```objective-c

    @protocol SICIDeleteClause <NSObject>

    - (id<SICIDelete>)equalTo:(id)value;

    - (id<SICIDelete>)notEqualTo:(id)value;

    - (id<SICIDelete>)greaterThan:(id)value;

    - (id<SICIDelete>)greaterThan:(id)value;
 
    - (id<SICIDelete>)lessThan:(id)value;

    - (id<SICIDelete>)lessThanEqual:(id)value;

    - (id<SICIDelete>)between:(id)start end:(id)end;

    - (id<SICIDelete>)like:(id)like;

    - (id<SICIDelete>)in:(id)values;

    @end

```

###### iOS Sample: Delete

```objective-c

    Liquor *liquor = [[Liquor alloc] init];
    [[liquor delete] execute];

```


## Windows API: Delete

```c#

    public IDelete Delete() { }

```

- Windows API: IDelete

```c#

    public interface IDelete 
    {

        IDeleteClause Where(String column);
	
        IDelete WhereClause(String whereClause); 

        IDeleteClause And(String column);
	
        IDeleteClause Or(String column);

        void Execute();

    }
```

- Windows API: IDeleteClause

```c#

    public interface IDeleteClause 
    {

        IDelete EqualTo(String value);

        IDelete NotEqualTo(String value);
	
        IDelete GreaterThan(String value);
	
        IDelete GreaterThanEqual(String value);
	
        IDelete LessThan(String value);
	
        IDelete LessThanEqual(String value);
	
        IDelete Between(String start, String end);
	
        IDelete Like(String like);
	
        IDelete In(String[] values);

    }

```

###### Windows Sample: Delete 

```c#

    Liquor liquor = new Liquor();
    liquor.Delete().Execute();

```

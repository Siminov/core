## Get Mandatory Fields

#### Android API: Get Mandatory Fields

```java

    public final Iterator<String> getMandatoryFields() throws DatabaseException;

```

- _Android Sample: Get Column Names_

```java

    Iterator<String> mandatoryFields = null;
    try {
        mandatoryFields = new Liquor().getMandatoryFields();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Get Mandatory Fields

```objective-c

    - (NSArray *)getMandatoryFields;

```

- _iOS Sample: Get Column Names_

```objective-c

    NSArray *mandatoryFields = nil;
    @try {
        mandatoryFields = [[[Liquor alloc] init] getMandatoryFields];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Get Mandatory Fields

```c#

    public IEnumerator<String> GetMandatoryFields();

```

- _Windows Sample: Get Column Names_

```c#

    IEnumerator<String> mandatoryFields = null;
    try 
    {
        mandatoryFields = new Liquor().GetMandatoryFields();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```

Exposes convert API which is responsible to provide column data type based on model variable data type.

#### Android API: IDataTypeHandler

```java

    public interface IDataTypeHandler {

        public String convert(String dataType);
	
    }  

```

#### iOS API: SICIDataTypeHandler

```objective-c

    @protocol SICIDataTypeHandler <NSObject>

    - (NSString *)convert:(NSString *)dataType;

    @end

```

#### Windows API: IDataTypeHandler

```c#

    public interface IDataTypeHandler 
    {

        String Convert(String dataType);
	
    }

```


## 1. Convert Data Type - [Android: convert(Data Type) | iOS:convert:(NSString *)Data-Type | Windows:Convert(Data Type)]
Converts java variable data type to database column data type.
 
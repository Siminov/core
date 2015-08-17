Based on java variable type siminov decides the data type of column.

## 1. Java Data-Types
- _**int**_: Java int primitive data type is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">int</property>
	</attribute>

```

- _**Integer**_: Java Integer class data type is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Integer</property>
	</attribute>
```

- _**long**_: Java long primitive data type is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">long</property>
	</attribute>
```

- _**Long**_: Java Long class data type is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Long</property>
	</attribute>
```


- _**float**_: Java float primitive data type is converted to <b>REAL</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">float</property>
	</attribute>
```

- _**Float**_: Java Float class data type is converted to <b>REAL</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Float</property>
	</attribute>
```

- _**boolean**_: Java boolean primitive data type is converted to <b>NUMERIC</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">boolean</property>
	</attribute>
```

- _**Boolean**_: Java Boolean class data type is converted to <b>NUMERIC</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Boolean</property>
	</attribute>
```

- _**char**_: Java char array primitive data type is converted to <b>TEXT</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">char</property>
	</attribute>
```

- _**Character**_: Java Character class data type is converted to <b>TEXT</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Character</property>
	</attribute>
```

- _**String**_: Java String class data type is converted to <b>TEXT</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.String</property>
	</attribute>
```

- _**byte**_: Java byte array data type is converted to <b>NONE</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">byte</property>
	</attribute>
```

- _**Byte**_: Java Byte class data type is converted to <b>NONE<b/> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Byte</property>
	</attribute>
```

- _**void**_: Java void primitive data type is converted to <b>NONE</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">void</property>
	</attribute>
```

- _**Void**_: Java Void class data type is converted to <b>NONE</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Void</property>
	</attribute>
```

- _**short**_: Java short primitive is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">short</property>
	</attribute>
```

- _**Short**_: Java Short class data type is converted to <b>INTEGER</b> SQLite data type.

```xml
	<attribute variable_name="name-of-variable" column_name="name-of-column">
		<property name="type">java.lang.Sort</property>
	</attribute>
```


## 2. SQLite Data-Types

- _**INTEGER**_: This SQLite data type generally contain <i>INT</i>, <i>INTEGER</i>, <i>TINYINT</i>, <i>SMALLINT</i>, <i>MEDIUMINT</i>, <i>BIGINT</i>, <i>UNSIGNED BIG INT</i>, <i>INT2</i>, <i>INT8</i>.

- _**TEXT**_: This SQLite data type generally contain <i>CHARACTER(20)</i>, <i>VARCHAR(255)</i>, <i>VARYING CHARACTER(255)</i>, <i>NCHAR(55)</i>, <i>NATIVE CHARACTER(70)</i>, <i>NVARCHAR(100)</i>, <i>TEXT</i>, <i>CLOB</i>.

- _**REAL**_: This SQLite data type generally contain <i>REAL</i>, <i>DOUBLE</i>, <i>DOUBLE PRECISION</i>, <i>FLOAT</i>.

- _**NONE**_: This SQLite data type generally contain <i>BLOB</i>, <i>NO DATA TYPE SPECIFIED</i>.

- _**NUMERIC**_: This SQLite data type generally contain <i>NUMERIC</i>, <i>DECIMAL(10,5)</i>, <i>BOOLEAN</i>, <i>DATE</i>, <i>DATETIME</i>.

> <b>Note</b>
> - If you define ORM using Annotation then you don't have to specify data type, it will automatically be configured based on variable data type.
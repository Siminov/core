## Create Index

Define index structure using DatabaseMappingDescriptor.si.xml file

#### Android Sample: DatabaseDescriptor.si.xml 
  
```xml

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="siminov.core.sample.model.Liquor">

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <column>HISTORY</column>
            </index>
									
        </entity>

    </database-mapping-descriptor>			

```

#### iOS Sample: DatabaseDescriptor.si.xml

```xml

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="Liquor">

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <column>HISTORY</column>
            </index>
									
        </entity>

    </database-mapping-descriptor>			

```

#### Windows Sample: DatabaseDescriptor.si.xml

```xml

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="Siminov.Core.Sample.Model.Liquor">

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <column>HISTORY</column>
            </index>
									
        </entity>

    </database-mapping-descriptor>			

```



## Many to One

Many-To-One relationship is in which one entity (typically a column or set of columns) contains values that refer to another entity (a column or set of columns) that has unique values.

**Example**: In a geography schema having tables Region, State, and City, there are many states that are in a given region, but no states are in two regions.

#### Many To One Syntax

```xml 

    <database-mapping-descriptor>

        <entity table_name="name-of-table" class_name="mapped_model_class_name">

            <relationships>
			
                <many-to-one refer="name-of-variable" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
                    <property name="load">true/false</property>
                </many-to-one>		
		    		
            </relationships>		

        </entity>

    </database-mapping-descriptor>		
```
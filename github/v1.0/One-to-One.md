## One to One

One-To-One relationship is in which each row in one database table is linked to 1 and 1 other row in another table.

**Example**: Relationship between Table A and Table B, each row in Table A is linked to another row in Table B. The number of rows in Table A must equal the number of rows in Table B.

## One To One Syntax

```xml

    <database-mapping-descriptor>

        <entity table_name="name-of-table" class_name="full-class-path-of-model-class">

            <relationships>
			
                <one-to-one refer="name-of-variable" refer_to="full-class-path-of-refer-variable" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
                    <property name="load">true/false</property>
                </one-to-one>		
		    		
            </relationships>		

        </entity>

    </database-mapping-descriptor>		

```
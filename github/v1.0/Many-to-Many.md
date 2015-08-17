## Many to Many

Many-To-Many relationship is in which one or more rows in the table can be related to 0, 1 or many rows in another table. A mapping table is required in order to implement such a relationship.

**Example**: All the customers belonging to a bank is stored in a customer table while all the bank's products are stored in a product table. Each customer can have many products and each product can be assigned to many customers.

#### Many To Many Syntax

```xml

    <database-mapping-descriptor>

        <entity table_name="name-of-table" class_name="mapped_model_class_name">

            <relationships>
			
                <many-to-many refer="name-of-variable" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
                    <property name="load">true/false</property>
                </many-to-many>		
		    		
            </relationships>		
 
        </entity>

    </database-mapping-descriptor>		
```
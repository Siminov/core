## One to Many

One-To-Many relationship is in which each row in the related to table can be related to many rows in the relating table. This effectively save storage as the related record does not need to be stored multiple times in the relating table.

**Example**: All the customers belonging to a business is stored in a customer table while all the customer invoices are stored in an invoice table. Each customer can have many invoices but each invoice can only be generated for a single customer.


#### One To Many Syntax:

```xml

    <database-mapping-descriptor>

        <entity table_name="name-of-table" class_name="mapped_model_class_name">

            <relationships>
			
                <one-to-many refer="name-of-variable" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
                    <property name="load">true/false</property>
                </one-to-many>		
		    		
            </relationships>		

        </entity>

    </database-mapping-descriptor>		

```
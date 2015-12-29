/**
 * [SIMINOV FRAMEWORK]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package siminov.core.database;

import java.util.Iterator;
import java.util.Map;

import siminov.core.database.design.IAverage;
import siminov.core.database.design.ICount;
import siminov.core.database.design.IDatabase;
import siminov.core.database.design.IDelete;
import siminov.core.database.design.IGroupConcat;
import siminov.core.database.design.IMax;
import siminov.core.database.design.IMin;
import siminov.core.database.design.ISelect;
import siminov.core.database.design.ISum;
import siminov.core.database.design.ITotal;
import siminov.core.exception.DatabaseException;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.EntityDescriptor;
import siminov.core.resource.ResourceManager;

/**
 * Exposes methods to interact with database. 
 * It has methods to create, delete, and perform other common database management tasks.
 */
public class Database implements IDatabase {

	private Object object = null;
	
	/**
	 * Database Constructor
	 */
	public Database() {
		
	}
	
	public Database(Object object) {
		this.object = object;
	}
	
	/**
	 * It drop's the whole database based on database name.
	  	<p>
			<pre> Drop the Book table.
	
	{@code
	
		DatabaseDescriptor databaseDescriptor = new Book().getDatabaseDescriptor();
		
		try {
			Database.dropDatabase(databaseDescriptor.getDatabaseName());
		} catch(DatabaseException databaseException) {
			//Log It.
		}
	
	}
			</pre>
		</p>
	 * @param databaseName Entity Descriptor object which defines the structure of table.
	 * @throws DatabaseException If not able to drop database.
	 */
	public static void dropDatabase(String databaseName) throws DatabaseException {
		ResourceManager resourceManager = ResourceManager.getInstance();
		DatabaseHelper.dropDatabase(resourceManager.getDatabaseDescriptorBasedOnName(databaseName));
	}
	
	/**
	   Is used to create a new table in an database.
	  	<p>
	  	Using SIMINOV there are three ways to create table in database.
	   	
	   	<pre> 
	  		<ul>
	  			<li> Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
	  				<p>
SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.
	  				
Example:
	{@code


<!-- Design Of EntityDescriptor.xml -->

<entity-descriptor>

    <!-- General Properties Of Table And Class -->
    
    	<!-- Mandatory Field -->
    		<!-- NAME OF TABLE -->
    <property name="table_name">name_of_table</property>
    
    	<!-- Mandatory Field -->
    		<!-- MAPPED CLASS NAME -->
    <property name="class_name">mapped_class_name</property>
    
    
    	<!-- Optional Field -->
    <attributes>
        
	    <!-- Column Properties Required Under This Table -->
	    
			<!-- Optional Field -->
		<attribute>
		    
			    <!-- Mandatory Field -->
					<!-- COLUMN_NAME: Mandatory Field -->
   		    <property name="column_name">column_name_of_table</property>
		    			
    		    <!-- Mandatory Field -->
					<!-- VARIABLE_NAME: Mandatory Field -->
		    <property name="variable_name">class_variable_name</property>
		    		    
			    <!-- Mandatory Field -->
			<property name="type">java_variable_data_type</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="primary_key">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="not_null">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="unique">true/false</property>
			
				<!-- Optional Field -->
			<property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
			
				<!-- Optional Field -->
			<property name="default">default_value_of_column (Eg: 0.1)</property>
		
		</attribute>		

    </attributes>
		
		
		<!-- Optional Field -->
    <indexes>
        
		<!-- Index Properties -->
		<index>
		    
			    <!-- Mandatory Field -->
			    	<!-- NAME OF INDEX -->
		    <property name="name">name_of_index</property>
		    
			    <!-- Mandatory Field -->
					<!-- UNIQUE: Optional Field (Default is false) -->
		    <property name="unique">true/false</property>
		    
		    	<!-- Optional Field -->
		    		<!-- Name of the column -->
		    <property name="column">column_name_needs_to_add</property>
		    
		</index>
        
    </indexes>
    
		
	<!-- Map Relationship Properties -->
				
		<!-- Optional Field's -->	
	<relationships>
		    
	    <relationship>
	        
	        	<!-- Mandatory Field -->
	        		<!-- Type of Relationship -->
	        <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER -->
	        <property name="refer">class_variable_name</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER TO -->
	        <property name="refer_to">map_to_class_name</property>
	            
	        	<!-- Optional Field -->
	        <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>    
	            
	        	<!-- Optional Field -->    
	        <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>    
	            
				<!-- Optional Field (Default is false) -->
	       	<property name="load">true/false</property>	            
	        
	    </relationship>
	    
	</relationships>

</entity-descriptor>
		
	}
					</p>
	  			</li>
	  		</ul> 
	  	</pre>
	  </p>
	  
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public void createTable() throws DatabaseException {
		DatabaseHelper.createTable(getEntityDescriptor());
	}

	/**
	 * It drop's the table from database based on entity descriptor.
	  	<p>
			<pre> Drop the Book table.
	
	{@code
	
	Book book = new Book();
	
	try {
		book.dropTable();
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	
			</pre>
		</p>

	 * @throws DatabaseException If not able to drop table.
	 */
	public void dropTable() throws DatabaseException {
		DatabaseHelper.dropTable(getEntityDescriptor());
	}

	/**
	   Is used to drop a index on a table in database.
	  	<p>
			<pre> Create Index On Book table.
	
	{@code
	
	String indexName = "BOOK_INDEX_BASED_ON_AUTHOR";
	Book cBook = new Book();
	
	try {
		cBook.dropIndex(indexName);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>

	 * @param indexName Name of a index needs to be drop.
	 * @throws DatabaseException If not able to drop index on table.
	 */
	public void dropIndex(String indexName) throws DatabaseException {
		DatabaseHelper.dropIndex(getEntityDescriptor(), indexName);
	}
	

	/**
 	Returns all tuples based on query from mapped table for invoked class object.
 
 	<pre>
 	
Example:

{@code

Book[] books = null;
try {
	books = new Book().select().execute();
} catch(DatabaseException de) {
	//Log it.
}
 		
} 			
 	</pre>

	 	@return ISelect object.
	 	@throws DatabaseException If any error occur while getting tuples from a single table.
	 */
	public ISelect select() throws DatabaseException {
		return new Where(getEntityDescriptor(), ISelect.class.getName(), this);
	}

	/**
 	Returns all tuples based on manual query from mapped table for invoked class object.
 
 	<pre>
 	
Example:

{@code

String query = "SELECT * FROM BOOK";

Book[] books = null;
try {
	books = new Book().select(query);
} catch(DatabaseException de) {
	//Log it.
}
 		
} 			
 	</pre>

		@param query Query to get tuples from database.
	 	@return ISelect object.
	 	@throws DatabaseException If any error occur while getting tuples from a single table.
	 */
	public Object[] select(String query) throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.select(object, query);
		} else {
			return DatabaseHelper.select(this, query);
		}
	}

	/**
	It adds a record to any single table in a relational database.

   	<pre>
   	
Example: Make Book Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

try {
	cBook.save();
} catch(DatabaseException de) {
	//Log it.
}

}

    </pre>
 
   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	public void save() throws DatabaseException {

		if(object != null) {
			DatabaseHelper.save(object);
		} else {
			DatabaseHelper.save(this);
		}
	}

	/**
	It updates a record to any single table in a relational database.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

try {
	cBook.update();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	public void update() throws DatabaseException {
		
		if(object != null) {
			DatabaseHelper.update(object);
		} else {
			DatabaseHelper.update(this);
		}
	}

	
	/**
		It finds out whether tuple exists in table or not.
		IF NOT EXISTS:
			adds a record to any single table in a relational database.
		ELSE:
			updates a record to any single table in a relational database.
	
	   	<pre>
	   	
Example: Make Beer Object

	{@code

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	try {
		cBook.saveOrUpdate();
	} catch(DatabaseException de) {
		//Log it.
	}
			
	}			
				
	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	public void saveOrUpdate() throws DatabaseException {
		
		if(object != null) {
			DatabaseHelper.saveOrUpdate(object);
		} else {
			DatabaseHelper.saveOrUpdate(this);
		}
	}

	/**
	It deletes a record from single table in a relational database.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

try {
	cBook.delete();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	public IDelete delete() throws DatabaseException {
		
		if(object != null) {
			return new Where(getEntityDescriptor(), IDelete.class.getName(), object);
		} else {
			return new Where(getEntityDescriptor(), IDelete.class.getName(), this);
		}
	}

	/**
	Returns the count of rows based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int noOfBooks = 0;

try {
	noOfBooks = cBook.count().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting count of tuples from database.
	 */
	public ICount count() throws DatabaseException {
		return new Where(getEntityDescriptor(), ICount.class.getName());
	}

	/**
	Returns the average based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int noOfBooks = 0;

try {
	noOfBooks = cBook.avg().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting average from database.
	 */
	public IAverage avg() throws DatabaseException {
		return new Where(getEntityDescriptor(), IAverage.class.getName());
	}

	/**
	Returns the sum based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int noOfBooks = 0;

try {
	noOfBooks = cBook.sum().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting sum from database.
	 */
	public ISum sum() throws DatabaseException {
		return new Where(getEntityDescriptor(), ISum.class.getName());
	}

	/**
	Returns the total based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int totalBooks = 0;

try {
	totalBooks = cBook.avg().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting total tuples from database.
	 */
	public ITotal total() throws DatabaseException {
		return new Where(getEntityDescriptor(), ITotal.class.getName());
	}

	/**
	Returns the min based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int minBooks = 0;

try {
	minBooks = cBook.min().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting min from database.
	 */
	public IMin min() throws DatabaseException {
		return new Where(getEntityDescriptor(), IMin.class.getName());
	}

	/**
	Returns the max based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int maxBooks = 0;

try {
	maxBooks = cBook.max().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting max from database.
	 */
	public IMax max() throws DatabaseException {
		return new Where(getEntityDescriptor(), IMax.class.getName());
	}

	/**
	Returns the group concat based on where clause provided.

   	<pre>

Example: Make Beer Object

{@code

Book cBook = new Book();
cBook.setTitle(Book.BOOK_TYPE_C);
cBook.setDescription(applicationContext.getString(R.string.c_description));
cBook.setAuthor(applicationContext.getString(R.string.c_author));
cBook.setLink(applicationContext.getString(R.string.c_link));

int groupConcatBooks = 0;

try {
	groupConcatBooks = cBook.groupConcat().execute();
} catch(DatabaseException de) {
	//Log it.
}

}
    </pre>
 
   	@throws DatabaseException If any error occurs while getting group concat from database.
	 */
	public IGroupConcat groupConcat() throws DatabaseException {
		return new Where(getEntityDescriptor(), IGroupConcat.class.getName());
	}

	/**
	 * Returns database descriptor object based on the POJO class called.

	 <pre>
Example:

	{@code
	try {
		DatabaseDescriptor databaseDescriptor = new Book().getDatabaseDescriptor();
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	</pre>
	
	 * @return Database Descriptor Object.
	 * @throws DatabaseException If any error occur while getting database descriptor object.
	 */
	public DatabaseDescriptor getDatabaseDescriptor() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getDatabaseDescriptor(object.getClass().getName());
		} else {
			return DatabaseHelper.getDatabaseDescriptor(this.getClass().getName());
		}
	}

	/**
 	Returns the actual entity descriptor object mapped for invoked class object.
 
 	<pre>
 	
Example:
{@code
 			
EntityDescriptor entityDescriptor = null;
try {
	entityDescriptor = new Book().getEntityDescriptor();
} catch(DatabaseException de) {
	//Log it.
}

} 			
		
 	</pre>
 	
 	@return EntityDescriptor Object
 	@throws DatabaseException If entity descriptor object not mapped for invoked class object.
	 */
	public EntityDescriptor getEntityDescriptor() throws DatabaseException {

		if(object != null) {
			return DatabaseHelper.getEntityDescriptor(object.getClass().getName());
		} else {
			return DatabaseHelper.getEntityDescriptor(this.getClass().getName());
		}
	}

	
	/**
 	Returns the mapped table name for invoked class object.
 
 	<pre>

Example:

{@code

String tableName = null;
try {
	tableName = new Book().getTableName();
} catch(DatabaseException de) {
	//Log it.
}

}
 			
 	</pre>
 
 	@return Mapped Table name.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public String getTableName() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getTableName(object);
		} else {
			return DatabaseHelper.getTableName(this);
		}
	}

	/**
 	Returns all column names of mapped table.
 	
 	<pre>

Example:

{@code

Iterator<String> columnNames = null;
try {
	columnNames = new Book().getColumnNames();
} catch(DatabaseException de) {
	//Log it.
}
 		
} 			
 	</pre>
 	
 	@return All column names of mapped table.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public Iterator<String> getColumnNames() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnNames(object);
		} else {
			return DatabaseHelper.getColumnNames(this);
		}
	}

	/**
 	Returns all column values in the same order of column names for invoked class object.
 	
 	<pre>

Example:
{@code

Map<String, Object> values = null;
try {
	values = new Book().getColumnValues();
} catch(DatabaseException de) {
	//Log it.
}

}	
 	</pre>
 	
 	@return All column values for invoked object.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public Map<String, Object> getColumnValues() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnValues(object);
		} else {
			return DatabaseHelper.getColumnValues(this);
		}
	}

	/**
 	Returns all columns with there data types for invoked class object.

 	<pre>

Example:

{@code

Map<String, String> columnTypes = null;
try {
	columnTypes = new Book().getColumnTypes();
} catch(DatabaseException de) {
	//Log it.
}	

} 		
 	</pre>
 	
 	@return All columns with there data types.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public Map<String, String> getColumnTypes() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnTypes(object);
		} else {
			return DatabaseHelper.getColumnTypes(this);
		}
	}

	/**
 	Returns all primary keys of mapped table for invoked class object.
 	
 	<pre>

Example:
 		
{@code

Iterator<String> primaryKeys = null;
try {
	primaryKeys = new Book().getPrimeryKeys();
} catch(DatabaseException de) {
	//Log it.
}

} 	
 	</pre>
 
 	@return All primary keys.
 	@throws DatabaseException If not mapped table found for invoked class object.
	 */
	public Iterator<String> getPrimaryKeys() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getPrimaryKeys(object);
		} else {
			return DatabaseHelper.getPrimaryKeys(this);
		}
	}

	/**
 	Returns all mandatory fields which are associated with mapped table for invoked class object.
 
 	<pre>

Example:
 			
{@code

Iterator<String> mandatoryFields = null;
try {
	mandatoryFields = new Book().getMandatoryFields();
} catch(DatabaseException de) {
	//Log it.
}

} 			
 	</pre>
 
 	@return All mandatory fields for mapped table.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public Iterator<String> getMandatoryFields() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getMandatoryFields(object);
		} else {
			return DatabaseHelper.getMandatoryFields(this);
		}
	}

	/**
 	Returns all unique fields which are associated with mapped table for invoked class object.
 
 	<pre>

Example:
 			
{@code
 			
Iterator<String> uniqueFields = null;
try {
	uniqueFields = new Book().getUniqueFields();
} catch(DatabaseException de) {
	//Log it.
}
 		
} 			
 	</pre>
 
 	@return All unique fields for mapped table.
 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	public Iterator<String> getUniqueFields() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getUniqueFields(object);
		} else {
			return DatabaseHelper.getUniqueFields(this);
		}
	}

	/**
 	Returns all foreign keys of mapped table for invoked class object.
 
 	<pre>
 		
Example:
 			
{@code
 			
Iterator<String> foreignKeys = null;
try {
	foreignKeys = new Book().getForeignKeys();
} catch(DatabaseException de) {
	//Log it.
}

} 			
 		
 	</pre>
 
 	@return All foreign keys of mapped table.
 	@throws DatabaseException If no mapped table found for invoked class object.
 */
	public Iterator<String> getForeignKeys() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getForeignKeys(object);
		} else {
			return DatabaseHelper.getForeignKeys(this);
		}
	}

	/**
	   Begins a transaction in EXCLUSIVE mode.
	   <p>
	   Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
	   The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
	  
	   <pre>

Example: Make Beer Object

	{@code

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	DatabaseDescriptor databaseDescriptor = cBook.getDatabaseDescriptor();

	try {
		Database.beginTransaction(databaseDescriptor);

	cBook.save();

		Database.commitTransaction(databaseDescriptor);
	} catch(DatabaseException de) {
		//Log it.
	} finally {
		Database.endTransaction(databaseDescriptor);
	}
	  		
	}  			
	   </pre>
	  
	 * @throws DatabaseException If beginTransaction does not starts.
	 */
	public static void beginTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		DatabaseHelper.beginTransaction(databaseDescriptor);
	}
	
	
	/**
	   Marks the current transaction as successful. 
	   <p> Finally it will End a transaction.
	   <pre>

Example: Make Beer Object
	{@code

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	DatabaseDescriptor databaseDescriptor = cBook.getDatabaseDescriptor();
  
	try {
		Database.beginTransaction(databaseDescriptor);

		cBook.save();
  
		Database.commitTransaction(databaseDescriptor);
	} catch(DatabaseException de) {
		//Log it.
	} finally {
		Database.endTransaction(databaseDescriptor);
	}
	
	}

	    </pre>
	 * @throws DatabaseException If not able to commit the transaction.
	 */
	public static void commitTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		DatabaseHelper.commitTransaction(databaseDescriptor);
	}
}

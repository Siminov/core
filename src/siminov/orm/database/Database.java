package siminov.orm.database;

import java.util.Iterator;
import java.util.Map;

import siminov.orm.database.design.IAverage;
import siminov.orm.database.design.ICount;
import siminov.orm.database.design.IDatabase;
import siminov.orm.database.design.IDelete;
import siminov.orm.database.design.IGroupConcat;
import siminov.orm.database.design.IMax;
import siminov.orm.database.design.IMin;
import siminov.orm.database.design.ISelect;
import siminov.orm.database.design.ISum;
import siminov.orm.database.design.ITotal;
import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.resource.Resources;

public class Database implements IDatabase {

	private Object object = null;
	
	public Database() {
		
	}
	
	public Database(Object object) {
		this.object = object;
	}
	
	public static void dropDatabase(String databaseName) throws DatabaseException {
		Resources resources = Resources.getInstance();
		DatabaseHelper.dropDatabase(resources.getDatabaseDescriptorBasedOnName(databaseName));
	}
	
	public void createTable() throws DatabaseException {
		DatabaseHelper.createTable(getDatabaseMappingDescriptor());
	}

	public void dropTable() throws DatabaseException {
		DatabaseHelper.dropTable(getDatabaseMappingDescriptor());
	}

	public void dropIndex(String indexName) throws DatabaseException {
		DatabaseHelper.dropIndex(getDatabaseMappingDescriptor(), indexName);
	}
	
	public ISelect select() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ISelect.class.getName());
	}

	public Object[] select(String query) throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.select(object, query);
		} else {
			return DatabaseHelper.select(this, query);
		}
	}

	public void save() throws DatabaseException {

		if(object != null) {
			DatabaseHelper.save(object);
		} else {
			DatabaseHelper.save(this);
		}
	}

	public void update() throws DatabaseException {
		
		if(object != null) {
			DatabaseHelper.update(object);
		} else {
			DatabaseHelper.update(this);
		}
	}

	public void saveOrUpdate() throws DatabaseException {
		
		if(object != null) {
			DatabaseHelper.saveOrUpdate(object);
		} else {
			DatabaseHelper.saveOrUpdate(this);
		}
	}

	public IDelete delete() throws DatabaseException {
		
		if(object != null) {
			return new Select(getDatabaseMappingDescriptor(), IDelete.class.getName(), object);
		} else {
			return new Select(getDatabaseMappingDescriptor(), IDelete.class.getName(), this);
		}
	}

	public ICount count() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ICount.class.getName());
	}

	public IAverage avg() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IAverage.class.getName());
	}

	public ISum sum() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ISum.class.getName());
	}

	public ITotal total() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ITotal.class.getName());
	}

	public IMin min() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IMin.class.getName());
	}

	public IMax max() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IMax.class.getName());
	}

	public IGroupConcat groupConcat() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IGroupConcat.class.getName());
	}

	public DatabaseDescriptor getDatabaseDescriptor() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getDatabaseDescriptor(object.getClass().getName());
		} else {
			return DatabaseHelper.getDatabaseDescriptor(this.getClass().getName());
		}
	}

	public DatabaseMappingDescriptor getDatabaseMappingDescriptor() throws DatabaseException {

		if(object != null) {
			return DatabaseHelper.getDatabaseMappingDescriptor(object.getClass().getName());
		} else {
			return DatabaseHelper.getDatabaseMappingDescriptor(this.getClass().getName());
		}
	}

	public String getTableName() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getTableName(object);
		} else {
			return DatabaseHelper.getTableName(this);
		}
	}

	public Iterator<String> getColumnNames() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnNames(object);
		} else {
			return DatabaseHelper.getColumnNames(this);
		}
	}

	public Map<String, Object> getColumnValues() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnValues(object);
		} else {
			return DatabaseHelper.getColumnValues(this);
		}
	}

	public Map<String, String> getColumnTypes() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getColumnTypes(object);
		} else {
			return DatabaseHelper.getColumnTypes(this);
		}
	}

	public Iterator<String> getPrimaryKeys() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getPrimaryKeys(object);
		} else {
			return DatabaseHelper.getPrimaryKeys(this);
		}
	}

	public Iterator<String> getMandatoryFields() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getMandatoryFields(object);
		} else {
			return DatabaseHelper.getMandatoryFields(this);
		}
	}

	public Iterator<String> getUniqueFields() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getUniqueFields(object);
		} else {
			return DatabaseHelper.getUniqueFields(this);
		}
	}

	public Iterator<String> getForeignKeys() throws DatabaseException {
		
		if(object != null) {
			return DatabaseHelper.getForeignKeys(object);
		} else {
			return DatabaseHelper.getForeignKeys(this);
		}
	}

	public static void beginTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		DatabaseHelper.beginTransaction(databaseDescriptor);
	}
	
	public static void commitTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		DatabaseHelper.commitTransaction(databaseDescriptor);
	}
	
	public static void endTransaction(final DatabaseDescriptor databaseDescriptor) {
		DatabaseHelper.endTransaction(databaseDescriptor);
	}
}

package siminov.orm.database;

import siminov.orm.database.design.IDataTypeHandler;
import siminov.orm.database.design.IDatabase;
import siminov.orm.database.design.IQueryBuilder;

public class DatabaseBundle {

	private IDatabase database = null;
	private IQueryBuilder queryBuilder = null;
	private IDataTypeHandler dataTypeHandler = null;
	
	public IDatabase getDatabase() {
		return this.database;
	}
	
	public void setDatabase(IDatabase database) {
		this.database = database;
	}
	
	public IQueryBuilder getQueryBuilder() {
		return this.queryBuilder;
	}
	
	public void setQueryBuilder(IQueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	
	public IDataTypeHandler getDataTypeHandler() {
		return this.dataTypeHandler;
	}
	
	public void setDataTypeHandler(IDataTypeHandler dataTypeHandler) {
		this.dataTypeHandler = dataTypeHandler;
	}
	
}

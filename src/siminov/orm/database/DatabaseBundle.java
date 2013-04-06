package siminov.orm.database;

import siminov.orm.database.impl.IDataTypeHandler;
import siminov.orm.database.impl.IDatabase;
import siminov.orm.database.impl.IQueryBuilder;
import siminov.orm.database.impl.ISyntaxProvider;

public class DatabaseBundle {

	private IDatabase database = null;
	private IQueryBuilder queryBuilder = null;
	private IDataTypeHandler dataTypeHandler = null;
	private ISyntaxProvider syntaxProvider = null;
	
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
	
	public ISyntaxProvider getSyntaxProvider() {
		return this.syntaxProvider;
	}
	
	public void setSyntaxProvider(ISyntaxProvider syntaxProvider) {
		this.syntaxProvider = syntaxProvider;
	}
	
}

package siminov.orm.database;

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

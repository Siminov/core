package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface ICount {

	public ICount distinct();
	
	public ICountClause where(String column);
	
	public ICount whereClause(String whereClause);
	
	public ICountClause and(String column);
	
	public ICountClause or(String column);

	public ICount groupBy(String...columns);
	
	public ICountClause having(String column);

	public ICount havingClause(String havingClause);
	
	public ICount column(String column);
	
	public Object execute() throws DatabaseException;
	
}

package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface IMin {

	public IMinClause where(String column);
	
	public IMin whereClause(String whereClause);
	
	public IMinClause and(String column);
	
	public IMinClause or(String column);

	public IMin groupBy(String...columns);
	
	public IMinClause having(String column);

	public IMin havingClause(String havingClause);
	
	public IMin column(String column);
	
	public Object execute() throws DatabaseException;

}

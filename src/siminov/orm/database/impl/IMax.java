package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface IMax {

	public IMaxClause where(String column);
	
	public IMax whereClause(String whereClause);
	
	public IMaxClause and(String column);
	
	public IMaxClause or(String column);

	public IMax groupBy(String...columns);
	
	public IMaxClause having(String column);

	public IMax havingClause(String havingClause);
	
	public IMax column(String column);
	
	public Object execute() throws DatabaseException;
	
}

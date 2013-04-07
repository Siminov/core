package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface ITotal {

	public ITotalClause where(String column);
	
	public ITotal whereClause(String whereClause);
	
	public ITotalClause and(String column);
	
	public ITotalClause or(String column);

	public ITotal groupBy(String...columns);
	
	public ITotalClause having(String column);

	public ITotal havingClause(String havingClause);
	
	public ITotal column(String column);
	
	public Object execute() throws DatabaseException;
	
}

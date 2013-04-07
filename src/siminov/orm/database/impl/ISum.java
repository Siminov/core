package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface ISum {

	public ISumClause where(String column);
	
	public ISum whereClause(String whereClause);
	
	public ISumClause and(String column);
	
	public ISumClause or(String column);

	public ISum groupBy(String...columns);
	
	public ISumClause having(String column);

	public ISum havingClause(String havingClause);
	
	public ISum column(String column);
	
	public Object execute() throws DatabaseException;

}

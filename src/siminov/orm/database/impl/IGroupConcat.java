package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface IGroupConcat {

	public IGroupConcat delimiter(String delimiter);
	
	public IGroupConcatClause where(String column);
	
	public IGroupConcat whereClause(String whereClause);
	
	public IGroupConcatClause and(String column);
	
	public IGroupConcatClause or(String column);

	public IGroupConcat groupBy(String...columns);
	
	public IGroupConcatClause having(String column);

	public IGroupConcat havingClause(String havingClause);
	
	public IGroupConcat column(String column);
	
	public Object execute() throws DatabaseException;

}

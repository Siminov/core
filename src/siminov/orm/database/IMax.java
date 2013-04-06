package siminov.orm.database;

import siminov.orm.database.Clause;
import siminov.orm.database.Select;
import siminov.orm.exception.DatabaseException;

public interface IMax {

	public Clause where(String column);
	
	public Select whereClause(String whereClause);
	
	public Clause and(String column);
	
	public Clause or(String column);

	public Select groupBy(String...columns);
	
	public Clause having(String column);

	public Select havingClause(String havingClause);
	
	public Select columns(String...columns);
	
	public int execute() throws DatabaseException;
	
}

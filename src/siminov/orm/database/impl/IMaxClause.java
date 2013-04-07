package siminov.orm.database.impl;

public interface IMaxClause {

	public IMax equalTo(String value);

	public IMax notEqualTo(String value);
	
	public IMax greaterThan(String value);
	
	public IMax greaterThanEqual(String value);
	
	public IMax lessThan(String value);
	
	public IMax lessThanEqual(String value);
	
	public IMax between(String start, String end);
	
	public IMax like(String like);
	
	public IMax in(String...values);
	
}

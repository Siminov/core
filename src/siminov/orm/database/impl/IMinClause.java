package siminov.orm.database.impl;

public interface IMinClause {

	public IMin equalTo(String value);

	public IMin notEqualTo(String value);
	
	public IMin greaterThan(String value);
	
	public IMin greaterThanEqual(String value);
	
	public IMin lessThan(String value);
	
	public IMin lessThanEqual(String value);
	
	public IMin between(String start, String end);
	
	public IMin like(String like);
	
	public IMin in(String...values);
	
}

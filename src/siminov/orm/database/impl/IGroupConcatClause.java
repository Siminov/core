package siminov.orm.database.impl;

public interface IGroupConcatClause {

	public IGroupConcat equalTo(String value);

	public IGroupConcat notEqualTo(String value);
	
	public IGroupConcat greaterThan(String value);
	
	public IGroupConcat greaterThanEqual(String value);
	
	public IGroupConcat lessThan(String value);
	
	public IGroupConcat lessThanEqual(String value);
	
	public IGroupConcat between(String start, String end);
	
	public IGroupConcat like(String like);
	
	public IGroupConcat in(String...values);

}

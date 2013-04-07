package siminov.orm.database.impl;

public interface ICountClause {

	public ICount equalTo(String value);

	public ICount notEqualTo(String value);
	
	public ICount greaterThan(String value);
	
	public ICount greaterThanEqual(String value);
	
	public ICount lessThan(String value);
	
	public ICount lessThanEqual(String value);
	
	public ICount between(String start, String end);
	
	public ICount like(String like);
	
	public ICount in(String...values);

}

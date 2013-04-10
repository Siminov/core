package siminov.orm.database.impl;

/**
 * Exposes API's to provide condition on where clause to get tuples from table..
 */
public interface ISelectClause {

	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return ISelect Interface.
	 */
	public ISelect equalTo(String value);

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return ISelect Interface.
	 */
	public ISelect notEqualTo(String value);
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return ISelect Interface.
	 */
	public ISelect greaterThan(String value);
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return ISelect Interface.
	 */
	public ISelect greaterThanEqual(String value);
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return ISelect Interface.
	 */
	public ISelect lessThan(String value);
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return ISelect Interface.
	 */
	public ISelect lessThanEqual(String value);
	
	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return ISelect Interface.
	 */
	public ISelect between(String start, String end);
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return ISelect Interface.
	 */
	public ISelect like(String like);
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return ISelect Interface.
	 */
	public ISelect in(String...values);
	
}

package siminov.orm.database.impl;

/**
 * Exposes API's to provide condition on where clause to get tuples from table..
 */
public interface IFetchClause {

	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return IFetch Interface.
	 */
	public IFetch equalTo(String value);

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return IFetch Interface.
	 */
	public IFetch notEqualTo(String value);
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return IFetch Interface.
	 */
	public IFetch greaterThan(String value);
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return IFetch Interface.
	 */
	public IFetch greaterThanEqual(String value);
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return IFetch Interface.
	 */
	public IFetch lessThan(String value);
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return IFetch Interface.
	 */
	public IFetch lessThanEqual(String value);
	
	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return IFetch Interface.
	 */
	public IFetch between(String start, String end);
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return IFetch Interface.
	 */
	public IFetch like(String like);
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return IFetch Interface.
	 */
	public IFetch in(String...values);
	
}

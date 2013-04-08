package siminov.orm.database.impl;

/**
 * Exposes API's to provide condition on where clause to calculate count.
 */
public interface ICountClause {

	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return ICount Interface.
	 */
	public ICount equalTo(String value);

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return ICount Interface.
	 */
	public ICount notEqualTo(String value);
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return ICount Interface.
	 */
	public ICount greaterThan(String value);
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return ICount Interface.
	 */
	public ICount greaterThanEqual(String value);
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return ICount Interface.
	 */
	public ICount lessThan(String value);
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return ICount Interface.
	 */
	public ICount lessThanEqual(String value);
	
	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return ICount Interface.
	 */
	public ICount between(String start, String end);
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return ICount Interface.
	 */
	public ICount like(String like);
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return ICount Interface.
	 */
	public ICount in(String...values);

}

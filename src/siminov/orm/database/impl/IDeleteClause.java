package siminov.orm.database.impl;

/**
 * Exposes API's to provide condition on where clause to delete tuple from table.
 */
public interface IDeleteClause {

	public String INTERFACE_NAME = IDeleteClause.class.getName();

	
	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return IAverage Interface.
	 */
	public IAverage equalTo(String value);

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return IAverage Interface.
	 */
	public IAverage notEqualTo(String value);
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return IAverage Interface.
	 */
	public IAverage greaterThan(String value);
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return IAverage Interface.
	 */
	public IAverage greaterThanEqual(String value);
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return IAverage Interface.
	 */
	public IAverage lessThan(String value);
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return IAverage Interface.
	 */
	public IAverage lessThanEqual(String value);
	
	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return IAverage Interface.
	 */
	public IAverage between(String start, String end);
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return IAverage Interface.
	 */
	public IAverage like(String like);
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return IAverage Interface.
	 */
	public IAverage in(String...values);

}

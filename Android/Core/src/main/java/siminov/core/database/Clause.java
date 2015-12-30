/**
 * [SIMINOV FRAMEWORK - CORE]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/


package siminov.core.database;

import siminov.core.database.design.IAverageClause;
import siminov.core.database.design.ICountClause;
import siminov.core.database.design.IDeleteClause;
import siminov.core.database.design.IGroupConcatClause;
import siminov.core.database.design.IMaxClause;
import siminov.core.database.design.IMinClause;
import siminov.core.database.design.ISelectClause;
import siminov.core.database.design.ISumClause;
import siminov.core.database.design.ITotalClause;


/**
 * It is used to create where clause used in database query.
 * It implements all the Clauses which are used to in the where clause.	
 */
public class Clause implements ISelectClause, IDeleteClause, ICountClause, ISumClause, ITotalClause, IAverageClause, IMaxClause, IMinClause, IGroupConcatClause {

	static final String EQUAL_TO = "=";
	static final String NOT_EQUAL_TO = "!=";
	static final String GREATER_THAN = ">";
	static final String GREATER_THAN_EQUAL = ">=";
	static final String LESS_THAN = "<";
	static final String LESS_THAN_EQUAL = "<=";
	static final String BETWEEN = "BETWEEN";
	static final String LIKE = "LIKE";
	static final String IN = "IN";
	static final String AND = "AND";
	static final String OR = "OR";
	
	static final String ASC_ORDER_BY = "ASC";
	static final String DESC_ORDER_BY = "DESC"; 
	
	private StringBuilder whereClause = new StringBuilder();
	
	private Where where = null;
	
	/**
	 * Clause Constructor
	 * @param where Where clause
	 */
	public Clause(Where where) {
		this.where = where;
	}
	
	/**
	 * Add column
	 * @param column Name of column
	 */
	void addCol(String column) {
		whereClause.append(column);
	}
	
	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return Where object.
	 */
	public Where equalTo(Object value) {
		whereClause.append(EQUAL_TO + " '" + value.toString() + "' ");
		return this.where;
	}

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return Where object.
	 */
	public Where notEqualTo(Object value) {
		whereClause.append(NOT_EQUAL_TO + " '" + value + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return Where object.
	 */
	public Where greaterThan(Object value) {
		whereClause.append(GREATER_THAN + " '" + value + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return Where object.
	 */
	public Where greaterThanEqual(Object value) {
		whereClause.append(GREATER_THAN_EQUAL + " '" + value + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return Where object.
	 */
	public Where lessThan(Object value) {
		whereClause.append(LESS_THAN + " '" + value + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return Where object.
	 */
	public Where lessThanEqual(Object value) {
		whereClause.append(LESS_THAN_EQUAL + " '" + value + "' ");
		return this.where;
	}

	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return Where object.
	 */
	public Where between(Object start, Object end) {
		whereClause.append(BETWEEN + " '" + start + "' " + AND + " '" + end + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return Where object.
	 */
	public Where like(Object like) {
		whereClause.append(LIKE + " '" + like + "' ");
		return this.where;
	}
	
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return Where object.
	 */
	public Where in(Object...values) {
		whereClause.append(IN + "(");
		
		if(values != null && values.length > 0) {
			for(int i = 0;i < values.length;i++) {
				if(i == 0) {
					whereClause.append("'" + values[i] + "'");
					continue;
				}
				
				whereClause.append(" ,'" + values[i] + "'");
			}
		} 
		
		whereClause.append(")");
		
		return this.where;
	}
	
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 */
	void and(String column) {
		whereClause.append(" " + AND + " " + column);
	}
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 */
	void or(String column) {
		whereClause.append(" " + OR + " " + column);
	}
	
	
	/**
	 * It returns the where clause.
	 * @return String where clause.
	 */
	public String toString() {
		return whereClause.toString();
	}
	
}

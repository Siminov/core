/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution|support@siminov.com]
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

package siminov.orm.database;

import siminov.orm.database.impl.IAverageClause;
import siminov.orm.database.impl.ICountClause;
import siminov.orm.database.impl.ISelectClause;
import siminov.orm.database.impl.IGroupConcatClause;
import siminov.orm.database.impl.IMaxClause;
import siminov.orm.database.impl.IMinClause;
import siminov.orm.database.impl.ISumClause;
import siminov.orm.database.impl.ITotalClause;

public class Clause implements ISelectClause, ICountClause, ISumClause, ITotalClause, IAverageClause, IMaxClause, IMinClause, IGroupConcatClause {

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
	
	private StringBuilder where = new StringBuilder();
	
	private Select select = null;
	
	public Clause(Select select) {
		this.select = select;
	}
	
	void addCol(String column) {
		where.append(column);
	}
	
	public Select equalTo(String value) {
		where.append(EQUAL_TO + " '" + value + "' ");
		return this.select;
	}
	
	public Select notEqualTo(String value) {
		where.append(NOT_EQUAL_TO + " '" + value + "' ");
		return this.select;
	}
	
	public Select greaterThan(String value) {
		where.append(GREATER_THAN + " '" + value + "' ");
		return this.select;
	}
	
	public Select greaterThanEqual(String value) {
		where.append(GREATER_THAN_EQUAL + " '" + value + "' ");
		return this.select;
	}
	
	public Select lessThan(String value) {
		where.append(LESS_THAN + " '" + value + "' ");
		return this.select;
	}
	
	public Select lessThanEqual(String value) {
		where.append(LESS_THAN_EQUAL + " '" + value + "' ");
		return this.select;
	}

	public Select between(String start, String end) {
		where.append(BETWEEN + " '" + start + "' " + AND + " '" + end + "' ");
		return this.select;
	}
	
	public Select like(String like) {
		where.append(LIKE + " '" + like + "' ");
		return this.select;
	}
	
	public Select in(String...values) {
		where.append(IN + "(");
		
		if(values != null && values.length > 0) {
			for(int i = 0;i < values.length;i++) {
				if(i == 0) {
					where.append("'" + values[i] + "'");
					continue;
				}
				
				where.append(" ,'" + values[i] + "'");
			}
		} 
		
		where.append(")");
		
		return this.select;
	}
	
	void and(String column) {
		where.append(" " + AND + " " + column);
	}
	
	void or(String column) {
		where.append(" " + OR + " " + column);
	}
	
	public String toString() {
		return where.toString();
	}
	
}

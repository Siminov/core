/**
 * [SIMINOV FRAMEWORK]
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

import java.util.Arrays;

import siminov.core.database.design.IAverage;
import siminov.core.database.design.ICount;
import siminov.core.database.design.IDelete;
import siminov.core.database.design.IGroupConcat;
import siminov.core.database.design.IMax;
import siminov.core.database.design.IMin;
import siminov.core.database.design.ISelect;
import siminov.core.database.design.ISum;
import siminov.core.database.design.ITotal;
import siminov.core.exception.DatabaseException;
import siminov.core.model.EntityDescriptor;

/**
 * It is used to provide condition between where clause.
 */
public class Where implements ISelect, IDelete, ICount, ISum, ITotal, IAverage, IMax, IMin, IGroupConcat {

	private EntityDescriptor entityDescriptor = null;
	private String interfaceName = null;
	private Object referObject = null;
	
	
	private String column = null;
	private String[] columns = new String[] {};
	
	private Clause where = null;
	private String whereClause = null;
	
	private String[] orderBy = null;
	private String whichOrderBy = null;

	private String[] groupBy;
	
	private Clause having = null;
	private String havingClause = null;
	
	private int limit;

	private boolean distinct = false;

	private String delimiter = null;
	
	/**
	 * Where Constructor
	 */
	public Where() {
		
	}
	
	/**
	 * Where Constructor
	 * @param entityDescriptor Entity Descriptor instance
	 * @param interfaceName Name of interface
	 */
	public Where(final EntityDescriptor entityDescriptor, final String interfaceName) {
		this.entityDescriptor = entityDescriptor;
		this.interfaceName = interfaceName;
	}

	/**
	 * Where Constructor
	 * @param entityDescriptor Entity Descriptor instance
	 * @param interfaceName Name of interface
	 * @param referObject Refered Object instance
	 */
	public Where(final EntityDescriptor entityDescriptor, final String interfaceName, final Object referObject) {
		this.entityDescriptor = entityDescriptor;
		this.interfaceName = interfaceName;
		this.referObject = referObject;
	}
	
	
	/**
	 * Used to specify DISTINCT condition.
	 * @return Where Where instance object.
	 */
	public Where distinct() {
		this.distinct = true;
		return this;
	}
	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return Clause Clause instance object.
	 */
	public Clause where(String column) {
		
		where = new Clause(this);
		where.addCol(column);
		
		return where;
	}
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return Where Where instance object.
	 */
	public Where whereClause(String whereClause) {
		this.whereClause = whereClause;
		return this;
	}

	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return Clause instance object.
	 */
	public Clause and(String column) {
		this.where.and(column);
		return this.where;
	}
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return Clause Clause instance object.
	 */
	public Clause or(String column) {
		this.where.or(column);
		return this.where;
	}
	
	
	/**
	 * Used to specify ORDER BY keyword to sort the result-set.
	 * @param columns Name of columns which need to be sorted.
	 * @return Where Where instance object.
	 */
	public Where orderBy(String...columns) {
		this.orderBy = columns;
		return this;
	}
	
	/**
	 * Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return Where Where instance object.
	 */
	public Where ascendingOrderBy(String...columns) {
		this.orderBy = columns;
		this.whichOrderBy = Clause.ASC_ORDER_BY;

		return this;
	}
	
	/**
	 * Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return Where Where instance object.
	 */
	public Where descendingOrderBy(String...columns) {
		this.orderBy = columns;
		this.whichOrderBy = Clause.DESC_ORDER_BY;

		return this;
	}
	
	/**
	 * Used to specify the range of data need to fetch from table.
	 * @param limit LIMIT of data.
	 * @return Where Where instance object.
	 */
	public Where limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return Where Where instance object.
	 */
	public Where groupBy(String...columns) {
		this.groupBy = columns;
		return this;
	}

	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return Clause Clause instance object.
	 */
	public Clause having(String column) {
		
		having = new Clause(this);
		having.addCol(column);
		
		return having;
	}
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return Where Where instance object.
	 */
	public Where havingClause(String havingClause) {
		this.havingClause = havingClause;
		return this;
	}
	
	public Where column(String column) {
		this.column = column;
		return this;
	}
	
	/**
	 * Used to provide name of columns only for which data will be fetched.
	 * @param columns Name of columns.
	 * @return Where Where instance object.
	 */
	public Where columns(String...columns) {
		this.columns = columns;
		return this;
	}

	/**
	 * Used to provide name of column for which average will be calculated.
	 * @param delimiter Name of Delimiter.
	 * @return Where Where instance object.
	 */
	public Where delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	@SuppressWarnings("unchecked")
	public<T> T execute() throws DatabaseException {

		String where = "";
		if(this.whereClause != null && this.whereClause.length() > 0) {
			where = this.whereClause;
		} else {
			if(this.where != null) {
				where = this.where.toString();
			}
		}
		
		String having = "";
		if(this.havingClause != null && this.havingClause.length() > 0) {
			having = this.havingClause;
		} else {
			if(this.having != null) {
				having = this.having.toString();
			}
		}

		if(this.columns == null) {
			this.columns = new String[] {};			
		}
		
		if(this.orderBy == null) {
			this.orderBy = new String[] {};
		}
		
		if(this.groupBy == null) {
			this.groupBy = new String[] {};
		}

		String limit = null;
		if(this.limit != 0) {
			limit = String.valueOf(this.limit);
		}

		
		if(interfaceName.equalsIgnoreCase(IDelete.class.getName())) {
			DatabaseHelper.delete(referObject, where);
		} else if(interfaceName.equalsIgnoreCase(ICount.class.getName())) {
			return (T) (Integer) DatabaseHelper.count(entityDescriptor, column, distinct, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IAverage.class.getName())) {
			return (T) (Integer) DatabaseHelper.avg(entityDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(ISum.class.getName())) {
			return (T) (Integer) DatabaseHelper.sum(entityDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(ITotal.class.getName())) {
			return (T) (Integer) DatabaseHelper.total(entityDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IMax.class.getName())) {
			return (T) (Integer) DatabaseHelper.max(entityDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IMin.class.getName())) {
			return (T) (Integer) DatabaseHelper.min(entityDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IGroupConcat.class.getName())) {
			return (T) DatabaseHelper.groupConcat(entityDescriptor, column, delimiter, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(ISelect.class.getName())) {
			return (T) DatabaseHelper.select(referObject, null, entityDescriptor, distinct, where, Arrays.asList(columns).iterator(), Arrays.asList(groupBy).iterator(), having, Arrays.asList(orderBy).iterator(), whichOrderBy, limit);
		}

		return null;
	}
}

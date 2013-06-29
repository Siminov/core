/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution LLP|support@siminov.com]
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

import java.util.Arrays;

import siminov.orm.database.impl.IAverage;
import siminov.orm.database.impl.ICount;
import siminov.orm.database.impl.IDelete;
import siminov.orm.database.impl.IGroupConcat;
import siminov.orm.database.impl.IMax;
import siminov.orm.database.impl.IMin;
import siminov.orm.database.impl.ISelect;
import siminov.orm.database.impl.ISum;
import siminov.orm.database.impl.ITotal;
import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseMappingDescriptor;

public class Select implements ISelect, IDelete, ICount, ISum, ITotal, IAverage, IMax, IMin, IGroupConcat {

	private DatabaseMappingDescriptor databaseMappingDescriptor = null;
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
	
	public Select() {
		
	}
	
	public Select(final DatabaseMappingDescriptor databaseMappingDescriptor, final String interfaceName) throws DatabaseException {
		this.databaseMappingDescriptor = databaseMappingDescriptor;
		this.interfaceName = interfaceName;
	}

	public Select(final DatabaseMappingDescriptor databaseMappingDescriptor, final String interfaceName, final Object referObject) throws DatabaseException {
		this.databaseMappingDescriptor = databaseMappingDescriptor;
		this.interfaceName = interfaceName;
		this.referObject = referObject;
	}
	

	public Select distinct() {
		this.distinct = true;
		return this;
	}
	
	public Clause where(String column) {
		
		where = new Clause(this);
		where.addCol(column);
		
		return where;
	}
	
	public Select whereClause(String whereClause) {
		this.whereClause = whereClause;
		return this;
	}

	public Clause and(String column) {
		this.where.and(column);
		return this.where;
	}
	
	public Clause or(String column) {
		this.where.or(column);
		return this.where;
	}
	
	
	public Select orderBy(String...columns) {
		this.orderBy = columns;
		return this;
	}
	
	public Select ascendingOrderBy(String...columns) {
		this.orderBy = columns;
		this.whichOrderBy = Clause.ASC_ORDER_BY;

		return this;
	}
	
	public Select descendingOrderBy(String...columns) {
		this.orderBy = columns;
		this.whichOrderBy = Clause.DESC_ORDER_BY;

		return this;
	}
	
	public Select limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public Select groupBy(String...columns) {
		this.groupBy = columns;
		return this;
	}

	public Clause having(String column) {
		
		having = new Clause(this);
		having.addCol(column);
		
		return having;
	}
	
	public Select havingClause(String havingClause) {
		this.havingClause = havingClause;
		return this;
	}
	
	public Select column(String column) {
		this.column = column;
		return this;
	}
	
	public Select columns(String...columns) {
		this.columns = columns;
		return this;
	}

	public Select delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	public Object execute() throws DatabaseException {

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

		
		if(interfaceName.equalsIgnoreCase(IDelete.INTERFACE_NAME)) {
			Database.delete(referObject, where);
		} else if(interfaceName.equalsIgnoreCase(ICount.INTERFACE_NAME)) {
			return Database.count(databaseMappingDescriptor, column, distinct, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IAverage.INTERFACE_NAME)) {
			return Database.avg(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(ISum.INTERFACE_NAME)) {
			return Database.sum(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(ITotal.INTERFACE_NAME)) {
			return Database.total(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IMax.INTERFACE_NAME)) {
			return Database.max(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IMin.INTERFACE_NAME)) {
			return Database.min(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(interfaceName.equalsIgnoreCase(IGroupConcat.INTERFACE_NAME)) {
			return Database.groupConcat(databaseMappingDescriptor, column, delimiter, where, Arrays.asList(groupBy).iterator(), having);
		} 

		return null;
	}

	public Object[] fetch() throws DatabaseException {

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
		
		return Database.select(databaseMappingDescriptor, distinct, where, Arrays.asList(columns).iterator(), Arrays.asList(groupBy).iterator(), having, Arrays.asList(orderBy).iterator(), whichOrderBy, limit);
		
	}
	
}

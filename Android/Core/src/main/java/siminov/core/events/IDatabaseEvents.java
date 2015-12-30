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


package siminov.core.events;

import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.EntityDescriptor;
import siminov.core.model.EntityDescriptor.Index;

/**
 * Exposes methods which deal with events associated with database operation's.
 * It has methods such as (databaseCreated, databaseDroped, tableCreated, tableDroped, indexCreated).
 */
public interface IDatabaseEvents {

	/**
	 * This event is fired when database gets created as per database descriptor.
	 * @param databaseDescriptor contains meta data associated with database.
	 */
	public void onDatabaseCreated(final DatabaseDescriptor databaseDescriptor);
	
	/**
	 * This event is fired when database is dropped.
	 * @param databaseDescriptor contains meta data associated with dropped database.
	 */
	public void onDatabaseDropped(final DatabaseDescriptor databaseDescriptor);
	
	
	
	/**
	 * This event is fired when a table is created.
	 * @param entityDescriptor contains meta data associated with created table.
	 */
	public void onTableCreated(final DatabaseDescriptor databaseDescriptor, final EntityDescriptor entityDescriptor);
	
	/**
	 * This event is fired when a table is dropped.
	 * @param entityDescriptor contains meta data associated with dropped table.
	 */
	public void onTableDropped(final DatabaseDescriptor databaseDescriptor, final EntityDescriptor entityDescriptor);
	
	
	
	
	/**
	 * This event is fired when a index is created on table.
	 * @param entityDescriptor contains meta data associated with table on which index is created.
	 * @param index meta data about index got created.
	 */
	public void onIndexCreated(final DatabaseDescriptor databaseDescriptor, final EntityDescriptor entityDescriptor, Index index);
	
	/**
	 * This event is fired when a index is dropped.
	 * @param entityDescriptor contains meta data associated with table on which index is dropped.
	 * @param index meta data about index got dropped.
	 */
	public void onIndexDropped(final DatabaseDescriptor databaseDescriptor, final EntityDescriptor entityDescriptor, Index index);
	
}

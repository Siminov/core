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

import java.io.File;

import siminov.core.Constants;
import siminov.core.model.ApplicationDescriptor;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.resource.ResourceManager;
import android.content.Context;
import android.os.Environment;


/**
 * Exposes utils methods to deal with database. 
 * It has methods to form database file paths.
 */
public class DatabaseUtils {

	/**
	 * Check whether SDCard is present on device or not.
	 * @return TRUE: If SDCard is present on device, FALSE: If SDCard is not present.
	 */
	public boolean isSDCardPresent() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * Get internal memory database path.
	 * @param databaseDescriptor Database Descriptor Object.
	 */
	public String internalMemoryDatabasePath(final DatabaseDescriptor databaseDescriptor) {
		ResourceManager resourceManager = ResourceManager.getInstance();
		ApplicationDescriptor applicationDescriptor = resourceManager.getApplicationDescriptor();
		Context context = resourceManager.getApplicationContext();
		
		String databaseDirName = databaseDescriptor.getDatabaseName();
		
		String databaseDirPath = Constants.DATABASE_PATH_DATA + File.separator + Constants.DATABASE_PATH_DATA + File.separator + context.getPackageName() + File.separator + applicationDescriptor.getName() + File.separator + Constants.DATABASE_PATH_DATABASE + File.separator + databaseDirName + File.separator;
		return databaseDirPath;
	}
	
	/**
	 * Get sdcard database path.
	 * @param databaseDescriptor Database Descriptor Object.
	 */
	public String sdcardMemoryDatabasePath(final DatabaseDescriptor databaseDescriptor) {
		File externalStorage = Environment.getExternalStorageDirectory();
		String externalStoragePath = externalStorage.getAbsolutePath();
		
		ResourceManager resourceManager = ResourceManager.getInstance();
		ApplicationDescriptor applicationDescriptor = resourceManager.getApplicationDescriptor();

		String databaseDirName = databaseDescriptor.getDatabaseName();

		String databaseDirPath = externalStoragePath + File.separator + applicationDescriptor.getName() + File.separator + Constants.DATABASE_PATH_DATABASE + File.separator + databaseDirName + File.separator;
		return databaseDirPath;
	}
	
	/**
	 * Get database path based on which storage needed.
	 * @param databaseDescriptor Database Descriptor Object.
	 */
	public String getDatabasePath(final DatabaseDescriptor databaseDescriptor) {
		DatabaseUtils databaseUtils = new DatabaseUtils();
		String databasePath = null;

		boolean isExternalStorageEnable = databaseDescriptor.isExternalStorageEnable();
		if(isExternalStorageEnable) {
			if(databaseUtils.isSDCardPresent()) {
				databasePath = databaseUtils.sdcardMemoryDatabasePath(databaseDescriptor);
			} else {
				databasePath = databaseUtils.internalMemoryDatabasePath(databaseDescriptor);
			}
		} else {
			databasePath = databaseUtils.internalMemoryDatabasePath(databaseDescriptor);
		}

		return databasePath;
	}
}

/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
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


#if __MOBILE__
#define XAMARIN
#endif

#if !__MOBILE__
#define WINDOWS
#endif


using Siminov.Core.Utils;
using Siminov.Core.Model;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


#if XAMARIN
using PCLStorage;
#endif

namespace Siminov.Core.Database
{

    /// <summary>
    /// Exposes utils methods to deal with database. 
    /// It has methods to form database file paths.
    /// </summary>
    public class DatabaseUtils
    {

        /// <summary>
        /// Check whether SDCard is present on device or not.
        /// </summary>
        /// <returns>TRUE: If SDCard is present on device, FALSE: If SDCard is not present.</returns>
        public bool IsSDCardPresent()
        {
            return false;//android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        }

        /// <summary>
        /// Get internal memory database path
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        /// <returns></returns>
        public String InternalMemoryDatabasePath(DatabaseDescriptor databaseDescriptor)
        {

            #if XAMARIN
            
                var rootFolder = FileSystem.Current.LocalStorage;
			    return rootFolder.Path;

            #elif WINDOWS

                ResourceManager resourceManager = ResourceManager.GetInstance();
                ApplicationDescriptor applicationDescriptor = resourceManager.GetApplicationDescriptor();

                String databaseDirName = databaseDescriptor.GetDatabaseName();

                String databaseDirPath = applicationDescriptor.GetName() + FileUtils.Separator + Constants.DATABASE_PATH_DATABASE + FileUtils.Separator + databaseDirName + FileUtils.Separator;
                return databaseDirPath;
            
            #endif

        }

        /// <summary>
        /// Get sdcard database path
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        /// <returns></returns>
        public String SdcardMemoryDatabasePath(DatabaseDescriptor databaseDescriptor)
        {
            /*File externalStorage = Environment.GetExternalStorageDirectory();
            String externalStoragePath = externalStorage.GetAbsolutePath();

            ResourceManager resourceManager = ResourceManager.GetInstance();
            ApplicationDescriptor applicationDescriptor = resourceManager.GetApplicationDescriptor();

            String databaseDirName = databaseDescriptor.GetDatabaseName();

            String databaseDirPath = externalStoragePath + File.separator + applicationDescriptor.getName() + File.separator + Constants.DATABASE_PATH_DATABASE + File.separator + databaseDirName + File.separator;
            return databaseDirPath;*/
            return "";
        }

        /// <summary>
        /// Get database path based on which storage needed
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        /// <returns></returns>
        public String GetDatabasePath(DatabaseDescriptor databaseDescriptor)
        {
            DatabaseUtils databaseUtils = new DatabaseUtils();
            String databasePath = null;

            bool isExternalStorageEnable = databaseDescriptor.IsExternalStorageEnable();
            if (isExternalStorageEnable)
            {
                if (databaseUtils.IsSDCardPresent())
                {
                    databasePath = databaseUtils.SdcardMemoryDatabasePath(databaseDescriptor);
                }
                else
                {
                    databasePath = databaseUtils.InternalMemoryDatabasePath(databaseDescriptor);
                }
            }
            else
            {
                databasePath = databaseUtils.InternalMemoryDatabasePath(databaseDescriptor);
            }

            return databasePath;
        }

    }
}

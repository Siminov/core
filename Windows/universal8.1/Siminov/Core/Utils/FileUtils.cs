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



using Siminov.Core.Exception;
using Siminov.Core.Log;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Storage;
using Windows.Storage.Streams;

namespace Siminov.Core.Utils
{

    public class FileUtils
    {

        public static readonly String Separator = "/";

        public static readonly int INSTALLED_FOLDER = 1;
        public static readonly int LOCAL_FOLDER = 2;

        public static bool DoesFolderExists(String path, int option)
        {

            try
            {
                GetFolder(path, option);

                return true;
            }
            catch
            {
                return false;
            }
        }


        public static bool DoesFileExists(String path, String name, int option)
        {

            StorageFolder folder = GetFolder(path, option);

            try
            {
                var fileTask = folder.GetFileAsync(name).AsTask();
                fileTask.Wait();

                if (fileTask.Exception != null)
                {
                    throw fileTask.Exception;
                }

                return true;
            }
            catch
            {
                return false;
            }
        }

        public static void DeleteFolder(String path, String name, int option)
        {

        }

        public static void DeleteFile(String path, String name, int option)
        {

            StorageFolder folder = GetFolder(path, option);

            try
            {
                var fileTask = folder.GetFileAsync(name).AsTask();
                fileTask.Wait();

                if (fileTask.Exception != null)
                {
                    throw fileTask.Exception;
                }

                StorageFile deleteFile = fileTask.Result;
                var actionTask = deleteFile.DeleteAsync().AsTask();
                actionTask.Wait();

                if (actionTask.Exception != null)
                {
                    throw actionTask.Exception;
                }
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(FileUtils).FullName, "DeleteFile", "Exception caught while deleting the file, PATH: " + path + ", NAME: " + name + ", Exception: " + exception.Message);
                throw new SiminovException(typeof(FileUtils).FullName, "DeleteFile", "Exception caught while deleting the file, Exception: " + exception.Message);
            }
        }

        public static Stream SearchFile(String path, String name, int option)
        {

            StorageFolder folder = SearchFolder(path, option);
            if (folder == null)
            {
                return SearchFile(path, name);
            }

            Stream file = ReadFile(folder, name, option);

            return file;
        }

        public static Stream SearchFile(String path, String name)
        {
            path = path.Replace("/", ".");
            StorageFolder storageFolder = Windows.ApplicationModel.Package.Current.InstalledLocation;

            var fileTask = storageFolder.GetFilesAsync().AsTask();
            fileTask.Wait();

            if (fileTask.Exception != null)
            {
                throw fileTask.Exception;
            }

            IReadOnlyList<StorageFile> files = fileTask.Result;
            for (int i = 0; i < files.Count; i++)
            {

                StorageFile file = files[i];
                if (file.FileType == ".dll" || file.FileType == ".exe")
                {
                    if (file.DisplayName.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.DisplayName.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                    {
                        continue;
                    }

                    AssemblyName assemblyName = new AssemblyName() { Name = file.DisplayName };
                    Assembly asm = Assembly.Load(assemblyName);

                    String[] resourceNames = asm.GetManifestResourceNames();
                    foreach (String resourceName in resourceNames)
                    {
                        String newResourceName = resourceName;

                        if (newResourceName.Contains(path + "." + name))
                        {
                            return asm.GetManifestResourceStream(resourceName);
                        }
                    }
                }
            }

            var foldersTask = storageFolder.GetFoldersAsync().AsTask();
            foldersTask.Wait();

            if (foldersTask.Exception != null)
            {
                throw foldersTask.Exception;
            }


            IReadOnlyList<StorageFolder> readFolders = foldersTask.Result;
            foreach (StorageFolder folder in readFolders)
            {
                String[] separators = new String[] { "/" };
                String[] paths = path.Split(separators, StringSplitOptions.None);

                fileTask = folder.GetFilesAsync().AsTask();
                fileTask.Wait();

                if (fileTask.Exception != null)
                {
                    throw fileTask.Exception;
                }

                files = fileTask.Result;
                for (int i = 0; i < files.Count; i++)
                {

                    StorageFile file = files[i];
                    if (file.FileType == ".dll" || file.FileType == ".exe")
                    {
                        if (file.DisplayName.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.DisplayName.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                        {
                            continue;
                        }

                        AssemblyName assemblyName = new AssemblyName() { Name = folder.Name + "/" + file.DisplayName };
                        Assembly asm = Assembly.Load(assemblyName);

                        String[] resourceNames = asm.GetManifestResourceNames();
                        foreach (String resourceName in resourceNames)
                        {
                            String newResourceName = resourceName;

                            if (newResourceName.Contains(path + "." + name))
                            {
                                return asm.GetManifestResourceStream(resourceName);
                            }
                        }
                    }
                }
            }

            return null;
        }


        public static StorageFolder SearchFolder(String path, int option)
        {

            StorageFolder storageFolder = null;
            if (option == INSTALLED_FOLDER)
            {
                storageFolder = Windows.ApplicationModel.Package.Current.InstalledLocation;
            }
            else
            {
                storageFolder = ApplicationData.Current.LocalFolder;
            }


            var foldersTask = storageFolder.GetFoldersAsync().AsTask();
            foldersTask.Wait();

            if (foldersTask.Exception != null)
            {
                throw foldersTask.Exception;
            }

            IReadOnlyList<StorageFolder> readFolders = foldersTask.Result;
            foreach (StorageFolder folder in readFolders)
            {
                String[] separators = new String[] { "/" };
                String[] paths = path.Split(separators, StringSplitOptions.None);

                StorageFolder searchedFolder = SearchFolder(paths, folder, option);
                if (searchedFolder != null)
                {
                    return searchedFolder;
                }
            }

            return null;
        }


        private static StorageFolder SearchFolder(String[] paths, StorageFolder storageFolder, int option)
        {
            if (paths == null || paths.Length <= 0)
            {
                return storageFolder;
            }

            var foldersTask = storageFolder.GetFoldersAsync().AsTask();
            foldersTask.Wait();

            if (foldersTask.Exception != null)
            {
                throw foldersTask.Exception;
            }

            IReadOnlyList<StorageFolder> readFolders = foldersTask.Result;
            foreach (StorageFolder folder in readFolders)
            {
                for (int i = 0; i < paths.Length;i++)
                {

                    if (folder.Name.Equals(paths[i], StringComparison.OrdinalIgnoreCase))
                    {
                        String[] newPaths = new String[paths.Length - 1];

                        int count = 0;
                        for (int j = 1;j < paths.Length; j++)
                        {
                            newPaths[count++] = paths[j];
                        }

                        return SearchFolder(newPaths, folder, option);
                    }
                }
            }

            return null;
        }



        public static Stream ReadFile(StorageFolder folder, String name, int option)
        {
            Stream fileStream = null;

            var fileTask = folder.GetFileAsync(name).AsTask();
            fileTask.Wait();

            if (fileTask.Exception != null)
            {
                throw fileTask.Exception;
            }


            StorageFile fileObject = fileTask.Result;
            var readStreamTask = fileObject.OpenAsync(FileAccessMode.Read).AsTask();
            readStreamTask.Wait();

            if (readStreamTask.Exception != null)
            {
                throw readStreamTask.Exception;
            }

            IRandomAccessStream readStreamTest = readStreamTask.Result;

            using (var reader = new DataReader(readStreamTest))
            {
                var readerallDataTask = reader.LoadAsync((uint)readStreamTest.Size).AsTask();
                readerallDataTask.Wait();

                if (readerallDataTask.Exception != null)
                {
                    throw readerallDataTask.Exception;
                }

                var buffer = new byte[(int)readStreamTest.Size];
                reader.ReadBytes(buffer);

                fileStream = new MemoryStream(buffer);
            }

            return fileStream;
        }

        public static Stream ReadFile(String path, String name, int option)
        {
            Stream fileStream = null;

            StorageFolder folder = GetFolder(path, option);
            var fileTask = folder.GetFileAsync(name).AsTask();
            fileTask.Wait();

            if (fileTask.Exception != null)
            {
                throw fileTask.Exception;
            }

            StorageFile fileObject = fileTask.Result;
            var readStreamTask = fileObject.OpenAsync(FileAccessMode.Read).AsTask();
            readStreamTask.Wait();

            if (readStreamTask.Exception != null)
            {
                throw readStreamTask.Exception;
            }

            IRandomAccessStream readStreamTest = readStreamTask.Result;

            using (var reader = new DataReader(readStreamTest))
            {
                var readerallDataTask = reader.LoadAsync((uint)readStreamTest.Size).AsTask();
                readerallDataTask.Wait();

                if (readerallDataTask.Exception != null)
                {
                    throw readerallDataTask.Exception;
                }


                var buffer = new byte[(int)readStreamTest.Size];
                reader.ReadBytes(buffer);

                fileStream = new MemoryStream(buffer);
            }

            return fileStream;
        }




        public static StorageFolder GetFolder(String path, int option)
        {
            path = path.Replace('/', '\\').TrimEnd('\\');

            StorageFolder storageFolder = null;

            if (option == INSTALLED_FOLDER)
            {
                storageFolder = Windows.ApplicationModel.Package.Current.InstalledLocation;
            }
            else
            {
                storageFolder = ApplicationData.Current.LocalFolder;
            }

            StorageFolder folder = storageFolder;

            string[] folderNames = path.Split('\\');
            for (int i = 0; i < folderNames.Length; i++)
            {
                var task = folder.CreateFolderAsync(folderNames[i], CreationCollisionOption.OpenIfExists).AsTask();
                task.Wait();

                if (task.Exception != null)
                {
                    throw task.Exception;
                }

                folder = task.Result;
            }

            return folder;
        }


        public static void CreateFolder(String path, int option)
        {

        }

        public static void CreateFile(String path, String name, int option)
        {

        }

    }
}

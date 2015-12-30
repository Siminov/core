///
/// [SIMINOV FRAMEWORK - CORE]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.


#if __MOBILE__
#define XAMARIN
#endif

#if !__MOBILE__
#define WINDOWS
#endif



using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Reflection;
using System.Threading;


#if XAMARIN
using PCLStorage;
#endif

#if WINDOWS
using Windows.Foundation;
using Windows.Storage;
using Windows.Storage.Streams;
#endif



namespace Siminov.Core.Utils
{

    public class FileUtils
    {

        public static readonly String Separator = "/";

        public static readonly int INSTALLED_FOLDER = 1;
        public static readonly int LOCAL_FOLDER = 2;

        private static ResourceManager resourceManager = ResourceManager.GetInstance();

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
            
            #if XAMARIN
                IFolder folder; 
            #elif WINDOWS
                StorageFolder folder;
            #endif

            folder = GetFolder(path, option);

            try
            {
                #if XAMARIN
					var fileTask = folder.GetFileAsync(name);
                #elif WINDOWS
                    var fileTask = folder.GetFileAsync(name).AsTask();
                #endif

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

            #if XAMARIN
                IFolder folder;
            #elif WINDOWS
                StorageFolder folder;
            #endif

            folder = GetFolder(path, option);

            try
            {
                #if XAMARIN
					var fileTask = folder.GetFileAsync(name);
                #elif WINDOWS
                    var fileTask = folder.GetFileAsync(name).AsTask();
                #endif

                fileTask.Wait();

                if (fileTask.Exception != null)
                {
                    throw fileTask.Exception;
                }

                #if XAMARIN
                    IFile deleteFile;
                #elif WINDOWS
                    StorageFile deleteFile;
                #endif

                deleteFile = fileTask.Result;

                #if XAMARIN
					var actionTask = deleteFile.DeleteAsync();
				#elif WINDOWS
					var actionTask = deleteFile.DeleteAsync().AsTask();
				#endif
                
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

            #if XAMARIN
                IFolder folder;
            #elif WINDOWS
                StorageFolder folder;
            #endif

            folder = SearchFolder(path, option);
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

            #if XAMARIN
                IFolder storageFolder = FileSystem.Current.LocalStorage;
            #elif WINDOWS
                StorageFolder storageFolder = Windows.ApplicationModel.Package.Current.InstalledLocation;
            #endif

            #if XAMARIN
				var fileTask = storageFolder.GetFilesAsync();
            #elif WINDOWS
                var fileTask = storageFolder.GetFilesAsync().AsTask();
            #endif

            fileTask.Wait();

            if (fileTask.Exception != null)
            {
                throw fileTask.Exception;
            }


            #if XAMARIN
                IList<IFile> files;
            #elif WINDOWS
                IReadOnlyList<StorageFile> files;
            #endif

            files = fileTask.Result;
            for (int i = 0; i < files.Count; i++)
            {

                #if XAMARIN
                    IFile file;
                #elif WINDOWS
                    StorageFile file;
                #endif

                file = files[i];

                #if WINDOWS
                if (file.FileType == ".dll" || file.FileType == ".exe")
                #endif
                {
                    #if XAMARIN
                    if (file.Name.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.Name.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                    #elif WINDOWS
                    if (file.DisplayName.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.DisplayName.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                    #endif
                    {
                        continue;
                    }

                    #if XAMARIN
                    AssemblyName assemblyName = new AssemblyName() { Name = file.Name };
                    #elif WINDOWS
                    AssemblyName assemblyName = new AssemblyName() { Name = file.DisplayName };
                    #endif

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


            #if XAMARIN
				var foldersTask = storageFolder.GetFoldersAsync();
            #elif WINDOWS
                var foldersTask = storageFolder.GetFoldersAsync().AsTask();
            #endif

            foldersTask.Wait();

            if (foldersTask.Exception != null)
            {
                throw foldersTask.Exception;
            }


            #if XAMARIN
                IList<IFolder> folders;
            #elif WINDOWS
                IReadOnlyList<StorageFolder> folders;
            #endif

            folders = foldersTask.Result;

            
			#if XAMARIN
				IFolder folder;
			#elif WINDOWS
				StorageFolder folder;
			#endif

            for(int i = 0;i < folders.Count;i++)
            {
                folder = folders[i];

                String[] separators = new String[] { "/" };
                String[] paths = path.Split(separators, StringSplitOptions.None);

                #if XAMARIN
					fileTask = folder.GetFilesAsync();
                #elif WINDOWS
                    fileTask = folder.GetFilesAsync().AsTask();
                #endif

                fileTask.Wait();

                if (fileTask.Exception != null)
                {
                    throw fileTask.Exception;
                }

                files = fileTask.Result;
                for (int j = 0; j < files.Count; j++)
                {

                    #if XAMARIN
                        IFile file;
                    #elif WINDOWS
                        StorageFile file;
                    #endif

                    file = files[j];

                    #if WINDOWS
                    if (file.FileType == ".dll" || file.FileType == ".exe")
                    #endif

                    {

                        #if XAMARIN
                        if (file.Name.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.Name.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                        #elif WINDOWS
                        if (file.DisplayName.Equals("sqlite", StringComparison.OrdinalIgnoreCase) || file.DisplayName.Equals("sqlite3", StringComparison.OrdinalIgnoreCase))
                        #endif
                        {
                            continue;
                        }


                        #if XAMARIN
                        AssemblyName assemblyName = new AssemblyName() { Name = folder.Name + "/" + file.Name };
                        #elif WINDOWS
                        AssemblyName assemblyName = new AssemblyName() { Name = folder.Name + "/" + file.DisplayName };
                        #endif

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


        #if XAMARIN
            public static IFolder SearchFolder(String path, int option)
            {
			    IFolder storageFolder = null;

			    IFolder rootFolder = FileSystem.Current.LocalStorage;
			    var foldersTask = rootFolder.GetFoldersAsync ();
			    foldersTask.Wait();

			    if (foldersTask.Exception != null)
			    {
				    throw foldersTask.Exception;
			    }

			    IList<IFolder> folders = foldersTask.Result;
			    foreach (IFolder folder in folders)
			    {
				    String[] separators = new String[] { "/" };
				    String[] paths = path.Split(separators, StringSplitOptions.None);

				    IFolder searchedFolder = SearchFolder(paths, folder, option);
				    if (searchedFolder != null)
				    {
					    return searchedFolder;
				    }
			    }

			    return null;
            }
        #elif WINDOWS
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

        #endif


        #if XAMARIN
            private static IFolder SearchFolder(String[] paths, IFolder storageFolder, int option)
            {
			    if (paths == null || paths.Length <= 0)
			    {
				    return storageFolder;
			    }

			    var foldersTasks = storageFolder.GetFoldersAsync();
			    foldersTasks.Wait ();

			    if (foldersTasks.Exception != null)
			    {
				    throw foldersTasks.Exception;
			    }


			    IList<IFolder> folders = foldersTasks.Result;
			    foreach (IFolder folder in folders) 
			    {
				    for (int i = 0; i < paths.Length; i++) 
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
        #elif WINDOWS
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
                    for (int i = 0; i < paths.Length; i++)
                    {

                        if (folder.Name.Equals(paths[i], StringComparison.OrdinalIgnoreCase))
                        {
                            String[] newPaths = new String[paths.Length - 1];

                            int count = 0;
                            for (int j = 1; j < paths.Length; j++)
                            {
                                newPaths[count++] = paths[j];
                            }

                            return SearchFolder(newPaths, folder, option);
                        }
                    }
                }

                return null;
            }

        #endif


        #if XAMARIN
            public static Stream ReadFile(IFolder folder, String name, int option)
            {
			    StreamReader fileStream = null;

			    var fileTask = folder.GetFileAsync(name);
			    fileTask.Wait ();

			    if (fileTask.Exception != null)
			    {
				    throw fileTask.Exception;
			    }

			    IFile file = fileTask.Result;

			    var readStreamTask = file.OpenAsync(FileAccess.Read);
			    readStreamTask.Wait ();

			    if (readStreamTask.Exception != null)
			    {
				    throw readStreamTask.Exception;
			    }

			    Stream readStream = readStreamTask.Result;

			    using (StreamReader reader = new System.IO.StreamReader (readStream)) {
				    fileStream = Utils.ToInputStream(reader.ReadToEnd());
			    }

			    return fileStream.BaseStream;
            }
        #elif WINDOWS
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


        #endif


        #if XAMARIN
        
		    public static Stream ReadFileFromEmbeddedResources(String resourceId) 
		    {
			    resourceId = resourceId.Replace("/", ".");

			    IEnumerator<Object> applicationContexts = resourceManager.GetApplicationContexts ();
			    while(applicationContexts.MoveNext()) 
			    {
				    Assembly applicationContext = (Assembly)applicationContexts.Current;
				    String[] resourceNames = applicationContext.GetManifestResourceNames ();

				    bool containResource = false;
				    for(int j = 0;j < resourceNames.Length;j++)
				    {
					    if(resourceId.Equals(resourceNames[j], StringComparison.OrdinalIgnoreCase)) 
					    {
						    containResource = true;
						    break;
					    }
				    }

				    if(containResource) 
				    {
					    return applicationContext.GetManifestResourceStream (resourceId);
				    }
			    }

			    return null;
		    }

        #endif

        public static Stream ReadFile(String path, String name, int option)
        {
            #if XAMARIN
			StreamReader fileStream = null;
            #elif WINDOWS
            Stream fileStream = null;
            #endif

            #if XAMARIN
                IFolder folder;
            #elif WINDOWS
                StorageFolder folder;
            #endif

            folder = GetFolder(path, option);

            #if XAMARIN
            var fileTask = folder.GetFileAsync(name);
            #elif WINDOWS
            var fileTask = folder.GetFileAsync(name).AsTask();
            #endif

            fileTask.Wait();

            if (fileTask.Exception != null)
            {
                throw fileTask.Exception;
            }


            #if XAMARIN
                IFile file;
            #elif WINDOWS
                StorageFile file;
            #endif

            file = fileTask.Result;

            #if XAMARIN
            var readStreamTask = file.OpenAsync(FileAccess.Read);
            #elif WINDOWS
            var readStreamTask = file.OpenAsync(FileAccessMode.Read).AsTask();
            #endif

            readStreamTask.Wait();

            if (readStreamTask.Exception != null)
            {
                throw readStreamTask.Exception;
            }


            #if XAMARIN
                Stream readStream;
            #elif WINDOWS
                IRandomAccessStream readStream;
            #endif

            readStream = readStreamTask.Result;

            #if XAMARIN
                using (StreamReader reader = new System.IO.StreamReader (readStream))	
                {
                    fileStream = Utils.ToInputStream(reader.ReadToEnd());
                }

                return fileStream.BaseStream;
            #elif WINDOWS
                using (var reader = new DataReader(readStream))
                {
                    var readerallDataTask = reader.LoadAsync((uint)readStream.Size).AsTask();
                    readerallDataTask.Wait();

                    if (readerallDataTask.Exception != null)
                    {
                        throw readerallDataTask.Exception;
                    }


                    var buffer = new byte[(int)readStream.Size];
                    reader.ReadBytes(buffer);

                    fileStream = new MemoryStream(buffer);
                }

                return fileStream;
            #endif

        }


        #if XAMARIN
            public static IFolder GetFolder(String path, int option)
            {
			    path = path.Replace('/', '\\').TrimEnd('\\');

			    var storageFolder = FileSystem.Current.LocalStorage;
			    IFolder folder = storageFolder;

			    string[] folderNames = path.Split('\\');
			    for (int i = 0; i < folderNames.Length; i++)
			    {
				    if(folderNames[i].Length <= 0) 
				    {
					    continue;
				    }	

				    var folderTask = folder.CreateFolderAsync(folderNames[i], CreationCollisionOption.OpenIfExists);
				    folderTask.Wait ();

				    if (folderTask.Exception != null)
				    {
					    throw folderTask.Exception;
				    }

				    folder = folderTask.Result;
				
			    }

			    return folder;
            }
        #elif WINDOWS
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

        #endif


        public static void CreateFolder(String path, int option)
        {

        }

        public static void CreateFile(String path, String name, int option)
        {

            #if XAMARIN
                IFolder folder = GetFolder(path, option);

                var fileTask = folder.CreateFileAsync(name, CreationCollisionOption.FailIfExists, new CancellationTokenSource().Token);
                fileTask.Wait();
            #endif

        }
    }
}

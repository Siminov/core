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


#if __MOBILE__
#define XAMARIN
#endif

#if !__MOBILE__
#define WINDOWS
#endif


using Siminov.Core.Exception;
using Siminov.Core.Log;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Siminov.Core.Resource;

#if WINDOWS
using Windows.Foundation;
using Windows.Storage;
using Windows.Storage.Streams;
#endif


namespace Siminov.Core.Utils
{

    /// <summary>
    /// Exposes class util methods to SIMINOV. 
    /// </summary>
    public class ClassUtils
    {

        private static IDictionary<String, Type> types = new Dictionary<String, Type>();
        private static ResourceManager resourceManager = ResourceManager.GetInstance();


        /// <summary>
        /// Create a Class Object based on class name provided.
        /// </summary>
        /// <param name="className">Name of Class</param>
        /// <returns>Class Object</returns>
        public static Type CreateClass(String className)
        {
            var type = Type.GetType(className);
            if (type != null)
            {
                return type;
            }

            if (types.ContainsKey(className))
            {
                return types[className];
            }


            #if XAMARIN

                IEnumerator<Object> applicationContexts = resourceManager.GetApplicationContexts();
                while (applicationContexts.MoveNext())
                {
                    Assembly asm = (Assembly)applicationContexts.Current;

                    foreach (Type typeObject in asm.ExportedTypes)
                    {
                        if (typeObject.FullName.Equals(className))
                        {
                            return typeObject;
                        }
                    }
                }

                return Type.GetType(className);

            #elif WINDOWS

                var folder = Windows.ApplicationModel.Package.Current.InstalledLocation;

                var fileTask = folder.GetFilesAsync().AsTask();
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

                        AssemblyName name = new AssemblyName() { Name = file.DisplayName };
                        Assembly asm = Assembly.Load(name);

                        foreach (Type typeObject in asm.ExportedTypes)
                        {
                            if (typeObject.FullName.Equals(className))
                            {
                                return typeObject;
                            }
                        }
                    }
                }

                return Type.GetType(className);

            #endif

        }


        /// <summary>
        /// Creates class object based on full class name provided.
        /// </summary>
        /// <param name="className">Name of class</param>
        /// <returns>Object of class</returns>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while creating class object based on class name provided.</exception>
        public static Object CreateClassInstance(String className)
        {

            Type classObject = CreateClass(className);

            Object model = null;
            try
            {
                model = Activator.CreateInstance(classObject);
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "CreateClassInstance", "Exception caught while creating new instance of class, CLASS-NAME: " + className + ", " + exception.Message);
                throw new SiminovCriticalException(typeof(ClassUtils).FullName, "CreateClassInstance", "Exception caught while creating new instance of class, CLASS-NAME: " + className + ", " + exception.Message);
            }

            return model;
        }

        public static Object CreateClassInstance(String className, Type[] constructorParameters)
        {

            Type classObject = CreateClass(className);

            Object model = null;
            try
            {
                model = Activator.CreateInstance(classObject, constructorParameters);
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "CreateClassInstance", "Exception caught while creating new instance of class, CLASS-NAME: " + className + ", " + exception.Message);
                throw new SiminovCriticalException(typeof(ClassUtils).FullName, "CreateClassInstance", "Exception caught while creating new instance of class, CLASS-NAME: " + className + ", " + exception.Message);
            }

            return model;
        }



        /// <summary>
        /// Create a method object.
        /// </summary>
        /// <param name="className">Name of Class</param>
        /// <param name="methodName">Name of Method</param>
        /// <param name="pamameterTypes">Parameter Types</param>
        /// <returns>Method Object</returns>
        public static MethodInfo CreateMethodBasedOnClassName(String className, String methodName, Type[] pamameterTypes)
        {

            Object classObject = CreateClassInstance(className);
            return CreateMethodBasedOnClassInstance(classObject, methodName, pamameterTypes);
        }


        /// <summary>
        /// Create a method object.
        /// </summary>
        /// <param name="classObject">Class Object</param>
        /// <param name="methodName">Name of Method</param>
        /// <param name="parameterTypes">Parameter Types</param>
        /// <returns>Method Object</returns>
        public static MethodInfo CreateMethodBasedOnClassInstance(Object classObject, String methodName, Type[] parameterTypes)
        {

            MethodInfo method = null;

            try
            {
                method = classObject.GetType().GetTypeInfo().GetDeclaredMethod(methodName);
            }
            catch (NotImplementedException noSuchMethodException)
            {
                Log.Log.Debug(typeof(ClassUtils).FullName, "CreateMethodBasedOnClassInstance", "NoSuchMethodException caught while creating method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + noSuchMethodException.Message);

                /*
                 * Try For Primitive Data Type
                 */
                parameterTypes = ConvertToPrimitiveClasses(parameterTypes);
                try
                {
                    method = classObject.GetType().GetTypeInfo().GetDeclaredMethod(methodName);
                }
                catch (System.Exception exception)
                {
                    Log.Log.Error(typeof(ClassUtils).FullName, "CreateMethodBasedOnClassInstance", "Exception caught while creating method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                    throw new SiminovCriticalException(typeof(ClassUtils).FullName, "CreateMethodBasedOnClassInstance", "Exception caught while creating method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                }
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "CreateMethodBasedOnClassInstance", "Exception caught while creating method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                throw new SiminovCriticalException(typeof(ClassUtils).FullName, "CreateMethodBasedOnClassInstance", "Exception caught while creating method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
            }

            return method;
        }


        /// <summary>
        /// Get column values based on class object and method name provided.
        /// </summary>
        /// <param name="classObject">Class Object</param>
        /// <param name="methodNames">Name Of Methods</param>
        /// <returns>Column Values</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exception occur while getting column values.</exception>
        public static IEnumerator<Object> GetValues(Object classObject, IEnumerator<String> methodNames)
        {

            ICollection<Object> columnValues = new List<Object>();
            while (methodNames.MoveNext())
            {
                String methodName = methodNames.Current;
                MethodInfo method = (MethodInfo)CreateMethodBasedOnClassName(classObject.GetType().FullName, methodName, null);

                try
                {
                    columnValues.Add(method.Invoke(classObject, new Object[] { }));
                }
                catch (System.Exception exception)
                {
                    Log.Log.Error(typeof(ClassUtils).FullName, "GetValues", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                    throw new SiminovException(typeof(ClassUtils).FullName, "GetValues", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                }
            }

            return columnValues.GetEnumerator();
        }


        /// <summary>
        /// Get column value based on class object and method name.
        /// </summary>
        /// <param name="classObject">Class Object</param>
        /// <param name="methodName">Name Of Method</param>
        /// <returns>Column Value</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exception occur while getting column value.</exception>
        public static Object GetValue(Object classObject, String methodName)
        {

            MethodInfo method = (MethodInfo)CreateMethodBasedOnClassName(classObject.GetType().FullName, methodName, null);
            try
            {
                return method.Invoke(classObject, new Object[] { });
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "GetValue", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
                throw new SiminovException(typeof(ClassUtils).FullName, "GetValue", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + methodName + ", " + exception.Message);
            }
        }


        /// <summary>
        /// Invoke method based on class object, method name and parameter provided.
        /// </summary>
        /// <param name="classObject">Class Object</param>
        /// <param name="methodName">Name Of Method</param>
        /// <param name="parameterTypes">Type of parameters</param>
        /// <param name="parameters">Parameters To Method</param>
        /// <returns>Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exception occur while invoking method.</exception>
        public static Object InvokeMethod(Object classObject, String methodName, Type[] parameterTypes, Object[] parameters)
        {

            MethodInfo method = (MethodInfo)CreateMethodBasedOnClassInstance(classObject, methodName, parameterTypes);
            return InvokeMethod(classObject, method, parameters);
        }

        public static Object InvokeMethod(Object classObject, MethodInfo method, Object[] parameters)
        {

            try
            {
                return method.Invoke(classObject, parameters);
            }
            catch (TargetInvocationException invocationTargetException)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "InvokeMethod", "InvocationTargetException caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + method.Name + ", " + invocationTargetException.Message);
                throw new SiminovException(invocationTargetException.Source, "InvokeMethod", invocationTargetException.Message);
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(ClassUtils).FullName, "InvokeMethod", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + method.Name + ", " + exception.Message);
                throw new SiminovException(typeof(ClassUtils).FullName, "InvokeMethod", "Exception caught while getting return value from method, CLASS-NAME: " + classObject.GetType().FullName + ", METHOD-NAME: " + method.Name + ", " + exception.Message);
            }
        }


        /// <summary>
        /// Get new object created and filled with values provided.
        /// </summary>
        /// <param name="className">Class Name</param>
        /// <param name="data">Column Values</param>
        /// <returns>Class Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exception occur while create and inflating class object.</exception>
        public static Object CreateAndInflateObject(String className, IDictionary<String, Object> data)
        {

            Object model = CreateClassInstance(className);

            ICollection<String> methodNames = data.Keys;
            IEnumerator<String> methodNamesIterate = methodNames.GetEnumerator();

            while (methodNamesIterate.MoveNext())
            {
                String methodName = methodNamesIterate.Current;
                Object methodParameter = data[methodName];

                if (methodParameter == null)
                {
                    continue;
                }

                InvokeMethod(model, methodName, new Type[] { methodParameter.GetType() }, new Object[] { methodParameter });
            }


            return model;
        }


        private static Type[] ConvertToPrimitiveClasses(Type[] classes)
        {

            ICollection<Type> convertedClasses = new HashSet<Type>();
            foreach (Type classObject in classes)
            {
                if (classObject.GetType().FullName.Equals(typeof(Double).FullName, StringComparison.OrdinalIgnoreCase))
                {
                    convertedClasses.Add(typeof(double));
                }
                else if (classObject.GetType().FullName.Equals(typeof(Boolean).FullName, StringComparison.OrdinalIgnoreCase))
                {
                    convertedClasses.Add(typeof(bool));
                }
                else
                {
                    convertedClasses.Add(classObject);
                }
            }


            Type[] convertedArrayClasses = new Type[convertedClasses.Count];
            IEnumerator<Type> convertedClassesItr = convertedClasses.GetEnumerator();

            int count = 0;
            while (convertedClassesItr.MoveNext())
            {
                convertedArrayClasses[count++] = convertedClassesItr.Current;
            }

            return convertedArrayClasses;
        }

    }
}

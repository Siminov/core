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



using Siminov.Core.Reader;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace Siminov.Core.Reader
{


    /// <summary>
    /// Exposes common methods which are required by every SIMINOV parsers. 
    /// </summary>
    public abstract class SiminovSAXDefaultHandler
    {


        /// <summary>
        /// Call this method to parse input stream.
        /// </summary>
        /// <param name="inputStream">Input Stream</param>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while parsing input stream.</exception>
        public void ParseMessage(Stream inputStream)
        {
            if (inputStream == null)
            {
                Log.Log.Error(typeof(SiminovSAXDefaultHandler).FullName, "ParseMessage", "Invalid InputStream Found.");
                throw new SiminovException(typeof(SiminovSAXDefaultHandler).FullName, "ParseMessage", "Invalid InputStream Found.");
            }

            XmlReader reader = XmlReader.Create(inputStream);

            while (reader.Read())
            {
                switch (reader.NodeType)
                {
                    case XmlNodeType.Element:
                        IDictionary<String, String> attributes = GetAttributes(reader);
                        StartElement(reader, attributes);
                        if (reader.IsEmptyElement)
                        {
                            EndElement(reader.Name);
                        }

                        break;
                    case XmlNodeType.Text:
                        Characters(reader.Value);
                        break;
                    case XmlNodeType.XmlDeclaration:
                        break;
                    case XmlNodeType.ProcessingInstruction:
                        break;
                    case XmlNodeType.Comment:
                        break;
                    case XmlNodeType.EndElement:
                        EndElement(reader.Name);
                        break;
                }
            }
        }


        public IDictionary<string, string> GetAttributes(XmlReader reader)
        {
            IDictionary<string, string> attributes = new Dictionary<string, string>();
            for (int i = 0; i < reader.AttributeCount; i++)
            {
                reader.MoveToAttribute(i);

                if (!reader.HasValue)
                {
                    continue;
                }

                attributes.Add(reader.Name, reader.Value);
            }

            reader.MoveToElement();
            return attributes;
        }

        public abstract void StartElement(XmlReader reader, IDictionary<String, String> attributes);

        public abstract void Characters(String value);

        public abstract void EndElement(String localName);

    }

}

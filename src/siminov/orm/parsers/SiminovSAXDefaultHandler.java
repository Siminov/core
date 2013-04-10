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

package siminov.orm.parsers;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import siminov.orm.exception.SiminovException;
import siminov.orm.exception.PrematureEndOfParseException;
import siminov.orm.log.Log;


/**
 * Exposes common methods which are required by every SIMINOV parsers. 
 */
public class SiminovSAXDefaultHandler extends DefaultHandler {

	/**
	 * Call this method to parse input stream.
	 * @param inputStream Input Stream.
	 * @throws SiminovException If any exception occur while parsing input stream.
	 */
	public void parseMessage(final InputStream inputStream) throws SiminovException {
		if(inputStream == null) {
			Log.loge(getClass().getName(), "parseMessage", "Invalid InputStream Found.");
			throw new SiminovException(getClass().getName(), "parseMessage", "Invalid InputStream Found.");
		}
		
		SAXParserFactory saxParserFactory = null;
		
		try {
			saxParserFactory = SAXParserFactory.newInstance();			
		} catch(FactoryConfigurationError factoryConfigurationError) {
			Log.logd(getClass().getName(), "parseMessage", "FactoryConfigurationError caught while creating new instance of sax parser factory.");
			throw new SiminovException(getClass().getName(), "parseMessage", "FactoryConfigurationError caught while creating new instance of sax parser factory, " + factoryConfigurationError.getMessage());
		}

		SAXParser saxParser = null;
		
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch(ParserConfigurationException parserConfigurationException) {
			Log.loge(getClass().getName(), "parseMessage", "ParserConfigurationException caught while creating new instance of sax parser.");
			throw new SiminovException(getClass().getName(), "parseMessage", "ParserConfigurationException caught while creating new instance of sax parser, " + parserConfigurationException.getMessage());
		} catch(SAXException saxException) {
			Log.loge(getClass().getName(), "parseMessage", "SAXException caught while creating new instance of sax parser.");
			throw new SiminovException(getClass().getName(), "parseMessage", "SAXException caught while creating new instance of sax parser, " + saxException.getMessage());
		}
		
		try {
			saxParser.parse(inputStream, this);			
		} catch(IllegalArgumentException illegalArgumentException) {
			Log.loge(getClass().getName(), "parserMessage", "IllegalArgumentException caught while parsing input stream, " + illegalArgumentException.getMessage());
			throw new SiminovException(getClass().getName(), "parseMessage", "IllegalArgumentException caught while parsing input stream, " + illegalArgumentException.getMessage());
		} catch(IOException ioException) {
			Log.loge(getClass().getName(), "parserMessage", "IOException caught while parsing input stream, " + ioException.getMessage());
			throw new SiminovException(getClass().getName(), "parseMessage", "IOException caught while parsing input stream, " + ioException.getMessage());
		} catch(PrematureEndOfParseException prematureEndOfParseException) {
			Log.logi(getClass().getName(), "parserMessage", "PrematureEndOfParseException caught while parsing input stream, " + prematureEndOfParseException.getMessage());
		} catch(SAXException saxException) {
			Log.loge(getClass().getName(), "parserMessage", "SAXException caught while parsing input stream, " + saxException.getMessage());
			throw new SiminovException(getClass().getName(), "parserMessage", "SAXException caught while parsing input stream, " + saxException.getMessage());
		} catch(Exception exception) {
			Log.loge(getClass().getName(), "parserMessage", "Exception caught while parsing input stream, " + exception.getMessage());
			throw new SiminovException(getClass().getName(), "parserMessage", "Exception caught while parsing input stream, " + exception.getMessage());
		}
	}

}

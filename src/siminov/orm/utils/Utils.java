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

package siminov.orm.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.resource.Resources;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;


/**
 * Exposes utility methods which can be used by both SIMINOV/Application. 
 */
public class Utils {

	/**
	 * Get string from input stream.
	 * @param inputStream Input Stream.
	 * @return String
	 * @throws SiminovException If any exception occur while getting string from input stream.
	 */
	public static String toString(final InputStream inputStream) throws SiminovException {
		if(inputStream == null) {
			Log.loge(Utils.class.getName(), "getString", "Invalid InputStream Found.");
			throw new SiminovException(Utils.class.getName(), "getString", "Invalid InputStream Found.");
		}
		
		byte[] bytes = new byte[1000];

		StringBuilder x = new StringBuilder();
		int numRead = 0;
		
		try {
			while ((numRead = inputStream.read(bytes)) >= 0) {
			    x.append(new String(bytes, 0, numRead));
			}
		} catch(Exception exception) {
			Log.loge(Utils.class.getName(), "getString", "Exception caught while getting string from inputstream, " + exception.getMessage());
		}

		try {
			inputStream.close();			
		} catch(Exception exception) {
			Log.loge(Utils.class.getName(), "getString", "Exception caught while closing inputstream, " + exception.getMessage());
		}
		
		return x.toString();
	}
	
	/**
	 * Get input stream from string.
	 * @param string String.
	 * @return Input Stream.
	 * @throws SiminovException If any exception occur while getting input stream from string.
	 */
	public static InputStream toInputStream(final String string) throws SiminovException {
		if(string == null || string.length() <= 0) {
			Log.loge(Utils.class.getName(), "getInputStream", "Invalid String Found.");
			throw new SiminovException(Utils.class.getName(), "getInputStream", "Invalid String Found.");
		}
		
		byte[] bytes = null;
		
		try {
			bytes = string.getBytes("UTF-8");
		} catch(Exception exception) {
			Log.loge(Utils.class.getName(), "getInputStream", "Exception caught while getting bytes from string, " + exception.getMessage());
			throw new SiminovException(Utils.class.getName(), "getInputStream", "Exception caught while getting bytes from string, " + exception.getMessage());
		}
		
		 return toInputStream(bytes);
	}
	
	/**
	 * Get input stream from string.
	 * @param bytes Byte Array.
	 * @return Input Stream.
	 * @throws SiminovException If any exception occur while getting input stream from byte array.
	 */
	public static InputStream toInputStream(final byte[] bytes) throws SiminovException {
		if(bytes == null || bytes.length <= 0) {
			Log.loge(Utils.class.getName(), "getInputStream", "Invalid ByteArray Found.");
			throw new SiminovException(Utils.class.getName(), "getInputStream", "Invalid ByteArray Found.");
		}
		
		return new ByteArrayInputStream(bytes);
	}
	
	/**
	 * Get generated unique id.
	 * @return Long Unique Id.
	 */
	public static Long generateUniqueId() {
		return new Random().nextLong();
	}
	
	/**
	 * Check whether application is running on emulator or not.
	 * @return TRUE: If application is running on emulator, FALSE: If application is not running on emulator.
	 */
	public static boolean isEmulator() {
		return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
	}
	
	/**
	 * Check whether device have network coverage or not.
	 * @return TRUE: If network coverage if there, FALSE: If network coverage is not there.
	 */
	public static boolean hasCoverage() {
		Context context = Resources.getInstance().getApplicationContext();
		
		final ConnectivityManager conMgr =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
		    return true;
		} else {
		    return false;
		}
	}
}

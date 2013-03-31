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

package siminov.orm.log;

import android.os.Build;

public abstract class Log {

	private static String TAG = "SIMINOV";
	
	/**
	 * Log info messages.
	 * @param className Class Name.
	 * @param methodName Method Name.
	 * @param message Message.
	 */
	public static void logi(final String className, final String methodName, final String message) {
		
		if(isEmulator()) {
			System.out.print(prepareMessage(className, methodName, message));
		}

		android.util.Log.i(TAG, prepareMessage(className, methodName, message));
	}

	/**
	 * Log error messages.
	 * @param className Class Name.
	 * @param methodName Method Name.
	 * @param message Message.
	 */
	public static void loge(final String className, final String methodName, final String message) {
		
		if(isEmulator()) {
			System.out.print(prepareMessage(className, methodName, message));
		}

		android.util.Log.e(TAG, prepareMessage(className, methodName, message));
	}

	/**
	 * Log debug messages.
	 * @param className Class Name.
	 * @param methodName Method Name.
	 * @param message Message.
	 */
	public static void logd(final String className, final String methodName, final String message) {
		
		if(isEmulator()) {
			System.out.print(prepareMessage(className, methodName, message));
		}

		android.util.Log.d(TAG, prepareMessage(className, methodName, message));
	}

	private static String prepareMessage(final String className, final String methodName, final String message) {
		return "Class Name: " + className + ", Method Name: " + methodName + ", Message: " + message;
	}
	
	private static boolean isEmulator() {
		return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
	}
}

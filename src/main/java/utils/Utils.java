/*
 * This file is part of ImageServer.
 *
 * ImageServer is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * ImageServer is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * ImageServer. If not, see <https://www.gnu.org/licenses/>. 
 */

package utils;

import java.util.Arrays;


/**
 * Mostly static helper functions.
 */
public class Utils {
	private static final String whoAmI = Utils.class.getName();
	
	/**
	 * Check to see if given string ends with any of a given list of ending strings.
	 * <p>
	 * @param str String to be checked.
	 * @param extensions List of Strings of endings to be checked.
	 * <p>
	 * @return
	 */
    public static boolean isEndWith(String str, String[] endings) {
    	return Arrays.stream(endings).anyMatch(str::endsWith);
    }
}

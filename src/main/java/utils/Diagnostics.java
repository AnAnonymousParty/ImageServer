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

import java.util.logging.*;

/**
 * Class that provides diagnostic utility functions for debugging, logging, etc.
 */
public class Diagnostics {
	private Logger logger = null;;	
		
	/**
	 * Constructor, given:
	 * <p>
	 * String containing the name of the servlet.
	 */
	public Diagnostics(String className) {
        logger = Logger.getLogger(className);
        
        logger.setLevel(Level.ALL);		
	}
	
	/**
	 * Log exception.
	 * <p>
	 * @param moduleName String containing name of module.
	 * @param subName String containing name of function where exception occurred.
	 * @param exception String containing exception details.
	 * @param extra String containing optional amplifying information.
	 * @param exit true = function will exit. false = function will continue.
	 */
	public void exceptional(String moduleName, String subName, String exception, String extra, boolean exit) {
		logger.log(Level.INFO, String.format("%s.%s %s() Exception: %s%s.", 
				true == exit ? "< " : "",
				moduleName, subName, 
				exception, 
				(true == extra.isEmpty() ? "" : (" - " + extra))));		
	}
	
	/**
	 * Log bad enumeration.
	 * <p>
	 * @param subName String containing name of function where bad enumeration occurred.
	 * @param enumTypeName String containing name of the enumerated type.
	 * @param badValue The offending value.
	 */	
	public void logBadEnum(String moduleName, String subName, String enumTypeName, Integer badValue) {
		logger.log(Level.INFO, String.format("Encountered unrecognized enumerated value %d for %s in %s.%s.", 
				badValue, 
				enumTypeName, 
				moduleName, subName));			
	}
	
	/**
	 * Log sub call.
	 * <p>
	 * @param moduleName String containing name of module.
	 * @param subName String containing name of function.
	 * @param subParms String containing parameter value(s).
	 * @param extra String containing optional amplifying information.
	 */		
	public void logSubCall(String moduleName, String subName, String subParms, String extra) {
		logger.log(Level.INFO, String.format("> %s.%s%s%s.", 
				moduleName, subName, 
				(true == subParms.isEmpty() ? "()" : ("(" + subParms +")")),
				(true == extra.isEmpty() ? "" : (" " + extra))));		
	}
	
	/**
	 * Log sub exit.
	 * <p>
	 * @param moduleName String containing name of module.
	 * @param subName String containing name of function.
	 * @param retVal String containing return value(s).
	 * @param extra String containing optional amplifying information.	
	 */
	public void logSubExit(String moduleName, String subName, String retVal, String extra) {
		logger.log(Level.INFO, String.format("< %s.%s()%s%s.", 
				moduleName, subName, 
				(true == retVal.isEmpty() ? "" : (" [" + retVal +"]")),
				(true == extra.isEmpty() ? "" : (" " + extra))));		
		
	}

	/**
	 * Log sub info.
	 * <p>
	 * @param moduleName String containing name of module.
	 * @param subName String containing name of function.
	 * @param message String containing message to be logged.
	 * @param extra String containing optional amplifying information.	
	 */	
	public void logSubInfo(String moduleName, String subName, String message, String extra) {
		logger.log(Level.INFO, String.format("  %s.%s%s%s.", 
				moduleName, subName, 
				(true == message.isEmpty() ? "" : (": " + message)),
				(true == extra.isEmpty() ? "" : (" - " + extra))));
	}		
}
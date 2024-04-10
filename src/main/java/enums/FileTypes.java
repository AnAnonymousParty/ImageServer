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

package enums;

/**
 * Identifies file as image or video.
 */
public enum FileTypes {
 IMAGE,
 UNKNOWN,
 VIDEO;

 
 @Override
 /**
  * Get descriptive string for enumerated value.
  * <p>
  * @return String describing enumerated value.
  */
 public String toString() {
     switch (this) {
         case IMAGE: {
          return "Image";
         }
         
         case UNKNOWN: {
          return "Unknown";          
         }
         
         case VIDEO: {
          return "Video";          
         }
            
         default: {
          return "Undefined";
         }         
     }
 }
 
 /**
  * Given a file path/name, determine its type based on its extension.
  * <p>
  * @param String containing the file path/name.
  * <p>
  * @return File type (enumerated).
  */
 public static FileTypes fromFilePathName(String filePathName) {
  String lc = filePathName.toLowerCase();
  
  for (;;) {
   if (true == lc.endsWith(".gif")) {
    return IMAGE;
   }   
   
   if (true == lc.endsWith(".jpg")) {
    return IMAGE;
   }
   
   if (true == lc.endsWith(".jpeg")) {
    return IMAGE;
   }     
   
   if (true == lc.endsWith(".mpg")) {
    return VIDEO;
   }   
   
   if (true == lc.endsWith(".mpeg")) {
    return VIDEO;
   }    
   
   if (true == lc.endsWith(".mp4")) {
    return VIDEO;
   }   
   
   if (true == lc.endsWith(".png")) {
    return IMAGE;
   }   
   
   if (true == lc.endsWith(".tiff")) {
    return IMAGE;
   }    
   
   break;
  }
  
  return UNKNOWN;
 }
}


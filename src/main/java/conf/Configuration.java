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

package conf;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.Diagnostics;

/**
 * Container of configuration information and functions to load/save them to the
 * xml configuration file: <server root>\webapps\ImageServer\conf\Conf.xml.
 */
public class Configuration {
	private static final String whoAmI = Configuration.class.getName();	
	
	private Diagnostics diags = null;
	
	private Document xmlDoc = null;
	
	private File confFile = null;
	
	private Integer delay;
	
	private String confFilePathName;	
	private String deletedImagesDirectory;	
	private String imagesDirectory;
	
	
	/**
	 * Constructor, given:
	 * <p>
	 * @param diags Diagnostics object to be used for logging.
	 * @param cofFilePathName String containing pathe/name of configuration file.
	 */	
	public Configuration(Diagnostics diags, String confFilePathName) {
		this.confFilePathName = confFilePathName;
		this.diags = diags;

		confFile = new File(confFilePathName);
		
		delay = 5;
		
		deletedImagesDirectory = "C://DeletedPictures";
		
		imagesDirectory = "C://Pictures";
	}
	
	/** 
	 * Get the configured delay value.
	 * <p>
	 * @return Delay value.
	 */
	public Integer getDelay() {
		return delay;
	}
	
	/** 
	 * Get the deleted images directory name.
	 * <p>
	 * @return Deleted Images directory name.
	 */	
	public String getDeletedImagesDirectory() {
		return deletedImagesDirectory;
	}
	
	/** 
	 * Get the configured images directory name.
	 * <p>
	 * @return Images directory name.
	 */	
	public String getDirectory() {
		return imagesDirectory;
	}
	
	/**
	 * Load the configuration values from the xml configuration file.
	 */
	public void loadConfiguration() {
        diags.logSubCall(whoAmI, "loadConfiguration", "", "");		
		
		DocumentBuilder docBuilder = null;
		
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			diags.exceptional(whoAmI, "loadConfiguration", e.getMessage(), "", true);
	        
	        return;
		}
		
		try {
			xmlDoc = docBuilder.parse(confFile);
		} catch (SAXException e) {
			diags.exceptional(whoAmI, "loadConfiguration", e.getMessage(), "", true);	
	        
	        return;
		} catch (IOException e) {
			diags.exceptional(whoAmI, "loadConfiguration", e.getMessage(), "", true);
	        
	        return;
		}
		
		xmlDoc.getDocumentElement().normalize();	
		
		NodeList list = xmlDoc.getElementsByTagName("Delay");
        
        delay = Integer.parseInt(list.item(0).getTextContent());
        
        list = xmlDoc.getElementsByTagName("ImagesDirectory");
        
        imagesDirectory = list.item(0).getTextContent();        
        
        list = xmlDoc.getElementsByTagName("DeletedImagesDirectory");
        
        deletedImagesDirectory = list.item(0).getTextContent();
      		
        diags.logSubExit(whoAmI, "loadConfiguration", "", "");
	}
	
	/**
	 * Save the configuration values to the xml configuration file.
	 */	
	public void saveConfiguration() {
        diags.logSubCall(whoAmI, "saveConfiguration", "", "");             
        
        if (null == xmlDoc) {
            diags.logSubExit(whoAmI, "saveConfiguration", "", "xmlDoc is null");
        	
        	return;  // TODO: Replace this with code to create doc and initialize it.
        }
                  
        DOMSource source = new DOMSource(xmlDoc); 
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance(); 

		try {
			Transformer transformer = transformerFactory.newTransformer();
			
	        try {
	            StreamResult result = new StreamResult(confFilePathName); 
	        	
				transformer.transform(source, result);
			} catch (TransformerException e) {
				diags.exceptional(whoAmI, "saveConfiguration", e.getMessage(), "", true);
			}			
		} catch (TransformerConfigurationException e) {
			diags.exceptional(whoAmI, "saveConfiguration", e.getMessage(), "", true);
		} 		
		
        diags.logSubExit(whoAmI, "saveConfiguration", "", "");
	}
	
	/**
	 * Set the configured delay value.
	 * <p>
	 * @param delay The Delay value.
	 */
	public void setDelay(Integer delay) {
		this.delay = delay;
		
		NodeList list = xmlDoc.getElementsByTagName("Delay");
		 
		list.item(0).setTextContent(delay.toString());
	}
	
	/**
	 * Set the deleted images directory name.
	 * <p>
	 * @param deletedImagesDirectory The deleted images directory path/name.
	 */	
	public void setDeletedImagesDirectory(String deletedImagesDirectory) {
		this.deletedImagesDirectory = deletedImagesDirectory;
		
		NodeList list = xmlDoc.getElementsByTagName("DeletedImagesDirectory");
		 
		list.item(0).setTextContent(deletedImagesDirectory);
	}	
	
	/**
	 * Set the configured images directory name.
	 * <p>
	 * @param imagesDirectory The images directory path/name.
	 */	
	public void setDirectory(String imagesDirectory) {
		this.imagesDirectory = imagesDirectory;
		
		NodeList list = xmlDoc.getElementsByTagName("ImagesDirectory");
		 
		list.item(0).setTextContent(imagesDirectory);
	}
}
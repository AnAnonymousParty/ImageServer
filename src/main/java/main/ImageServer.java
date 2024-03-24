/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>. 
 */

package main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import conf.Configuration;
import containers.GetRandomFileResult;
import utils.Diagnostics;
import utils.Utils;

/**
 * Servlet implementation class, a kind of HttpServlet.
 */
public class ImageServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String whoAmI = ImageServer.class.getName();
	
	private Configuration conf = null;
	
	private Diagnostics diags = null;
	
	private String url;
	
       
 /**
  * Constructor (default).
  */
 public ImageServer() {
  super();
  
  diags = new Diagnostics(whoAmI);
  
  String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
  
  absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/ImageServer"));
  
  String decodedStr = null;
        
		try {
			decodedStr = java.net.URLDecoder.decode(absolutePath, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			diags.exceptional(whoAmI, "ImageServer", e.getMessage(), "", false);
			
	  diags.logSubExit(whoAmI, "ImageServer", "", ""); 
			
			return;
		}
        
  diags.logSubCall(whoAmI, "ImageServer", decodedStr, "");
 
  conf = new Configuration(diags, decodedStr + "\\ImageServer\\conf\\Conf.xml" );
 
  conf.loadConfiguration();
           
  diags.logSubExit(whoAmI, "ImageServer", "", "");        
 }

	/**
	 * Handle html get request.
	 * <p>
	 * @param request  HttpServletRequest object.
	 * @param response HttpServletResponse object.
	 */
  @Override
	 protected void doGet(HttpServletRequest  request, 
			                    HttpServletResponse response) throws IOException,
	                                                    ServletException {		
		diags.logSubCall(whoAmI, "doGet", (null != request.getQueryString() ? request.getQueryString() : ""), "");
		
		url = request.getRequestURL().toString();
		
		String uri = request.getRequestURI();
				
		url = url.replaceFirst(uri, "");
		
		diags.logSubInfo(whoAmI, "doGet", url, "");		
		
		conf.loadConfiguration();
		
		String action = request.getParameter("action");
				
		for (;;) {
			if (null == action) {
				showMainPage(response);
				
				break;
			}		
			
			if (true == action.isEmpty()) {				
				showMainPage(response);
				
				break;
			}		
			
			if (true == action.equals("config")) {
				handleConfigAction(request, response);
				
				break;
			}	
			
			if (true == action.equals("delete")) {
				handleDeleteAction(request, response);
				
				break;
			}			
			
			if (true == action.equals("getDefaultImage")) {
				getDefaultImage(response);	        	
				
				break;
			}
			
			if (true == action.equals("getNamedImage")) {
				getNamedImage(request, response);
				
				break;
			}				
			
			if (true == action.equals("getRandomImage")) {
				getRandomImage(response);
				
				break;
			}	
			
			if (true == action.equals("reqConfig")) {
				showConfigPage(request, response);
				
				break;
			}			
						
			break;
		}
		
		diags.logSubExit(whoAmI, "doGet", "", "");		
	}

	/**
	 * Handle html post request.
	 * <p>
	 * @param request  HttpServletRequest object.
	 * @param response HttpServletResponse object.
	 */
    @Override
	protected void doPost(HttpServletRequest request, 
			                    HttpServletResponse response) throws IOException,
	                                                           ServletException {	
		diags.logSubCall(whoAmI, "doPost", request.toString(), "");
		
		conf.loadConfiguration();
		
		String action = request.getParameter("action");
		
		diags.logSubExit(whoAmI, "doPost", action, "");	
		
		if (null == action) {		
			doGet(request, response);
		}
		
		if (true == action.isEmpty()) {		
			doGet(request, response);
		}		
		
		for(;;) {
			if (true == action.equals("reqConfigPage")) {				
				showConfigPage(request, response);
				
				break;
			}			
			
			if (true == action.equals("configure")) {				
				handleConfigAction(request, response);
				
				break;
			}
				
			doGet(request, response);	
			
			break;
		}
				
		diags.logSubExit(whoAmI, "doPost", "", "");		
	}
	
	/**
	 * Generate main page html.
	 * <p>
	 * @param response HttpServletResponse object.
	 * @param pathFileNameStr String containing path/name of image file to be displayed.
	 */	
	private void generatePageHtml(HttpServletResponse response) {
		diags.logSubCall(whoAmI, "generatePageHtml", response.toString(), "");			
		
		PrintWriter printWriter = null;;
		
		try {
			printWriter = response.getWriter();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "generatePageHtml", e.getMessage(), "", true);
			
			return;
		}	
		
		response.setContentType("text/html");
		
  GetRandomFileResult result = getRandomImagePathFileName();		
		
		String filePathNameStr = result.getRandomPathFileName();
  String filePathNameEncStr = "";
        
  try {
			filePathNameEncStr = java.net.URLEncoder.encode(filePathNameStr, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			diags.exceptional(whoAmI, "showDefaultScreen", e.getMessage(), "", true);	
			
			return;
		}
		
		printWriter.println("<!DOCTYPE html>");
		printWriter.println("<html>");
  printWriter.println(" <head>");
  printWriter.println("  <link href=\"css/Styles.css\" rel=\"stylesheet\">");
  printWriter.println("  <script src=\"javascript/common.js\"></script>");
  printWriter.println(" </head>");        
  printWriter.println(" <body class=\"mainpagebodystyle\" id=\"MainPage\" onLoad=\"InitializePage();\">");
        
  // Conditionally displayed when the user clicks the screen: 
        
	    printWriter.println("  <div class=\"popup\" id=\"ControlsContainer\">");
		   printWriter.println("   <form id=\"ConfigForm\" method=\"post\" name=\"ConfigForm\">");     
			  printWriter.println("    <input id=\"action\" name=\"action\" type=\"hidden\" value=\"placeholder\">");	 // Page javascript will modify this via button onclick handler.	
			  printWriter.println("    <input id=\"availableFiles\" name=\"availableFiles\" type=\"hidden\" value=\"" + result.getAvailableImages().toString() + "\">");			
			  printWriter.println("    <input id=\"filePathName\" name=\"filePathName\" type=\"hidden\" value=\"" + filePathNameStr + "\">");			
			  printWriter.println("    <input id=\"serverUrl\" name=\"serverUrl\" type=\"hidden\" value=\"" + url + "\">");	
     printWriter.println("    <div>");
	    printWriter.println("     <img height=\"30px;\" id=\"SettingsImage\" onclick=\"RequestConfigPage();\" src=\"images\\GearCyan.png\" style=\"vertical-align: middle;\" width=\"30px;\">");
     printWriter.println("    </div>");
     printWriter.println("    <div>");
     printWriter.println("     <span>");
	    printWriter.println("      <label class=\"controllabel\" for=\"requestedFilePathName\">File:</label>");
	    printWriter.println("      <input class=\"controltextbox\" id=\"requestedFilePathName\" name=\"requestedFilePathName\" readonly size=\"64\" type=\"text\" value=\"" + filePathNameStr + "\" />");
	    printWriter.println("      <input class=\"controlbutton\" id=\"deleteBtn\" name=\"deleteBtn\" onclick=\"DoDelete();\" type=\"button\" value=\"Delete\" />");
     printWriter.println("      <input class=\"controlbutton\" id=\"nextBtn\" name=\"nextBtn\" onclick=\"DoNext();\" type=\"button\" value=\"Next Picture\" />"); 	    
     printWriter.println("     </span>");
     printWriter.println("    </div>");
     printWriter.println("    <div>");
     printWriter.println("     <span>");     
	    printWriter.println("      <label class=\"controllabel\" for=\"requestedDelay\">Delay (5-60 seconds):</label>");
	    printWriter.println("      <input class=\"controltextbox\" id=\"requestedDelay\" name=\"requestedDelay\" max=\"60\" min=\"5\" size=\"3\" type=\"number\" value= \"" + conf.getDelay().toString() + "\"/>");
	    printWriter.println("      <input class=\"controlbutton\" id=\"applyBtn\" name=\"applyBtn\" onclick=\"SubmitConfig();\" type=\"button\" value=\"Apply\" />");
     printWriter.println("     </span>");
     printWriter.println("    </div>");
			  printWriter.println("   </form>");        
	    printWriter.println("  </div>");      
        
	  printWriter.println("  <div id=\"ImageContainer\" style=\"align-items: center; display: flex; height: 98vh; justify-content: center; overflow: hidden; width: 100vw; \">");
   printWriter.println("   <img id=\"DispImage\" src=\"" + url + "/ImageServer/ImageServer?action=getNamedImage&requestedFilePathName=" + filePathNameEncStr + "\" style=\"height: 100vh; object-fit: scale-down; overflow: hidden; width=100vw;\">");
   printWriter.println("  </div>");   
        
   // Conditionally displayed if there are no images available:
        
	     printWriter.println("  <div id=\"ErrorContainer\" style=\"align-items: center; background-color: lightblue; display: none; height: 100vh; justify-content: center; overflow: hidden; visibility: collapse; width: 100vw;\">");
	     printWriter.println("   There are no images available to display in the configured images directory:<br />");
	     printWriter.println("   " + conf.getDirectory() + ".<br /><br />");    
	     printWriter.println("   Click anywhere on the page to reveal the control panel<br />");      
	     printWriter.println("   and use the GEAR icon to access the configuration page<br />");
	     printWriter.println("   to enter the appropriate directory values.");            
	     printWriter.println("  </div>"); 
        
   printWriter.println(" </body>");
   printWriter.println("</html>");
        
   printWriter.close();	
        
		diags.logSubExit(whoAmI, "generatePageHtml", "", "");	
	}	
		
	/**
	 * Get list of all available image files.
	 * <p>
	 * @param directory String containing directory name.
	 * <p>.
	 * @return List of file names, may be empty.
	 */	
	private List<Path> getFilesList(String directory) {
		diags.logSubCall(whoAmI, "getFilesList", "", "");	
		
  Path path = Paths.get(directory);
  
  String[] extensions = {"gif", "jpg", "jpeg", "png", "tiff"};

  List<Path> result = new ArrayList<Path>();
  
  try (Stream<Path> walk = Files.walk(path)) {
      result = walk.filter(Files::isRegularFile)
      		     .filter(f -> Utils.isEndWith(f.toString(), extensions))
      			 .collect(Collectors.toList());
  } catch (IOException e) {
			diags.exceptional(whoAmI, "getFilesList", e.getMessage(), "", false);
		}        
        
		diags.logSubCall(whoAmI, "getFilesList", "", String.format("Available files = %d", result.size()));
        
        return result;
	}
	
	/**
	 * Get a random image.
	 * <p>
	 * @param response HttpServletResponse object.
	 */		
	private void getDefaultImage(HttpServletResponse response) {
		diags.logSubCall(whoAmI, "getDefaultImage", "", "");	
        
  ServletOutputStream outStream = null;
  
		try {
			outStream = response.getOutputStream();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}
              
  response.setContentType("image/jpeg");        	

  String imgFile = "webapps\\ImageServer\\images\\default.jpg";
  
  FileInputStream fin = null;
        
		try {
			fin = new FileInputStream(imgFile);
		} catch (FileNotFoundException e) {
			diags.exceptional(whoAmI, "getDefaultImage", e.getMessage(), "", false);
		}

  BufferedInputStream bin = new BufferedInputStream(fin);
  BufferedOutputStream bout = new BufferedOutputStream(outStream);
  
  int ch = 0;
  
  try {
			while(-1 != (ch = bin.read()))
			 bout.write(ch);
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getDefaultImage", e.getMessage(), "", false);
		}

  try {
			bin.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getDefaultImage", e.getMessage(), "", false);
		}
  
 try {
			fin.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getDefaultmage", e.getMessage(), "", false);
		}
 
 try {
			bout.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getDefaultImage", e.getMessage(), "", false);
		}
 
 try {
			outStream.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getDefaultImage", e.getMessage(), "", false);
		}	            
        	                
		diags.logSubExit(whoAmI, "getDefaultImage", "", "");	
	}	
	
	/**
	 * Get an image, given its path/name.
	 * <p>
	 * @param response HttpServletResponse object.
	 */		
	private void getNamedImage(HttpServletRequest request, HttpServletResponse response) {
		diags.logSubCall(whoAmI, "getNamedImage", "", "");	      
        
		String filePathName = request.getParameter("requestedFilePathName");
		
		if (null == filePathName) {
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "getNamedImage", "", "");	
			
			return;
		}
		     
  response.setContentType("image/jpeg");
  
  FileInputStream fin = null;
        
		try {
			fin = new FileInputStream(filePathName);
		} catch (FileNotFoundException e) {
			try {
				fin = new FileInputStream("webapps\\ImageServer\\images\\noImage.jpg");
			} catch (FileNotFoundException e1) {
				diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
				
				showMainPage(response);
			}			
		}

  BufferedInputStream bin   = new BufferedInputStream(fin);
  
  ServletOutputStream outStream = null;
        
		try {
			outStream = response.getOutputStream();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}	        
        
  BufferedOutputStream bout = new BufferedOutputStream(outStream);
  
  int ch = 0;
  
  try {
			while(-1 != (ch = bin.read())) {
				bout.write(ch);
			}
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}

  try {
			bin.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}
        
  try {
		 fin.close();
	 } catch (IOException e) {
		 diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
	 }
        
  try {
		 bout.close();
	 } catch (IOException e) {
	 	diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
	 }
        
  try {
	 	outStream.close();
	 } catch (IOException e) {
	 	diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
	 }	                           
        
	 diags.logSubExit(whoAmI, "getNamedImage", "", "");	
	}	
	
	/**
	 * Get a random image.
	 * <p>
	 * @param response HttpServletResponse object.
	 */		
	private void getRandomImage(HttpServletResponse response) {
		diags.logSubCall(whoAmI, "getRandomImage", "", "");	        
        
  response.setContentType("image/jpeg");

  GetRandomFileResult result = getRandomImagePathFileName();
  
  String imgPathFileName = "";
        
		diags.logSubInfo(whoAmI, "getRandomImage", result.getRandomPathFileName(), result.getAvailableImages().toString());
        
  if (0 == result.getAvailableImages()) {
  	imgPathFileName = "webapps\\ImageServer\\images\\noImage.jpg";
  } else {         
  	imgPathFileName = result.getRandomPathFileName();
  }
        
		diags.logSubInfo(whoAmI, "getRandomImage", url, imgPathFileName);	        
            
  FileInputStream fin = null;
        
		try {
			fin = new FileInputStream(imgPathFileName);
		} catch (FileNotFoundException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", true);
			
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "getRandomImage", "", "");
			
			return;
		}

  BufferedInputStream bin = new BufferedInputStream(fin);
  
  ServletOutputStream outStream = null;
        
		try {
			outStream = response.getOutputStream();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);			
			
			try {
				bin.close();
			} catch (IOException e1) {
				diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
			}
			
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "getRandomImage", "", "");	
			
			return;			
		}	        
        
  BufferedOutputStream bout = new BufferedOutputStream(outStream);
  
  int ch = 0;
  
  try {
			while(-1 != (ch = bin.read())) {
			 bout.write(ch);
			}
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}

  try {
			bin.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}
        
  try {
			fin.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}
        
  try {
			bout.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}
        
  try {
			outStream.close();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
		}	                    	       
        
		diags.logSubExit(whoAmI, "getRandomImage", "", "");	
	}
		
	/**
	 * Attempt to get a random image path/file name.
	 * <p>
	 * @return GetRandomFileResult object containing path/name of random image
	 *         number of available images, may be empty/0 if there are no images
	 *         available in the configured image directory path.
	 */
	private GetRandomFileResult getRandomImagePathFileName() {
		diags.logSubCall(whoAmI, "getRandomImagePathFileName", "", "");	
		
   List<Path> paths = getFilesList(conf.getDirectory());
   
   if (0 != paths.size()) {     
    Random random = new Random();
    
    int index = random.ints(0, paths.size()).findFirst().getAsInt();	
    
    String pathFileNameStr = String.format("%s", paths.get(index));
       
	   diags.logSubExit(whoAmI, "getRandomImagePathFileName", pathFileNameStr, "");	
   	
    return new GetRandomFileResult(paths.size(), pathFileNameStr);
   }		
        
		diags.logSubExit(whoAmI, "getRandomImagePathFileName", "", "");	
        
  return new GetRandomFileResult(0, "");
	}		
		
	/**
	 * Handle Config action request.
	 * <p>
	 * @param request HttpServletRequest object.	 
	 * @param response HttpServletResponse object.
	 */			
	private void handleConfigAction(HttpServletRequest request, 
			                              HttpServletResponse response) {
		diags.logSubCall(whoAmI, "handleConfigAction", "", "");	
		
		conf.loadConfiguration();		
					
		String delayStr = request.getParameter("requestedDelay");		
		
		if (null == delayStr) {
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "handleConfigAction", "", "");	
			
			return;
		}
		
		if (true == delayStr.isEmpty()) {
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "handleConfigAction", "", "");	
			
			return;			
		}		
				
		Integer delay = Integer.parseInt(delayStr);
				
		conf.setDelay(delay);
		
		String reqDelDirStr = request.getParameter("requestedDeleteDirectory");
		
		if (null != reqDelDirStr ) {
			if (false == reqDelDirStr.isEmpty()) {
				try {
					String decodedStr = java.net.URLDecoder.decode(reqDelDirStr, StandardCharsets.UTF_8.name());
					
					conf.setDeletedImagesDirectory(decodedStr);
				} catch (UnsupportedEncodingException e) {
					diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
					
					showMainPage(response);
					
					diags.logSubExit(whoAmI, "handleConfigAction", "", "");	
					
					return;						
				}				
			}
		}
		
		String reqImgDirStr = request.getParameter("requestedDirectory");
		
		if (null != reqImgDirStr ) {
			if (false == reqImgDirStr.isEmpty()) {
				try {
					String decodedStr = java.net.URLDecoder.decode(reqImgDirStr, StandardCharsets.UTF_8.name());
					
					conf.setDirectory(decodedStr);
				} catch (UnsupportedEncodingException e) {
					diags.exceptional(whoAmI, "getRandomImage", e.getMessage(), "", false);
					
					showMainPage(response);
					
					diags.logSubExit(whoAmI, "handleConfigAction", "", "");	
					
					return;						
				}				
			}
		}		
					
		conf.saveConfiguration();	
		
  showMainPage(response);
        
		diags.logSubExit(whoAmI, "handleConfigAction", "", "");	
	}		
	
	/**
	 * Handle Delete action request.
	 * <p>
	 * @param request  HttpServletRequest object.	 
	 * @param response HttpServletResponse object.
	 */			
	private void handleDeleteAction(HttpServletRequest request, 
			                              HttpServletResponse response) {
		diags.logSubCall(whoAmI, "handleDeleteAction", "", "");		
		
		String filePathNameToDeleteStr = request.getParameter("filePathNameToDelete");
		
		if (null == filePathNameToDeleteStr) {
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "handleDeleteAction", "", "");	
			
			return;			
		}
		
		if (true == filePathNameToDeleteStr.isEmpty()) {
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "handleDeleteAction", "", "");	
			
			return;
		}			
		
  String filePathNameToDeleteStrEnc = null;
        
		try {
			filePathNameToDeleteStrEnc = java.net.URLDecoder.decode(filePathNameToDeleteStr, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			diags.exceptional(whoAmI, "handleDeleteAction", e.getMessage(), "", false);
			
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "handleDeleteAction", "", "");	
			
			return;
		}		
		
		Path candidate = Paths.get(filePathNameToDeleteStr);
		
		Path target = Paths.get(conf.getDeletedImagesDirectory() + "\\" + candidate.getFileName());
		
		diags.logSubInfo(whoAmI, "handleDeleteAction", candidate.toString(), target.toString());	
		
		try {
			Files.move(candidate, target);
		} catch (IOException e) {
			diags.exceptional(whoAmI, "handleDeleteAction", e.getMessage(), "", false);
		}
				
		diags.logSubInfo(whoAmI, "handleDeleteAction", filePathNameToDeleteStrEnc, "");
		
  generatePageHtml(response);		
		
		diags.logSubExit(whoAmI, "handleDeleteAction", "", "");		
	}

	/**
	 * Show the configuration screen.
	 * <p>
	 * @param response HttpServletResponse object.
	 */			
	private void showConfigPage(HttpServletRequest request, HttpServletResponse response) {
		diags.logSubCall(whoAmI, "showConfigPage", "", "");	
        		
		Integer availableFiles = 0;
		
		String availableFilesStr = request.getParameter("availableFiles");		
		
		if (null != availableFilesStr) {
			if (true != availableFilesStr.isEmpty()) {
				availableFiles = Integer.parseInt(availableFilesStr);		
			}							
		}		
		
		PrintWriter printWriter = null;;
		
		try {
			printWriter = response.getWriter();
		} catch (IOException e) {
			diags.exceptional(whoAmI, "generatePageHtml", e.getMessage(), "", false);
			
			showMainPage(response);
			
			diags.logSubExit(whoAmI, "showConfigPage", "", "");    
			
			return;
		}	
		
		response.setContentType("text/html");
				
		printWriter.println("<!DOCTYPE html>");
		printWriter.println("<html>");
  printWriter.println(" <head>");
  printWriter.println("  <link href=\"css/Styles.css\" rel=\"stylesheet\" >");
  printWriter.println("  <script src=\"javascript/common.js\"></script>");
  printWriter.println(" </head>");        
  printWriter.println(" <body class=\"cfgpagebodystyle\" id=\"ConfigPagey\" >");
  printWriter.println("  <div id=\"ControlsContainer\" style=\"align-items: center; background-color: lightblue; height: 500px; justify-content: center; width: 100%;\">");
		printWriter.println("   <form id=\"ConfigForm\" method=\"post\" name=\"ControlsForm\">");     	
		printWriter.println("    <input id=\"action\" name=\"action\" type=\"hidden\" value=\"placeholder\">");  // Page javascript will modify this via button onclick handler.				
		printWriter.println("    <input id=\"serverUrl\" name=\"serverUrl\" type=\"hidden\"  value=\"" + url + "\">");	
		printWriter.println("    <h1 style=\"text-align:center;\">Server Configuration</h1>");
		printWriter.println("    <br /><br />");
  printWriter.println("    <div id=\"ParmsContainer\" style=\"align-items: center; background-color: lightblue; height: 100vh; margin: 0 auto; position: relative; width: 50%;\">");		
  printWriter.println("     <label for=\"requestedDirectory\">Photos Directory:</label>");
  printWriter.println("     <input id=\"requestedDirectory\" name=\"requestedDirectory\" type=\"text\" value=\"" + conf.getDirectory() + "\"/>");  
		printWriter.println("     <br />");
  printWriter.println("     <label for=\"availableImages\">Images available:</label>");
  printWriter.println("     <input id=\"availableImage\" readonly type=\"text\" value=\"" + availableFiles.toString() + "\">");  
		printWriter.println("     <br />");		
  printWriter.println("     <label for=\"requestedDeleteDirectory\">Delete Target Directory:</label>");
  printWriter.println("     <input id=\"requestedDeleteDirectory\" name=\"requestedDeleteDirectory\" type=\"text\" value=\"" + conf.getDeletedImagesDirectory() + "\">");  
		printWriter.println("     <br />");
  printWriter.println("     <label for=\"requestedDelay\">Delay (5-60 seconds):</label>");
  printWriter.println("     <input id=\"requestedDelay\" name=\"requestedDelay\" max=\"60\" min=\"5\" size=\"3\" type=\"number\" value=\"" + conf.getDelay().toString() + "\">");
		printWriter.println("     <br /><br />");
  printWriter.println("     <input id=\"applyBtn\" name=\"applyBtn\" onclick=\"SubmitConfig();\" type=\"button\" value=\"Apply\" />");
  printWriter.println("     <input id=\"backBtn\" name=\"backBtn\" onclick=\"GoBack();\" type=\"button\" value=\"Return to Main Page\" />");  
  printWriter.println("    </div>");
		printWriter.println("   </form>");        
  printWriter.println("  </div>");              
  printWriter.println(" </body>");
  printWriter.println("</html>");
        
  printWriter.close();
        
		diags.logSubExit(whoAmI, "showConfigPage", "", "");	
	}			
	
	/**
	 * Show the main (default) page.
	 * <p>
	 * @param response HttpServletResponse object.
	 */			
	private void showMainPage(HttpServletResponse response) {
		diags.logSubCall(whoAmI, "showMainPage", "", "");	
        		
  generatePageHtml(response);
        
		diags.logSubExit(whoAmI, "showMainPage", "", "");        
	}	
}
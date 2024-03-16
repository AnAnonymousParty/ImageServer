# ImageServer
A java servlet for Tomcat that serves a randomly selected image from a user configurable directory and periodically updates it.

The JAVA code is compatible with Tomcat 9 and JAVA JRE 8.2020, both of which are included in the prerequisites directory.

This code was developed under Eclipse Version: 2023-12 (4.30.0) for Windows.

To use the app:

1 - Generate and install the .war file in the Tomcat webapps folder in the usual manner.
2 - Point a web browser to it (<ip address:port/ImageServer/ImageServer).
3 - The app will attempt to display an image from its configured images directory.
4 - Click on the web page to open the image controls.
5 - Click the gear to access the configuration page.
6 - Set the images and image deletion directory as appropriate for your server system.
7 - When using the app, the control panel is accessed by clicking the displayed image.
    The delete button moves the current image to the deleted images directory where they can be later reviewed and deleted or moved.
	
An xml configuration file (Conf.xml) is maintained in the webapp/conf directory.
	



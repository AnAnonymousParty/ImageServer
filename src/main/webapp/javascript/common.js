var controlsTimer;
var delayTimer;

function DelayTimerElapsed() {
	console.log("DelayTimerElapsed()");
	
	ReloadPage();
}
 
function DoNext() {	 	 
	console.log("> DoNext()");
	
	location.href = document.getElementById("serverUrl").value + "/ImageServer/ImageServer";
	              
	console.log("< DoNext()");	              
} 
 
function DoDelete() {	 	 
	console.log("> DoDelete()");
	
	location.href = document.getElementById("serverUrl").value + "/ImageServer/ImageServer?action=delete&filePathNameToDelete=" 
	              + encodeURIComponent(document.getElementById("requestedFilePathName").value);
	              
	console.log("< DoDelete()");	              
} 
 
function GoBack() {	 	 
	console.log("> GoBack()");
	
	location.href = document.getElementById("serverUrl").value + "/ImageServer/ImageServer";
	              
	console.log("< GoBack()");	              
}  
 
function HideControls() {
	document.getElementById("ControlsContainer").style.display = "none";
	
	var delay = document.getElementById("requestedDelay").value * 1000;
	
	delayTimer = setInterval(DelayTimerElapsed, delay);
} 

function HandleVideoEnded() {	 	 
	console.log("> HandleVideoEnded");
	
	ReloadPage();
	              
	console.log("< HandleVideoEnded)");	              
} 
 
function InitializeImagePage() {
	console.log("> InitializeImagePage()");

     var availableFiles = document.getElementById("availableFiles").value;
	 var delay = document.getElementById("requestedDelay").value * 1000;
	 
	 if (0 == availableFiles) {
	    document.getElementById("ErrorContainer").style.display    = "flex";		 
		document.getElementById("ErrorContainer").style.visibility = "visible";		 
	    document.getElementById("ImageContainer").style.display    = "none";	
	    document.getElementById("ImageContainer").style.visibility = "collapse";		 
	 } else {
		document.getElementById("ErrorContainer").style.display    = "none";
		document.getElementById("ErrorContainer").style.visibility = "collapse";
	    document.getElementById("ImageContainer").style.display    = "flex";
	    document.getElementById("ImageContainer").style.visibility = "visible";	    
	 }
	 
	 document.onclick = ShowControls;
	 document.getElementById("DispImage").onclick = ShowControls;
	 
	 if (null == delayTimer) {
	 	delayTimer = setInterval(DelayTimerElapsed, delay);
	 }
	 
	console.log("< InitializeImagePage()");	 
} 

function InitializeVideoPage() {
	console.log("> InitializeVideoPage()");

     var availableFiles = document.getElementById("availableFiles").value;
	 
	 if (0 == availableFiles) {
	    document.getElementById("ErrorContainer").style.display    = "flex";		 
		document.getElementById("ErrorContainer").style.visibility = "visible";		 
	    document.getElementById("VideoContainer").style.display    = "none";	
	    document.getElementById("VideoContainer").style.visibility = "collapse";		 
	 } else {
		document.getElementById("ErrorContainer").style.display    = "none";
		document.getElementById("ErrorContainer").style.visibility = "collapse";
	    document.getElementById("VideoContainer").style.display    = "flex";
	    document.getElementById("VideoContainer").style.visibility = "visible";	    
	 }
	 
	 document.onclick = ShowControls;
	 document.getElementById("DispVideo").onclick = ShowControls;
	 
	 if (null != delayTimer) {
	 	clearInterval(delayTimer);
	 }
	 
	console.log("< InitializeVideoPage()");	 
} 
 
function ReloadPage() {
	console.log("ReloadPage()");
	
	location.href = document.getElementById("serverUrl").value + "/ImageServer/ImageServer";
}

function RequestConfigPage() {	 	 
	console.log("> RequestConfigPage()");
	
	document.getElementById("action").value = "reqConfigPage";
	document.getElementById("ConfigForm").submit();
	              
	console.log("< RequestConfigPage()");	              
}  

function ShowControls() {
	if (null != delayTimer) {
		clearInterval(delayTimer);
	}
	
    document.getElementById("ControlsContainer").style.display    = "block";
	document.getElementById("ControlsContainer").style.visibility = "visible";
	
	if (null == controlsTimer) {
		controlsTimer = setTimeout(HideControls, 20000);
	}
}

function SubmitConfig() {	 	 
	console.log("> SubmitConfig()");
	
	document.getElementById("action").value = "configure";
	document.getElementById("ConfigForm").submit();	
	            
	console.log("< SubmitConfig()");	              
}
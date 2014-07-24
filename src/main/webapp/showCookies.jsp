<%@page import="java.util.Enumeration"%>
<html>
	<head>
		<script src="js/jquery.js" type="text/javascript"></script>
		<script src="js/jquery.fileDownload.js" type="text/javascript"></script>
		<%
		/* for(Enumeration e =request.getAttributeNames(); e.hasMoreElements() ;) {
			 String n = (String)e.nextElement();
			 System.out.println(n + ":" + request.getAttribute(n));
		}  */
		%>
	</head>
<body>
	  <form id="test" action="1.ck" method=post>
	  	<input type=text name="times" id="times" size=60 value="<%=request.getAttribute("times")==null ? 0 : request.getAttribute("times")%>" />
	  	<input type=button value=recheck onclick="$('#test').submit();">	
	  </form>
	  
	  <input type=text id="status" size=60>
	  
</body>
<script type="text/javascript">

	function getCookie(name) {
		var start = document.cookie.indexOf( name + "=" );
		var len = start + name.length + 1;
		if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
			return null;
		}
		if ( start == -1 ) return null;
		var end = document.cookie.indexOf( ";", len );
		if ( end == -1 ) end = document.cookie.length;
		return unescape( document.cookie.substring( len, end ) );
	}

	var _D= function() {
		console.debug(arguments);
	}
	var settings = {
	           //
            //the cookie name to indicate if a file download has occured
            //
            cookieName: "fileDownload",

            //
            //the cookie value for the above name to indicate that a file download has occured
            //
            cookieValue: "true",

            //
            //the cookie path for above name value pair
            //
            cookiePath:"<%=request.getContextPath() %>",
            //
            //if specified it will be used when attempting to clear the above name value pair
            //useful for when downloads are being served on a subdomain (e.g. downloads.example.com)
            //	
            cookieDomain: null
	};
	
	_D(settings);
	_D(document.cookie, settings.cookieName,  settings.cookieValue);
	
	var k = settings.cookieName + "=" + settings.cookieValue;
	
	
	if (document.cookie.indexOf(k) != -1) {
	
	    //execute specified callback
	    //internalCallbacks.onSuccess(fileUrl);
	    $('#status').val('OK, ' + getCookie(settings.cookieName));
		 _D('Success!');	
	    //remove cookie
	    var cookieData = settings.cookieName + "=;expires=" + new Date(0).toUTCString() + ";";
	    if (settings.cookieDomain) cookieData += " domain=" + settings.cookieDomain + ";";
	    _D('cookieData!', cookieData);
		document.cookie = cookieData;
	
	    _D('Clear all cookies!', document.cookie);
	    //remove iframe
	  //  cleanUp(false); 
	}else {
		$('#status').val('NG');
	}
</script>
</html>

<html>
	<head>
		 <link rel="stylesheet" type="text/css" href="css/jquery-ui.css"></link> 
		<script src="js/jquery.js" type="text/javascript"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.fileDownload.js" type="text/javascript"></script>
	</head>
<body>
<h2>Hello World!</h2>
	<a href="1.do">test</a>
	<input type=button id=checkDownload value="Start download.">
	<script type="text/javascript"> 
	</script>
</body>
	<script type="text/javascript">
	
	   var _D = function() {
		   console.debug(arguments);
	   } ;
	   
	   var _E = function() {
		   console.error(arguments);
	   } ;
	   
	   
		$(
		  function() {
			  var times  = 0;
			  var max = 100;
			  var downloadUrl = "download.do";
			  var intervalExec = 10000;
			  var downloadExpr = " $.fileDownload(downloadUrl, getOptions())";
			  var invokeDownloadCmd = function() {eval(downloadExpr)};
			  var success = function(url) {
				    if(times < max) {
				    	_D('Time '+ (times-1) + ' has completed! Delaying 2 secs to continue...');
						setTimeout(invokeDownloadCmd, intervalExec);
					}else {
						_D('All file has download!');
					}
			  };
			  
			  var fail = function(html, url) {
				  _E('Failed time ' + (times-1) + "!");
			  };
			  
			  
			  var getOptions = function() {
				  
				  times++;
				  _D('Start times ' + times + "....");
				  return  {
						  successCallback: success,
						  failCallback: fail,
						  httpMethod : 'POST',
						  cookiePath :  '<%=request.getContextPath() %>',
						  preparingMessageHtml : 'Download file "test' +  times + '.csv"...',
						  failMessageHtml : 'Failure to download file "test' +  times + '.csv"...',  
						  data : {
							  'times' : times,
							  'id' : 'CheckDownload' 
						  }
				  }
			  };
			
			  $('#checkDownload').click(
					function() {
						  //Begin to download file.
						  invokeDownloadCmd(); 	  
					}
			  );
		  }
		);
	</script>
</html>

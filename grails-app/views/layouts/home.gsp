<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
	<g:render template="/shared/meta" />
	<link rel="shortcut icon" href="${resource(dir: 'images/public', file: 'domeo.ico')}" type="image/x-icon">
	
	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css/shared', file: 'reset.css')}" >
	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css/home', file: 'footer.css')}" >
	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css/social-buttons', file: 'social-buttons.css')}" >
	<link rel="stylesheet" href="${resource(dir: 'css/navigation', file: 'bootstrap.css')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css/navigation', file: 'navigation-custom.css')}" type="text/css">

	<script type="text/javascript" src="${resource(dir: 'js/navigation', file: 'bootstrap.min.js')}"></script>
		
	<style type="text/css">
        html, body { margin: 0; padding: 0; height: 100%; }

       	#outer {height: 85%; overflow: hidden; position: relative; width: 100%;}
       	#outer[id] {display: table; position: static;}

       	#middle {position: absolute; top: 50%; width: 100%; text-align: center;}
       	#middle[id] {display: table-cell; vertical-align: middle; position: static;}

       	#inner {position: relative; top: -50%; text-align: left;}
       	#inner {margin-left: auto; margin-right: auto;}
        #inner {width: 100px; font-size: 300%} /* this width should be the width of the box you want centered */
        
        #header {text-align: left;}
        #title {display: inline; font-size: 200%; white-space:nowrap; height: 40px;}
        #subtitle {display: inline; font-size: 200%; white-space:nowrap; height: 40px; color: #bbb;}
       </style>	
		
	<g:layoutHead/>
	<r:layoutResources />
</head>
<body>
	<div id="outer">
       	<div id="middle">
	       	<div class="container">
	       		<div id="header">
		       		<div id="title">Annotopia</div><br/>
		       		<div id="subtitle">Universal Annotation Hub</div>
		       		 <div class="socialbuttons">
						<a class="twitter" href="https://twitter.com/annotopia" title="Twitter @annotopia"><img src="${resource(dir:"images/social-buttons", file: "twitter.png")}" /></a>
						<%-- <a class="rss" href=""><img src="${resource(dir:"images/social-buttons", file: "rss.png")}" /></a> --%>
					</div> 
	       		</div>	       		
	   	    </div>  
   	    </div>
   	</div>	
   
   	<g:render template="/shared/footer" />
</body>
</html>
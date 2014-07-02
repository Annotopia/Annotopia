<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
	<g:render template="/shared/meta" />
	<title><g:layoutTitle default="Annotopia"/></title>
	<%-- <link rel="shortcut icon" href="${resource(dir: 'images/public', file: 'domeo.ico')}" type="image/x-icon"> --%>
	
	<link rel="stylesheet" href="${resource(dir: 'css/shared', file: 'reset.css', plugin: 'af-shared')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css/shared', file: 'administration-bar.css', plugin: 'af-shared')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css/navigation', file: 'bootstrap.css')}" type="text/css">
	<link rel="stylesheet" href="${resource(dir: 'css/navigation', file: 'navigation-custom.css')}" type="text/css">

	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'csc-general.css', plugin: 'cs-commons')}" />
	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'csc-info.css', plugin: 'cs-commons')}" />

	<script type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery-2.1.1.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js/navigation', file: 'bootstrap.min.js')}"></script>
		
	<g:layoutHead/>
	<r:layoutResources />
</head>
<body>
	<div class="header" >
	    <g:render template="/secure/navigation-bar" />
	</div>
	<div style="top:60px;">
		<g:layoutBody/>
	</div>
	<br/><br/><br/><br/>
	<g:render template="/shared/footer" />
</body>
</html>
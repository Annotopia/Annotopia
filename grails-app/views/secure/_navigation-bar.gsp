<%@ page import="org.mindinformatics.grails.domeo.dashboard.security.*" %>

<g:render template="/dashboard/shared/administration-general-topbar" plugin="cs-security-dashboard"/>

<nav class="navbar navbar-w3r " role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<g:link controller="secure" action="index" class="navbar-brand">
				${grailsApplication.config.annotopia.general.about.title}
			</g:link>
		</div>
		<div class="navbar-collapse collapse navbar-right">
			<ul class="nav navbar-nav">
				<%--<li><g:link controller="public" action="home">Home</g:link></li> --%>
				<li><g:link controller="secure" action="search">Search</g:link></li>	
				<li><g:link controller="secure" action="myannotations">My annotations</g:link></li>		
				<li><g:link controller="secure" action="profile">My profile</g:link></li>	
				<li><g:link controller="logout" action="index"><img id="groupsSpinner" src="${resource(dir:'images/dashboard',file:'exit.png')}" title="Logout" /></g:link></li>				
			</ul>
		</div>
	</div>
</nav>
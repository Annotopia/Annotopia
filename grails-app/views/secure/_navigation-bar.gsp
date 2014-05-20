<%@ page import="org.mindinformatics.grails.domeo.dashboard.security.*" %>
<div style="background: black;">
	<div class="container">
		<sec:access expression="hasRole('ROLE_ADMIN')">
			<div style="height:20px; display: block;  color: white;">
				<g:render template="/dashboard/shared/administration-general" plugin="cs-security-dashboard"/>
			</div>
		</sec:access>
	</div>
</div>
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
				<li><g:link controller="secure" action="index">My annotations</g:link></li>
				<li><g:link controller="secure" action="index">My resources</g:link></li>		
				<li><g:link controller="secure" action="profile">My profile</g:link></li>	
				<li><g:link controller="logout" action="index"><img id="groupsSpinner" src="${resource(dir:'images/dashboard',file:'exit.png')}" title="Logout" /></g:link></li>				
			</ul>
		</div>
	</div>
</nav>
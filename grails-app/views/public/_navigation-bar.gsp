<%@ page import="org.mindinformatics.grails.domeo.dashboard.security.*" %>
<nav class="navbar navbar-w3r navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<g:link controller="secured" action="home" class="navbar-brand">
				${grailsApplication.config.annotopia.general.about.title}
			</g:link>
		</div>
		<div class="navbar-collapse collapse navbar-right">
			<ul class="nav navbar-nav">
				<%--<li><g:link controller="public" action="home">Home</g:link></li> --%>
				<li><g:link controller="public" action="signup">Sign Up</g:link></li>					
			</ul>
 
	        <form class="navbar-form navbar-right">
	            <div class="form-group">
	                <input type="text" placeholder="Email" class="form-control">
	            </div>
	            <div class="form-group">
	                <input type="password" placeholder="Password" class="form-control">
	            </div>
	            <button type="submit" class="btn btn-success">Sign in</button>
	        </form>
		</div>
	</div>
</nav>
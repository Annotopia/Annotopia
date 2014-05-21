<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="secure"/>
		<title>My profile</title>
	</head>
	<body>
		<div class="container">
			<div class="csc-main">
				<h2>My Profile</h2>
					<g:form method="post" >
						<div class="csc-lens-container">
							<g:hiddenField name="id" value="${user.id}" /> 
							<g:render plugin="cs-users" template="/users/userShow" />
						</div>
						<br/>
						<div class="buttons">
							<span class="button">
								<g:actionSubmit class="edit" action="editUser" value="${message(code: 'org.commonsemantics.grails.users.profile.submit', default: 'Edit Profile')}" />
							</span>
						</div>				
					</g:form>
				<br/>
			</div>
		</div>
	</body>
</html>
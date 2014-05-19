<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="public"/>
		<title>Sign Up</title>
	</head>
	<body>
		<div class="container">
			<div class="csc-main">
				<h2>Create Profile</h2>
				<g:form method="post" >
					<div class="csc-lens-container">
						<g:render plugin="cs-users" template="/users/userSignUp" />
					</div>
					<br/>
					<tr>
						<td valign="top" colspan="2" >
							<div class="buttons">
								<span class="button">
									<g:actionSubmit class="save" action="signUpUser" 
										value="${message(code: 'org.commonsemantics.grails.users.profile.create', default: 'Sign Up')}" />
								</span>
								<span class="button">
									<g:actionSubmit class="cancel" action="index" 
										value="${message(code: 'org.commonsemantics.grails.general.cancel', default: 'Cancel')}" />
								</span>
							</div>
						</td>
					</tr>
				</g:form>
			</div>
		</div>
	</body>
</html>
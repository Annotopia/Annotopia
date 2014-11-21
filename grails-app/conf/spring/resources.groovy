import org.annotopia.grails.security.LoggingSecurityEventListener
import org.commonsemantics.grails.security.oauth.OAuthAuthorizationCodeTokenGranter
import org.commonsemantics.grails.security.oauth.OAuthClientCredentialsAuthenticationProvider
import org.commonsemantics.grails.security.oauth.OAuthClientDetailsService
import org.commonsemantics.grails.security.oauth.OAuthTokenStore
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
import org.springframework.security.oauth2.provider.token.DefaultTokenServices

//import org.annotopia.grails.services.CustomUserDetailsService
// Place your Spring DSL code here
beans = {
	securityEventListener(LoggingSecurityEventListener)
	//userDetailsService(CustomUserDetailsService)
	
	// OAuth
	customOAuthAuthenticationProvider(OAuthClientCredentialsAuthenticationProvider)
	
	clientDetailsService(OAuthClientDetailsService) {
		grailsApplication = ref("grailsApplication")
	}

	tokenStore(OAuthTokenStore)

	tokenServices(DefaultTokenServices) {
		tokenStore = ref("tokenStore")
		supportRefreshToken = "true"
		clientDetailsService = ref("clientDetailsService")
		accessTokenValiditySeconds = 86400
	}

	authorizationCodeServices(InMemoryAuthorizationCodeServices)

	oauth2TokenGranter1(OAuthAuthorizationCodeTokenGranter,
            tokenServices = ref("tokenServices"),
            authorizationCodeServices = ref("authorizationCodeServices"),
            clientDetailsService = ref("clientDetailsService"))

    oauth2TokenGranter2(RefreshTokenGranter,
            tokenServices = ref("tokenServices"),
            clientDetailsService = ref("clientDetailsService"))
			
	oauth2TokenGranter3(ClientCredentialsTokenGranter,
			tokenServices = ref("tokenServices"),
			clientDetailsService = ref("clientDetailsService"))

    oauth2TokenGranter(CompositeTokenGranter, [
		ref("oauth2TokenGranter1"), 
		ref("oauth2TokenGranter2"),
		ref("oauth2TokenGranter3")
	])
	
}

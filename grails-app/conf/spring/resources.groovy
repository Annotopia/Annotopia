import org.annotopia.grails.security.LoggingSecurityEventListener
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.commonsemantics.grails.security.oauth.OAuthAuthorizationCodeTokenGranter;
import org.commonsemantics.grails.security.oauth.OAuthClientDetailsService;
import org.commonsemantics.grails.security.oauth.OAuthTokenStore;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

//import org.annotopia.grails.services.CustomUserDetailsService
// Place your Spring DSL code here
beans = {
	securityEventListener(LoggingSecurityEventListener)
	//userDetailsService(CustomUserDetailsService)
	
	// OAuth
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

	oauth2TokenGranter(OAuthAuthorizationCodeTokenGranter,
		tokenServices = ref("tokenServices"),
		authorizationCodeServices = ref("authorizationCodeServices"),
		clientDetailsService = ref("clientDetailsService"))
	{
	}

}

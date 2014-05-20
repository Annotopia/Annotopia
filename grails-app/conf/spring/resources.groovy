import org.annotopia.grails.security.LoggingSecurityEventListener
import org.annotopia.grails.services.CustomUserDetailsService
// Place your Spring DSL code here
beans = {
	securityEventListener(LoggingSecurityEventListener)
	//userDetailsService(CustomUserDetailsService)
}

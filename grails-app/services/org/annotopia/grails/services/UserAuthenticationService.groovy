package org.annotopia.grails.services

class UserAuthenticationService {

	def grailsApplication;
	def springSecurityService;
	
	def getUserId(def ip) {
		log.info("Retrieving User ID on request from IP: " + ip);
		// Validation mockup for testing mode
		if(grailsApplication.config.annotopia.storage.testing.enabled=='true' &&
				grailsApplication.config.annotopia.storage.testing.user.id!=null)
			return grailsApplication.config.annotopia.storage.testing.user.id;

		def principal = springSecurityService.principal;
		if(!principal.equals("anonymousUser")) {
			String userId = principal.id
			return userId
		}
	}
}

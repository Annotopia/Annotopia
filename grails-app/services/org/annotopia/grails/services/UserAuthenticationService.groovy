package org.annotopia.grails.services

class UserAuthenticationService {

	def grailsApplication;
	def springSecurityService;
	def configAccessService;
	
	def getUserId(def ip) {
		log.info("Retrieving User ID on request from IP: " + ip);
		// Validation mockup for testing mode
		if(configAccessService.getAsString("annotopia.storage.testing.enabled")=='true' &&
				configAccessService.getAsString("annotopia.storage.testing.user.id")!=null)
			return configAccessService.getAsString("annotopia.storage.testing.user.id");

		def principal = springSecurityService.principal;
		if(!principal.equals("anonymousUser")) {
			String userId = principal.id
			return userId
		}
	}
}

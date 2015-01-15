/*
 * Copyright 2014 Massachusetts General Hospital
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.annotopia.grails.services

import org.commonsemantics.grails.security.oauth.OAuthToken


/**
 * This service manages access through API keys that are assigned to 
 * applications or users that want to make use of the Annotopia server.
 * 
 * Notice that the service extends the mockup service in the Smart
 * Storage plugin and override the validation method.
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class ApiKeyAuthenticationService extends org.annotopia.grails.services.storage.authentication.ApiKeyAuthenticationService {

	def springSecurityService
	def grailsApplication
	def configAccessService
	def systemsService;
	
	/**
	 * Returns true if the tested apiKey is valid. At the moment this validates
	 * the testing ApiKey only. Later it will test the validity against real 
	 * API keys.
	 * @param apiKey	The ApiKey assigned to a client
	 * @return True if the client is authorized
	 */
	def isApiKeyValid(def ip, def apiKey) {
		log.info("New-> Validating API key [" + apiKey + "] on request from IP: " + ip);
		// Validation mockup for testing mode
		boolean allowed = (
			configAccessService.getAsString("annotopia.storage.testing.enabled")=='true' &&
			apiKey==configAccessService.getAsString("annotopia.storage.testing.apiKey")
		);
		// Validation against real apiKeys
		if(!allowed) {
			allowed = systemsService.isApiKeyValid(apiKey);	
		}
	 	return allowed;
	}
	
	def getUserId(def ip, def id) {
		log.info("Authenticating User [" + id + "] on request from IP: " + ip);
		// Validation mockup for testing mode
		if(configAccessService.getAsString("annotopia.storage.testing.enabled")=='true' &&
				configAccessService.getAsString("annotopia.storage.testing.userid")!=null)
			return configAccessService.getAsString("annotopia.storage.testing.userid");
			
		def principal = springSecurityService.principal;
		if(!principal.equals("anonymousUser")) {
			String userId = principal.id
			return userId
		}
	}
	
	def getUserIdentifiedByToken(def userTokenParameter) {
		if(userTokenParameter.length()<30) return null;
		def oauthToken = OAuthToken.findByToken(userTokenParameter.substring(7))
		def user = oauthToken.user;
		return user;
	}
}

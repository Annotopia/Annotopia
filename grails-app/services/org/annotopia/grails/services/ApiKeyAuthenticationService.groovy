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

	def grailsApplication
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
			grailsApplication.config.annotopia.storage.testing.enabled=='true' &&
			apiKey==grailsApplication.config.annotopia.storage.testing.apiKey
		);
		// Validation against real apiKeys
		if(!allowed) {
			allowed = systemsService.isApiKeyValid(apiKey);	
		}
	 	return allowed;
	}
}

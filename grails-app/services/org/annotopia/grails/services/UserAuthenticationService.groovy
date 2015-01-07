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
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
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

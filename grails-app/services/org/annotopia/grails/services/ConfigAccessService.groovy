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

import java.nio.Buffer;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class ConfigAccessService {

	def grailsApplication;
	
	public static final String DEFAULT_EMAIL_ADDRESS = "paolo.ciccarese@gmail.com"
	public static final String DEFAULT_EMAIL_LABEL = "-please define instance administrator email-"
	
	public String getAsString(key) {
		def buffer = grailsApplication.config;
		List tokens = key.tokenize('.');
		tokens.each { token ->
			buffer = buffer[token];
		}
		return buffer.toString();
	}
	
	public String getConfigAdminMissingMessage() {
		return '--->>> Please define the administratin properties'
	}
	
	public String getAdministratorName() {
		try {
			return (configAccessService.getAsString("annotopia.admin.name"));
		} catch (Exception e) {
			log.error("Administrator name not defined");
			return "-Please define instance administrator name-";
		}
	}
	
	public String getAdministratorOrganization() {
		try {
			return (configAccessService.getAsString("annotopia.admin.organization"));
		} catch (Exception e) {
			log.error("Administrator organization not defined");
			return "-Please define instance administrator organization-";
		}
	}
	
	public boolean doesAdministratorEmailAddressExists() {
		try {
			return (grailsApplication.config.annotopia.admin.email.address && getAsString("annotopia.admin.email.address").length()>0);
		} catch (Exception e) {
			log.error("Administrator email address not defined");
			return false;
		}
	}
	
	public String getAdministratorEmailAddress() {
		if(doesAdministratorEmailAddressExists()) {
			return getAsString("annotopia.admin.email.to");
		} else {
			log.warn("Administrator email address not defined");
			return DEFAULT_EMAIL_ADDRESS
		}
	}
	
	public String getAdministratorEmailLabel() {
		try {
			return getAsString("annotopia.admin.email.display");
		} catch (Exception e) {
			log.error("Administrator email label not defined");
			return DEFAULT_EMAIL_LABEL;
		}
	}
}

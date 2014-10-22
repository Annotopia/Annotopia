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
 * This class mediates the access to the Grails configuration. When retrieving
 * configuration values the getAsString() method should be used to make sure 
 * the returned value is a String.  
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class ConfigAccessService {

	def grailsApplication;
	
	/**
	 * Returns the value identified by the requested key in
	 * the Grails configuraiton.
	 * @param key	The requested key.
	 * @return The Grails configuration property value or null if no property is found.
	 */
	public String getAsString(key) {
		def buffer = grailsApplication.config;
		List tokens = key.tokenize('.');
		tokens.each { token -> buffer = buffer[token]; }
		if(buffer.isEmpty()) return null;
		buffer.toString();
	}
	
	/**
	 * Private method for simplifying the retrieval of specific properties.
	 * If the property is not present, an exception is thrown.
	 * @param key			The key of the requested property
	 * @return The value of the requested property if present.
	 */
	public String getPropertyAsStringNotNull(key) {
		String value = getAsString(key);
		if(value==null) {
			log.error("Parameter not found: " + key);
			throw new IllegalArgumentException(key + " not found.");
		}
		return value;
	}
	
	/**
	 * Private method for simplifying the retrieval of specific properties.
	 * If the property is not present, an exception is thrown.
	 * @param key			The key of the requested property
	 * @param errorMessage	The message to log in case of error/missing parameter
	 * @param returnMessage The message to return to the requester.
	 * @return The value of the requested property if present.
	 */
	public String getPropertyAsStringNotNull(key, errorMessage, returnMessage) {
		String value = getAsString(key);
		if(value==null) {
			log.error(errorMessage);
			throw new IllegalArgumentException(returnMessage);
		}
		return value;
	}
	
	/**
	 * Private method for simplifying the retrieval of specific properties.
	 * If the property is not present the return message is returned.
	 * @param key			The key of the requested property
	 * @param returnMessage	The message to log in case of missing parameter
	 * @param returnMessage The message to return to the requester.
	 * @return The value of the requested property if present.
	 */
	public String getPropertyAsString(key, warnMessage, returnMessage) {
		String value = getAsString(key);
		if(value==null) {
			log.warn(warnMessage);
			return returnMessage;
		}
		return value;
	}
	
	/**
	 * Returns true if the property exists and is not empty.
	 * @param key	The key of the requested property
	 * @return True if the property exists and is not empty.
	 */
	public boolean doesPropertyExists(key) {
		return (grailsApplication.config.annotopia.admin.email.address && getAsString(key).length()>0);
	}
	
	/*
	public String getConfigAdminMissingMessage() {
		return '--->>> Please define the administratin properties'
	}
	
	public static final String DEFAULT_EMAIL_ADDRESS = "paolo.ciccarese@gmail.com"
	public static final String DEFAULT_EMAIL_LABEL = "-please define instance administrator email-"
	
	public String getAdministratorName() {
		getPropertyAsString(
			"annotopia.admin.name", "Administrator name not defined", 
			"-Please define instance administrator name-");
	}
	
	public String getAdministratorOrganization() {
		getPropertyAsString(
			"annotopia.admin.organization", "Administrator organization not defined",
			"-Please define instance administrator organization-");
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
		getPropertyAsString(
			"annotopia.admin.email.to", "Administrator email address not defined",
			DEFAULT_EMAIL_ADDRESS);
	}
	
	public String getAdministratorEmailLabel() {
		getPropertyAsString(
			"annotopia.admin.email.display", "Administrator email label not defined",
			DEFAULT_EMAIL_LABEL);
	}
	*/
}

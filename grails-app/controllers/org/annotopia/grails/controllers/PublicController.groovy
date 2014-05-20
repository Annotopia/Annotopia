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
package org.annotopia.grails.controllers

import org.commonsemantics.grails.agents.commands.PersonCreateCommand
import org.commonsemantics.grails.agents.model.Person
import org.commonsemantics.grails.users.commands.UserCreateCommand
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.utils.DefaultUsersRoles
import org.commonsemantics.grails.users.utils.UserStatus
import org.commonsemantics.grails.users.utils.UsersUtils

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class PublicController {

	def springSecurityService
	def agentsService;
	def usersService;
	
	/*
	 * Loading by primary key is usually more efficient because it takes
	 * advantage of Hibernate's first-level and second-level caches
	 */
	protected def injectUserProfile() {
		def principal = springSecurityService.principal
		if(principal.equals("anonymousUser") || !principal.hasProperty("id")) {
			return null
		} else {
			String userId = principal.id
			def user = User.findById(userId);
			if(user==null) {
				log.error "Error:User not found for id: " + userId
				render (view:'error', model:[message: "User not found for id: "+userId]);
			}
			user
		}
	}
	
	def index = {
		def user = injectUserProfile()
		if(user==null) render(view: "home", model: [menu: 'index'])
		else redirect(controller:'secure',action:'index')
	}
	
	def signup = {
		render(view: "signup", model: [menu: 'signup'])
	}
	
	def signUpUser = { PersonCreateCommand cmd ->
		log.debug("[TEST] singup-user " + cmd.displayName);
		UserCreateCommand c = new UserCreateCommand();
		c.person = cmd;
		def validationFailed = agentsService.validatePerson(cmd);
		if (validationFailed) {
			log.error("[TEST] While Saving User's Person " + cmd.errors)
			render (view:'signup', model:[user:c]);
		} else {
			def person = new Person();
			person.title = params.title;
			person.firstName = params.firstName;
			person.middleName = params.middleName;
			person.lastName = params.lastName;
			person.affiliation = params.affiliation;
			person.country = params.country;
			person.displayName = params.displayName;
			person.email = params.email;

			Person.withTransaction { personStatus ->
				if(!person.save(flush: true)) {
					log.error("[TEST] While Saving User's Person " + person.errors)
					person.errors.each {
						// http://grails.org/doc/latest/api/grails/validation/ValidationErrors.html
						log.error("[TEST] While Saving User's Person " + it.target)
						it.fieldErrors.each { error ->
							// http://docs.spring.io/spring/docs/1.2.9/api/org/springframework/validation/FieldError.html
							// println '---- error ----' + error.getClass().getName()
							// println '---- error ----' + error.getField()
							// println '---- error ----' + error.getDefaultMessage()
							cmd.errors.rejectValue(error.getField(),
									g.message(code: 'org.commonsemantics.grails.users.model.field.email.not.available.message', default: error.getDefaultMessage()))
						}
					}
					render (view:'signup', model:[user:c]);
				} else {
					def user = new User(username: params.username, password: params.password, person:person)
					if(!user.save(flush: true)) {
						log.error("[TEST] While Saving User " + cmd.errors)
						user.errors.each {
							// http://grails.org/doc/latest/api/grails/validation/ValidationErrors.html
							log.error("[TEST] While Saving User " + it.target)
							it.fieldErrors.each { error ->
								// http://docs.spring.io/spring/docs/1.2.9/api/org/springframework/validation/FieldError.html
								//println '---- error ----' + error.getClass().getName()
								//println '---- error ----' + error.getField()
								//println '---- error ----' + error.getDefaultMessage()
								c.errors.rejectValue(error.getField(),
										g.message(code: 'org.commonsemantics.grails.users.model.field.username.not.available.message', default: error.getDefaultMessage()))

								println c.errors
							}
						}
						log.error("[TEST] Rolling back User's Person " + person)
						personStatus.setRollbackOnly();

						c.username = params.username;
							
						if(c.isPasswordValid()) {
							c.password = params.password;
						} else {
							log.error("Passwords not matching while saving " + it.target)
							c.errors.rejectValue("password", 
								g.message(code: 'org.commonsemantics.grails.users.model.field.password.not.matching.message', default: "Passwords not matching"));
						}
						
						
						c.status = params.userStatus;
						c.person = cmd;
						usersService.validateUser(c);

						render (view:'signup', model:[user:c]);
					} else {
						log.debug("[TEST] save-user roles and status");
						UsersUtils.updateUserRole(user, Role.findByAuthority(DefaultUsersRoles.USER.value()), 'on')
						UsersUtils.updateUserStatus(user, UserStatus.CREATED_USER.value())
						UsersUtils.updateUserProfilePrivacy(user, params.userProfilePrivacy)
						render (view:'registered', model:[user:user]);
						return;
					}
				}
			}
		}
	}
}

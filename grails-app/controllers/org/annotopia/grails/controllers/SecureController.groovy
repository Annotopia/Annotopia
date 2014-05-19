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

import org.commonsemantics.grails.users.model.User

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class SecureController {
	
	def index = {
		render(view: "index", model: [menu: 'index'])
	}
	
	def listUsers = {
		log.debug("List-users max:" + params.max + " offset:" + params.offset)
		render (view:'users-list', model:[users:User.list(params), usersTotal: User.count(), max: params.max, offset: params.offset]);
	}
	
	def users = {
		log.debug("List-users max:" + params.max + " offset:" + params.offset)
		render (view:'listUsers', model:[users:User.list(params), usersTotal: User.count(), max: params.max, offset: params.offset]);
	}
}

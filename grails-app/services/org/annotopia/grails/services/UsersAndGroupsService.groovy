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

import org.commonsemantics.grails.groups.model.Group
import org.commonsemantics.grails.groups.model.UserGroup
import org.commonsemantics.grails.users.model.User

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class UsersAndGroupsService extends org.annotopia.grails.services.storage.authentication.UsersAndGroupsService {

	boolean doesUserBelongToGroup(def userId, def groupId) {
		def user = User.findById(userId);
		if(user) {
			def group = Group.findById(groupId);
			if(group) {
				def ug = UserGroup.findByUserAndGroup(user, group);
				if(ug) {
					return true;
				}
			}
		}
		return false;
	}
}

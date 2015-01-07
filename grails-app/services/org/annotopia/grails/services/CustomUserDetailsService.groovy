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

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser

import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
class CustomUserDetailsService {// implements GrailsUserDetailsService {
 
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]
 
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }
 
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     
//        User.withTransaction { status ->
//            User user = User.findByUsernameOrEmail(username, username)
//            if (!user)
//                throw new UsernameNotFoundException('User not found', username)
//                 
//            def authorities = user.authorities.collect {new GrantedAuthorityImpl(it.authority)}
//             
//            return new GrailsUser(user.username, user.password, user.enabled, !user.accountExpired,
//                !user.passwordExpired, !user.accountLocked,
//                authorities ?: NO_ROLES, user.id)
//        }
    }
}


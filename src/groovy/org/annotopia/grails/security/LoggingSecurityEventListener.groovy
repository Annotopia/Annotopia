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
package org.annotopia.grails.security

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler

// Taken from
// http://www.redtoad.ca/ataylor/2011/05/logging-spring-security-events-in-grails/
class LoggingSecurityEventListener implements
    ApplicationListener<AbstractAuthenticationEvent>, LogoutHandler {
 
    private static final log = LogFactory.getLog(this)
 
    void onApplicationEvent(AbstractAuthenticationEvent event) {
        event.authentication.with {
            def username = principal.hasProperty('username')?.getProperty(principal) ?: principal
            log.info "event=${event.class.simpleName} username=${username} " +
                "remoteAddress=${details?.remoteAddress} sessionId=${details?.sessionId}"
        }
    }
 
    void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
		if(authentication==null) return;
        authentication.with {
            def username = principal.hasProperty('username')?.getProperty(principal) ?: principal
            log.info "event=Logout username=${username} " +
                "remoteAddress=${details?.remoteAddress} sessionId=${details?.sessionId}"
        }
    }
}

import grails.util.Holders

import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
import org.commonsemantics.grails.agents.model.AgentUri
import org.commonsemantics.grails.agents.model.Person
import org.commonsemantics.grails.agents.model.Software
import org.commonsemantics.grails.groups.model.Group
import org.commonsemantics.grails.groups.model.GroupPrivacy
import org.commonsemantics.grails.groups.model.GroupRole
import org.commonsemantics.grails.groups.model.GroupStatus
import org.commonsemantics.grails.groups.model.UserGroup
import org.commonsemantics.grails.groups.model.UserStatusInGroup
import org.commonsemantics.grails.groups.utils.DefaultGroupPrivacy
import org.commonsemantics.grails.groups.utils.DefaultGroupRoles
import org.commonsemantics.grails.groups.utils.DefaultGroupStatus
import org.commonsemantics.grails.groups.utils.DefaultUserStatusInGroup
import org.commonsemantics.grails.security.oauth.OAuthStoredAccessToken
import org.commonsemantics.grails.systems.model.SystemApi
import org.commonsemantics.grails.systems.model.UserSystemApi
import org.commonsemantics.grails.users.model.ProfilePrivacy
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersProfilePrivacy
import org.commonsemantics.grails.users.utils.DefaultUsersRoles
import org.springframework.context.ApplicationContext

class BootStrap {

	def grailsApplication
   	def springSecurityService; 
	def configAccessService;   
	def connectorsManagerService;
	   
	def usersInitializationService
	def groupsInitializationService
	def systemsInitializationService
	
	def bioPortalService
   
    def init = { servletContext ->
		
		// ABOUT
		// ------
		demarcation(
			' MIND INFORMATICS: ANNOTOPIA (v.' +
			grailsApplication.metadata['app.version'] + ", b." +
			grailsApplication.metadata['app.build'] + ")");		
		separator();
		log.info  ' By Paolo Ciccarese (http://paolociccarese.info/)'
		log.info  ' Copyright 2014 Mass General Hospital'
		separator();
		log.info  ' Released under the Apache License, Version 2.0'
		log.info  ' url:http://www.apache.org/licenses/LICENSE-2.0'
		demarcation('>> Bootstrapping....');
		demarcation('>> INITIALIZING DEFAULTS ENUMERATIONS');
		
		// USERS
		// ------
		separator('** Users Roles');
		usersInitializationService.initializeRoles();
		separator('** Users Profile Privacy');
		usersInitializationService.initializeProfilePrivacy();

		// GROUPS
		// ------
		separator('** Groups Roles');
		groupsInitializationService.initializeRoles();
		separator('** Groups Status');
		groupsInitializationService.initializeStatus();
		separator('** Groups Privacy');
		groupsInitializationService.initializePrivacy();
		separator('** User Status in Group');
		groupsInitializationService.initializeUserStatusInGroup();
		
		// SYSTEMS
		// -------
		separator('** Systems Status');
		systemsInitializationService.initializeStatus();
		separator('** Systems Types');
		systemsInitializationService.initializeTypes();
		
		// ENTITIES
		// --------
		demarcation('>> INITIALIZING DEFAULTS ENTITIES');
		demarcation('>> AGENTS INITIALIZATION');
		separator('** Users');
		
		def person = Person.findByEmail('paolo.ciccarese@gmail.com');
		if(person==null) {
			person = new Person(
				firstName: 'Jack', 
				lastName: 'White',
				displayName: 'Dr. White',
				email:'paolo.ciccarese@gmail.com'
			).save(flush: true, failOnError: true);
		
			person.uris.add 'http://orcid.org/0000-0002-5156-2703';
		}
		
		if (!AgentUri.findByAgentAndUri(person, 'http://orcid.org/0000-0002-5156-2703'))
			AgentUri.create person, 'orcid', 'http://orcid.org/0000-0002-5156-2703'
		
		def password = 'password'
		def adminUsername = 'admin'
		log.info  "Initializing: " + adminUsername
		def admin = User.findByUsername(adminUsername);
		if(admin==null) {
			admin = new User(username: adminUsername,
				password: encodePassword(password), person: person,
				enabled: true, profilePrivacy:  ProfilePrivacy.findByValue(DefaultUsersProfilePrivacy.PRIVATE.value()),  email:'paolo.ciccarese@gmail.com').save(failOnError: true)
			log.warn  "CHANGE PASSWORD for: " + adminUsername + "!!!"
		} else {
			log.info "Found: " + adminUsername;
		}
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.USER.value()))) 
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.USER.value())
			
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.MANAGER.value()))) 
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.MANAGER.value())
			
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.ADMIN.value())))
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.ADMIN.value())
			
		log.info admin.person.uris

		def managerUsername = 'manager'
		log.info  "Initializing: " + managerUsername
		def manager = User.findByUsername(managerUsername);
		if(manager==null) {
			manager = new User(username: managerUsername,
				password: encodePassword(password), person: person,
				enabled: true, profilePrivacy:  ProfilePrivacy.findByValue(DefaultUsersProfilePrivacy.PRIVATE.value()), email:'paolo.ciccarese@gmail.com').save(failOnError: true)
			log.warn  "CHANGE PASSWORD for: " + managerUsername + "!!!"
		} else {
			log.info "Found: " + managerUsername;
		}
		if (!manager.authorities.contains(Role.findByAuthority(DefaultUsersRoles.USER.value())))
			UserRole.create manager, Role.findByAuthority(DefaultUsersRoles.USER.value())
			
		if (!manager.authorities.contains(Role.findByAuthority(DefaultUsersRoles.MANAGER.value())))
			UserRole.create manager, Role.findByAuthority(DefaultUsersRoles.MANAGER.value())
		
		// --------------------------------------
		// Test user, system and authentication
		// --------------------------------------
		if(configAccessService.getAsString("annotopia.storage.testing.enabled")=='true') {
			// create test user
			def userUsername = 'user'
			log.info  "Initializing: " + userUsername
			def user = User.findByUsername(userUsername);
			if(user==null) {
				user = new User(username: userUsername,
					password: encodePassword(password), person: person,
					enabled: true, profilePrivacy:  ProfilePrivacy.findByValue(DefaultUsersProfilePrivacy.PRIVATE.value()),  email:'paolo.ciccarese@gmail.com').save(failOnError: true)
				log.warn  "CHANGE PASSWORD for: " + userUsername + "!!!"
			} else {
				log.info "Found: " + userUsername;
			}
			if (!user.authorities.contains(Role.findByAuthority(DefaultUsersRoles.USER.value())))
				UserRole.create user, Role.findByAuthority(DefaultUsersRoles.USER.value())
				
			def testGroupName = "Testing Group "
			def testGroup = Group.findByName(testGroupName) ?: new Group(
				name: testGroupName,
				shortName: 'TG',
				description: testGroupName,
				enabled: true,
				locked: false,
				createdBy: admin,
				status: GroupStatus.findByValue(DefaultGroupStatus.ACTIVE.value()),
				privacy: GroupPrivacy.findByValue(DefaultGroupPrivacy.PUBLIC.value())
			).save(failOnError: true)
			
			if (!AgentUri.findByAgentAndUri(testGroup, 'http://localhost:8090/agent/testinggroup'))
			AgentUri.create testGroup, 'local', 'http://localhost:8090/agent/testinggroup'
			
			def testUserGroup = UserGroup.findByUserAndGroup(user, testGroup)?: new UserGroup(
				user: user,
				group: testGroup,
				status: UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value())
			).save(failOnError: true, flash: true)
				
			// create test system
			def systemName = "TestSystem";
			def system = SystemApi.findByName(systemName);
			if(system == null) {
				system = new SystemApi(name: systemName, shortName: systemName, description: systemName,
					createdBy: admin, apikey: "164bb0e0-248f-11e4-8c21-0800200c9a66", 
					secretkey: "4fd23632-205a-478d-b1eb-a0933caacd79", enabled: true)
				.save(failOnError: true)
				def userSystem = new UserSystemApi(user: admin, system: system).save(failOnError: true)
			} else {
				log.info "Found: " + systemName;
			}
			
			// create test token
			def token = "caeb2990-248f-11e4-8c21-0800200c9a66";
			def accessToken = OAuthStoredAccessToken.findByToken(token);
			if(accessToken == null) {
				// read the authentication file
				def file = servletContext.getResourceAsStream("/WEB-INF/data/UserAuthToken");
				List<Byte> authentication = new ArrayList<Byte>( );
				int b;
				while((b = file.read( )) != -1) {
					authentication.add((byte)b);
				} 
				accessToken = new OAuthStoredAccessToken(user: user, system: system, token: token, 
					authentication: (byte[ ])authentication.toArray( )).save(failOnError: true)
			}
		}
			
		separator();
		def name = 'Software Test';
		log.info  '** Software' 
		log.info  "Initializing: " + name
		def software = Software.findByName(name);
		if(software==null) {
			software = new Software(
				ver: '1.0',
				name: name,
				displayName: 'Software Test display',
				description: 'Software Test description'
			).save(failOnError: true);
		} else {
			log.info "Found: " + name;
		}
		
		//////////GROUPS TESTS
		separator();
		log.info  '** Groups'
		def group0 = "Test Group 0"
		log.info  "Initializing: " + group0
		def testGroup0 = Group.findByName(group0) ?: new Group(
			name: group0,
			shortName: 'TG0',
			description: group0,
			enabled: true,
			locked: false,
			createdBy: admin,
			status: GroupStatus.findByValue(DefaultGroupStatus.ACTIVE.value()),
			privacy: GroupPrivacy.findByValue(DefaultGroupPrivacy.PUBLIC.value())
		).save(failOnError: true)
		
		def testUserGroup1 = UserGroup.findByUserAndGroup(admin, testGroup0)?: new UserGroup(
			user: admin,
			group: testGroup0,
			status: UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value())
		).save(failOnError: true, flash: true)
		testUserGroup1.addToRoles GroupRole.findByAuthority(DefaultGroupRoles.ADMIN.value())
		
		def testUserGroup2 = UserGroup.findByUserAndGroup(manager, testGroup0)?: new UserGroup(
			user: manager,
			group: testGroup0,
			status: UserStatusInGroup.findByValue(DefaultUserStatusInGroup.ACTIVE.value())
		).save(failOnError: true, flash: true)
		testUserGroup2.addToRoles GroupRole.findByAuthority(DefaultGroupRoles.ADMIN.value())
		
		//---------------------------------------
		// CONNECTORS
		//---------------------------------------
		demarcation(">> CONNECTORS DETECTION");
		connectorsManagerService.registerConnectors();
		demarcation(">> Bootstrapping completed!")
		separator()
    }
	def encodePassword(def password) {
		return springSecurityService.encodePassword(password)
	}
	
	private demarcation() {
		log.info  '========================================================================';
	}
	private demarcation(message) {
		demarcation();
		log.info  message
	}
	private separator() {
		log.info  '------------------------------------------------------------------------';
	}
	private separator(message) {
		separator();
		log.info  message
	}
	
    def destroy = {
    }
}

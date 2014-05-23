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
import org.commonsemantics.grails.systems.model.SystemStatus
import org.commonsemantics.grails.systems.utils.DefaultSystemStatus
import org.commonsemantics.grails.users.model.ProfilePrivacy
import org.commonsemantics.grails.users.model.Role
import org.commonsemantics.grails.users.model.User
import org.commonsemantics.grails.users.model.UserRole
import org.commonsemantics.grails.users.utils.DefaultUsersProfilePrivacy
import org.commonsemantics.grails.users.utils.DefaultUsersRoles

class BootStrap {

	def grailsApplication
   	def springSecurityService;
   
    def init = { servletContext ->
		
		demarcation();
		log.info  ' MIND INFORMATICS: ANNOTOPIA (v.' +
			grailsApplication.metadata['app.version'] + ", b." +
			grailsApplication.metadata['app.build'] + ")";
			
		separator();
		log.info  ' By Paolo Ciccarese (http://paolociccarese.info/)'
		log.info  ' Copyright 2014 Mass General Hospital'
		
		separator();
		log.info  ' Released under the Apache License, Version 2.0'
		log.info  ' url:http://www.apache.org/licenses/LICENSE-2.0'

		demarcation();
		log.info  'Bootstrapping....'
		
		separator();
		
		log.info  '>> DEFAULTS INITIALIZATION'
		separator();
		log.info  '** Users Roles'
		
		DefaultUsersRoles.values().each {
			if(!Role.findByAuthority(it.value())) {
				new Role(authority: it.value(), ranking: it.ranking(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}
		
		DefaultUsersProfilePrivacy.values().each {
			if(!ProfilePrivacy.findByValue(it.value())) {
				new ProfilePrivacy(value: it.value(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}

		// GROUPS
		// ------
		//////////ROLES
		separator();
		log.info  '** Groups Roles'
		DefaultGroupRoles.values().each {
			if(!GroupRole.findByAuthority(it.value())) {
				new GroupRole(authority: it.value(), ranking: it.ranking(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}		
		//////////STATUS
		separator();
		log.info  '** Groups Status'
		DefaultGroupStatus.values().each {
			if(!GroupStatus.findByValue(it.value())) {
				new GroupStatus(value: it.value(), uuid: it.uuid(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}
		//////////PRIVACY
		separator();
		log.info  '** Groups Privacy'
		DefaultGroupPrivacy.values().each {
			if(!GroupPrivacy.findByValue(it.value())) {
				new GroupPrivacy(value: it.value(), uuid: it.uuid(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}
		//////////USER STATUS IN GROUP
		separator();
		log.info  '** User Status in Group'
		DefaultUserStatusInGroup.values().each {
			if(!UserStatusInGroup.findByValue(it.value())) {
				new UserStatusInGroup(value: it.value(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}
		
		// SYSTEMS
		// -------
		//////////STATUS
		separator();
		log.info  '** Systems Status'
		DefaultSystemStatus.values().each {
			if(!SystemStatus.findByValue(it.value())) {
				new SystemStatus(value: it.value(), uuid: it.uuid(), label: it.label(), description: it.description()).save(failOnError: true)
				log.info "Initialized: " + it.value()
			} else {
				log.info "Found: " + it.value()
			}
		}
		
		separator();
		log.info  '>> AGENTS INITIALIZATION'
		separator();
		log.info  '** Users'
		
		def person = Person.findByEmail('paolo.ciccarese@gmail.com');
		if(person==null) {
			person = new Person(
				firstName: 'Jack', 
				lastName: 'White',
				displayName: 'Dr. White',
				email:'paolo.ciccarese@gmail.com'
			).save(flush: true, failOnError: true);
		}
		
		
		def password = 'password'
		def adminUsername = 'admin'
		log.info  "Initializing: " + adminUsername
		def admin = User.findByUsername(adminUsername);
		if(admin==null) {
			admin = new User(username: adminUsername,
				password: encodePassword(password), person: person,
				enabled: true, email:'paolo.ciccarese@gmail.com').save(failOnError: true)
			log.warn  "CHANGE PASSWORD for: " + adminUsername + "!!!"
		}
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.USER.value()))) 
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.USER.value())
			
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.MANAGER.value()))) 
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.MANAGER.value())
			
		if (!admin.authorities.contains(Role.findByAuthority(DefaultUsersRoles.ADMIN.value())))
			UserRole.create admin, Role.findByAuthority(DefaultUsersRoles.ADMIN.value())
	
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
		}
		
		//////////GROUPS TESTS
		separator();
		def group0 = "Test Group 0"
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
		
		demarcation();

    }
	def encodePassword(def password) {
		return springSecurityService.encodePassword(password)
	}
	def separator = {
		log.info  '------------------------------------------------------------------------';
	}
	def demarcation = {
		log.info  '========================================================================';
	}
    def destroy = {
    }
}

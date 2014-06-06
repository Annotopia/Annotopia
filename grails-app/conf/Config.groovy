import grails.util.Metadata
grails.app.context="/"

// configuration for plugin testing - will not be included in the plugin zip

// Necessary for Grails 2.0 as the variable ${appName} is not available
// anymore in the log4j closure. It needs the import above.
def appName = Metadata.current.getApplicationName();

grails.config.locations = ["classpath:${appName}-config.properties", "file:./${appName}-config.properties"]

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}


environments {
	development {
		log4j = {
		    appenders {
				console name:'stdout', threshold: org.apache.log4j.Level.ALL, 
					layout:pattern(conversionPattern: '%d{mm:ss,SSS} %5p %c{1} %m%n')
			}
		
		    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
		           'org.codehaus.groovy.grails.web.pages', //  GSP
		           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
		           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
		           'org.codehaus.groovy.grails.web.mapping', // URL mapping
		           'org.codehaus.groovy.grails.commons', // core / classloading
		           'org.codehaus.groovy.grails.plugins', // plugins
		           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
		           'org.springframework',
		           'org.hibernate',
		           'net.sf.ehcache.hibernate'
		
		    warn    'org.mortbay.log'
			
			info 	'grails.app', 									// Necessary for Bootstrap logging
			        'org.annotopia.grails.security'
			
			trace  	'grails.app.services.org.commonsemantics.grails.agents.services.AgentsService',
					'grails.app.services.org.commonsemantics.grails.users.services.UsersService',
					'grails.app.services.org.commonsemantics.grails.groups.services.GroupsService',
					'grails.app.services.org.commonsemantics.grails.systems.services.SystemsService',
					'grails.app.services.org.annotopia.grails.services.storage.jena.virtuoso.JenaVirtuosoStoreService',
					'grails.app.services.org.annotopia.grails.services.storage.jena.openannotation.OpenAnnotationWithPermissionsStorageService',
					'org.commonsemantics.grails.agents.utils',
					'org.commonsemantics.grails.users.utils',
					'org.commonsemantics.grails.groups.utils'
		}
	}
	
	production {
		log4j = {
			appenders {
				console name:'stdout', threshold: org.apache.log4j.Level.INFO,
					layout:pattern(conversionPattern: '%d{mm:ss,SSS} %5p %c{1} %m%n')
			}
		
			error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
				   'org.codehaus.groovy.grails.web.pages', //  GSP
				   'org.codehaus.groovy.grails.web.sitemesh', //  layouts
				   'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
				   'org.codehaus.groovy.grails.web.mapping', // URL mapping
				   'org.codehaus.groovy.grails.commons', // core / classloading
				   'org.codehaus.groovy.grails.plugins', // plugins
				   'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
				   'org.springframework',
				   'org.hibernate',
				   'net.sf.ehcache.hibernate'
		
			warn    'org.mortbay.log'
			
			info 	'grails.app', 									// Necessary for Bootstrap logging
					'org.annotopia.grails.security'
			
			debug  	'grails.app.services.org.commonsemantics.grails.agents.services.AgentsService',
					'grails.app.services.org.commonsemantics.grails.users.services.UsersService',
					'grails.app.services.org.commonsemantics.grails.groups.services.GroupsService',
					'grails.app.services.org.commonsemantics.grails.systems.services.SystemsService',
					'org.commonsemantics.grails.agents.utils',
					'org.commonsemantics.grails.users.utils',
					'org.commonsemantics.grails.groups.utils'
		}
	}
}

// Spring Security Configuration
// The following have to be defined in the Config.groovy of the main application in order for Spring Security to work properly
// -------------------------------------------------------------------------------------------------------------------------------------------
grails.plugin.springsecurity.userLookup.userDomainClassName 			= 'org.commonsemantics.grails.users.model.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName 			= 'org.commonsemantics.grails.users.model.UserRole'
grails.plugin.springsecurity.authority.className 						= 'org.commonsemantics.grails.users.model.Role'

grails.plugin.springsecurity.rememberMe.persistentToken.domainClassName = 'org.annotopia.grails.security.PersistentLogin'
grails.plugin.springsecurity.rememberMe.persistent 						= true

// Default since spring-security-core:2.0-RC2
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.logout.postOnly = false

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/public/**'							: ['permitAll'],
	'/openAnnotation/**'					: ['permitAll'],
	'/openAnnotationWithPermissions/**'		: ['permitAll'],
	'/annotationIntegrated/**'				: ['permitAll'],
	'/secure/**'				: ['ROLE_ADMIN'],
	'/secret/**'				: ['ROLE_ADMIN'],
	'/crunch/**'				: ['ROLE_ADMIN'],
	'/dashboard/**'				: ['ROLE_ADMIN'],
	'/dashboardAjax/**'				: ['ROLE_ADMIN'],
	'/**/js/**'					: ['permitAll'],
	'/**/css/**'        		: ['permitAll'],
	'/**/images/**'     		: ['permitAll'],
	'/**/favicon.ico'   		: ['permitAll']
]

cors.url.pattern = ['/s/annotation/*','/s/annotationset/*']
cors.headers = ['Access-Control-Allow-Origin':'*']

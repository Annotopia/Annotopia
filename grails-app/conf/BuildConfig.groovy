grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

//grails.plugin.location.'at-smart-storage' = '../AtSmartStorage'
grails.plugin.location.'at-data-cruncher' = '../AtDataCruncher'

grails.plugin.location.'cs-commons' = '../commonsemantics/CsCommons'
grails.plugin.location.'cs-systems' = '../commonsemantics/CsSystems'
grails.plugin.location.'cs-agents' = '../commonsemantics/CsAgents'
grails.plugin.location.'cs-groups' = '../commonsemantics/CsGroups'
grails.plugin.location.'cs-users' = '../commonsemantics/CsUsers'
grails.plugin.location.'cs-security-dashboard' = '../commonsemantics/CsSecurityDashboard'


grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
		
		mavenRepo "http://repo.spring.io/milestone/"
		mavenRepo "https://repository.apache.org/content/repositories/snapshots/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.

        // runtime 'mysql:mysql-connector-java:5.1.22'
		
		runtime 'virtuoso:virtjdbc:4'
		runtime 'virtuoso.sesame:virt_jena:2'
		
		compile ("org.apache.jena:jena-core:2.11.2-SNAPSHOT") {
			excludes 'slf4j-api', 'xercesImpl'
		}
		compile ("org.apache.jena:jena-arq:2.11.2-SNAPSHOT")
		
		compile ("xml-apis:xml-apis:1.4.01") {
			excludes 'xercesImpl'
		}

		compile("xerces:xercesImpl:2.9.1") {
			excludes 'xml-apis'
		}
		
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.1.6"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.3.2"

        compile ':cache:1.0.1'
		
		compile ":spring-security-core:2.0-RC2"
    }
}

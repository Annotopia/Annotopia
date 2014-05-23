
// H2 In Memory Database Configuration
/*
dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}

environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
*/

// MySql Database Configuration
// Driver: grails install-dependency mysql:mysql-connector-java:5.1.16
// Then add to BuildConfig the dependency: runtime 'mysql:mysql-connector-java:5.1.16'
dataSource {
	pooled = true
	driverClassName = "com.mysql.jdbc.Driver"
	username = "root"
	password = "CfwAM69M"
	dialect = org.hibernate.dialect.MySQL4InnoDBDialect
	logSql = false
	properties {
		maxActive = 50
		maxIdle = 25
		minIdle = 5
		initialSize = 5
		minEvictableIdleTimeMillis = 60000
		timeBetweenEvictionRunsMillis = 60000
		maxWait = 10000
		validationQuery = "/* ping */"
	}
}

// CREATE DATABASE AnnotopiaDev DEFAULT CHARACTER SET utf8;
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost/AnnotopiaDev?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/AnnotopiaTest?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8"
			loggingSql = true
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://localhost/Annotopia?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8"
		}
	}
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}



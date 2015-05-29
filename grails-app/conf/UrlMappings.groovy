class UrlMappings {

	static mappings = {
		// Discovering user identity from OAuth token
		"/u/whoami" {
			controller = "identity"
			action = [GET:"whoami"]
		}
		// New "RESTful"/"incremental" API
		"/s/annotationset/$id/annotations"{
			controller = "openAnnotationSetREST"
			action = [POST:"createAnnotation"]
		}
		// Storage
		"/s/annotationset/$id?"{
			controller = "annotationIntegrated"
			action = [GET:"show", PUT:"update"]
		}
		"/s/annotationset"{
			controller = "annotationIntegrated"
			action = [GET:"index", POST:"create"]
		}
		"/s/annotation/$id?"{
			controller = "openAnnotation"       /* Plain single Open Annotation */
			/*controller = "openAnnotationWithPermissions"*/ 
			/*controller = "annotationIntegrated"*/ /* Domeo-Utopia */
			action = [GET:"show", POST:"save", PUT:"update", DELETE:"delete"]
		}

		// BioPortal Connector
		"/cn/bioportal/search"{
			controller = "bioPortal"
			action = [GET:"search"]
		}

		"/cn/bioportal/textmine"{
			controller = "bioPortal"
			action = [POST:"textmine"]
		}

		"/cn/bioportal/vocabularies"{
			controller = "bioPortal"
			action = [GET:"vocabularies"]
		}

		"/cn/nif/textmine"{
			controller = "nif"
			action = [POST:"textmine"]
		}

		"/cn/nif/search"{
			controller = "nif"
			action = [GET:"search"]
		}

		"/cn/ebi/textmine"{
			controller = "ebi"
			action = [POST:"textmine"]
		}

		"/cn/pubmed/$action" (controller: "pubmed")

		// Validation
		"/oa/validate"{
			controller = "openAnnotation"
			action = "validate"
		}
		"/oa/search"{
			controller = "openAnnotation"
			action = "search"
		}

		"/api/stats/$action?" {
			controller = "openAnnotationReporting"
		}

		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(action:"index", controller:"public")
		"500"(view:'/error')
	}
}

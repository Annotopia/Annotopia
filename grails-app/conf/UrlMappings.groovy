class UrlMappings {

	static mappings = {
		// Storage
		"/s/annotationset/$id?"{
			controller = "annotationIntegrated"
			action = [GET:"showAnnotationSet", POST:"saveAnnotationSet", PUT:"updateAnnotationSet"]
		}
		"/s/annotation/$id?"{
			controller = "openAnnotation"       /* Plain single Open Annotation */
			/*controller = "openAnnotationWithPermissions" */
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

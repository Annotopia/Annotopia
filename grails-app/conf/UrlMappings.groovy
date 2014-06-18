class UrlMappings {

	static mappings = {
		"/s/annotationset/$id?"{
			controller = "annotationIntegrated"
			action = [GET:"showAnnotationSet", POST:"saveAnnotationSet", PUT:"updateAnnotationSet"]
		}
		"/s/annotation/$id?"{
			controller = "openAnnotation"       /* Plain single Open Annotation */
			/*controller = "openAnnotationWithPermissions"*/
			/*controller = "annotationIntegrated"*/ /* Domeo-Utopia */
			action = [GET:"show", POST:"save", PUT:"update", DELETE:"delete"]
		}
		
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

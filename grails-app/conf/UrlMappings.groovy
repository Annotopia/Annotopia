class UrlMappings {

	static mappings = {
		"/s/annotationset/$id?"{
			controller = "annotationIntegrated"
			action = [GET:"show", POST:"save", PUT:"update"]
		}
		"/s/annotation/$id?"{
			/*controller = "openAnnotation"*/
			controller = "openAnnotationWithPermissions"
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

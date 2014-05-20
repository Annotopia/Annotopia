class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(action:"index", controller:"public")
		"500"(view:'/error')
	}
}

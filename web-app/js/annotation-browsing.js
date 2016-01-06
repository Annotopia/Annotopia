function AnnotationBrowsingCtrl($scope, $sce, $http) {
	
	$scope.$watch('$viewContentLoaded', function(){
		// Whatever initialization
	});
	
	$scope.totalResults = 0;
	$scope.duration = '';

	// ---------------------------
	//  FACETS
	// ---------------------------	
	
/*
	$scope.permissions = [
	    {name:'public', selected:true},
        {name:'private', selected:true}
	];
	
	// selected fruits
	$scope.selectionPermissions = [];

	// helper method to get selected fruits
	$scope.selectedPermissions = function selectedPermissions() {
	    return filterFilter($scope.permissions, { selected: true });
	};

	// watch fruits for changes
	$scope.$watch('permissions|filter:{selected:true}', function (nv) {
	    $scope.selectionPermissions = nv.map(function (permission) {
	        return permission.name;
	    });
	}, true);
*/
	
	$scope.isArray = angular.isArray;
	
	$scope.sources = [
          {name:'domeo', selected:true},
          {name:'utopia', selected:true},
          {name:'any', selected:true},
          {name:'unspecified', selected:true}
    ];
  	                  
  	// selected fruits
  	$scope.selectionSources = [];

  	// helper method to get selected fruits
  	$scope.selectionSources = function selectedSources() {
  	    return filterFilter($scope.sources, { selected: true });
  	};

  	// watch fruits for changes
  	$scope.$watch('sources|filter:{selected:true}', function (nv) {
  	    $scope.selectionSources = nv.map(function (source) {
  	        return source.name;
  	    });
  	}, true);

	$scope.motivations = [
        {name:'commenting', selected:true},
        {name:'highlighting', selected:true},
        {name:'tagging', selected:true},
        {name:'describing', selected:true},
        {name:'micropublishing', selected:true},
        {name:'unmotivated', selected:true}
    ];
	                  
	// selected fruits
	$scope.selectionMotivations = [];

	// helper method to get selected fruits
	$scope.selectionMotivations = function selectedMotivations() {
	    return filterFilter($scope.motivations, { selected: true });
	};

	// watch fruits for changes
	$scope.$watch('motivations|filter:{selected:true}', function (nv) {
	    $scope.selectionMotivations = nv.map(function (motivation) {
	        return motivation.name;
	    });
	}, true);
	                      
	
	// ---------------------------
	//  BROWSING
	// ---------------------------	
	$scope.browse = function(page) {
		$('#progressIcon').show();
		$scope.paginationMax = $scope.paginationSizeSelection.name;
		$scope.paginationOffset = (page ? page*$scope.paginationMax:0);
		var results;
		
		var sources = ($scope.selectionSources!=undefined)?'&sources=' +$scope.selectionSources:''
		var motivations = ($scope.selectionMotivations!=undefined)?'&motivations=' +$scope.selectionMotivations:''
		var permissions = ($scope.selectionPermissions!=undefined)?'&permissions=' + $scope.selectionPermissions:''
		
	    $http({method: 'GET', url: '/secure/getAnnotation?max=' + $scope.paginationMax + '&offset=' + $scope.paginationOffset  + sources + permissions + motivations + '&outCmd=frame'}).
		    success(function(data, status, headers, config) {
		    	results = data.result.items;
		    	
		    	// Cache all items
//		    	$scope.cache = [];
//		    	$scope.retrieveById(results, "http://127.0.0.1:8888/tests/index.html", $scope.cache);
//		    	console.error(JSON.stringify($scope.cache));
		    	
		    	$scope.totalResults = data.result.total;
		    	$scope.duration = data.result.duration;
		    	
				$scope.paginationTotal = data.result.total;
				$scope.annotationResults = [];
				for ( var i = 0; i < results.length; i++) {
					$scope.annotationResults.push(results[i]['@graph']);
				}
				$scope.refreshPagination(page);
				$('#progressIcon').hide();
		    }).
		    error(function(data, status, headers, config) {
		    	results = data;
		    	$('#progressIcon').hide();
		    });
		
		console.log("browse [page:" + (page?page:0) + ", max:" + $scope.paginationMax + "]")
	};
	
	
	/*
	---------------------------------------------------------------------
	 Object retrieval
	---------------------------------------------------------------------
	*/
	$scope.retrieveById = function(json, id, matches) {
		$scope.traverse(json, 0, id, matches);
		return matches[0];
	}
	
	/*
	---------------------------------------------------------------------
	 JSON traversal
	---------------------------------------------------------------------
	*/
	$scope.isArray = function(what) {
	    return Object.prototype.toString.call(what) === '[object Array]';
	}
	
	$scope.traverseArray = function(arr, level, targetId, matches) {
		//console.log(level + "<array>");
		arr.forEach(function(x) {
			$scope.traverse(x, level + "  ", targetId, matches);
		});
	}
	
	$scope.traverseObject = function(obj, level, targetId, matches) {
		//console.log(level + "<object>");
		for (var key in obj) {
		    if (obj.hasOwnProperty(key)) {
		    	if(key==="@context") continue;
		    	if(key==="@id") {
		    		//console.log(obj[key]);
		    		if(obj[key]===targetId) {
		    			matches.push(obj);
			    	}
		    	}
		    	//console.log(level + "  " + key + ":");
		    	$scope.traverse(obj[key], level + "    ", targetId, matches);
		    }
		}
	}
	
	$scope.traverse = function(x, level, targetId, matches) {
		if ($scope.isArray(x)) {
			$scope.traverseArray(x, level, targetId, matches);
		} else if ((typeof x === 'object') && (x !== null)) {
			$scope.traverseObject(x, level, targetId, matches);
		} else {
		    //console.log(level + x);
		}
	}
	
	// ---------------------------
	//  PAGINATION
	// ---------------------------	
	$scope.paginationSize = [
		{ name: "2", id: 0 }, 
		{ name: "10", id: 1 }, 
		{ name: "20", id: 2 }, 
		{ name: "30", id: 2 }, 
		{ name: "40", id: 2 }, 
		{ name: "50", id: 2 }
	];
	
	$scope.pages = [];
	
	$scope.paginationSizeSelection = $scope.paginationSize[1];
	$scope.paginationTotal = 0;
	$scope.paginationOffset = 0;
	$scope.paginationMax = 0;
	$scope.paginationTop = '';
	$scope.currentPage = 0;
	
	$scope.refreshPagination  = function(page) {
		console.log('refreshingPagination max: ' + $scope.paginationMax + ' offset: ' + $scope.paginationOffset + ' total: ' + $scope.paginationTotal);
		
		var linksNumber = Math.floor($scope.paginationTotal /  $scope.paginationMax) + (($scope.paginationTotal %  $scope.paginationMax>0)?1:0);
		console.log("# links "+ linksNumber);
		$scope.currentPage = page
			
		$scope.pages = [];
		for(var i=0;i<linksNumber;i++) {
			$scope.pages.push(i);
		}
	}
	
	$scope.$watch('paginationSizeSelection', function(){
		$scope.browse(0);
	});

	// ---------------------------
	//  Lenses
	// ---------------------------
	$scope.exploreUser = function(user) {
		$('#sidebar').html(user['@type'] + '<br/>' +
			'user: ' + user['@id'] + '<br/>' +
			'name: ' + user['name']);
	}
	
	$scope.exploreResource = function(resource) {
		var ids = ''
		if(resource['http://purl.org/vocab/frbr/core#embodimentOf']) {
			if(resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://prismstandard.org/namespaces/basic/2.0/doi'])
				ids += 'doi: ' + resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://prismstandard.org/namespaces/basic/2.0/doi'] + '<br/>'
			if(resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPubMedCentralId'])
				ids += 'pmcid: ' + resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPubMedCentralId'] + '<br/>'
			if(resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPubMedId'] )
				ids += 'pmid: ' + resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPubMedId'] + '<br/>'
			if(resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPII'])
				ids += 'pii: ' + resource['http://purl.org/vocab/frbr/core#embodimentOf']['http://purl.org/spar/fabio#hasPII'] + '<br/>'
		}
			
		$('#sidebar').html(
				'Target: <br/>' +
			resource['@id'] + '<br/>' + 
			'type: ' +resource['@type'] + '<br/>' +
			'format: ' + resource['format'] + '<br/>' +
			ids
			);
	}
	
	$scope.exploreSemanticTag = function(semantictag) {
		$('#sidebar').html('Semantic Tag: <br/>' + semantictag['@id'] + '<br/>' +
				'label: ' + semantictag['label']
				);
	}
}
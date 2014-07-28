function AnnotationBrowsingCtrl($scope, $sce, $http) {
	
	$scope.$watch('$viewContentLoaded', function(){
		// Whatever initialization
	});
	
	$scope.totalResults = 0;
	$scope.duration = '';
	
	$scope.permissions = [
	    {name:'public', selected:true},
        {name:'private', selected:true}
	];
	$scope.motivations = [
        {name:'commenting', selected:true},
        {name:'highlighting', selected:true},
        {name:'tagging', selected:true}
    ];
	                      
	                      
	
	// ---------------------------
	//  BROWSING
	// ---------------------------	
	$scope.browse = function(page) {
		$('#progressIcon').show();
		$scope.paginationMax = $scope.paginationSizeSelection.name;
		$scope.paginationOffset = (page ? page*$scope.paginationMax:0);
		var results;
	    $http({method: 'GET', url: '/secure/getAnnotation?max=' + $scope.paginationMax + '&offset=' + $scope.paginationOffset + '&outCmd=frame'}).
		    success(function(data, status, headers, config) {
		    	results = data.result.items;
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
		    	results = mockupResults;
		    	$('#progressIcon').hide();
		    });
		
		console.log("browse [page:" + (page?page:0) + ", max:" + $scope.paginationMax + "]")
	};
	
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
		//console.log($scope.paginationTotal /  $scope.paginationMax);
		//console.log(Math.floor($scope.paginationTotal /  $scope.paginationMax));
		//console.log($scope.paginationTotal %  $scope.paginationMax)
		
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
}
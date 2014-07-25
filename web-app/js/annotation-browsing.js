function AnnotationBrowsingCtrl($scope, $sce, $http) {
	
	$scope.$watch('$viewContentLoaded', function(){
		// Whatever initialization
	});
	
	$scope.totalResults = 0;
	$scope.duration = '';
	
	// ---------------------------
	//  BROWSING
	// ---------------------------	
	$scope.browse = function(page) {
		$('#progressIcon').show();
		$scope.paginationMax = $scope.paginationSizeSelection.name;
		var results;
	    $http({method: 'GET', url: '/secure/getAnnotation?max=' + $scope.paginationMax + '&offset=' + $scope.paginationOffset + '&outCmd=frame'}).
		    success(function(data, status, headers, config) {
		    	results = data.result.items;
		    	$scope.totalResults = data.result.total;
		    	$scope.duration = data.result.duration;

		    	$scope.paginationOffset = (page ? page*$scope.paginationMax:0);
				$scope.paginationTotal = data.result.total;

				$scope.annotationResults = [];
				for ( var i = $scope.paginationOffset; i < (Math.min($scope.paginationOffset+$scope.paginationMax,$scope.paginationTotal)); i++) {
					$scope.annotationResults.push(results[i]['@graph']);
				}		
				$scope.refreshPagination();
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
	
	$scope.refreshPagination  = function() {
		console.log('refreshingPagination max: ' + $scope.paginationMax + ' offset: ' + $scope.paginationOffset + ' total: ' + $scope.paginationTotal);
		//console.log($scope.paginationTotal /  $scope.paginationMax);
		//console.log(Math.floor($scope.paginationTotal /  $scope.paginationMax));
		//console.log($scope.paginationTotal %  $scope.paginationMax)
		
		var linksNumber = Math.floor($scope.paginationTotal /  $scope.paginationMax) + (($scope.paginationTotal %  $scope.paginationMax>0)?1:0);
		console.log("# links "+ linksNumber);
		var currentPage = 0
		if($scope.paginationOffset>0) currentPage = Math.floor($scope.paginationTotal /  $scope.paginationOffset);
			
		$scope.pages = [];
		for(var i=0;i<linksNumber;i++) {
			$scope.pages.push(i);
		}
	}
	
	$scope.$watch('paginationSizeSelection', function(){
		$scope.browse(0);
	});
}
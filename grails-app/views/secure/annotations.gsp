<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="secure"/>
		<title>Annotations</title>
		
		<script type="text/javascript" src="${resource(dir: 'js/angular', file: 'angular.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js/angular', file: 'angular-resource.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'annotation-browsing.js')}"></script>
		
		<style>
			.counter {
				font-size:18px; 
				padding-right: 5px;
			}
			
			.titleBar {
				font-weight: bold;
			}
		</style>
	</head>
	<body>
		<div class="container">
			<div ng-app="" ng-controller="AnnotationBrowsingCtrl">
				<form ng-submit="browse()">
					<div id="sidebar" class="viewerSidebar well" >
				    	<div id='contributorsTitle'>Filtering by permissions</div>
						<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
				    	<div style="padding: 5px; padding-top: 10px; ">
						    <input id="publicFilter" type="checkbox" name="vehicle" checked="checked"> Public<br>
						    <input id="privateFilter" type="checkbox" name="private"> Private<br/>
							<br/>										
							<div align="center"><input class="btn btn-primary" type="submit" value="Refresh"> </div>
						</div>
				  	</div>
					<div style="background: #fff; padding: 5px;">				
						<%-- 
						<input type="text" ng-model="searchText" size="30" placeholder="search bibliography"> 
						<input class="btn btn-primary" type="submit" value="Search"> 
						<span ng-bind="paginationTotal"></span> Results 
						--%>
						
						<span style="float: right; padding-right: 10px">Display: 
							<select	data-ng-options="o.name for o in paginationSize"
								data-ng-model="paginationSizeSelection"></select>
						</span>
					</div>
				</form>
				<div style="padding:0px; padding-top: 10px; min-height: 260px;">
					<table class="tablelist">
						<thead>
							<tr>
								<th><input id="selectAll" type="checkbox" ng-click="selectAll($event)"></th>
								<th></th>				
								<th></th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="annotation in annotationResults" ng-class="$index % 2 == 0 && 'even' || 'odd'">
								<td></td>
								<td><a href="{{annotation['hasTarget']['@id']}}">{{annotation['hasTarget']['@id']}}</a> createdWith {{annotation['serializedBy']}}</td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div style="padding:0px; padding-top: 10px; min-height: 260px;">
					<div ng-if="pages.length>1">
						<div class="pagination" style="padding-left:10px;"> 
							<span ng-repeat="page in pages">
								<a href="" ng-click="search(page)">{{page}}</a>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
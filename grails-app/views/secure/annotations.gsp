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
								<th>Annotation</th>				
								<th></th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="annotation in annotationResults" ng-class="$index % 2 == 0 && 'even' || 'odd'">
								<td>
									<input id="annotation" type="checkbox">
								</td>
								<td>
									<span ng-if="annotation[0]['hasTarget']['format']=='text/html'"><img src="${resource(dir:'images/secure',file:'file_html.png')}" style="width:40px;"/></span>
									<span ng-if="annotation[0]['hasTarget']['format']=='application/pdf'"><img src="${resource(dir:'images/secure',file:'file_pdf.png')}" style="width:40px;"/></span>
								</td>
								<td style="vertical-align: top;">
									Annotation on <a href="{{annotation[0]['hasTarget']['@id']}}">{{annotation[0]['hasTarget']['@id']}}</a> 
									<span ng-if="annotation[0]['serializedBy']!=null">createdWith {{annotation[0]['serializedBy']}}</span><br/>
									<span ng-if="annotation[0]['annotatedBy']!=null && annotation[0]['annotatedBy']['name']!=null">by {{annotation[0]['annotatedBy']['name']}}</span>
									<span ng-if="annotation[0]['annotatedAt']!=null"> on {{annotation[0]['annotatedAt']}}</span>	
								</td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div style="padding:0px; padding-top: 10px; min-height: 260px;">
					<div class="pagination" style="padding-left:10px;"> 
						Pages
						<span ng-repeat="page in pages">
							<a href="" ng-click="browse(page)">{{page}}</a>
						</span>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
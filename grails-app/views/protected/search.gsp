<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="protected"/>
		<title>Search Annotopia</title>
		
		<script type="text/javascript" src="${resource(dir: 'js/angular', file: 'angular.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js/angular', file: 'angular-resource.min.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'protected-annotation-search.js')}"></script>
		
		<style>
			.counter {
				font-size:18px; 
				padding-right: 5px;
			}
			
			.titleBar {
				font-weight: bold;
			}
			
			.odd {
				background: none repeat scroll 0 0 #F3F3F3;
			}
			
			.even {
				background: none repeat scroll 0 0 #FFFFFF;
			}
			
			
			
			.ann-body-content {
				padding: 5px;
				font-weight: bold;
				/*border-left: 3px solid #bbb;*/
			}
			
			.pagination {
			    margin-bottom: 20px;
			}
			
			.page {
			    display: inline-block;
			    padding: 0px 9px;
			    margin-right: 4px;
			    border-radius: 3px;
			    border: solid 1px #c0c0c0;
			    background: #e9e9e9;
			    box-shadow: inset 0px 1px 0px rgba(255,255,255, .8), 0px 1px 3px rgba(0,0,0, .1);
			    font-size: .875em;
			    font-weight: bold;
			    text-decoration: none;
			    color: #717171;
			    text-shadow: 0px 1px 0px rgba(255,255,255, 1);
			}
			
			.page:hover, .page.gradient:hover {
			    background: #fefefe;
			    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#FEFEFE), to(#f0f0f0));
			    background: -moz-linear-gradient(0% 0% 270deg,#FEFEFE, #f0f0f0);
			}
			
			.page.active {
			    border: none;
			    background: #616161;
			    box-shadow: inset 0px 0px 8px rgba(0,0,0, .5), 0px 1px 0px rgba(255,255,255, .8);
			    color: #f0f0f0;
			    text-shadow: 0px 0px 3px rgba(0,0,0, .5);
			}
			
			.page.gradient {
			    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#f8f8f8), to(#e9e9e9));
			    background: -moz-linear-gradient(0% 0% 270deg,#f8f8f8, #e9e9e9);
			}
			
			.pagination.dark {
			    background: #414449;
			    color: #feffff;
			}
			
			.page.dark {
			    border: solid 1px #32373b;
			    background: #3e4347;
			    box-shadow: inset 0px 1px 1px rgba(255,255,255, .1), 0px 1px 3px rgba(0,0,0, .1);
			    color: #feffff;
			    text-shadow: 0px 1px 0px rgba(0,0,0, .5);
			}
			
			.page.dark:hover, .page.dark.gradient:hover {
			    background: #3d4f5d;
			    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#547085), to(#3d4f5d));
			    background: -moz-linear-gradient(0% 0% 270deg,#547085, #3d4f5d);
			}
			
			.page.dark.active {
			    border: none;
			    background: #2f3237;
			    box-shadow: inset 0px 0px 8px rgba(0,0,0, .5), 0px 1px 0px rgba(255,255,255, .1);
			}
			
			.page.dark.gradient {
			    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#565b5f), to(#3e4347));
			    background: -moz-linear-gradient(0% 0% 270deg,#565b5f, #3e4347);
			}
			
			
			blockquote.contextQuote {
			  font: 14px/20px;
			  padding-left: 40px;
			  padding-right: 10px;
			  min-height: 40px;
			  margin: 5px;
			  /*background-image: url(../../images/secure/quotes.png);*/
			  background-position: middle left;
			  background-repeat: no-repeat;
			  text-indent: 5px;
			} 

			.contextPrefix {
				color: #aaa;
				font-style: italic;
			}
			
			.contextMatch {
				font-weight: bold;
			}
			
			.contextSuffix {
				color: #aaa;
				font-style: italic;
			}
			
			.contextTitle {
				text-align: left;
				padding-left: 5px;
			}
			
			.facet {
				float: left;
				padding-left: 15px;
			}
			
			#searchfacet {
				width: 100%;
				padding-left: 15px;
				padding-bottom: 10px;
			}
		</style>
	</head>
	<body>
		<div class="container">
			<div ng-app="" ng-controller="AnnotationBrowsingCtrl">
				<form ng-submit="search()">
					
					<div id="facets" class="viewerSidebar well" >
						<div id="searchfacet">
							<input type="search" ng-model="searchText" placeholder="Search..." style="width: 590px;">						
						</div>
						
						<div class="facet">
					    	<div id='contributorsTitle'>Filtering by permissions</div>
							<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
					    	<div style="padding: 5px; padding-top: 10px; ">
					    		<div ng-repeat="permission in permissions">
					    			<input
									    type="checkbox"
									    name="selectedPermissions[]"
									    value="{{permission.name}}"
									    ng-model="permission.selected"
									  > {{permission.name}} 
					    		</div>
							</div>
						</div>
						
						<div class="facet">
					    	<div id='contributorsTitle'>Include</div>
							<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
					    	<div style="padding: 5px; padding-top: 10px; ">
							    <div ng-repeat="inclusionCriteria in inclusionCriterias">
					    			<input
									    type="checkbox"
									    name="selectedInclusionCriteria[]"
									    value="{{inclusionCriteria.name}}"
									    ng-model="inclusionCriteria.selected"
									  > {{inclusionCriteria.name}} 
					    		</div>
							</div>
						</div>
						<div class="facet">
					    	<div id='contributorsTitle'>Filtering by source</div>
							<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
					    	<div style="padding: 5px; padding-top: 10px; ">
							    <div ng-repeat="source in sources">
					    			<input
									    type="checkbox"
									    name="selectedSources[]"
									    value="{{source.name}}"
									    ng-model="source.selected"
									  > {{source.name}} 
					    		</div>
							</div>
						</div>
						<div class="facet">
					    	<div id='contributorsTitle'>Filtering by type</div>
							<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
					    	<div style="padding: 5px; padding-top: 10px; ">
							    <div ng-repeat="motivation in motivations">
					    			<input
									    type="checkbox"
									    name="selectedMotivations[]"
									    value="{{motivation.name}}"
									    ng-model="motivation.selected"
									  > {{motivation.name}} 
					    		</div>
							</div>
						</div>
						<br/>										
						<div align="center" style="clear: left;"><input class="btn btn-primary" type="submit" value="Refresh"> </div>
				  	</div>
					<div style="background: #fff; padding: 5px; height: 30px; padding-bottom: 10px;">				
						<%-- 
						<input type="text" ng-model="searchText" size="30" placeholder="search bibliography"> 
						<input class="btn btn-primary" type="submit" value="Search"> 
						<span ng-bind="paginationTotal"></span> Results 
						--%>
						<table style="width: 700px;">
							<tr>
								<td>
								 	<span style="padding-left: 10px">
								    	<span class='counter'>{{annotationResults.length}}</span> of 
								    	<span class='counter'>{{totalResults}}</span> 
								    	<span ng-if="totalResults==1">annotation</span>
								    	<span ng-if="totalResults!=1">annotations</span>
							    	in {{duration}}</span> 
								</td>
								<td>
									<div id="progressIcon" align="center" style="padding-left: 10px; display: none;">
								    	<img id="groupsSpinner" src="${resource(dir:'images/secure',file:'ajax-loader-4CAE4C.gif')}" /> Searching...
								    </div>
								</td>
								<td style="text-align: right;">
									<span style="padding-right: 10px">Display: 
										<select	data-ng-options="o.name for o in paginationSize"
											data-ng-model="paginationSizeSelection"></select>
									</span>
								</td>
							</tr>
						</table>
					</div>
				</form>
				<div id="maincontent" style="width: 700px; margin:0px; float: left;">
					<div style="padding:0px; padding-top: 10px; min-height: 60px;">
						<table class="tablelist" style="width: 700px;">
							<thead>
								<tr>
									<th style="padding: 2px;"><input id="selectAll" type="checkbox" ng-click="selectAll($event)"></th>
									<th></th>	
									<th>Annotation</th>				
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="annotation in annotationResults" ng-class="$index % 2 == 0 && 'odd' || 'even'">
									<td style="vertical-align: top; padding-top: 10px;">
										<input id="annotation" type="checkbox">
									</td>
									<td style="vertical-align: top; padding: 5px; text-align: center;">
										<span ng-if="annotation[0]['hasTarget']['format']=='text/html' || annotation[0]['hasTarget']['hasSource']['format']=='text/html'"><img src="${resource(dir:'images/secure',file:'file_html.png')}" style="width:40px;"/></span>
										<span ng-if="annotation[0]['hasTarget']['format']=='application/pdf' || annotation[0]['hasTarget']['hasSource']['format']=='application/pdf'"><img src="${resource(dir:'images/secure',file:'file_pdf.png')}" style="width:40px;"/></span>
									</td>
									<td style="vertical-align: top; padding-bottom: 10px; ">
										<span ng-switch="annotation[0]['motivatedBy']" style="font-size: 11px; font-weight: bold; text-transform:uppercase; letter-spacing:1px">
											<span ng-switch-when="oa:commenting">Comment</span>
											<span ng-switch-when="oa:highlighting">Highlight</span>
											<span ng-switch-when="oa:tagging">Tagging</span>
											<span ng-switch-default>Annotation</span>
										</span>
										
										<%-- <span ng-if="annotation[0]['serializedBy']!=null">createdWith {{annotation[0]['serializedBy']}}</span> --%>
										<span ng-if="annotation[0]['annotatedBy']!=null && annotation[0]['annotatedBy']['name']!=null" style="font-size: 12px;">
											<a ng-click="exploreUser(annotation[0]['annotatedBy'])">by {{annotation[0]['annotatedBy']['name']}}</a>
										</span>
										<span ng-if="annotation[0]['annotatedAt']!=null" style="font-size: 12px;"> on {{annotation[0]['annotatedAt']}}</span>	
										<hr style="height: 5px; padding:0px; margin-top: 4px; margin-bottom: 0px; border-top: 0px dotted #aaa;"/>
										
										<span ng-switch="annotation[0]['motivatedBy']">
											<div  ng-switch-when="oa:commenting" class="ann-body-content"  style="background: #428bca; color: white; border-radius: 5px; padding: 5px;">{{annotation[0].hasBody.chars}}</div>
											<div  ng-switch-when="oa:highlighting" class="ann-body-content" style="background: yellow; border-radius: 5px;  padding: 5px;">{{annotation[0]['hasTarget']['hasSelector'].exact}}</div>
											<div  ng-switch-when="oa:tagging" class="ann-body-content" >
												<span ng-if="annotation[0].hasBody.length>0">
													<span ng-repeat="body in annotation[0].hasBody">
														<a style="background:#CC6600; color:white; border-radius:5px; padding:5px; cursor:pointer;" ng-click="exploreSemanticTag(body)">
														{{body.label}}
														</a>
														&nbsp;
													</span>
												</span>
												<span ng-if="!annotation[0].hasBody.length">												
													<a style="background:#CC6600; color:white; border-radius:5px; padding:5px; cursor:pointer;" ng-click="exploreSemanticTag(annotation[0].hasBody)">
													{{annotation[0].hasBody.label}}
													</a>
													&nbsp;
												</span>
											</div>
										</span>
										
										<%-- Display of textual fragment if any --%>
										<span ng-if="annotation[0]['hasTarget']['hasSelector']['@type']=='oa:TextQuoteSelector'">
											<hr style="height: 5px; padding:0px; margin-top: 4px; margin-bottom: 4px; border-bottom: 1px dotted #aaa;"/>
											<div class="contextTitle">Annotating 
												<span style="font-size: 12px;"> <a ng-click="exploreResource(annotation[0]['hasTarget']['hasSource'])" style="cursor:pointer;">{{annotation[0]['hasTarget']['hasSource']['@id']}}</a></span> 
											</div>
											<blockquote class="contextQuote">
												...
									       		<span class="contextPrefix">{{annotation[0]['hasTarget']['hasSelector'].prefix}}</span>
									       		<span class="contextMatch">{{annotation[0]['hasTarget']['hasSelector'].exact}}</span>
									       		<span class="contextSuffix">{{annotation[0]['hasTarget']['hasSelector'].suffix}}</span>
									       		...
									       	</blockquote>
										</span>
										<span ng-if="annotation[0]['hasTarget']['hasScope']!=null && annotation[0]['hasTarget']['hasSource']['format']=='image/jpeg'">
											<div class="contextTitle">Annotating 
												<span style="font-size: 12px; cursor:pointer;"> <a ng-click="exploreResource(annotation[0]['hasTarget']['hasSource'])">{{annotation[0]['hasTarget']['hasSource']['@id']}}</a></span> 
											</div> 
											<div style="padding:5px;">
												<img src="{{annotation[0]['hasTarget']['hasSource']['@id']}}" style="max-width:580px"/>
											</div>
										</span>
									</td>
									<td style="vertical-align: top; padding: 5px; padding-left: 10px; text-align: center;">
										<div style="border: 1px solid #ccc; padding: 5px; border-radius: 5px;">
											<span style="font-size:12px;border-bottom: solid 1px gray; text-transform: uppercase;">Access</span><br/>										
											<span ng-if="annotation[0]['permissions']==null" style="font-size:11px;">?</span>
											<span ng-if="annotation[0]['permissions']!=null">
												<span ng-if="annotation[0]['permissions']['read']==null"><img src="${resource(dir:'images/secure',file:'public.png')}" title="Public annotation" style="width:24px;"/><br/><span style="font-size:11px;">Public</span></span>
												<%--
												--%>
												<span ng-if="annotation[0]['permissions']['read']!=null">
													<span ng-if="!isArray(annotation[0]['permissions']['read'])">
														<img src="${resource(dir:'images/secure',file:'person.png')}" title="Private annotation" style="width:24px;"/>
														<br/><span style="font-size:11px;">Private</span>
													</span>
													<span ng-if="isArray(annotation[0]['permissions']['read'])">
														<img src="${resource(dir:'images/secure',file:'people.png')}" title="Group shared annotation" style="width:24px;"/>
														<br/><span style="font-size:11px;">Shared</span>
													</span>
												</span>
											</span>
										</div>
										<div style="line-height: 4px;">&nbsp;</div>
										<div style="border: 1px solid #ccc; padding: 5px; border-radius: 5px;">
											<span style="font-size:12px;  border-bottom: solid 1px gray; text-transform: uppercase;">Source</span><br/>
											<span ng-if="annotation[0]['serializedBy']=='urn:application:domeo'">
												<img src="${resource(dir:'images/secure',file:'domeo_logo.png')}" title="Domeo Annotation Tool" style="width:24px;"/><br/>
												<span style="font-size:11px;">Domeo</span>											
											</span>
											<span ng-if="annotation[0]['serializedBy']=='urn:application:utopia'">
												<img src="${resource(dir:'images/secure',file:'utopia_logo.png')}" title="Utopia for PDF" style="width:24px;"/><br/>
												<span style="font-size:11px;">Utopia</span>
											</span>
										</div>		
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="padding:0px; padding-top: 10px; min-height: 260px;">
						<div class="pagination" style="padding-left:10px;"> 
							Pages &nbsp; 
							<span ng-repeat="page in pages">
								<span ng-if="page==currentPage">
									<a href="" class="page active" ng-click="search(page)">{{page}}</a>
								</span>
								<span ng-if="page!=currentPage">
									<a href="" class="page" ng-click="search(page)">{{page}}</a>
								</span>
							</span>
						</div>
					</div>
				</div>
				
				<div id="sidebar" style="float: right; width: 410px; padding: 5px; border: 1px solid #ddd; border-radius: 5px;">
					Semantic Lens
				</div>
			</div>
			
		</div>
	</body>
</html>
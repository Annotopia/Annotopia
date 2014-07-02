<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="secure"/>
		<title>My Annotations</title>
	</head>
	<body>
		<div class="container">
			<div id="sidebar" class="viewerSidebar well" >
		    	<div id='contributorsTitle'>Filtering by permissions</div>
				<div id="contributors" style="border-top: 3px solid #ddd; padding-bottom: 2px;"></div>
		    	<div style="padding: 5px; padding-top: 10px; ">
				    <input id="publicFilter" type="checkbox" name="vehicle" checked="checked"> Public<br>
				    <input id="privateFilter" type="checkbox" name="private"> Private<br/>
				    
				    <%--  
				   	<g:if test="${userGroups.size()>0}">
					  	<div id="groupsList">
					  	 	<br/>Groups<br/>	    
					  		<g:each in="${userGroups}" status="i" var="usergroup">
					  			<input type="checkbox" name="${usergroup.group.name}" class="groupCheckbox" value="${usergroup.group.id}"> ${usergroup.group.name}<br/>
					  		</g:each>
					  	</div
				  	</g:if>
				  	--%>
				<br/>					
				
				<div align="center"><input value="Refresh" title="Search" name="lucky" type="submit" id="btn_i" onclick="loadAnnotationSets('', 0, '')" class="btn btn-success"></div>
				</div>
		  	</div>
		
			<div id="progressIcon" align="center" style="padding: 5px; padding-left: 10px; display: none;">
		    	<img id="groupsSpinner" src="${resource(dir:'images/secure',file:'ajax-loader-4CAE4C.gif')}" />
		    </div>
		    
		    <table width="790px;">
		    	<tr><td>
		    		<div id="resultsSummary" style="padding:5px; padding-left: 10px;"></div>
		    		<div class="resultsPaginationTop"></div>
		    	</td><td style="text-align:right">
		    		<div id="resultsStats" style="padding: 5px; "></div>
		    	</td></tr>
		    </table>
		    
		    <div id="resultsList" style="padding: 5px; padding-left: 10px; width: 790px;"></div>
		    <div class="resultsPaginationBottom"></div>
	      	<div class="clr"></div>
		</div>
		<script type="text/javascript">
			
			$(document).ready(function() {
				$('#progressIcon').css("display","block");
				//var url = getURLParameter("url")!=null ? getURLParameter("url"):'';
				var url = '';
				loadAnnotations(url,0);
			});

			function loadAnnotations(url, paginationOffset, paginationRange) {
				$("#resultsList").empty();
				$('.resultsPaginationTop').empty();
				$('.resultsPaginationBottom').empty(); 

				var groups = '';
				$(".groupCheckbox").each(function(i) {
					if($(this).attr('checked')!=undefined) 
						groups += $(this).attr('value') + " ";
				});

				try {
					var dataToSend = { 
						id: '${loggedUser.id}', 
						documentUrl: url,
						paginationOffset:paginationOffset, 
						paginationRange:paginationRange, 
						publicData: $("#publicFilter").attr('checked')!==undefined, 
						groupsData: $("#groupsFilter").attr('checked')!==undefined, 
						groupsIds: groups,
						privateData:$("#privateFilter").attr('checked')!==undefined
					};
					$.ajax({
				  	  	url: "${appBaseUrl}/secure/getAnnotation",
				  	  	context: $("#resultsList"),
				  	  	data: dataToSend,
				  	  	success: function(data){
				  	  		$("#progressIcon").css("display","none");
							if(!data.result || !data.result.total || data.result.total==0) {
								$("#resultsSummary").html("");
								$("#resultsList").html("No results to display");	
							} else {
								$("#resultsSummary").html("Displaying " + data.result.items.length + " out of " + data.result.total + (data.result.items.length==1?" annotation":" annotations") + " in " + data.result.duration);

								$.each(data.result.items, function(i,item){
									var annotationGraph = item['@graph'][0];
									$("#resultsList").append(annotationGraph['@type']);
									$("#resultsList").append(' of ');
									$("#resultsList").append(annotationGraph['hasTarget']);
									$("#resultsList").append(' on ');
									$("#resultsList").append(annotationGraph['annotatedAt']);
									$("#resultsList").append('<br/>');
									$("#resultsList").append(' by ');
									$("#resultsList").append(annotationGraph['annotatedBy']);
									$("#resultsList").append(' createdWith ');
									$("#resultsList").append(annotationGraph['serializedBy']);
									$("#resultsList").append('<br/>');
									$("#resultsList").append('<br/>');
								});
					  		}
					  		
				  	  	}
					});	
				} catch(e) {
					alert(e);
				}
			}
		</script>
	</body>
</html>
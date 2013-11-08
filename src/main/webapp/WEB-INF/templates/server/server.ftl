<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="home.css" rel="stylesheet" type="text/css"/> 
    <@js src="jquery.min.js"/>
    <@js src="foundation/foundation.js"/>
	<@js src="foundation/foundation.reveal.js"/>
	<@js src="foundation/foundation.topbar.js"/>
	<@js src="foundation/foundation.forms.js"/>
</head>
<script type="text/javascript">
$(document).ready(function() {
	$("#close-delete").click(function(){ 
		$("#delete-refinement-reveal").foundation('reveal','close'); 
	});
});
</script>
<body>

	<nav class="top-bar">
		<ul class="title-area">
			<li class="name">
				<h1><a href="<@url value='/home'/>/"><span style="color:White;">Analyzing Log</span></a></h1>
			</li>
		</ul>
		<ul class="right"><li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li></ul>
	</nav>
	
	<div id="new-refine-reveal" class="reveal-modal">
		<form id="serverNotExistedForm">
			<div class="row">
	    		<div id="info" class="large-4 large-centered  columns">
	    			<h4>New Server option</h4>
	    		</div>
	    	</div>
	    	<div class="row"> 
	            <div class="large-8 large-centered columns"> 
	                <p id="addSuccessInfo" class="successBox" style="display: none">Save successfully!</p> 
	                <p id="errInfo" class="errorBox" style="display: none">Save error!</p>
	            </div> 
	        </div>
	    	<div class="row">
    			<div class="large-8 large-centered columns">
    				<div id="messagebox" class="row" style="padding-top: 1em">
    				</div>
				</div>	
			</div>
		</form>
		<a class="close-reveal-modal" onclick="closeWindow()">&#215;</a> 
	</div>

	<div class="row">
		<div class="large-12 columns">
			<div><a class="small radius button" onclick="openAddServer()">Add New Server</a></div>
			<table class="business-rules" style="width:100%" id="serverDisplayTable" name="serverDisplayTable">
				<thead>
					<tr>
						<th>Server Id</th>
						<th>Server Name</th>
					</tr>
				</thead>
				<tbody>
					<input id="projectValue" type="hidden" value=${projectId} />
					<#if servers??>
						<#list servers as server>
							<tr>
								<td>${server.id}</td>
								<td>${server.serverName}</td>
							</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
function closeWindow(){
    window.location.href="<@url value='/server/details'/>?projectId=" + $("#projectValue").val();
}

function openAddServer(){
	var projectId = $("#projectValue").val();
	$.ajax({
		type : "POST",
		url : "<@url value='/instance/server'/>",
		data : "projectId=" + parseInt(projectId),
		success : function(result) {
			var serversNotExisted = result.serversNotExisted;
			if (0 == serversNotExisted.length || null == serversNotExisted){
				alert("there is no new server");
			}else{
				for(i=0;i<serversNotExisted.length;i++){
					var input="<div id=" + serversNotExisted[i] + " class=\"row\"><div class=\"large-4 columns text-right\"><label class=\"right-label\">Project Name:</label></div><div class=\"large-4 columns text-right\"><label class=\"right-label\">" + serversNotExisted[i] + "</label></div><div class=\"large-4 columns text-right\"><a class=\"small radius button\" style=\"padding: 0.2em 0.5em 0.2em\" onclick=\"addServer(" + serversNotExisted[i] + ")\">add</a></div></div>";
					$(input).insertAfter($("#messagebox"));
					$('div#new-refine-reveal').foundation('reveal', 'open');
				}
			}
		}
	}); 
}
	
function addServer(object){
	var serverName = object.id;
	var projectId = $("#projectValue").val();
	$.ajax({
		type : "POST",
		url : "<@url value='/server/generate'/>",
		data : "projectId=" + projectId + "&serverName=" + serverName, 
		success : function(result) {
			if(result.status == "failure"){
				$("#errInfo").show();
			}else{
				$("#addSuccessInfo").show();
				$("#" + serverName + "").remove();
			}
		}
	});
}
	
</script>
</html>
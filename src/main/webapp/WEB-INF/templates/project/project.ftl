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
		$("#delete-project-reveal").foundation('reveal','close'); 
		$("#delete-server-reveal").foundation('reveal','close'); 
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
	
	<!-- new project hop up -->
	<div id="new-project-reveal" class="reveal-modal">
		<form id="projectNotExistedForm">
			<div class="row">
	    		<div id="projectInfo" class="large-4 large-centered  columns">
	    			<h4>New Project option</h4>
	    		</div>
	    	</div>
	    	<div class="row"> 
	            <div class="large-8 large-centered columns"> 
	                <p id="projectAddSuccessInfo" class="successBox" style="display: none">Save successfully!</p> 
	                <p id="projectErrInfo" class="errorBox" style="display: none" >Save error!</p>
	            </div> 
	        </div>
	    	<div class="row">
    			<div class="large-8 large-centered columns">
    				<div id="projectMessagebox" class="row" style="padding-top: 1em">
    				</div>
				</div>	
			</div>
		</form>
		<a class="close-reveal-modal" onclick="closeWindow()">&#215;</a> 
	</div>
	
	<!-- new server hop up -->
	<div id="new-server-reveal" class="reveal-modal">
		<form id="serverNotExistedForm">
			<div class="row">
	    		<div id="serverInfo" class="large-4 large-centered  columns">
	    			<h4>New Server option</h4>
	    		</div>
	    	</div>
	    	<div class="row"> 
	            <div class="large-8 large-centered columns"> 
	                <p id="serverAddSuccessInfo" class="successBox" style="display: none">Save successfully!</p> 
	                <p id="serverErrInfo" class="errorBox" style="display: none">Save error!</p>
	            </div> 
	        </div>
	    	<div class="row">
    			<div class="large-8 large-centered columns">
    				<div id="serverMessagebox" class="row" style="padding-top: 1em">
    				</div>
				</div>	
			</div>
		</form>
		<a class="close-reveal-modal" onclick="closeWindow()">&#215;</a> 
	</div>

	<div class="row">
		<div class="large-12 columns">
			<div><a class="small radius button" onclick="openAddProject()">Scan New Project</a>
				 <a class="small radius button" onclick="openAddServer()">Scan New Server</a>
			</div>
			<table class="business-rules" style="width:100%" id="projectDisplayTable" name="projectDisplayTable">
				<thead>
					<tr>
						<th>Project Id</th>
						<th>Project Name</th>
						<th>Instance</th>
					</tr>
				</thead>
				<tbody>
					<#if instanceDetails??>
						<#list instanceDetails as instanceDetail>
							<#if instanceDetail.servers??>
								<#list instanceDetail.servers as server>
									<tr>
										<td>${instanceDetail.projectId}</td>
										<td>${instanceDetail.projectName}</td>
										<td>${server}</td>
									</tr>
								</#list>
							</#if>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
function closeWindow(){
    window.location.reload();
}

function openAddProject(){
	$.ajax({
		type : "POST",
		url : "<@url value='/project/project'/>",
		success : function(result) {
			var projectsNotExisted = result.projectsNotExisted;
			if (0 == projectsNotExisted.length || null == projectsNotExisted){
				alert("there is no new project");
			}else{
				for(i=0;i<projectsNotExisted.length;i++){
					var input="<div id=" + projectsNotExisted[i] + " class=\"row\"><div class=\"large-4 columns text-right\"><label class=\"right-label\">Project Name:</label></div><div class=\"large-4 columns text-right\"><label class=\"right-label\">" + projectsNotExisted[i] + "</label></div><div class=\"large-4 columns text-right\"><a class=\"small radius button\" style=\"padding: 0.2em 0.5em 0.2em\" onclick=\"addProject(" + projectsNotExisted[i] + ")\">add</a></div></div>";
					$(input).insertAfter($("#projectMessagebox"));
					$('div#new-project-reveal').foundation('reveal', 'open');
				}
			}
		}
	}); 
}

function addProject(object){
	 var projectName = object.id;
	 $.ajax({
		 type : "POST",
		 url : "<@url value='/project/generate'/>",
		 data : "projectName=" + projectName, 
		 success : function(result) {
			 if(result.status == "failure"){
				 $("#projectErrInfo").show();
			 }else{
				 $("#projectAddSuccessInfo").show();
				 $("#" + projectName + "").remove();
			 }
		 }
	 });
}

function openAddServer(){
	$.ajax({
		type : "POST",
		url : "<@url value='/instance/server'/>",
		success : function(result) {
			var serversNotExisted = result.serversNotExisted;
			if (0 == serversNotExisted.length || null == serversNotExisted){
				alert("there is no new server");
			}else{
				for(i=0;i<serversNotExisted.length;i++){
					for (var j=0; j < serversNotExisted[i].servers.length; j++) {
						var projectId = serversNotExisted[i].projectId;
						var serverName = serversNotExisted[i].servers[j];
						var input="<div id=" + projectId + "-" + serverName + " class=\"row\"><div class=\"large-4 columns text-right\"><label class=\"right-label\">Server Name:</label></div><div class=\"large-4 columns text-right\"><label class=\"right-label\">" + serverName + "</label></div><div class=\"large-4 columns text-right\"><a class=\"small radius button\" style=\"padding: 0.2em 0.5em 0.2em\" onclick=\"addServer(this)\">add</a></div></div>";
						$(input).insertAfter($("#serverMessagebox"));
						$('div#new-server-reveal').foundation('reveal', 'open');
					};
				}
			}
		}
	}); 
}
	
function addServer(obj){
	var message = $(obj).parent().parent().attr("id").split("-");	
	var projectId = message[0];
	var serverName = message[1];
	$.ajax({
		type : "POST",
		url : "<@url value='/server/generate'/>",
		data : "projectId=" + parseInt(projectId) + "&serverName=" + serverName, 
		success : function(result) {
			if(result.status == "failure"){
				$("#serverErrInfo").show();
			}else{
				$("#serverAddSuccessInfo").show();
				$("#" + projectId + "-" + serverName + "").remove();
			}
		}
	});
}
	
</script>
</html>
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
		<form id="projectNotExistedForm">
			<div class="row">
	    		<div id="info" class="large-4 large-centered  columns">
	    			<h4>New Project option</h4>
	    		</div>
	    	</div>
	    	<div class="row"> 
	            <div class="large-8 large-centered columns"> 
	                <p id="addSuccessInfo" class="successBox" style="display: none">Save successfully!</p> 
	                <p id="errInfo" class="errorBox" >Save error!</p>
	            </div> 
	        </div>
	    	<div class="row">
    			<div class="large-8 large-centered columns">
    				<div id="messagebox" class="row" style="padding-top: 1em">
    				</div>
				</div>	
			</div>
		</form>
	</div>

	<div class="row">
		<div class="large-12 columns">
			<div><a class="small radius button" onclick="openAddProject()">Add New Project</a></div>
			<table class="business-rules" style="width:100%" id="projectDisplayTable" name="projectDisplayTable">
				<thead>
					<tr>
						<th>Project Id</th>
						<th>Project Name</th>
						<th>Instance</th>
					</tr>
				</thead>
				<tbody>
					<#if projects??>
						<#list projects as project>
							<tr>
								<td>${project.id}</td>
								<td>${project.name}</td>
								<td><a href="<@url value='/server/details'/>?projectId=${project.id}">details</a></td>
							</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
function openAddProject(){
	$.ajax({
		type : "POST",
		url : "<@url value='/project/project'/>",
		success : function(result) {
			var projectsNotExisted = result.projectsNotExisted;
			if (0==projectsNotExisted.length){
				alert("there is no new project");
			}else{
				for(i=0;i<projectsNotExisted.length;i++){
					var input="<div id=" + projectsNotExisted[i] + " class=\"row\"><div class=\"large-4 columns text-right\"><label class=\"right-label\">Project Name:</label></div><div class=\"large-4 columns text-right\"><label class=\"right-label\">" + projectsNotExisted[i] + "</label></div><div class=\"large-4 columns text-right\"><a class=\"small radius button\" style=\"padding: 0.2em 0.5em 0.2em\" onclick=\"addProject(" + projectsNotExisted[i] + ")\">add</a></div></div>";
					$(input).insertAfter($("#messagebox"));
					$('div#new-refine-reveal').foundation('reveal', 'open');
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
				$("#errInfo").show();
			}else{
				$("addSuccessInfo").show();
				$("#" + projectName + "").remove();
			}
		}
	});
}
	
</script>
</html>
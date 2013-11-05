<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="home.css" rel="stylesheet" type="text/css"/> 
    <@js src="jquery.min.js"/>
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

<div class="row">
		<div class="large-12 columns">
			<div><a class="small radius button" onclick="scanProject()">Scan New Project</a></div>
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
	function scanProject(){
		$.ajax({
			type : "POST",
			url : "<@url value='/project/project'/>",
			success : function() {
	        	window.location.reload(); 
			}
		}); 
	}
</script>
</html>
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
			<h1><span style="color:White;">Analyzing Log</span></h1>
		</li>
	</ul>
	<ul class="right"><li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li></ul>
	<ul class="right"><li class="has-form"><a class="small radius button" onclick="scanServer()">Scan New Server</a></li></ul>
</nav>

<div class="row">
		<div class="large-12 columns">
			<table class="business-rules" style="width:100%" id="serverDisplayTable" name="serverDisplayTable">
				<thead>
					<tr>
						<th>Server Id</th>
						<th>Server Name</th>
						<th>Project Id</th>
					</tr>
				</thead>
				<tbody>
					<#if servers??>
						<#list servers as server>
							<tr>
								<td>${server.id}</td>
								<td>${server.name}</td>
								<td>${server.projectId}</td>
							</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	function scanServer(){
		$.ajax({
			type : "POST",
			url : "<@url value='/instance/server'/>",
			success : function() {
	        	window.location.reload(); 
			}
		}); 
	}
</script>
</html>
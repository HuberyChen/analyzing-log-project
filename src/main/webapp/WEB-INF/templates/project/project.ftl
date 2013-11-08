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
	
	<div class="row">
		<div class="large-12 columns">
			<div><a class="small radius button" onclick="scan()">Scan</a>
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
										<td>${instanceDetail.project.id}</td>
										<td>${instanceDetail.project.name}</td>
										<td>${server.serverName}</td>
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
</html>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="foundation.min.css" rel="stylesheet" type="text/css"/>
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
		<div class="large-12 columns" id="info">
			<a class="small radius button" id="scan" onclick="scan()">Scan</a>
            <p id="addSuccessInfo" style="display: none">Save successfully!</p> 
            <p id="errInfo" style="display: none">Save error! Server is existed.</p>
			<table class="business-rules" style="width:100%" id="projectDisplayTable" name="projectDisplayTable">
				<thead>
					<tr>
						<th>Project Id</th>
						<th>Project Name</th>
						<th>Instance</th>
						<th>Operation</th>
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
										<td></td>
									</tr>
								</#list>
							</#if>
						</#list>
					</#if>
					<tr id="input"></tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
function scan(){
	$.ajax({
		type : "POST",
		url : "<@url value='/scan'/>",
		success : function(result) {
			var detailNotExisted = result.detailNotExisted;
			var errMsg = result.errMsg;
            $("#errMsg").remove();
			var input="<p id=\"errMsg\" style=>" + errMsg + "</p>";
			$(input).insertAfter($("#scan"));
			if (0 != detailNotExisted.length && null != detailNotExisted){
				for (var i=0; i < detailNotExisted.length; i++) {
					for (var j=0; j < detailNotExisted[i].servers.length; j++) {
						var input="<tr><td name = \"projectId\">" + detailNotExisted[i].project.id +"</td><td name = \"projectName\">" + detailNotExisted[i].project.name +"</td><td name = \"serverName\">" + detailNotExisted[i].servers[j].serverName +"</td><td><a onclick=\"generate(this) \">add</a></td></tr>";
						$(input).insertAfter($("#input").siblings().last());
					};
				};
			}
		}
	}); 
}

function generate(obj){
	var projectName = $(obj).parent().parent().find("td[name='projectName']").text();
	var serverName = $(obj).parent().parent().find("td[name='serverName']").text();
	$("#errMsg").remove();
	$.ajax({
		type : "POST",
		url : "<@url value='/generation'/>",
		data : "projectName=" + projectName + "&serverName=" + serverName, 
		success : function(result) {
			if("SUCCESS" == result.status){
				$("#addSuccessInfo").show();
				$(obj).parent().parent().find("a").remove();
			}else{
				$("#errInfo").show();
			}
		}
	});
}
</script>
</html>
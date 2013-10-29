<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing Portal</title>
    <meta charset="utf-8"/>
    <@css href="ui.datepicker.min.css,public.css" rel="stylesheet" type="text/css"/> 
    <@js src="jquery.min.js,jquery.form.js,common.js,ui.datepicker.js"/>
</head>
<script type="text/javascript">
$(document).ready(function() {
	$("#date").datepicker();
});
</script>
<body>
	<form id="analyzingLogForm" action="<@url value='/project/instance/log/action'/>" method="post">
		<table>
			<tr>
				<td>Project:</td>
				<td><select id="projectList" name="project">
						<option>All</option>
						<#if projects??>
							<#list projects as project>
								<option value=${project.name} onclick="findServerByProject(this)">${project.name}</option>
	                    	</#list>
                    	</#if>
					</select>
				</td>
			</tr>
			<tr>
				<td>Instance:</td>
				<td><select id="serverList" name="instance">
						<option id="default">All</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Date:</td>
				<td><input class="date left hasDatePicker"readonly="readonly" type="date" id="date" name="date" /></td>
			</tr>
			<tr>
				<td><input id="button" type="button" onclick="analyzingLog()" value="confirm"/></td>
				<td><span class="loadingDiv displayNone" id="loadingLogo"></span></td>
			</tr>
		</table>
	</form>
</body>
	<script type="text/javascript">
	
	function findServerByProject(object){
		$("#default").nextAll().remove();
		var projectName = $(object).val();
		 $.ajax({
			type : "POST",
			url : "<@url value='/project/instance'/>",
			data : "projectName=" + projectName,
			success : function(result) {
				var servers = result.servers;
				if (null != servers) {
					for(var i=0;i<servers.length;i++){
						var inputValue = "<option value=\""+servers[i].serverName+"\">\""+servers[i].serverName+"\"</option>";
						$(inputValue).insertAfter($("#serverList").children(":last"));
					}
				}
			}
		}); 
	}
	
	function analyzingLog(){
		$("#loadingLogo").css("display","inline-block");
		$("#button").attr('disabled',true);
	    $("#analyzingLogForm").ajaxSubmit({callback:function(result){
	    	if(result == 'success'){
	    		alert("success");
	    	} else {
	    		alert("failure");
	    	}
	    	$("#loadingLogo").css("display","none");
			$("#button").attr('disabled',false);
        	window.location.reload();
        },validate:false});
	}
	</script>
</html>
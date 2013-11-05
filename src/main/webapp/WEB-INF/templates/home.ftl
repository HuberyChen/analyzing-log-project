<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="ui.datepicker.min.css,public.css,home.css" rel="stylesheet" type="text/css"/> 
    <@js src="jquery.min.js,jquery.form.js,common.js,ui.datepicker.js,jquery.validate.js"/>
</head>
<script type="text/javascript">
$(document).ready(function() {
	$("#startDate").datepicker();
	$("#endDate").datepicker();
	$("#analyzingLogForm").validate({
        messages: {
           startDate:{
               required:"Start date is required."
           },
           endDate:{
               required:"End date is required."
           },
       },
       errorPlacement:function(error,element){
           error.appendTo($("#errInfo"));
       },
       success:function(error,element){
            error.parent("li").remove();
            error.remove();
       },
       wrapper:"li"
    });
});
</script>
<body>

<nav class="top-bar">
	<ul class="title-area">
		<li class="name">
			<h1><span style="color:White;">Analyzing Log</span></h1>
		</li>
	</ul>
	<ul class="right"><li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li></ul>
	<ul class="right"><li class="has-form"><a class="small radius button" href="<@url value='/project/details'/>">Project</a></li></ul>
</nav>

<div class="main">
	<div class="container">
		<form id="analyzingLogForm" action="<@url value='/project/instance/log/action'/>" method="post">
			<div><p id="errInfo" class="errorBox"></p></div>
			<table class="anlyzinglog-tb">
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
					<td>Start Date:</td>
					<td><input class="date left hasDatePicker required" readonly="readonly" type="date" id="startDate" name="startDate" /></td>
				</tr>
				<tr>
					<td>End Date:</td>
					<td><input class="date left hasDatePicker required" readonly="readonly" type="date" id="endDate" name="endDate" /></td>
				</tr>
				<tr>
					<td></td>
					<td><p class="clearfix">
						<input id="button" type="button" onclick="analyzingLog()" value="confirm"/>
						<span class="loadingDiv displayNone" id="loadingLogo"></span>
						</p>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
	<script type="text/javascript">
	
	function scheduleAnalyzingLog(){
		$("#loadingLogo").css("display","inline-block");
		$("#button").attr('disabled',true);
		$("#button").css({'background':'gray','border-color':'gray'});
		$.ajax({
			type : "POST",
			url : "<@url value='/project/instance/log/action/schedule'/>",
			success : function(result) {
				if(result.status == 'success'){
		    		alert("success");
		    	} else {
		    		alert("failure");
		    	}
		    	$("#button").attr('disabled',false);
	        	window.location.reload();
			}
		}); 
	}
	
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
						var inputValue = "<option value=\""+servers[i].serverName+"\">"+servers[i].serverName+"</option>";
						$(inputValue).insertAfter($("#serverList").children(":last"));
					}
				}
			}
		}); 
	}
	
	function analyzingLog(){
		if(!$("#analyzingLogForm").valid()){
        	return false;
    	}
    	
    	var start = $("#startDate").val();
    	var end = $("#endDate").val();
    	var sDate = new Date(start.replace(/\-/g,'/')); 
        var eDate = new Date(end.replace(/\-/g,'/')); 
    	if(start > end){
    		alert("End date cannot be less than start date");
    		return;
    	}
		$("#loadingLogo").css("display","inline-block");
		$("#button").attr('disabled',true);
		$("#button").css({'background':'gray','border-color':'gray'});
	    $("#analyzingLogForm").ajaxSubmit({callback:function(result){
	    	if(result.status == 'success'){
	    		alert("success");
	    	} else {
	    		alert("failure");
	    	}
	    	$("#button").attr('disabled',false);
        	window.location.reload();
        },validate:false});
	}
	</script>
</html>
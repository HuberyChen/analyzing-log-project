<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<title>Quidsi, Inc. |Log Analyzing Portal</title>
    <meta charset="utf-8"/>
    <@css href="ui.datepicker.min.css" rel="stylesheet" type="text/css"/> 
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
				<td><input type="text" name="project"/></td>
			</tr>
			<tr>
				<td>Instance:</td>
				<td><input type="text" name="instance"/></td>
			</tr>
			<tr>
				<td>Date:</td>
				<td><input class="date left hasDatePicker" type="date" id="date" name="date" /></td>
			</tr>
			<tr>
				<td><input type="button" onclick="analyzingLog()" value="confirm"/></td>
			</tr>
		</table>
	</form>
</body>
	<script type="text/javascript">
	function analyzingLog(){
	    $("#analyzingLogForm").ajaxSubmit({callback:function(result){
	        var status=result.status;
	        if("SUCCESS"==status){
	            return;
	        }
	        window.location.reload();
	    },validate:false});
	}
	</script>
</html>
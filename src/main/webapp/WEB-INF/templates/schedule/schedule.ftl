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
    <ul class="right">
        <li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li>
    </ul>
</nav>

<div class="row">
    <div class="large-12 columns">
        <table class="business-rules" style="width:100%" id="scheduleDisplayTable" name="scheduleDisplayTable">
            <thead>
            <tr>
                <th>Project</th>
                <th>Instance</th>
                <th>Start date</th>
                <th>End date</th>
                <th>Effective start time</th>
                <th>Status</th>
                <th>Detail</th>
            </tr>
            </thead>
            <tbody>
            <#if schedules??>
                <#list schedules as schedule>
                    <tr>
                        <td>${schedule.project}</td>
                        <td>${schedule.instance}</td>
                        <td>${schedule.startDate}</td>
                        <td>${schedule.endDate}</td>
                        <td>${schedule.effectiveStartTime}</td>
                        <td>${schedule.status}</td>
                        <td><a href="<@url value='/schedule/detail?scheduleId='/>${schedule.id}">detail</a></td>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
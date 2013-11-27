<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="ui.datepicker.min.css,public.css,foundation.min.css" rel="stylesheet" type="text/css"/>
    <@js src="jquery.min.js,jquery.form.js,common.js,ui.datepicker.js,jquery.validate.js"/>
    <@js src="foundation/foundation.js"/>
    <@js src="foundation/foundation.reveal.js"/>
    <@js src="foundation/foundation.topbar.js"/>
    <@js src="foundation/foundation.forms.js"/>
</head>
<script type="text/javascript">
    $(document).ready(function () {
        $("#startDate").datepicker();
        $("#endDate").datepicker();
        $("#projectList").change(function () {
            findServerByProject(this);
        });
        $("#analyzingLogForm").validate({
            messages: {
                startDate: {
                    required: "Start date is required."
                },
                endDate: {
                    required: "End date is required."
                }
            },
            errorPlacement: function (error) {
                error.appendTo($("#errInfo"));
            },
            success: function (error) {
                error.parent("li").remove();
                error.remove();
            },
            wrapper: "li"
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
    <ul class="right">
        <li class="has-form"><a style="margin-left: 5px;" class="small radius button" href="<@url value='/signOut'/>">Logout</a></li>
    </ul>
    <ul class="right">
        <li class="has-form"><a style="margin-left: 5px;" class="small radius button" href="<@url value='/project/instance/details'/>">Project</a></li>
    </ul>
    <ul class="right">
        <li class="has-form"><a style="margin-left: 5px;" class="small radius button" href="<@url value='/schedule/details'/>">Schedule</a></li>
    </ul>
    <ul class="right">
        <li class="has-form"><a style="margin-left: 5px;" class="small radius button" href="<@url value='/project/instance/log/action/detail'/>">Detail</a></li>
    </ul>
</nav>

<div class="main">
    <div class="container">
        <form id="analyzingLogForm" class="custom" action="<@url value='/project/instance/log/action'/>" method="post">
            <div class="row">
                <div class="large-12 large-centered columns">
                    <p id="errInfo" class="errorBox"></p></div>
            </div>
            <div class="row">
                <div class="large-12 large-centered columns">
                    <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">Project:</label></div>
                    <div class="large-8 small-9 columns">
                        <select id="projectList" name="project">
                            <option>All</option>
                            <#if projects??>
                                <#list projects as project>
                                    <option value=${project.name}>${project.name}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="large-12 large-centered columns">
                    <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">Instance:</label></div>
                    <div class="large-8 small-9 columns">
                        <select id="serverList" name="instance">
                            <option id="default">All</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="large-12 large-centered columns">
                    <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">Start Date:</label></div>
                    <div class="large-8 small-9 columns"><input class="date left hasDatePicker required date" type="date" id="startDate" name="startDate"/></div>
                </div>
            </div>
            <div class="row">
                <div class="large-12 large-centered columns">
                    <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">End Date:</label></div>
                    <div class="large-8 small-9 columns"><input class="date left hasDatePicker required date" type="date" id="endDate" name="endDate"/></div>
                </div>
            </div>
            <div class="row">
                <div class="large-12 large-centered columns">
                    <p>
                        <input style="margin-left: 180px;margin-top: -10px;" id="button" type="button" onclick="analyzingLog()" value="confirm"/>
                        <span style="margin-top: -12px;" class="loadingDiv displayNone" id="loadingLogo"></span>
                    </p>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
<script type="text/javascript">

    function scheduleAnalyzingLog() {
        $("#loadingLogo").css("display", "inline-block");
        $("#button").attr('disabled', true);
        $("#button").css({'background': 'gray', 'border-color': 'gray'});
        $.ajax({
            type: "POST",
            url: "<@url value='/project/instance/log/action/schedule'/>",
            success: function (result) {
                if (result.status == 'success') {
                    alert("success");
                } else {
                    alert("failure");
                }
                $("#button").attr('disabled', false);
                window.location.reload();
            }
        });
    }

    function findServerByProject(object) {
        $("#default").nextAll().remove();
        var projectName = $(object).val();
        $.ajax({
            type: "POST",
            url: "<@url value='/project/instance'/>",
            data: "projectName=" + projectName,
            success: function (result) {
                var servers = result.servers;
                if (null != servers) {
                    for (var i = 0; i < servers.length; i++) {
                        var inputValue = "<option value=\"" + servers[i].serverName + "\">" + servers[i].serverName + "</option>";
                        $(inputValue).insertAfter($("#serverList").children(":last"));
                        $(document).foundation();
                    }
                }
            }
        });
    }

    function analyzingLog() {
        if (!$("#analyzingLogForm").valid()) {
            return false;
        }

        var start = $("#startDate").val();
        var end = $("#endDate").val();
        var sDate = new Date(start.replace(/\-/g, '/'));
        var eDate = new Date(end.replace(/\-/g, '/'));
        if (start > end) {
            alert("End date cannot be less than start date");
            return;
        }
        $("#loadingLogo").css("display", "inline-block");
        $("#button").attr('disabled', true);
        $("#button").css({'background': 'gray', 'border-color': 'gray'});
        $("#analyzingLogForm").ajaxSubmit({callback: function (result) {
            alert(result.errMsg);
            $("#button").attr('disabled', false);
            window.location.href = "<@url value='/schedule/details'/>";
        }, validate: false});
    }
</script>
<script>$(document).foundation();</script>
</html>
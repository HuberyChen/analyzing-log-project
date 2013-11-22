<!DOCTYPE HTML>
<html lang="en-US">
<head>
    <title>Quidsi, Inc. |Log Analyzing</title>
    <meta charset="utf-8"/>
    <@css href="ui.datepicker.min.css,public.css,foundation.min.css,searchportal.css" rel="stylesheet" type="text/css"/>
    <@js src="jquery.min.js,jquery.form.js,common.js,ui.datepicker.js,jquery.validate.js"/>
    <@js src="foundation/foundation.js"/>
    <@js src="foundation/foundation.reveal.js"/>
    <@js src="foundation/foundation.topbar.js"/>
    <@js src="foundation/foundation.forms.js"/>
</head>
<script type="text/javascript">
    golbalRootUrl = "<@url value='/' />";
    $(document).ready(function () {
        $("#startDate").datepicker();
        $("#endDate").datepicker();
        findServerByProject($("#projectList"));
        $("#projectList").change(function () {
            findServerByProject(this);
            $("#isChange").attr("value", true);
        });
        $("#serverList").change(function () {
            $("#isChange").attr("value", true);
        });
        $("#startDate").change(function () {
            $("#isChange").attr("value", true);
        });
        $("#endDate").change(function () {
            $("#isChange").attr("value", true);
        });
        $("#showDetailForm").validate({
            messages: {
                startDate: {
                    required: "start date is required."
                },
                endDate: {
                    required: "end date is required."
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
            <h1><a href="<@url value='/home'/>/"><span style="color:White;">Analyzing Log</span></a></h1>
        </li>
    </ul>
    <ul class="right">
        <li class="has-form"><a class="small radius button" href="<@url value='/signOut'/>">Logout</a></li>
    </ul>
</nav>

<input id="logIdListHidden" type="hidden">

<div class="row full page-title">
    <form class="custom" id="showDetailForm" action="<@url value='/project/instance/log/action/show'/>" method="post">
        <div id="logIdShowList"></div>
        <input id="totalCount" name="totalCount" type="hidden" value="0">
        <input id="isChange" name="change" type="hidden">
        <input id="offset" name="offset" type="hidden" value="0"/>

        <div class="row collapse">
            <div class="large-2 large columns">
                <h4>Select Condition:</h4>
            </div>
            <div class="large-2 left columns">
                <div id="errInfo" class="errorBox"></div>
            </div>
        </div>

        <div class="row collapse">
            <div class="large-3 centered columns">
                <div class="small-4 columns text-right">
                    <label style="margin-top:10px;">Project:</label>
                </div>
                <div class="small-7 columns">
                    <select id="projectList" name="project">
                        <#if projects??>
                            <#list projects as project>
                                <option value=${project.name}>${project.name}</option>
                            </#list>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="large-3 centered columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">Instance:</label></div>
                <div class="small-7 columns">
                    <select id="serverList" name="serverName">
                        <option id="default">All</option>
                    </select>
                </div>
            </div>
            <div class="large-3 centered columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">*Start date:</label></div>
                <div class="small-7 columns"><input class="date left hasDatePicker required date" type="date" id="startDate" name="startDate"/></div>
            </div>
            <div class="large-3 centered columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">*End date:</label></div>
                <div class="small-7 columns"><input class="date left hasDatePicker required date" type="date" id="endDate" name="endDate"/></div>
            </div>

        </div>
        <div class="row collapse">
            <div class="large-3 centered columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">Status:</label></div>
                <div class="small-7 columns">
                    <select id="status" name="status">
                        <option>SUCCESS</option>
                        <option>ERROR</option>
                        <option>WARNING</option>
                    </select>
                </div>
            </div>

            <div class="large-3 centered columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">Interface:</label></div>
                <div class="small-7 columns">
                    <input id="interface" type="text" value="" name="interface">
                </div>
            </div>
            <div class="small-3 columns">
                <div class="small-4 columns text-right"><label style="margin-top:10px;">Error Code:</label></div>
                <div class="small-7 columns">
                    <input id="errorCode" type="text" name="errorCode"></div>
            </div>
            <div class="small-3 columns" style="padding-left: 55px;">
                <div class="small-3 columns">
                    <a class="small radius button" id="button" onclick="detailShow()" href="javascript:void(0)">Search</a>
                </div>

                <div class="loadingDiv displayNone" id="loadingLogo"></div>
            </div>
        </div>
    </form>
    <div>
        <form id="pageChangeForm" action="<@url value='/project/instance/log/action/search/change'/>" method="post"></form>
    </div>
</div>

<table class="business-rules" style="width:100%" id="detailDisplayTable" name="detailDisplayTable">
    <thead>
    <tr>
        <th>RecordTime</th>
        <th>Status</th>
        <th>Interface</th>
        <th>ElapsedTime</th>
        <th>RequestMethod</th>
        <th>ErrorCode</th>
        <th>ExceptionMsg</th>
        <th>LogAddress</th>
    </tr>
    </thead>
    <tbody id="detailList"></tbody>
</table>
<div class="row">
    <div class="large-12 column">
        <div class="pagination-centered">
            <ul id="pageMessage" class="pagination">
            </ul>
        </div>
    </div>
</div>


</body>
<@js src="detail.show.page.operation.js"/>
<script type="text/javascript">

    function detailShow() {
        if (!$("#showDetailForm").valid()) {
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

        detailShowOperation();
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

</script>
<script>$(document).foundation();</script>
</html>
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
    $(document).ready(function () {
        $("#startDate").datepicker();
        $("#endDate").datepicker();
        $("#projectList").change(function () {
            findServerByProject(this);
        });
        $('div#search-details-reveal').foundation('reveal', 'open');
        $("#close-delete").click(function () {
            $("#delete-refinement-reveal").foundation('reveal', 'close');
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

<!-- search detail hop up -->
<div id="search-details-reveal" class="reveal-modal">
    <form class="custom" id="showDetailForm" action="<@url value='/project/instance/log/action/show'/>" method="post">
        <div class="row">
            <div id="info" class="large-12 large-centered  columns">
                <h3>Action Log Detail Show</h3>
            </div>
        </div>

        <div class="row">
            <div class="large-12 large-centered columns">
                <p id="errInfo" class="errorBox"></p></div>
        </div>

        <div class="row">
            <div class="large-12 large-centered columns">
                <div class="row">
                    <div class="large-12 large-centered columns">
                        <div class="large-4 small-3 columns text-right">
                            <label style="margin-top:10px;">Project</label>
                        </div>
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
                        <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">Start date:</label></div>
                        <div class="large-8 small-9 columns"><input class="date left hasDatePicker required date" type="date" id="startDate" name="startDate"/></div>
                    </div>
                </div>

                <div class="row">
                    <div class="large-12 large-centered columns">
                        <div class="large-4 small-3 columns text-right"><label style="margin-top:10px;">End date:</label></div>
                        <div class="large-8 small-9 columns"><input class="date left hasDatePicker required date" type="date" id="endDate" name="endDate"/></div>
                    </div>
                </div>

                <input id="offset" name="offset" type="hidden" value="0"/>

                <div class="row">
                    <div class="large-12 large-centered columns">
                        <p>
                            <input style="margin-left: 180px;margin-top: -10px;" id="button" type="button" onclick="detailShow()" value="confirm"/>
                        </p>
                        <span class="loadingDiv displayNone" id="loadingLogo"></span>
                    </div>
                </div>

            </div>
        </div>
    </form>
    <a class="close-reveal-modal">&#215;</a>
</div>

<div class="row right">
    <div class="large-5 column right">
        <div class="row collapse">
            <div class="small-10 columns">
                <input id="fuzzy" type="text" value="" name="fuzzy" placeholder="fuzzy select">
            </div>
            <div class="small-2 columns">
                <a class="small radius button" id="search" onclick="search()">Search</a>
            </div>
        </div>
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
<script type="text/javascript">

    var logIdList;

    function detailShow() {
        if (!$("#showDetailForm").valid()) {
            return false;
        }

        $("#showDetailForm").ajaxSubmit({callback: function (result) {
            $('div#search-details-reveal').foundation('reveal', 'close');
            successOperation(result);
        }, validate: false});
    }

    function successOperation(result) {
        logIdList = result.logIdList;
        var detailList = result.actionLogDetails;
        if (null != detailList && 0 != detailList.length) {
            $("#detailList").text('');
            for (var i = 0; i < detailList.length; i++) {
                var detail = detailList[i];
                var str = "<tr>";
                str += "<th>" + detail.recordTime + "</th>";
                str += "<th>" + detail.status + "</th>";
                str += "<th>" + detail.interfaceName + "</th>";
                str += "<th>" + detail.elapsedTime + "</th>";
                str += "<th>" + detail.requestMethod + "</th>";
                str += "<th>" + detail.errorCode + "</th>";
                str += "<th>" + detail.exceptionMsg + "</th>";
                str += "<th>" + detail.logAddress + "</th>";
                str += "</tr>";
                $(str).appendTo($("#detailList"));
            }
        }

        $("#pageMessage").text('');
        var offset = result.offset;
        var fetchSize = result.fetchSize;
        var maxPageNum = result.maxPageNum;
        $("#pageMessage").attr("data-fetchSize", fetchSize);
        $("#pageMessage").attr("data-current-index", offset / fetchSize + 1);

        var pageMsg;
        var previous = "<li";
        if (0 == offset / fetchSize) {
            previous += " class=\"arrow unavailable\"";
        }
        else {
            previous += " class=\"arrow\" onClick=\"previous(this)\"";
        }
        previous += " ><a href=\"javascript:void(0)\">&laquo;</a></li>"
        pageMsg += previous;

        var limit = 0;
        if (maxPageNum > offset / fetchSize + 3) {
            limit = offset / fetchSize + 3;
        } else {
            limit = maxPageNum;
        }

        for (var i = offset / fetchSize; i < limit; i++) {
            var pageNum = "<li";
            if (i == offset / fetchSize) {
                pageNum += " class= \"current\"";
            }
            else {
                pageNum += " onClick = \"searchDetail(this)\"";
            }
            pageNum += " data-index =" + (i + 1) + "><a href = \"javascript:void(0)\" >" + (i + 1) + " </a></li >"
            pageMsg += pageNum;
        }

        var next = "<li";
        if (maxPageNum == offset / fetchSize) {
            next += " class=\"arrow unavailable\"";
        } else {
            next += " class=\"arrow\" onClick=\"next(this)\"";
        }
        next += " ><a href=\"javascript:void(0)\">&raquo;</a></li>";
        pageMsg += next;

        $(pageMsg).appendTo($("#pageMessage"));
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

    function previous(obj) {

        var currentIndex = $(obj).parent().attr("data-current-index");
        var fetchSize = $(obj).parent().attr("data-fetchSize");
        var offset = (currentIndex - 2) * fetchSize;
        redirectToSearch(offset);
    }

    function next(obj) {
        var currentIndex = $(obj).parent().attr("data-current-index");
        var fetchSize = $(obj).parent().attr("data-fetchSize");
        var offset = currentIndex * fetchSize;
        redirectToSearch(offset);
    }

    function searchDetail(obj) {
        var index = $(obj).attr("data-index");
        var fetchSize = $(obj).parent().attr("data-fetchSize");
        var offset = (index - 1) * fetchSize;
        redirectToSearch(offset);
    }

    function redirectToSearch(offset) {
        $.ajax({
            type: "POST",
            url: "<@url value='/project/instance/log/action/change'/>",
            data: "logIdList=" + logIdList + "&offset=" + parseInt(offset),
            success: function (result) {
                successOperation(result);
            }
        });
    }

    function search() {

    }
</script>
<script>$(document).foundation();</script>
</html>
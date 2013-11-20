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

<!-- wait hop up-->
<div id="wait-reveal" class="reveal-modal" style="height: 150px;">
    <div class="row">
        <div class="large-12 large-centered columns">
            <h2>Please wait a moment!</h2>
        </div>
        <div class="large-2 large-centered columns">
            <div class="loadingDiv"></div>
        </div>
    </div>
</div>

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
                            <input style="margin-left: 180px;" class="small radius button" id="button" type="button" onclick="detailShow()" value="confirm"/>
                        </p>

                        <div class="loadingDiv displayNone" id="loadingLogo"></div>
                    </div>
                </div>

            </div>
        </div>
    </form>
    <a class="close-reveal-modal">&#215;</a>
</div>

<div class="row full page-title">
    <div class="large-7 column left">
        <div class="row collapse">
            <div class="small-2 columns">
                <h6>Select Range:</h6>
            </div>
            <div class="small-2 columns">
                <input style="border:0px;box-shadow:none;" type="text" readonly id="project">
            </div>
            <div class="small-2 columns">
                <input style="border:0px;box-shadow:none;" type="text" readonly id="instance">
            </div>
            <div class="small-2 columns">
                <input style="border:0px;box-shadow:none;" type="text" readonly id="start">
            </div>
            <div class="small-2 columns">
                <input style="border:0px;box-shadow:none;" type="text" readonly id="end">
            </div>
            <div class="small-2 columns">
                <a class="small radius button" id="changeRange" onclick="openDetailShow()" href="javascript:void(0)">Change Range</a>
            </div>
        </div>
    </div>
    <div class="large-5 column right">
        <div class="row collapse">
            <form id="searchForm" action="<@url value='/project/instance/log/action/search/show'/>" method="post">
                <div class="small-3 columns">
                    <select id="status" name="status">
                        <option>ERROR</option>
                        <option>SUCCESS</option>
                        <option>WARNING</option>
                    </select>
                </div>
                <div class="small-5 columns">
                    <input id="interface" type="text" value="" name="interface">
                </div>
                <div class="small-2 columns">
                    <input id="errorCode" type="text" name="errorCode">
                </div>
                <div class="small-2 columns">
                    <a class="small radius button" id="search" onclick="conditionSearch()" href="javascript:void(0)">Search</a>
                </div>
            </form>
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
            <ul id="conditionPageMessage" class="pagination">
            </ul>
        </div>
    </div>
</div>


</body>
<@js src="detail.show.page.operation.js"/>
<@js src="condition.detail.show.page.operation.js"/>
<script type="text/javascript">

    var logIdList;

    function openDetailShow() {
        $('div#search-details-reveal').foundation('reveal', 'open');
    }

    function detailShow() {
        if (!$("#showDetailForm").valid()) {
            return false;
        }

        $("#loadingLogo").css("display", "inline-block");
        $("#button").attr("disabled", true);

        $("#showDetailForm").ajaxSubmit({callback: function (result) {
            $('#loadingLogo').css("display", "none");
            $("#button").attr("disabled", false);
            $("#project").attr("value", $("#projectList option:selected").text());
            $("#instance").attr("value", $("#serverList option:selected").text());
            $("#start").attr("value", $("#startDate").val());
            $("#end").attr("value", $("#endDate").val());
            $('div#search-details-reveal').foundation('reveal', 'close');
            detailShowSuccessOperation(result);
        }, validate: false});
    }

    function conditionSearch() {
        $('div#wait-reveal').foundation('reveal', 'open');
        $("#searchForm").ajaxSubmit({callback: function (result) {
            $('div#wait-reveal').foundation('reveal', 'close');
            conditionDetailSuccessOperation(result);
        }, validate: false});
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
/**
 * @author hubery
 */

var conditionTotalCount;
function conditionDetailSuccessOperation(result) {
    logIdList = result.logIdList;
    $("#detailList").text('');
    var detailList = result.actionLogDetails;
    if (null != detailList && 0 != detailList.length) {
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
    $("#conditionPageMessage").text('');
    var offset = result.offset;
    var fetchSize = result.fetchSize;
    conditionTotalCount = result.totalCount;
    var maxPageNum = parseInt(conditionTotalCount / fetchSize);
    $("#conditionPageMessage").attr("data-fetchSize", fetchSize);
    $("#conditionPageMessage").attr("data-current-index", offset / fetchSize + 1);

    var pageMsg;
    var previous = "<li";
    if (0 == offset / fetchSize) {
        previous += " class=\"arrow unavailable\"";
    }
    else {
        previous += " class=\"arrow\" onClick=\"conditionPrevious(this)\"";
    }
    previous += " ><a href=\"javascript:void(0)\">&laquo;</a></li>"
    pageMsg += previous;

    var limit = 0;
    if (maxPageNum > offset / fetchSize + 3) {
        limit = offset / fetchSize + 3;
    } else {
        limit = maxPageNum + 1;
    }

    for (var i = offset / fetchSize; i < limit; i++) {
        var pageNum = "<li";
        if (i == offset / fetchSize) {
            pageNum += " class= \"current\"";
        }
        else {
            pageNum += " onClick = \"conditionSearchDetail(this)\"";
        }
        pageNum += " data-index =" + (i + 1) + "><a href = \"javascript:void(0)\" >" + (i + 1) + " </a></li >"
        pageMsg += pageNum;
    }

    var next = "<li";
    if (maxPageNum == offset / fetchSize) {
        next += " class=\"arrow unavailable\"";
    } else {
        next += " class=\"arrow\" onClick=\"conditionNext(this)\"";
    }
    next += " ><a href=\"javascript:void(0)\">&raquo;</a></li>";
    pageMsg += next;

    $(pageMsg).appendTo($("#conditionPageMessage"));
}

function conditionPrevious(obj) {

    var currentIndex = $(obj).parent().attr("data-current-index");
    var fetchSize = $(obj).parent().attr("data-fetchSize");
    var offset = (currentIndex - 2) * fetchSize;
    conditionRedirectToSearch(offset);
}

function conditionNext(obj) {
    var currentIndex = $(obj).parent().attr("data-current-index");
    var fetchSize = $(obj).parent().attr("data-fetchSize");
    var offset = currentIndex * fetchSize;
    conditionRedirectToSearch(offset);
}

function conditionSearchDetail(obj) {
    var index = $(obj).attr("data-index");
    var fetchSize = $(obj).parent().attr("data-fetchSize");
    var offset = (index - 1) * fetchSize;
    conditionRedirectToSearch(offset);
}

function conditionRedirectToSearch(offset) {
    $('div#wait-reveal').foundation('reveal', 'open');
    $.ajax({
        type: "POST",
        url: golbalRootUrl + "project/instance/log/action/condition/change",
        data: "logIdList=" + logIdList + "&offset" + parseInt(offset) + "&conditionMessage=" + conditionMessage + "&totalCount=" + parseInt(conditionTotalCount),
        success: function (result) {
            $('div#wait-reveal').foundation('reveal', 'close');
            conditionDetailSuccessOperation(result);
        }
    });
}
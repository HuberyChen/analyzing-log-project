/**
 * @author hubery
 */

var totalCount;
function detailShowSuccessOperation(result) {
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
    $("#fuzzyPageMessage").text('');
    var offset = result.offset;
    var fetchSize = result.fetchSize;
    totalCount = result.totalCount;
    var maxPageNum = parseInt(totalCount / fetchSize);
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
    $('div#wait-reveal').foundation('reveal', 'open');
    var logIdShowList = logIdList;
    $.ajax({
        type: "POST",
        url: golbalRootUrl + "project/instance/log/action/change",
        data: "logIdList=" + logIdShowList + "&offset=" + parseInt(offset) + "&totalCount=" + parseInt(totalCount),
        success: function (result) {
            $('div#wait-reveal').foundation('reveal', 'close');
            detailShowSuccessOperation(result);
        }
    });
}
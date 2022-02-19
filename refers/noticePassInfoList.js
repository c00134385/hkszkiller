var pageIndex = 1;
var totalCount = 0;
var pageSize = 10;
var pageCount = 0;
$(function () {
    getNoticePassInfoListV2();
    // //加载更多
    // $(".hotel-more-btn").on('click', function () {
    //     pageIndex++;
    //     getNoticePassInfoList();
    // });
});

/**
 * 获取公示订单列表
 */
function getNoticePassInfoListV2() {
    $.ajax({
        url: "/passInfo/getNoticePassInfoList",
        type: "POST",
        data: {
            pageIndex: pageIndex
        },
        dataType: "json",
        beforeSend: function () {
            layerHelp.loading();
        },
        success: function (resp) {
            layer.closeAll();
            if (resp.status == 200 && resp.data != null) {
                var data = resp.data;
                totalCount = data.count;
                pageSize = data.pageSize;
                pageCount = Math.ceil(totalCount * 1.0 / pageSize);
                var list = data.list;
                $("#divDate").text(data.today);
                $("#divTotalCount").text(totalCount);
                if (totalCount == 0) {
                    $("#divNoData").show();
                    $("#divPage").hide();
                } else {
                    $("#divPage").show();
                    $("#divNoData").hide();
                    $(".spanTotalPage").text(pageIndex + "/" + pageCount);
                    var pageHtml = '';
                    var pageTemp = ' <option  value="$i" $selected >$page</option>'
                    for (var i = 1; i <= pageCount; i++) {
                        var select = '';
                        if (i == pageIndex) {
                            select = 'selected="true"';
                        }
                        pageHtml += pageTemp.replace(/\$i/g, i).replace("$selected", select)
                            .replace("$page", i + "/" + pageCount);
                    }
                    $(".SelectPages").html(pageHtml);
                    if (pageIndex <= 1) {
                        $(".claFirst").addClass("disable");
                    } else {
                        $(".claFirst").removeClass("disable");
                    }
                    if (pageIndex >= pageCount) {
                        $(".claLast").addClass("disable");
                    } else {
                        $(".claLast").removeClass("disable");
                    }
                }
                var html = '';
                $(list).each(function (i, item) {
                    html += _.template($("#temp").html())({
                        data: item
                    });
                });
                $("#div_noticeList").html(html);
            } else {
                layerHelp.msg(resp.msg);
            }
        }, error: function () {
            layer.closeAll();
        }
    });
}

//首页
function goFirst() {
    if (pageIndex == 1) {
        return;
    }
    pageIndex = 1;
    getNoticePassInfoListV2();
}

//尾页
function goLast() {
    if (pageIndex == pageCount) {
        return;
    }
    pageIndex = pageCount == 0 ? 1 : pageCount;
    getNoticePassInfoListV2();
}

//上一步
function goPre() {
    if (pageIndex == 1) {
        return;
    }
    if (pageIndex > 1) {
        pageIndex--;
    }
    getNoticePassInfoListV2();
}

//下一步
function goNext() {
    if (pageIndex == pageCount) {
        return;
    }
    if (pageIndex < pageCount) {
        pageIndex++;
    }
    getNoticePassInfoListV2();
}

//跳转任一页
function goPage(obj) {
    var page = parseInt($(obj).val());
    if (page >= 1 && page <= pageCount) {
        pageIndex = page;
    }
    getNoticePassInfoListV2();
}


/**
 * 获取公示订单列表
 */
function getNoticePassInfoList() {
    $.ajax({
        url: "/passInfo/getNoticePassInfoList",
        type: "POST",
        data: {
            pageIndex: pageIndex
        },
        dataType: "json",
        beforeSend: function () {
            layerHelp.loading();
        },
        success: function (resp) {
            layer.closeAll();
            if (resp.status == 200) {
                if (resp.data != null && resp.data.list != null && resp.data.list.length > 0) {
                    var data = resp.data.list;
                    var content = "";
                    if (pageIndex == 1) {
                        content += '<section class="card_info">\n' +
                            '<div class="flexbox f16">\n' +
                            '<strong>' + orderPublicView + '</strong>\n' +
                            '<div class="flex1 ta_r">\n' +
                            '' + resp.data.today + '（' + resp.data.count + '人）' +
                            '</div>\n' +
                            '</div>\n' +
                            '</section>' +
                            '<section class="card_info" style="background: #F4F6F9 ;">\n' +
                            '<div class="flexbox">\n' +
                            '<p class="yuyuenum">' + no + '</p>\n' +
                            '<div class="flex1">\n' +
                            '' + userName + '\n' +
                            '</div>\n' +
                            '<p class="zhenjianhao">\n' +
                            '' + certNo + '\n' +
                            '</p>\n' +
                            '</div>\n' +
                            '</section>';
                    }
                    data.forEach(function (it, i) {
                        content += '<section class="card_info">\n' +
                            '<div class="flexbox">\n' +
                            '<p class="yuyuenum f16""><strong>' + it.passNo + '</strong></p>\n' +
                            '<div class="flex1">\n' +
                            '' + it.userName + '' +
                            '</div>\n' +
                            '<p class="zhenjianhao">\n' +
                            '' + it.certNo + '' +
                            '</p>\n' +
                            '' +
                            '</section>';
                    });
                    $('#div_noticeList').append(content);
                    $(".hotel-more-btn").show();
                } else if (pageIndex == 1) {
                    content = '<section class="card_info">\n' +
                        '<div class="flexbox f16">\n' +
                        '<strong>' + orderPublicView + '</strong>\n' +
                        '<div class="flex1 ta_r">\n' +
                        '' + resp.data.today + '（' + resp.data.count + '人）' +
                        '</div>\n' +
                        '</div>\n' +
                        '</section>' +
                        '<section class="card_info" style="background: #F4F6F9 ;">\n' +
                        '<div class="flexbox">\n' +
                        '<p class="yuyuenum">' + no + '</p>\n' +
                        '<div class="flex1">\n' +
                        '' + userName + '\n' +
                        '</div>\n' +
                        '<p class="zhenjianhao">\n' +
                        '' + certNo + '\n' +
                        '</p>\n' +
                        '</div>\n' +
                        '</section>';
                    content += '<section class="card_info">\n' +
                        '<div class="flexbox" style="color:red;text-align: center;">\n' +
                        '暂无数据' +
                        '</section>';
                    $('#div_noticeList').append(content);
                    $(".hotel-more-btn").hide();
                } else {
                    $(".hotel-more-btn").hide();
                }
            } else {
                layerHelp.msg(resp.msg);
            }
        }, error: function () {
            layer.closeAll();
        }
    });
}
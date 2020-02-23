(function ($) {
    function crateCommentInfo(obj, options) {
        if (typeof(obj.time) === "undefined" || obj.time === "") {
            obj.time = getNowDateFormat();
        }
        var el = "<div class='comment-info'><header><img src='" + obj.img + "'></header><div class='comment-right'><h3>" + obj.replyName + "</h3>"
            + "<div class='comment-content-header'><span><i class='glyphicon glyphicon-time'></i>" + obj.time + "</span>";
        if (typeof(obj.address) !== "undefined" && obj.browse !== "") {
            el = el + "<span><i class='glyphicon glyphicon-map-marker'></i>" + obj.address + "</span>";
        }
        el = el + "</div><p class='content'>" + obj.content + "</p><div class='comment-content-footer'><div class='row'><div class='col-md-10'>";
        if (typeof(obj.osname) !== "undefined" && obj.osname !== "") {
            el = el + "<span><i class='glyphicon glyphicon-pushpin'></i> 来自:" + obj.osname + "</span>";
        }
        if (typeof(obj.browse) !== "undefined" && obj.browse !== "") {
            el = el + "<span><i class='glyphicon glyphicon-globe'></i> " + obj.browse + "</span>";
        }

        if (options.context) {
            el = el + "</div><div class='col-md-2'><span class='reply-btn' breId='" + obj.userId + "' drid='" + obj.id + "' dtid='" + obj.replyUserId + "'>回复</span></div></div></div><div class='reply-list'>";
        } else {
            el = el + "</div></div></div><div class='reply-list'>";
        }
        if (obj.replyBody !== "" && obj.replyBody.length > 0) {
            var arr = obj.replyBody;
            for (var j = 0; j < arr.length; j++) {
                var replyObj = arr[j];
                el = el + createReplyComment(replyObj);
            }
        }
        el = el + "</div></div></div>";
        return el;
    }

    function createReplyComment(reply) {
        return "<div class='reply'><div><a href='javascript:void(0)' class='replyname'>" + reply.replyName + "</a>:<a href='javascript:void(0)'>@" + reply.beReplyName + "</a><span>" + reply.content + "</span></div>"
            + "<p><span>" + reply.time + "</span> <span class='reply-list-btn'>回复</span></p></div>";
    }

    function getNowDateFormat() {
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = filterNum(nowDate.getMonth() + 1);
        var day = filterNum(nowDate.getDate());
        var hours = filterNum(nowDate.getHours());
        var min = filterNum(nowDate.getMinutes());
        var seconds = filterNum(nowDate.getSeconds());
        return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + seconds;
    }

    function filterNum(num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return num;
        }
    }

    function replyClick(el, options) {
        el.parent().parent().append("<div class='replybox'><textarea cols='80' rows='50' placeholder='请在这里回复评论,最多300个字哟~~' class='mytextarea' id='content2' maxlength='300' ></textarea><div class='btn btn-success pull-right' id='send'><i class='fa fa-edit'></i> 发表评论</div></div>").find("#send").click(function () {
            debugger;
            var content = $(this).prev().val();
            if (content !== "") {
                var parentEl = $(this).parent().parent().parent().parent();
                var obj = {};
                obj.replyName = options.context.cname;
                if (el.parent().parent().hasClass("reply")) {
                    obj.beReplyName = el.parent().parent().find("a:first").text();
                } else {
                    obj.beReplyName = parentEl.find("h3").text();
                }

                if (obj.replyName === obj.beReplyName) {
                    options.toastr.error("不能回复自己");
                    return;
                }
                obj.content = content;
                obj.time = getNowDateFormat();


                doFatherComment(content, options.drid, options.breId);
                var replyString = createReplyComment(obj);
                $(".replybox").remove();
                parentEl.find(".reply-list").append(replyString).find(".reply-list-btn:last").click(function () {
                    options.toastr.error("不能回复自己");
                    return;
                });
            } else {
                options.toastr.error("请输入该回复的评论内容");
                return;
            }
        });
    }

    $.fn.addCommentList = function (options) {
        var defaults = {data: [], add: ""};
        var option = $.extend(defaults, options);
        if (option.data.length > 0) {
            var dataList = option.data;
            var totalString = "";
            for (var i = 0; i < dataList.length; i++) {
                var obj = dataList[i];
                var objString = crateCommentInfo(obj, options);
                totalString = totalString + objString;
            }
            $(this).append(totalString).find(".reply-btn").click(function () {
                if (options.context.id === $(this).attr("dtid")) {
                    options.toastr.error("不能回复自己");
                    return;
                }

                if ($(this).parent().parent().find(".replybox").length > 0) {
                    $(".replybox").remove();
                } else {
                    $(".replybox").remove();
                    debugger
                    options.drid = $(this).attr("drid")
                    options.breId = $(this).attr("breId")
                    replyClick($(this), options);
                }
            });
            $(".reply-list-btn").click(function () {
                if ($(this).parent().parent().find(".replybox").length > 0) {
                    $(".replybox").remove();
                } else {
                    $(".replybox").remove();
                    debugger
                    replyClick($(this), options);
                }
            })
        }
    }
})(jQuery);
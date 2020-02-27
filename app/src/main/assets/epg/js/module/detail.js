var curPage = 0;
var innerDom = $("#contenter_inner");
var lineSpanDom = $("#line_span");
var innerHight = 0;
var lineSpanHeight = 0;
var code = getRequest()["code"];
var backUrl = getRequest()["backUrl"];
var stbType = typeof Authentication !== "undefined" ? Authentication.CTCGetConfig("STBType") : '';

window.onload = function () {
    key_Binds();
    init();
    window.focus();
}

function init() {
    //创维E900和E900s两盒子字体默认缩小了，针对这两盒子增加修正css
    if (stbType.indexOf("E900") > -1) {
        $("head")[0].innerHTML += '<link rel="stylesheet" href="./css/detail_revise.css">';
    }

    dataGetter.detailQuery({
        params: code,
        success: function (res) {
            scrollText('title_0', res.name, 36);
            scrollText('title_1', res.remark, 61);
            innerDom.innerHTML = res.contentText;
            setTimeout(function () {
                innerHight = innerDom.offsetHeight;
                lineSpanHeight = 490 / (Math.ceil(innerHight / 494));
                lineSpanDom.style.height = lineSpanHeight + "px";
                if (494 >= innerHight) {
                    lineSpanDom.parentNode.style.display = 'none';
                }
            }, 100)
            // 修复盒子B860AV1.1-T2文字显示一半问题
            if (stbType.indexOf("B860AV") > -1) {
                var contenterChildren = Array.prototype.slice.call($('#contenter_inner').children);
                for (var i = 0; i < contenterChildren.length; i++) {
                    contenterChildren[i].style.backgroundImage = 'url(./img/transparent.png)';
                }
            }
        },
        noData: function () {
            lineSpanDom.parentNode.style.display = 'none';
            innerDom.innerHTML = '<div class="empty">该内容已下架</div>';
        }
    })
}

function key_down_event() {
    if ((curPage + 1) * 494 < (innerHight - 1)) {
        curPage++;
        innerDom.style.top = -curPage * 494 + "px"
        lineSpanDom.style.top = curPage * lineSpanHeight + "px"
    }
}

function key_up_event() {
    if ((curPage - 1) >= 0) {
        curPage--;
        innerDom.style.top = -curPage * 494 + "px"
        lineSpanDom.style.top = curPage * lineSpanHeight + "px"
    }
}

function key_back_event() {
    window.location.href = backUrl;
}

/* 标题滚动 */
function scrollText(id, scroll, length) {
    if (!!scroll && scroll.length >= length) {
        $('#' + id).innerHTML = "<marquee>" + scroll + "</marquee>";
    } else {
        $('#' + id).innerHTML = scroll;
    }
}
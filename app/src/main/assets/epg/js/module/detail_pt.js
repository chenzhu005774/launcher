var auto;
var timeOut;
var num = 0;
var computerNum = false;
var autoPlayTime = 3000;
var curPage = 0;
var innerHight = 0;
var lineSpanHeight = 0;
var imagesLenth = 0;
var innerDom = $("#contenter_inner");
var lineDom = $("#line");
var lineSpanDom = $("#line_span");
var backUrl = getRequest()["backUrl"];
var code = getRequest()["code"];
var picUrl = 'http://223.221.38.160:8081';
var stbType = typeof Authentication !== "undefined" ? Authentication.CTCGetConfig("STBType") : '';
// var debug = $('#debug');

window.onload = function () {
    key_Binds();
    init();
    window.focus();
}

function init() {
    if (stbType.indexOf("E900") > -1) {
        $("head")[0].innerHTML += '<link rel="stylesheet" href="./css/detail_pt_revise.css">';
    }

    dataGetter.detailQuery({
        params: code,
        success: function (res) {
            var bannerStr = '';
            var nrStr = '';
            imagesLenth = res.posters.length;
            for (var i = 0; i < imagesLenth; i++) {
                bannerStr += '<span class="imgMiddle ' + (i === 0 ? "active" : "") + '" >' +
                    '<img id="banner_' + i + '" class="bannerImg"   src="' + picUrl + res.posters[i] + '">' +
                    '</span>';
                nrStr += '<li class="nr ' + (i === 0 ? "active" : "") + '"></li>';
            }
            bannerStr += '<img class="banner_img" src="img/banner_span.png">' +
                '<img class="banner_left" src="img/pic_left_0.png">' +
                '<img class="banner_right" src="img/pic_right_0.png">' +
                '<ul class="title">' + nrStr + '</ul>';
            scrollText('title_0', res.name, 36);
            scrollText('title_1', res.remark, 61);
            $('#banner').innerHTML = bannerStr;
            innerDom.innerHTML = res.contentText;
            setTimeout(function () {
                innerHight = innerDom.offsetHeight;
                lineSpanHeight = 380 / (Math.ceil(innerHight / 380));
                lineSpanDom.style.height = lineSpanHeight + "px";
                if (lineSpanHeight === 380) {
                    lineDom.style.display = 'none';
                }
            }, 100);
            // 修复盒子B860AV1.1-T2文字显示一半问题
            if (stbType.indexOf("B860AV1.1-T2") > -1) {
                var contenterChildren = Array.prototype.slice.call($('#contenter_inner').children);
                for (var i = 0; i < contenterChildren.length; i++) {
                    contenterChildren[i].style.backgroundImage = 'url(./img/transparent.png)';
                }
            }
            autoPlay();
        },
        noData: function () {
            var ele = document.createElement('div');
            ele.setAttribute('class', 'empty');
            ele.innerText = '该内容已下架';
            $('#box').appendChild(ele);
        }
    })
}

/* 文字向下翻页 */
function key_down_event() {
    if ((curPage + 1) * 380 < (innerHight - 1)) {
        curPage++;
        innerDom.style.top = -curPage * 380 + "px";
        lineSpanDom.style.top = curPage * lineSpanHeight + "px";
    }
}

/* 文字向上翻页 */
function key_up_event() {
    if ((curPage - 1) >= 0) {
        curPage--;
        innerDom.style.top = -curPage * 380 + "px"
        lineSpanDom.style.top = curPage * lineSpanHeight + "px"
    }
}

/* 右键事件 */
function key_right_event() {
    if (computerNum) {
        if (num <= (imagesLenth - 1)) {
            computerNum = false;
            clearInterval(auto);
            clearTimeout(timeOut);
            play(num);
            timeOut = setTimeout(function () {
                autoPlay();
            }, 1000);
        }
    } else {
        if ((num + 1) <= (imagesLenth - 1)) {
            num++;
            clearInterval(auto);
            clearTimeout(timeOut);
            play(num);
            timeOut = setTimeout(function () {
                autoPlay();
            }, 1000);
        }
    }
}

/* 左键事件 */
function key_left_event() {
    if (computerNum) {
        if ((num - 2) >= 0) {
            num -= 2;
            computerNum = false;
            clearInterval(auto);
            clearTimeout(timeOut);
            play(num);
            timeOut = setTimeout(function () {
                autoPlay();
            }, 1000);
        }
    } else {
        if ((num - 1) >= 0) {
            num--;
            clearInterval(auto);
            clearTimeout(timeOut);
            play(num);
            timeOut = setTimeout(function () {
                autoPlay();
            }, 1000);
        }
    }
}

/* 返回事件 */
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

/* 自动播放 */
function autoPlay() {
    var imgNum = imagesLenth - 1;
    auto = setInterval(function () {
        if (num > imgNum) {
            num = 0;
        }
        play(num);
        num++;
        computerNum = true;
    }, autoPlayTime)
}

/* 指定播放那个图片 */
function play(current) {
    var controller = $('.nr');
    var mod = $('.imgMiddle');
    for (var j = 0; j < controller.length; j++) {
        controller[j].className = 'nr';
    }
    controller[current].className = 'nr active';
    for (var x = 0; x < mod.length; x++) {
        mod[x].className = 'imgMiddle';
    }
    mod[current].className = 'imgMiddle active';
}
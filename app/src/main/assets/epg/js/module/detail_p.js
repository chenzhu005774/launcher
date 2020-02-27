var imagesLenth = 1; //默认有1张图片；
var bgCurPage = 0; //图片页码
var backUrl = getRequest()["backUrl"];
var code = getRequest()["code"];
var picUrl = 'http://223.221.38.160:8081';
var stbType = typeof Authentication !== "undefined" ? Authentication.CTCGetConfig("STBType") : '';

window.onload = function () {
    key_Binds();
    init();
    window.focus();
}

function init() {
    //创维E900和E900s两盒子字体默认缩小了，针对这两盒子增加修正css
    if (stbType.indexOf("E900") > -1) {
        $("head")[0].innerHTML += '<link rel="stylesheet" href="./css/detail_pic_revise.css">';
    }

    dataGetter.detailQuery({
        params: code,
        success: function (res) {
            var bgBannerStr = '';
            imagesLenth = res.posters.length;
            for (var i = 0; i < imagesLenth; i++) {
                bgBannerStr += '<span class="bgImgMiddle" >' +
                    '<img id="bgBanner_' + i + '" class="bgBannerImg"  src="' + picUrl + res.posters[i] + '">' +
                    '</span>';
            }
            $('#bgBanner').innerHTML = bgBannerStr;
            $('.title')[0].innerHTML = res.name + ' ' + res.remark;
            $('.title1')[0].innerHTML = res.name + ' ' + res.remark;
            $("#bgBanner_" + bgCurPage).style.display = "inline-block";
            changePageIcon(bgCurPage, imagesLenth);
        },
        noData: function () {
            var ele = document.createElement('div');
            ele.setAttribute('class', 'empty');
            ele.innerText = '该内容已下架';
            $('#box').appendChild(ele);
        }
    })
}

/* 切换大图箭头 */
function changePageIcon(currentPage, end) {
    $('bgBanner_left')[0].src = currentPage === 0 ? 'img/bg_left_0.png' : 'img/bg_left_1.png';
    $('bgBanner_right')[0].src = currentPage === end - 1 ? 'img/bg_right_0.png' : 'img/bg_right_1.png';
}



/* 右键事件 */
function key_right_event() {
    if (bgCurPage + 1 < imagesLenth) {
        if ($("#bgBanner_" + bgCurPage)) {
            $("#bgBanner_" + bgCurPage).style.display = "none";
        }
        bgCurPage++;
        $("#bgBanner_" + bgCurPage).style.display = "inline-block";
        changePageIcon(bgCurPage, imagesLenth);
    }
}

/* 左键事件 */
function key_left_event() {
    if (bgCurPage - 1 >= 0) {
        if ($("#bgBanner_" + bgCurPage)) {
            $("#bgBanner_" + bgCurPage).style.display = "none";
        }
        bgCurPage--;
        $("#bgBanner_" + bgCurPage).style.display = "inline-block";
        changePageIcon(bgCurPage, imagesLenth);
    }
}

/* 返回事件 */
function key_back_event() {
    window.location.href = backUrl;
}

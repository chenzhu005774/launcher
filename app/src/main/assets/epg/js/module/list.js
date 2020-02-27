var curId = getRequest()['curId'] || "left_0"; //默认选中值
var curLeftId = getRequest()["curLeftId"] || "left_0"; //默认驻留
var backUrl = getRequest()["backUrl"];
var title = getRequest()["title"];
var code = getRequest()["code"];
var curPage = getRequest()["curPage"] || 0;
var picUrl = 'http://223.221.38.160:8081';
var contentContainerHight = 0;
var sliderHeight = 0;
var stbType = typeof Authentication !== "undefined" ? Authentication.CTCGetConfig("STBType") : '';
var type = 1; // 1、头像列表；2、常规图文列表；3、常规文字列表；4、地图列表
var colLen = 4;

window.onload = function () {
    init();
    key_Binds();
    getTime();
    window.focus();
}

function init() {
    //创维E900和E900s两盒子字体默认缩小了，针对这两盒子增加修正css
    if (stbType.indexOf("E900") > -1) {
        $("head")[0].innerHTML += '<link rel="stylesheet" href="./css/list_revise.css">';
    }

    $('#title').innerText = title;
    dataGetter.categoryQuery({
        params: {
            cpCode: '004',
            limit: 6,
            parentCode: code,
            start: 1
        },
        success: function (data) {
            var str = '';
            for (var i = 0; i < data.content.length; i++) {
                str += '<div class="item" id="left_' + i + '"' +
                    'style="top:' + (122 + i * 70) + 'px;"' +
                    'type="' + data.content[i].type + '"' +
                    'code="' + data.content[i].code + '">' +
                    '<span class="left_txt" >' + data.content[i].name + '</span>' +
                    '<img class="focus_img" src="img/left_focus.png">' +
                    '<img class="check_img" src="img/left_check_bg.png">' +
                    '</div>';
            }
            $('#left').innerHTML = str;
            initLeftKey(data.content.length);
            $('#' + curLeftId).className = 'item item_focus';
            getListData(curLeftId, true);
        }
    });
}

/* 获取内容列表数据 */
function getListData(id, isInit) {
    var code = $('#' + id).getAttribute('code');
    // var type = $('#' + id).getAttribute('type');
    dataGetter.resourceQuery({
        params: {
            cpCode: '004',
            limit: 999,
            attributesOrderByAsc: ['sequence'],
            categoryCode: code,
            start: 1
        },
        success: function (res) {
            var str = '';
            if (res.content.length !== 0) {
                for (var i = 0; i < res.content.length; i++) {
                    switch (type) {
                        case 1:
                            colLen = 4;
                            $('#rightContanier').className = 'type_1';
                            str += '<li class="item" id="right_' + i + '"' +
                                'type="' + res.content[i].type + '"' +
                                'code="' + res.content[i].code + '">' +
                                '<div class="item_inner">' +
                                '<img class="focus_img" src="img/focus_type_1.png">' +
                                '<img class="origin_img" src="' + picUrl + res.content[i].thumbnail + '">' +
                                '<img class="span_img" src="img/span_img.png">' +
                                '<span class="right_txt">' + res.content[i].name + '</span>' +
                                '</div>'
                            '</li>';
                            break;
                        case 2:
                            colLen = 3;
                            $('#rightContanier').className = 'type_2';
                            str += '<li class="item" id="right_' + i + '"' +
                                'type="' + res.content[i].type + '"' +
                                'code="' + res.content[i].code + '">' +
                                '<div class="item_inner">' +
                                '<img class="focus_img" src="img/focus_type_2.png">' +
                                '<img class="origin_img" src="' + picUrl + res.content[i].thumbnail + '">' +
                                '</div>' +
                                '<span class="right_txt">' + res.content[i].name + '</span>' +
                                '</li>';
                            break;
                        case 3:
                            colLen = 1;
                            $('#rightContanier').className = 'type_3';
                            str += '<li class="item" id="right_' + i + '"' +
                                'type="' + res.content[i].type + '"' +
                                'code="' + res.content[i].code + '">' +
                                '<div class="item_inner">' +
                                '<img class="focus_img" src="img/focus_type_3.png">' +
                                '<span class="right_txt">' + res.content[i].name + '</span>' +
                                '<span class="right_time">' + res.content[i].createTime.split(" ")[0].replace(/-/g, '.') + '</span>' +
                                '</div>'
                            '</li>';
                            break;
                        case 4:
                            str += '<li class="item" id="right_' + i + '"' +
                                'type="' + res.content[i].type + '"' +
                                'code="' + res.content[i].code + '">' +
                                '<div class="item_inner">' +
                                '<img class="focus_img" src="img/focus_type_1.png">' +
                                '<img class="origin_img" src="' + picUrl + res.content[i].thumbnail + '">' +
                                '<img class="span_img" src="img/span_img.png">' +
                                '<span class="right_txt">' + res.content[i].name + '</span>' +
                                '</div>'
                            '</li>';
                            break;

                    }

                }
                $("#right").innerHTML = str;
                contentContainerHight = $("#right").offsetHeight;
                sliderHeight = 550 / (Math.ceil(contentContainerHight / 550));
                $('#slider').style.height = sliderHeight + "px";
                $("#empty").style.display = 'none';
                $('#right').style.top = "0px";
                $('#slider').style.top = "0px";
                curPage = 0;
                if (contentContainerHight > 550) $('#scrollbar').style.display = 'block';
                initRightKey(res.content.length);
            } else {
                $("#right").innerHTML = str;
                $("#empty").style.display = 'block';
            }
            if (isInit) {
                key_effect_opt(curId)
            }
        }
    })
}

/* 左侧焦点绑定 */
function initLeftKey(length) {
    for (var i = 0; i < length; i++) {
        var obj = {
            'effect': {
                'blur': {
                    'func': function () {
                        if (curLeftId === currentSelectObjectId) {
                            $('#' + currentSelectObjectId).className = 'item item_check';
                        } else {
                            $('#' + currentSelectObjectId).className = 'item';
                        }
                    }
                },
                'focus': {
                    "class": "item item_focus",
                }
            },
            'nav': {
                'right': {
                    'id': 'right_0_txt,right_0'
                },
                'left': {
                    'id': ''
                },
                'up': {
                    'id': "left_" + (i - 1),
                },
                'down': {
                    'id': "left_" + (i + 1),
                }
            },
            'click': {
                'func': function () {
                    if ($('#' + curLeftId)) {
                        $('#' + curLeftId).className = 'item';
                    }
                    $('#' + currentSelectObjectId).className = 'item item_focus';
                    curLeftId = currentSelectObjectId;
                    getListData(currentSelectObjectId);
                }
            }
        };
        elementMap.put("left_" + i, obj);
    }
}

/* 右侧焦点绑定 */
function initRightKey(length) {
    for (var i = 0; i < length; i++) {
        var obj = {
            'effect': {
                'blur': {
                    "class": "item",
                    'func': reScrollNew
                },
                'focus': {
                    "class": "item item_focus",
                    'func': scrollNew
                }
            },
            'nav': {
                'right': {
                    'id': (function () {
                        if ((i + 1) % colLen === 0) {
                            return '';
                        }
                        return "right_" + (i + 1)
                    })()
                },
                'left': {
                    "id": (function () {
                        if (i % colLen === 0) {
                            return curLeftId;
                        }
                        return "right_" + (i - 1)
                    })()
                },
                'up': {
                    'id': "right_" + (i - colLen),
                    'func': function (id) {
                        var _i = parseInt(id.split('_')[1]) + 1;
                        var row = type === 3 ? 8 : 2;
                        var martop = curPage - 1 ? -41 : 0;
                        if ((curPage - 1) >= 0 && Math.ceil(_i / colLen) % row === 0) {
                            curPage--;
                            $('#right').style.top = martop - curPage * 550 + "px"
                            $('#slider').style.top = curPage * sliderHeight + "px";
                        }
                    }
                },
                "down": {
                    "id": "right_" + (i + colLen),
                    'func': function (id) {
                        var _i = parseInt(id.split('_')[1]) + 1;
                        var row = type === 3 ? 8 : 2;
                        var flag = _i >= length ? true : Math.ceil(_i / colLen) % row === 1;
                        if ((curPage + 1) * 550 < (contentContainerHight - 1) && flag) {
                            curPage++;
                            $('#right').style.top = -41 - curPage * 550 + "px";
                            $('#slider').style.top = curPage * sliderHeight + "px";
                        }
                        if (_i >= length) {
                            key_blur_opt(currentSelectObjectId);
                            currentSelectObjectId = 'right_' + (length - 1);
                            key_effect_opt(currentSelectObjectId);
                        }
                    }
                },
            },
            'click': {
                'func': forwardPage
            }
        };
        elementMap.put("right_" + i, obj);
    }
}

/* 返回 */
function key_back_event() {
    location.href = backUrl;
}

/* 得到加密的返回地址 */
function getBackUrl() {
    var url = escape(
        document.location.href.split('?')[0] +
        "?curId=" + currentSelectObjectId +
        "&curLeftId=" + curLeftId +
        "&title=" + escape(title) +
        "&code=" + code +
        "&curPage=" + curPage +
        "&backUrl=" + escape(backUrl));
    return url;
}

/* 聚焦滚动 */
function scrollNew() {
    var parent = $('#' + currentSelectObjectId);
    var dom = parent.getElementsByClassName('right_txt')[0];
    var scroll = dom.innerText;
    var length = 9;
    if (!!scroll && scroll.length > length) {
        dom.innerHTML = "<marquee>" + scroll + "</marquee>";
    }
}

/* 失焦取消滚动 */
function reScrollNew() {
    var parent = $('#' + currentSelectObjectId);
    var dom = parent.getElementsByClassName('right_txt')[0];
    dom.innerHTML = dom.innerText;
}

/* 跳转页面 */
function forwardPage() {
    var code = $('#' + currentSelectObjectId).getAttribute('code');
    var type = $('#' + currentSelectObjectId).getAttribute('type');
    if (getRequest()["backUrl"]) {
        memoryTool.push(getRequest()['backUrl']);
    }
    switch (type) {
        case '0':
            window.location.href = 'detail.html?code=' + code + '&backUrl=' + getBackUrl();
            break; // 文字详情
        case '1':
            window.location.href = 'detail_pt.html?code=' + code + '&backUrl=' + getBackUrl();
            break; // 图文详情
        case '2':
            dataGetter.detailQuery({
                params: code,
                success: function (res) {
                    window.location.href = 'jump.html?isSelfPage=1&playCode=' + res.playCode + '&contentCode=' + code + "&backUrl=" + getBackUrl();
                }
            })
            break; // 视频详情
        case '3':
            window.location.href = 'detail_p.html?code=' + code + '&backUrl=' + getBackUrl();
            break; // 图片详情  
    }
}
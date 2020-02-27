var currentSelectObjectId=""; //当前元素ID
var currentSelectOldObjectId = ""; //上个元素ID
var historySelectObjectId=null; //历史选择ID
var backNavigationUrl=""; //返回导航地址
var siteBackType=1; //1:使用cookie记录导航地址,2:使用cookie记录当前焦点参数,3:使用框架返回调度
var currentPageKey="";//当前页面标识，为了适应第2种导航,需要使用第2种方式在每一个页面赋一个唯一的标识值如：mov_index,mov_list,mov_detail
var currentPageCookiePrmObj=null; //当前页面cookie参数对象
var effectDefaultObj={"blur":{"class":"item"},"focus":{"class":"item item_focus"}}; //默认效果对象
var titleUrlObj = {"titleUrl":""}; //调用title里的URL跳转

var KEY_BACK = 8;
var KEY_OK = 13;
var KEY_LEFT = 37;
var KEY_UP = 38;
var KEY_RIGHT = 39;
var KEY_DOWN = 40;
var KEY_PAGEUP = 33;
var KEY_PAGEDOWN = 34;
var KEY_0 = 48;
var KEY_1 = 49;
var KEY_2 = 50;
var KEY_3 = 51;
var KEY_4 = 52;
var KEY_5 = 53;
var KEY_6 = 54;
var KEY_7 = 55;
var KEY_8 = 56;
var KEY_9 = 57;
var KEY_VOLUP = 259;
var KEY_VOLDOWN = 260;
var KEY_DEL = 46; //海信键值

var elementMapObj={};  //元素关系对象
var elementMapClass = function(){
    this.put=function(_key,_value){
        elementMapObj[_key]=_value;
    };
    this.get=function(_key){
        return elementMapObj[_key];
    };
}

var elementMap = new elementMapClass();

//键值绑定
function key_Binds() {
    //document.onkeypress = key_opt_event;
    document.onkeydown = key_opt_event;
}

//键值处理事件
function key_opt_event() {
    var keyCode = event.which ? event.which: event.keyCode;
    key_opt(keyCode);
    if(keyCode==340) //禁止华数ipanel盒子自动返回
        return 0;  //兼容ipannel 返回
}

//键值处理
function key_opt(keyCode){
    switch (keyCode) {
        case KEY_0:
        case KEY_1:
        case KEY_2:
        case KEY_3:
        case KEY_4:
        case KEY_5:
        case KEY_6:
        case KEY_7:
        case KEY_8:
        case KEY_9:
            key_number_event(keyCode-48);
            break;
        case 1:  //ipannel
        case KEY_UP:
            key_up_event();
            break;
        case 2:  //ipannel
        case KEY_DOWN:
            key_down_event();
            break;
        case 3:  //ipannel
        case KEY_LEFT:
            key_left_event();
            break;
        case 4:  //ipannel
        case KEY_RIGHT:
            key_right_event();
            break;
        case KEY_OK:
            key_ok_event();
            break;
        case 32: //空格键
        case 45: //兼容云平台
        case 340: //ipannel 返回
        case KEY_BACK:
            key_back_event();
            break;
        case KEY_PAGEUP:
            key_pageUp_event();
            break;
        case KEY_PAGEDOWN:
            key_pageDown_event();
            break;
        case KEY_DEL:
            key_del_event();
            break;
        case 768:
            if(!!goUtility){
                goUtility();
            }
            break;
        default:
            key_default_event(keyCode);
            break;
    }
}

function key_default_event(){}
function key_number_event(num){}
function key_up_event(){key_up_opt();}
function key_down_event(){key_down_opt();}
function key_left_event(){key_left_opt();}
function key_right_event(){key_right_opt();}
function key_ok_event(){key_click_opt();}
function key_back_event(){key_back_opt();}
function key_pageUp_event(){}
function key_pageDown_event(){}
function key_del_event(){};

//定时执行按键翻上页,防止用户快速按键跳页
var keyPageUpTimeObj=null;
function timeKeyPageUp(){
    if(keyPageUpTimeObj!=null)
        clearTimeout(keyPageUpTimeObj);
    keyPageUpTimeObj=setTimeout("key_pageUp_event()",500);
}

//定时执行按键翻下页,防止用户快速按键跳页
var keyPageDownTimeObj=null;
function timeKeyPageDown(){
    if(keyPageDownTimeObj!=null)
        clearTimeout(keyPageDownTimeObj);
    keyPageDownTimeObj=setTimeout("key_pageDown_event()",500);
}


//上键处理
function key_up_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null && typeof(obj.nav)=="object" && typeof(obj.nav.up)=="object")
        key_navSon_opt(obj.nav.up);
}

//下键处理
function key_down_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null && typeof(obj.nav)=="object" && typeof(obj.nav.down)=="object")
        key_navSon_opt(obj.nav.down);
}
//左键处理
function key_left_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null && typeof(obj.nav)=="object" && typeof(obj.nav.left)=="object")
        key_navSon_opt(obj.nav.left);
}
//右键处理
function key_right_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null && typeof(obj.nav)=="object" && typeof(obj.nav.right)=="object")
        key_navSon_opt(obj.nav.right);
}


//点击处理
function key_click_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="undefined" || obj==null || typeof(obj)!="object" || typeof(obj.click)!="object")
        return;
    var levelKey = null;
    if(typeof(obj.levelKey)!="undefined"){
        levelKey = obj.levelKey;
    }
    var notRecordUrl=false;
    if(typeof(obj.notRecordUrl)!="undefined" && obj.notRecordUrl)
        notRecordUrl = true;
    for(var item in obj.click){
        var itemObj = obj.click[item];
        if(item!="titleUrl"){
            if(itemObj==null || itemObj=="")
                continue;
        }
        switch(item){
            case "func":
                if(typeof(itemObj)=="function")
                    itemObj();
                break;
            case "url":
                if(siteBackType==3)
                    key_level_goto_Url(itemObj,levelKey)
                else
                    key_goto_Url(itemObj,notRecordUrl);
                break;
            case "titleUrl":
                var tempObj = document.getElementById(currentSelectObjectId);
                if(tempObj!=null && typeof(tempObj.title)!="undefined"){
                    if(siteBackType==3)
                        key_level_goto_Url(tempObj.title,levelKey)
                    else
                        key_goto_Url(tempObj.title,notRecordUrl);
                }
                break;
        }
    }
}

//返回处理
function  key_back_opt(){
    switch(siteBackType){
        case 1: //使用cookie记录导航
            if(typeof(gotoBackNavigationUrl)=="function"){
                gotoBackNavigationUrl();
            }else{
                history.back();
            }
            break;
        case 2: //使用cookie记录参数
            history.back();
            break;
        case 3: //使用框架调度
            if(typeof(parent.levelBack)=="function"){
                //调用页面层级离开方法
                if(typeof(leaveLayer)=="function")
                    leaveLayer();
                parent.levelBack();
            }else{
                //history.back();
            }
            break;
    }
}

//导航子元素处理
function key_navSon_opt(navSonObj){
    var func;
    var id;
    for(var item in navSonObj){
        var itemObj = navSonObj[item];
        if(itemObj==null || itemObj=="")
            continue;
        switch(item){
            case "func":
                if(typeof(itemObj)=="function")
                    // itemObj();
                    func = itemObj;
                break;
            case "id":
                if(itemObj.indexOf(",")>-1){
                    var strs = itemObj.split(",");
                    var len = strs.length;
                    for(var i=0;i<len;i++){
                        var isObj = key_effect_opt(strs[i]);
                        id = strs[i];
                        if(isObj)
                            break;
                    }
                }else{
                    id = itemObj;
                    key_effect_opt(itemObj);
                }
                break;
        }
    }

    if(func) func(id);
}

//效果处理
function key_effect_opt(elementId){
    var isObj=false;
    var obj = document.getElementById(elementId);
    if(typeof(obj)=="object" && obj!=null && obj.style.display !="none"){
        key_blur_opt();
        currentSelectObjectId = elementId;
        key_focus_opt();
        currentSelectOldObjectId = currentSelectObjectId;
        isObj=true;
    }
    return isObj;
}

//移除焦点处理
function key_blur_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null){
        var tempObj = effectDefaultObj.blur;
        if(typeof(obj.effect)=="object" && obj.effect!=null && typeof(obj.effect.blur)=="object" && obj.effect.blur!=null)
            tempObj = obj.effect.blur;
        key_effectSon_opt(tempObj);
        if(typeof(obj.scrollText)=="object" && obj.scrollText!=null && typeof(obj.scrollText.id)!="undefined" && typeof(scrollText)=="object")
            scrollText.revert();
    }
}

//焦点处理
function key_focus_opt(){
    var obj = elementMap.get(currentSelectObjectId);
    if(typeof(obj)=="object" && obj!=null){
        var tempObj = effectDefaultObj.focus;
        if(typeof(obj.effect)=="object" && obj.effect!=null && typeof(obj.effect.focus)=="object" && obj.effect.focus!=null)
            tempObj = obj.effect.focus;
        key_effectSon_opt(tempObj);
        if(typeof(obj.scrollText)=="object" && obj.scrollText!=null && typeof(obj.scrollText.id)!="undefined" && typeof(obj.scrollText.enTextLen)!="undefined"  && typeof(obj.scrollText.enSingleWidth)!="undefined" && typeof(scrollText)=="object")
            scrollText.init({"scrollId":obj.scrollText.id,"enTextLen":obj.scrollText.enTextLen,"enSingleWidth":obj.scrollText.enSingleWidth,"timeDelayScroll":200});
    }
}

//效果子元素处理
function key_effectSon_opt(effectSonObj){
    for(var item in effectSonObj){
        var itemObj = effectSonObj[item];
        if(itemObj==null || itemObj=="")
            continue;
        switch(item){
            case "class":
                var obj = document.getElementById(currentSelectObjectId);
                if(typeof(obj)=="object" && obj!=null)
                    obj.className=itemObj;
                break;
            case "style":
                var obj = document.getElementById(currentSelectObjectId);
                if(typeof(obj)=="object" && obj!=null)
                    obj.style.cssText=itemObj;
                break;
            case "func":
                if(typeof(itemObj)=="function")
                    itemObj();
                break;
        }
    }
}

//根据Url进行层级跳转
function key_level_goto_Url(urlStr,levelKey){
    if(urlStr==null || urlStr=="" || urlStr.length<1)
        return;
    var temp = urlStr.split("?");
    var url = temp[0];
    if (temp.length > 1)
        url += "?" + encodeURI(temp[1]);
    if(typeof(parent.levelCutover)=="function")
        parent.levelCutover(url,levelKey,currentSelectObjectId);
    else
        window.location.href = urlStr ;
}

//根据Url进行跳转
function key_goto_Url(urlStr,notRecordUrl){
    if(urlStr==null || urlStr=="" || urlStr.length<1)
        return;
    var temp = urlStr.split("?");
    var url = temp[0];
    if (temp.length > 1)
        url += "?" + encodeURI(temp[1]);
    if(!notRecordUrl){
        key_recordUrl();
    }
    window.location.href = urlStr ;
}

//记录地址
function key_recordUrl(){
    //记录Url
    if(siteBackType==1){
        addNavigationUrl(getCurrentSaveUrl());
    }else if(siteBackType==2){ //记录参数
        setCookie(currentPageKey,getCurrentSaveParameter());
    }
}

//页面加载获取当前页cookie保存值
function pageLoadGetCookieSaveValue(){
    //URL地址返回
    if(siteBackType==1){
        historySelectObjectId=getHistorySelectObjectId();
        var tempSelectObjectId = getCookieHistorySelectObjectId();
        if(tempSelectObjectId.length>0)
            historySelectObjectId=tempSelectObjectId;
    }else if(siteBackType==2){
        var para=getCookie(currentPageKey); //获取当前页cookie存储参数
        if(para!=null){
            var tempObj = getUrlParameterObj(para);
            if(typeof(tempObj.historySelectObjectId)!="undefined"){
                currentPageCookiePrmObj=tempObj;
                historySelectObjectId=tempObj.historySelectObjectId;
                delCookie(currentPageKey); //使用cookie数据后，开始删除
            }
        }
    }
}

//页面加载显示焦点
function pageLoadShowFocus(){
    var tempObjectId = currentSelectObjectId;
    if(historySelectObjectId!=null){
        var obj = document.getElementById(historySelectObjectId);
        //当历史元素存在，再更改，避免焦点丢失
        if(typeof(obj)=="object" && obj!=null && obj.style.display !="none"){
            tempObjectId=historySelectObjectId;
        }
    }
    var obj = document.getElementById(tempObjectId);
    if(typeof(obj)=="object" && obj!=null && obj.style.display !="none"){
        key_blur_opt();
        currentSelectObjectId = tempObjectId;
        key_focus_opt();
        if(currentSelectObjectId==historySelectObjectId){ //焦点执行一次，则清空
            historySelectObjectId=null;
        }
    }
}


//获取当前地址
function getCurrentSaveUrl(){
    var url = window.location.href;
    if(typeof(pageIndex)!="undefined" && pageIndex>0)
        url=replaceUrlParams(url,"pageIndex",pageIndex);
    return replaceUrlParams(url,"historySelectObjectId",currentSelectObjectId);
}

//获取当前保存参数
function getCurrentSaveParameter(){
    var para = "historySelectObjectId="+currentSelectObjectId;
    if(typeof(pageIndex)!="undefined" && pageIndex>0)
        para +="&pageIndex="+pageIndex;
    return para;
}

//获取记录地址里的参数对象
function getUrlParameterObj(url){
    var pos = url.indexOf("?");
    var parastr = url.substring(pos + 1);
    var para = parastr.split("&");
    var obj = {};
    for (i = 0; i < para.length; i++) {
        var tempstr = para[i];
        pos=tempstr.indexOf("=");
        obj[tempstr.substring(0,pos)]=tempstr.substring(pos+1);
    }
    return obj;
}

//获取历史记录焦点
function getHistorySelectObjectId(){
    if(historySelectObjectId!=null)
        return historySelectObjectId;
    historySelectObjectId = getUrlParam("historySelectObjectId");
    return historySelectObjectId;
}

//获取Cookie历史记录焦点
function getCookieHistorySelectObjectId(){
    backNavigationUrl = getBackNavigationUrl();
    var reStr="";
    if(backNavigationUrl.length>0){
        historySelectObjectId = getUrlParam("historySelectObjectId",backNavigationUrl);
        reStr=historySelectObjectId;
    }
    return reStr;
}

//替换地址参数值
function replaceUrlParams(url,key,value){
    var index= url.indexOf(key+"=");
    if(index>-1){
        var before =  url.substring(0,index);
        var after =  url.substring(index);
        index = after.indexOf("&");
        after = (index>-1) ?  after.substring(index) : "";
        url = before+key+"="+value+after;
    }else{
        url += (url.indexOf("?")>-1) ? "&" : "?";
        url += key+"="+value;
    }
    return url;
}

//获取URL地址中的参数
function getUrlParam(strname,url) {
    var hrefstr, pos, parastr, para, tempstr;
    hrefstr = window.location.href;
    if(typeof(url)!="undefined")
        hrefstr=url;
    pos = hrefstr.indexOf("?");
    parastr = hrefstr.substring(pos + 1);
    para = parastr.split("&");
    tempstr = "";
    for (i = 0; i < para.length; i++) {
        tempstr = para[i];
        pos = tempstr.indexOf("=");
        if (tempstr.substring(0, pos) == strname) {
            var temppath = tempstr.substring(pos + 1);
            return temppath;
        }
    }
    return null;
}

//初始化滚动文字，过长使用截取字符串，并在后面加...
function initElementScrollText(){
    var elements = elementMap.elements;
    var len = elements.length;
    for(i=0;i<len;i++){
        var obj = elements[i].value;
        //当有文字滚动对象才处理
        if(typeof(obj.scrollText)=="object" && obj.scrollText!=null){
            obj=obj.scrollText;
            var eleObj = ele(obj.id);
            //如果元素存在
            if(eleObj.getObj()!=null){
                var textStr = eleObj.html();
                var realLen = getStrRealLen(textStr);
                if(realLen>obj.enTextLen){
                    var subStrs = getSubStr(textStr,obj.enTextLen,true);
                    eleObj.attr("title",subStrs+","+textStr);
                    eleObj.html(subStrs);
                    var tempEleObj=ele(obj.id+"_vessel");
                    //如果text上级元素存在，则把txtSlideWrap txtNoSlide 换成txtSlideWrap,避免导致不能滚动
                    if(tempEleObj.getObj()!=null && tempEleObj.className().indexOf("txtNoSlide")>-1){
                        tempEleObj.className("txtSlideWrap");
                    }
                }
            }
        }
    }
}
/*简单文字滚动*/
var scrollTool={
    startScroll:function(){
        var obj = elementMap.get(currentSelectObjectId);
        var scroll = obj["scroll"];
        if(!!scroll && getStrRealLen(scroll.text)>scroll.len){
            $(scroll.id).innerHTML="<marquee>"+scroll.text+"</marquee>";
        }
    },
    removeScroll:function(){
        var obj = elementMap.get(currentSelectObjectId);
        var scroll = obj["scroll"];
        if(!!scroll){
            $(scroll.id).innerHTML=scroll.text;
        }
    }
}

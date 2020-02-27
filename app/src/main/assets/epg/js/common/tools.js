/*实时刷新时间*/
function getTime() {
    var fnW = function (str) {
        return str >= 10 ? str : "0" + str;
    };
    var timeDom = document.getElementById("weektime");
    var dateDom = document.getElementById("date");
    var date = new Date(); //日期对象
    var hour = date.getHours();
    var minute = date.getMinutes();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var currentDay = date.getDate();
    var days = ['日', '一', '二', '三', '四', '五', '六'];
    if(timeDom) timeDom.innerHTML = fnW(hour) + ":" + fnW(minute);
    if(dateDom) dateDom.innerHTML = '周' + days[date.getDay()] + ' ' + year + '.' + fnW(month) + '.' + fnW(currentDay);
    setTimeout(function () {
        getTime();
    }, 1000)
}

/*处理地址*/
function getRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

/*获取epgDomain*/
function getDomain() {
    var start = 0;
    var EPGDomain = typeof Authentication !== "undefined" ? Authentication.CTCGetConfig("EPGDomain") : '';
    if (EPGDomain.indexOf("https") > -1) start = 8;
    else if ((EPGDomain.indexOf("http") > -1)) start = 7;
    var tmp = EPGDomain.substring(start, EPGDomain.length);
    var result = EPGDomain.substring(0, tmp.indexOf("/") + start);
    return result;
}

/* 设置cookie */
function setCookie(key, val) {
    var Days = 7; //此 cookie 将被保存 7 天
    var exp = new Date(); //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = key + "=" + escape(val) + ";expires=" + exp.toGMTString() + ";path=/";
};

/* 获取cookie */
function getCookie(objName) { //获取指定名称的cookie的值
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName) return unescape(temp[1]);
    }
}

/* 删除cookie */
function delCookie(key) {
    //为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
    var date = new Date();
    date.setTime(date.getTime() - 10000);
    document.cookie = key + "=;expires=" + date.toGMTString() + ";path=/";
}

/* 获取样式 */
function getStyle(obj, name) {
    if (window.getComputedStyle) {
        return getComputedStyle(obj, null)[name];
    } else {
        return obj.currentStyle[name];
    }
}

/* 使用$,获取相应对象 */
function $(name) {
    if (name[0] === '#') {
        return document.getElementById(name.substr(1));
    } else if (name[0] === '.') {
        return document.getElementsByClassName(name.substr(1));
    } else {
        return document.getElementsByTagName(name);
    }
}

/* 获取字符串真实长度,中文字符算2长度 */
function getStrRealLen(str) {
    if(str==null)
        return 0;
    var len = 0;
    var strLen = str.length;
    for(var i = 0;i<strLen;i++){
        a = str.charAt(i);
        len++;
        if(escape(a).length > 4)
        {//中文字符的长度经编码之后大于4
            len++;
        }
    }
    return len;
}

/* 截取字符串,中文字符算2长度 */
function getSubStr(str,len,isSuffix)
{
    if(str==null)
        return "";
    var realLen = getStrRealLen(str);
    if(realLen<=len){
        return str;
    }else{
        var str_length = 0;
        var str_cut = new String();
        var str_len = str.length;
        if(isSuffix)
            len -=3;
        for(var i = 0;i<str_len;i++){
            var a = str.charAt(i);
            str_length++;
            if(escape(a).length > 4)
            {
                //中文字符的长度经编码之后大于4
                str_length++;
            }
            str_cut = str_cut.concat(a);
            if(str_length>=len)
            {
                if(isSuffix){
                    str_cut = str_cut.concat("...");
                }
                return str_cut;
            }
        }
        //如果给定字符串小于指定长度，则返回源字符串；
        if(str_length<len)
            return  str;
    }
}
var dataGetter = {
	domain: "http://223.221.38.160:8093",
	// domain: "http://192.168.2.207:1790/industry-iptv-api",
	ajax: function (obj) {
		var xmlhttprequest;
		if (window.XMLHttpRequest) {
			xmlhttprequest = new XMLHttpRequest();
			if (xmlhttprequest.overrideMimeType) {
				xmlhttprequest.overrideMimeType("text/xml");
			}
		} else if (window.ActiveXObject) {
			var activeName = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];
			for (var i = 0; i < activeName.length; i++) {
				try {
					xmlhttprequest = new ActiveXObject(activeName[i]);
					break;
				} catch (e) {
					console.log(e)
				}
			}
		}
		if (!!xmlhttprequest) {
			if (obj.method == "get") {
				// var IPTVAccount = getCookie("UserID") || "ywzc43@ITVP";
				var url = obj.url;
				// url += (url.indexOf("?") == -1 ? "?" : "&");
				// url += "IPTVAccount=" + IPTVAccount;
			} else {
				var url = obj.url;
			}
			xmlhttprequest.open(obj.method, url, true);
			xmlhttprequest.onreadystatechange = function () {
				if (xmlhttprequest.readyState == 4) { // 4 = "loaded"
					if (xmlhttprequest.status == 200) { // 200 = OK
						var data = xmlhttprequest.responseText;
						if (!!data && data.length > 0) {
							try {
								var dataObj = eval('(' + data + ')');
								obj.success && obj.success(dataObj);
							} catch (e) {
								console.log(e)
								obj.fail && obj.fail();
							}
						} else {
							obj.fail && obj.fail();
						}
					} else if (xmlhttprequest.status == 0) { //取消连接

					} else {
						if (obj.fail) obj.fail();
					}
				}
			};
			if (obj.method == "get") {
				xmlhttprequest.send();
			} else {
				xmlhttprequest.setRequestHeader("content-type", "application/json");
				xmlhttprequest.send(obj.data);
			}

		}
		return xmlhttprequest;
	},
	queryHomePage: function (obj) {
		obj.method = "get";
		obj.url = this.domain + "/api/epg/template/" + obj.params.orgCode + "?itvAccount=" + obj.params.itvAccount;
		// obj.url = this.domain + "/manage/templatePage/getAll/" + obj.params.orgCode;
		return this.ajax(obj);
	},
	//栏目查询(除首页其他栏目)
	categoryQuery: function (obj) {
		obj.method = "post";
		obj.url = this.domain + "/api/epg/categories";
		obj.data = JSON.stringify(obj.params)
		return this.ajax(obj);
	},
	//栏目内容查询
	resourceQuery: function (obj) {
		obj.method = "post";
		obj.url = this.domain + "/api/epg/contents";
		obj.data = JSON.stringify(obj.params)
		return this.ajax(obj);
	},
	//栏目内容查询
	detailQuery: function (obj) {
		obj.method = "get";
		obj.url = this.domain + "/api/epg/content/" + obj.params;
		return this.ajax(obj);
	},
	//播放记录查询
	playHistoryQuery: function (obj) {
		obj.method = "post";
		obj.url = this.domain + "/api/epg/playHistory";
		obj.data = JSON.stringify(obj.params)
		return this.ajax(obj);
	},
	//保存播放时间点
	playBookMarksSave: function (obj) {
		obj.method = "post";
		obj.url = this.domain + "/api/user/playBookMarks";
		obj.data = JSON.stringify(obj.params)
		return this.ajax(obj);
	},
	//公告获取
	announcementQuery: function (obj) {
		obj.method = "get";
		obj.url = this.domain + "/api/epg/marquee/" + obj.params.cpCode + "/" + obj.params.name;
		return this.ajax(obj);
	},
};
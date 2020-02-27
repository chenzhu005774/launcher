var playCode = getRequest()["playCode"],
    contentCode = getRequest()["contentCode"],
    backUrl = getRequest()["backUrl"],
    bookmark = getRequest()["bookmark"],
    isSelfPage = getRequest()["isSelfPage"],
    RETURNCODE = getRequest()["RETURNCODE"],
    CODE = getRequest()["CODE"],
    BOOKMARK = getRequest()["BOOKMARK"],
    timer;

function init() {
    if (!!isSelfPage) {
        timer = setTimeout(function () {
            forwardVedio(0);
        }, 500)
        dataGetter.playHistoryQuery({
            params: {
                cpCode: '004',
                contentCode: contentCode,
                itvAccount: Authentication.CTCGetConfig("UserID"),
                limit: 10,
                start: 1
            },
            success: function (data) {
                var bookmark = data.content.length > 0 && data.content[0].breakPointTime || 0;
                forwardVedio(bookmark);
            },
            fail: function () {
                forwardVedio(0);
            }
        });
    } else {
        if (RETURNCODE !== -1) {
            timer = setTimeout(function () {
                clearTimeout(timer);
                window.location.href = backUrl;
            }, 500)
            dataGetter.playBookMarksSave({
                params: {
                    breakPointTime: BOOKMARK,
                    itvAccount: Authentication.CTCGetConfig("UserID"),
                    playCode: CODE,
                },
                success: function (data) {
                    if (timer) clearTimeout(timer);
                    window.location.href = backUrl;
                },
                fail: function () {
                    if (timer) clearTimeout(timer);
                    window.location.href = backUrl;
                }
            });
        }
    }
}

function forwardVedio(bookmark) {
    var IPURL = getDomain();
    var url = IPURL + '/EPG/jsp/jiaoyu/en/play/play_auth.jsp'; // 华为
    if (timer) clearTimeout(timer);
    if (Authentication.CTCGetConfig("EPGDomain").indexOf('/iptvepg/') > -1) url = IPURL + '/iptvepg/frame79/play/play_auth.jsp'; // 中兴
    window.location.href = url +
        "?CODE=" + playCode +
        "&PARENTCODE=" + playCode +
        "&BOOKMARK=" + bookmark +
        "&USERID=" + Authentication.CTCGetConfig("UserID") +
        "&USERTOKEN=" + Authentication.CTCGetConfig("UserToken") +
        "&PLAYTYPE=1" +
        "&ISPLAYNEXT=0" +
        "&SPID=QHFP" +
        "&ISAUTHORIZATION=0" +
        "&BACKURL=" + escape(document.location.href.split('?')[0] + "?backUrl=" + escape(backUrl));
}
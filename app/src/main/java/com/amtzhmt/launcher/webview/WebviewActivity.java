package com.amtzhmt.launcher.webview;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WebviewActivity extends MVPBaseActivity<WebviewContract.View, WebviewPresenter> implements WebviewContract.View {

    WebView webview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webview = (WebView)findViewById(R.id.webview);
        mPresenter.initwebview(webview);
    }

    @Override
    public void loadurl(WebView webview) {
        webview.loadUrl("http://192.168.2.4:5500/list.html?code=01564457320587756&title=%u65B0%u95FB%u52A8%u6001");
    }
}

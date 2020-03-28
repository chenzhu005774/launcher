package com.amtzhmt.launcher.webview;

import android.content.Context;
import android.webkit.WebView;

import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WebviewContract {
    interface View extends BaseView {
    }

    interface  Presenter extends BasePresenter<View> {
       void initwebview(WebView webView);

        void destoryWebView(WebView webView);
    }
}

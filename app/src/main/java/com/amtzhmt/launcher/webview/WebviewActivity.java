package com.amtzhmt.launcher.webview;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.vodplay.VodplayActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WebviewActivity extends MVPBaseActivity<WebviewContract.View, WebviewPresenter> implements WebviewContract.View {
   String jumpurl;
    private FrameLayout contentContainer; // 容器
    private List<String> urlList = new ArrayList<>(); // 记录访问的URL

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        contentContainer = (FrameLayout) findViewById(R.id.framelayout_main);
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        jumpurl=intent.getStringExtra("jumpurl");
        LogUtils.i("-->-->-->:"+intent.getStringExtra("jumpurl")+"--|||||||--"+jumpurl);
        addWeb(jumpurl);

//        WebView webView =   (WebView) findViewById(R.id.webview);
//        //添加js接口，指明供js调用的对象和名称
//        webView.addJavascriptInterface(this, "MediaPlayer");
//
//
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
//        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
//        settings.setSupportZoom(true);//是否可以缩放，默认true
//        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
//        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
//        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
//        settings.setAppCacheEnabled(true);//是否使用缓存
//        settings.setDomStorageEnabled(true);//DOM Storage
//        settings.setSupportMultipleWindows(true);
//
//        webView.setBackgroundColor(0);
//        webView.getSettings().setSupportMultipleWindows(true);
//        //启用js支持
//        webView.getSettings().setJavaScriptEnabled(true);
//        //添加js接口，指明供js调用的对象和名称
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//
//        webView.loadUrl("file:///android_asset/test.html");

    }



    /**
     * 添加webview
     *
     * @param url
     */
    private void addWeb(String url) {  // 重点在这里，每次都新的URL都会创建一个WebView实例，添加到容器中
        final WebView mWeb = new WebView(this);
        mWeb.setVisibility(View.INVISIBLE);
//        mWeb.setLayerType(View.LAYER_TYPE_HARDWARE,null);//开启硬件加速
        mWeb.setBackgroundColor(Color.parseColor("#23345B"));
        WebSettings webSettings = mWeb.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWeb.setWebChromeClient(new WebChromeClient());
        mWeb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWeb.setVisibility(View.VISIBLE);
                mWeb.requestFocus();
                mWeb.setFocusable(true);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!urlList.contains(url)) {
                    addWeb(url);
                    urlList.add(url);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

        });
        //添加js接口，指明供js调用的对象和名称
        mWeb.addJavascriptInterface(this, "MediaPlayer");
        mWeb.loadUrl(url);
        contentContainer.addView(mWeb);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mWeb.setLayoutParams(params);


    }
    // 返回处理时移除容器的最顶的视图（即当前页面视图）
    @Override
    public void onBackPressed() { // 点击返回按钮事件
        int childCount = contentContainer.getChildCount();
        if (childCount > 1) {
            contentContainer.removeViewAt(childCount - 1);
            urlList.remove(urlList.size() - 1);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    @JavascriptInterface
    public void playVideo(String  url) {
        LogUtils.toast(this,"form js html playvideo:"+url);
        Intent intent = new Intent();
        intent.setClass(this,VodplayActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

}

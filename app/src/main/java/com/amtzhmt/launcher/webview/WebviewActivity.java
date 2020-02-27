package com.amtzhmt.launcher.webview;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
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
    }



    /**
     * 添加webview
     *
     * @param url
     */
    private void addWeb(String url) {  // 重点在这里，每次都新的URL都会创建一个WebView实例，添加到容器中
        WebView mWeb = new WebView(this);
        mWeb.setBackgroundColor(Color.parseColor("#262d47"));
        mWeb.setBackgroundResource(R.mipmap.bg);
        WebSettings webSettings = mWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWeb.setWebChromeClient(new WebChromeClient());
        mWeb.setWebViewClient(new WebViewClient(){
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
        LogUtils.toast(this,"form js html playvideo");
        startActivity(new Intent(this, VodplayActivity.class));
    }

}

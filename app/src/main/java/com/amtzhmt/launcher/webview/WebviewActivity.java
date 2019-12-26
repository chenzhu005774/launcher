package com.amtzhmt.launcher.webview;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.vodplay.VodplayActivity;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WebviewActivity extends MVPBaseActivity<WebviewContract.View, WebviewPresenter> implements WebviewContract.View {
   String jumpurl;
    WebView webview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        jumpurl=intent.getStringExtra("jumpurl");
        LogUtils.i("-->-->-->:"+intent.getStringExtra("jumpurl")+"--|||||||--"+jumpurl);
        webview = (WebView)findViewById(R.id.webview);
        mPresenter.initwebview(webview);
       //添加js接口，指明供js调用的对象和名称
        webview.addJavascriptInterface(this, "MediaPlayer");
    }

    @Override
    public void loadurl(WebView webview) {
//        webview.loadUrl("http://192.168.2.4:5500/list.html?code=01564457320587756&title=%u65B0%u95FB%u52A8%u6001");
//        webview.loadUrl("file:///android_asset/test.html");
          webview.loadUrl(jumpurl);
          LogUtils.toast(WebviewActivity.this, jumpurl);

    }


    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    @JavascriptInterface
    public void playVideo(String  url) {
        LogUtils.toast(this,"form js html playvideo");
        startActivity(new Intent(this, VodplayActivity.class));
    }
    @JavascriptInterface
    public void hello(String msg) {
          Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
      }

}

package com.amtzhmt.launcher.advert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.channelplay.ChannelplayActivity;
import com.amtzhmt.launcher.home.HomeActivity;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.CleanMessageUtil;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.UpdateManager;
import com.amtzhmt.launcher.util.utils.annima.RotationAnimation;
import com.amtzhmt.launcher.util.utils.customizeview.MyVideoView;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewToolBean;
import com.amtzhmt.launcher.vodplay.VodplayActivity;
import com.amtzhmt.launcher.webview.WebviewActivity;
import com.youth.banner.Banner;

import static com.amtzhmt.launcher.util.utils.CheckNet.getMacDefault;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class AdvertActivity extends MVPBaseActivity<AdvertContract.View, AdvertPresenter> implements AdvertContract.View {

    String result;
    Banner advertBanner;
    MyVideoView videoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        advertBanner = (Banner)findViewById(R.id.advertBanner);
        videoView = (MyVideoView)findViewById(R.id.videoView);
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        String data=intent.getStringExtra("data");
        result = data;
        //获取广告信息
        mPresenter.getAdvetrInfo();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void getAdvertSuccess(AdvertPresenter.AdvertInfo data) {
        LogUtils.toast(this,data.toString()+(data.displayStyle==0));
        LogUtils.i("chenzhu--->这里这里:"+data.toString());
        if (data.displayStyle==0){
           mPresenter.showAdvert(advertBanner,0,data);
        }else {

           mPresenter.showAdvert(videoView,1,data);
        }
    }

    @Override
    public void getAdvertFail(String data) {
        LogUtils.toast(this,data);
        this.showAdvertOver();
    }

    @Override
    public void showAdvertOver() {
        Intent intent = new Intent();
        if (null!=result) {
            intent.putExtra("data", result);
        }
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


}

package com.amtzhmt.launcher.home;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.catalog.CatalogActivity;
import com.amtzhmt.launcher.channelplay.ChannelplayActivity;
import com.amtzhmt.launcher.util.utils.CleanMessageUtil;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.UpdateManager;
import com.amtzhmt.launcher.util.utils.annima.RotationAnimation;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewToolBean;
import com.amtzhmt.launcher.vodplay.VodplayActivity;
import com.amtzhmt.launcher.webview.WebviewActivity;

import static com.amtzhmt.launcher.util.utils.CheckNet.getMacDefault;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class HomeActivity extends MVPBaseActivity<HomeContract.View, HomePresenter> implements HomeContract.View, View.OnClickListener,View.OnFocusChangeListener ,DialogCallback{
    int PLAYSTATUS = Constant.UNINIT;
    RotationAnimation rotationAnimation =  new RotationAnimation();
    String result;
    RelativeLayout  parentlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        parentlayout  =  (RelativeLayout)findViewById(R.id.root);
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        String data=intent.getStringExtra("data");
        result = data;
        mPresenter.initData(result,this,this,parentlayout);
        mPresenter.startClien(this); //开启心跳 连接
        //升级
        mPresenter.getUpdateApkinfo(LogUtils.getVersionCode(this));
    }

    @Override
    public void inintSuccess() {
        int versioncode =LogUtils.getVersionCode(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        LogUtils.showDialog(this,"this APK version:"+versioncode+" 基带版本："+android.os.Build.VERSION.INCREMENTAL);
        PLAYSTATUS = Constant.PLAY;
    }

    @Override
    public void initFail() {
    LogUtils.toast(this,"布局失败...:"+getMacDefault(this));
    LogUtils.showDialog(this,"布局失败,点击重试",this);
    LogUtils.i("build.prop info :"+android.os.Build.VERSION.RELEASE+LogUtils. getSystemProperty("build.prop")+"\n"+
        //VERSION.RELEASE 固件版本
        ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE+"\n"+
        ", VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME+"\n"+
         //VERSION.INCREMENTAL 基带版本
        ", VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL+"\n"
        )   ;
    }

    @Override
    public void needUpdateApk(String url) {
        new UpdateManager(HomeActivity.this,1).showDialog(Constant.UPDATEHTTP+url,"1.更新桌面应用 \n2.修复若干bug ");
    }

    @Override
    public void unneedUpdateApk(String message) {
       LogUtils.toast(this,message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PLAYSTATUS!=Constant.UNINIT){
            mPresenter.start();
        }

    }

    @Override
    public void onClick(View view) {
        Object object = view.getTag();
        if (object instanceof VideoViewToolBean) {
            mPresenter.pause();
            PLAYSTATUS = Constant.PAUSE;
            Intent intent = new Intent();
            intent.setClass(this, ChannelplayActivity.class);
            intent.putExtra("url",((VideoViewToolBean) object).getUrl());
            startActivity(intent);
            return;
        }else if (object instanceof ImageViewToolBean){

            mPresenter.pause();
            PLAYSTATUS = Constant.PAUSE;

            if (((ImageViewToolBean) object).getContentType()==4){
                Intent intent = new Intent();
                intent.setClass(this,VodplayActivity.class);
                intent.putExtra("url",((ImageViewToolBean) object).getJumpurl());
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("jumpurl", ((ImageViewToolBean) object).getJumpurl());
                LogUtils.i("-->-->-->:" + ((ImageViewToolBean) object).getJumpurl());
                startActivity(intent);
            }
        }
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("00119389676");
//        arrayList.add("00624970452");
//        new CustomerInfoDB(this). DeletData(arrayList);
    }
    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            new UpdateManager(HomeActivity.this,1).showDialog("http://192.168.2.183:8081/file/downFile/c30a5444087e41459ba3eca1d901a2f0","1.本次apk升级 \n 2.修复若干bug ");
            return true;
        }else
        if(keyCode ==KeyEvent.KEYCODE_0){
            //首页按键 开始系统升级 com.inpor.fmctv.activity.LoginActivity
            new UpdateManager(HomeActivity.this,2).showDialog("http://192.168.2.40:9000/systemimg.zip","1.本次系统升级 \n 2.修复若干bug ");
//            mPresenter.jumpApk("com.inpor.fmctv",this);
        }else if (keyCode ==KeyEvent.KEYCODE_1){
            CleanMessageUtil.clearAllCache(getApplicationContext());
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void clickSure() {
        mPresenter.initData(result,this,this,parentlayout);
    }


}

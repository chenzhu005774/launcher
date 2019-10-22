package com.amtzhmt.launcher.home;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.catalog.CatalogActivity;
import com.amtzhmt.launcher.channelplay.ChannelplayActivity;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.UpdateManager;
import com.amtzhmt.launcher.util.utils.annima.RotationAnimation;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewToolBean;

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
    }

    @Override
    public void inintSuccess() {
        LogUtils.toast(this,"布局完成");
        PLAYSTATUS = Constant.PLAY;
    }

    @Override
    public void initFail() {
    LogUtils.toast(this,"布局失败...:"+getMacDefault(this));
    LogUtils.showDialog(this,"布局失败,点击重试",this);
    startActivity(new Intent(this,ChannelplayActivity.class));
    LogUtils.i("build.prop info :"+android.os.Build.VERSION.RELEASE+LogUtils. getSystemProperty("build.prop")+"\n"+
        //VERSION.RELEASE 固件版本
        ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE+"\n"+
        ", VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME+"\n"+
         //VERSION.INCREMENTAL 基带版本
        ", VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL+"\n"
        )   ;
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
            Intent intent = new Intent();
            intent.setClass(this, CatalogActivity.class);
            startActivity(intent);
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
            new UpdateManager(HomeActivity.this,1).showDialog("http://192.168.2.40:9000/new.apk","apk升级 \n 2.修复若干bug ");
            return true;
        }else if(keyCode ==KeyEvent.KEYCODE_DPAD_LEFT){
            new UpdateManager(HomeActivity.this,2).showDialog("http://192.168.2.40:9000/systemimg.zip","系统升级 \n 2.修复若干bug ");
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void clickSure() {
        mPresenter.initData(result,this,this,parentlayout);
    }
}

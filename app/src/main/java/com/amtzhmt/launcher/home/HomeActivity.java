package com.amtzhmt.launcher.home;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.catalog.CatalogActivity;
import com.amtzhmt.launcher.channelplay.ChannelplayActivity;
import com.amtzhmt.launcher.push.clientService;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.annima.RotationAnimation;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;

import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewToolBean;

import java.util.Timer;
import java.util.TimerTask;

import static com.amtzhmt.launcher.util.utils.CheckNet.getMacDefault;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class HomeActivity extends MVPBaseActivity<HomeContract.View, HomePresenter> implements HomeContract.View, View.OnClickListener,View.OnFocusChangeListener {

    int PLAYSTATUS = Constant.UNINIT;
    RotationAnimation rotationAnimation =  new RotationAnimation();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RelativeLayout  parentlayout  =  (RelativeLayout)findViewById(R.id.root);
        Intent intent =getIntent();
        //getXxxExtra方法获取Intent传递过来的数据
        String data=intent.getStringExtra("data");
        String result = data;
        mPresenter.initData(result,this,this,parentlayout);
    }

    @Override
    public void inintSuccess() {
        LogUtils.toast(this,"布局完成");
        PLAYSTATUS = Constant.PLAY;
    }

    @Override
    public void initFail() {
    LogUtils.toast(this,"布局失败...:"+getMacDefault(this));

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("提示" ) ;
        builder.setMessage("获取页面数据失败:"+getMacDefault(this) ) ;
        builder.setPositiveButton("确认" ,  null );
        builder.show();


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
				System.out.println("chenzhu--->Service  onStart will in  applocation");
                Intent actIntent = new Intent(getApplicationContext(), clientService.class);
                actIntent.setAction("android.intent.action.MAIN");
                actIntent.addCategory("android.intent.category.LAUNCHER");
                actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(actIntent);
            }
        },1* 1000);

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (PLAYSTATUS!=Constant.UNINIT){
            LogUtils.toast(this,"Onresum");
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
//
//        new CustomerInfoDB(this). DeletData(arrayList);
    }
    @Override
    public void onFocusChange(View view, boolean b) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }else if(keyCode==17) {
            finish();
//            Intent intent = new Intent();
//            intent.setClass(HomeActivity.this, com.amtzhmt.launcher.main.MainActivity.class);
//            startActivity(intent);
//            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}

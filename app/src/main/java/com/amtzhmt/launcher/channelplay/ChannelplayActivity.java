package com.amtzhmt.launcher.channelplay;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;
import com.amtzhmt.launcher.util.utils.customizeview.MyVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 * implements
 */

public class ChannelplayActivity extends MVPBaseActivity<ChannelplayContract.View, ChannelplayPresenter> implements ChannelplayContract.View,
        MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener{
  MyVideoView channelplay;
  boolean isinitSuccess =false;
  int channelIndex =0;
  int totalNum =0;
  TextView channelNumtxt;
  List<ChannelEntity> channelEntities = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channelplay);
        channelplay = (MyVideoView)findViewById(R.id.channelplay);
        channelNumtxt= (TextView)findViewById(R.id.channelnumber);
        channelplay.setOnErrorListener(this);
        channelplay.setOnPreparedListener(this);
        mPresenter.getChannelNumber();
    }

    @Override
    public void getChannelSuccess(String result, List<ChannelEntity> list) {
        Intent intent = getIntent();
        String data=intent.getStringExtra("url");
        LogUtils.i("put here url"+data );
        totalNum = list.size();
        if (totalNum!=0) {
            for (int a = 0; a < list.size(); a++) {
                if (list.get(a).getUrl().equals(data)) {
                    channelIndex = a;
                }
            }
            channelEntities = list;
            channelplay.setVideoURI(Uri.parse(channelEntities.get(channelIndex).getUrl()));
        }else {
            channelplay.setVideoURI(Uri.parse(data));
        }
        channelNumtxt.setText(channelIndex+1+"");
        mPresenter.timeSend();
    }

    @Override
    public void controllerView() {
      //        控制播放的视图
        channelNumtxt.setVisibility(View.GONE);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        LogUtils.toast(this,"播放错误... ");
        LogUtils.i("-------:"+i +" "+i1);
        channelplay.stopPlayback();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        LogUtils.i("准备完毕...");
        channelplay.start();
        isinitSuccess=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        channelplay.stopPlayback();
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isinitSuccess){
            LogUtils.i("onkey:"+keyCode);
          //如果准备完毕那么上下键就是切台
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER||keyCode == KeyEvent.KEYCODE_ENTER) {
                return true;
            }else  if ( keyCode == KeyEvent.KEYCODE_DPAD_UP||keyCode==166){
                channelIndex+=1;
                if (channelIndex>totalNum-1){
                    channelIndex=0;
                }
                channelplay.setVideoURI(Uri.parse(channelEntities.get(channelIndex).getUrl()));
                channelNumtxt.setText(channelIndex+1+"");
                mPresenter.timeSend();
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN||keyCode==167){
                channelIndex-=1;
                if (channelIndex<0){
                    channelIndex=totalNum-1;
                }
                channelplay.setVideoURI(Uri.parse(channelEntities.get(channelIndex).getUrl()));
//                channelplay.setVideoURI(Uri.parse("http://192.168.2.40:9000/mov_bbb.mp4"));
                channelNumtxt.setText(channelIndex+1+"");
                LogUtils.i("isnull--?"+channelNumtxt);
                mPresenter.timeSend();

                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                return true;
            }else if(keyCode==184){
                startAPP("com.android.settings");
            }else {
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void startAPP(String appPackageName){
        try{
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(appPackageName);
            getContext().startActivity(intent);
        }catch(Exception e){
            Toast.makeText(getContext(), "没有安装", Toast.LENGTH_LONG).show();
        }
    }
}

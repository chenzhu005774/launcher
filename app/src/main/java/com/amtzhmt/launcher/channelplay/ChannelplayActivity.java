package com.amtzhmt.launcher.channelplay;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;
import com.amtzhmt.launcher.util.utils.customizeview.MyVideoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 * implements
 *
 */

public class ChannelplayActivity extends MVPBaseActivity<ChannelplayContract.View, ChannelplayPresenter> implements ChannelplayContract.View,
        MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener,DialogCallback {
  MyVideoView channelplay;
  boolean isinitSuccess =false;
  int channelIndex =0;
  int totalNum =0;
  TextView channelNumtxt;
  List<ChannelEntity> channelEntities = new ArrayList<>();

    private Handler VideoHander = new Handler();
    private Runnable run =  new Runnable() {

        public void run() {
            LogUtils.i("buffer size:"+channelplay.getBufferPercentage());
            VideoHander.postDelayed(run, 1000);
        }
    };

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
    public void getChannelSuccess(String result, final List<ChannelEntity> list) {

       LogUtils.i("排序前：" + list);
        Collections.sort(list, new Comparator<ChannelEntity>() {

            @Override
            public int compare(ChannelEntity o1, ChannelEntity o2) {
                // 按照学生的年龄进行升序排列
                if (Integer.valueOf(o1.getNum()) > Integer.valueOf(o2.getNum())) {
                    return 1;
                }
                if (Integer.valueOf(o1.getNum()) == Integer.valueOf(o2.getNum())) {
                    return 0;
                }
                return -1;
            }
        });

        LogUtils.i("升序排序后：" + list);
        // 延迟1s避免出现小窗口黑屏
        channelplay.postDelayed(new Runnable() {
            @Override
            public void run() {
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
                mPresenter.timeSend(channelNumtxt);
            }
        },1000);

    }

    @Override
    public void controllerView() {
      //        控制播放的视图
        channelNumtxt.setVisibility(View.GONE);
    }

    @Override
    public void showchoiceChanel(int number) {
        channelNumtxt.setText(number+"");
        mPresenter.timeSend(channelNumtxt);
    }

    @Override
    public void setchoiceChanel(int number) {
        if (channelEntities.size()==0||number>channelEntities.size()){
            return;
        }else {
            channelplay.setVideoURI(Uri.parse(channelEntities.get(number-1).getUrl()));
            channelIndex=(number-1);
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        LogUtils.toast(this,"播放错误... ");
        LogUtils.i("-------:"+i +" "+i1);
        LogUtils.showDialog(this,"播放失败,请检查网络以及视频源",this);
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
        if (mPresenter!=null) {
            mPresenter.detachView();
        }
        LogUtils.d("ChannelplayActivity--onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (channelEntities.size()==0){
            return true;
        }
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
                mPresenter.timeSend(channelNumtxt);
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN||keyCode==167){
                channelIndex-=1;
                if (channelIndex<0){
                    channelIndex=totalNum-1;
                }
                channelplay.setVideoURI(Uri.parse(channelEntities.get(channelIndex).getUrl()));
                channelNumtxt.setText(channelIndex+1+"");
                LogUtils.i("isnull--?"+channelNumtxt);
                mPresenter.timeSend(channelNumtxt);
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                return true;
            }else if ( KeyEvent.KEYCODE_0==keyCode){
                mPresenter.choiceChanel("0",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_1==keyCode){
                mPresenter.choiceChanel("1",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_2==keyCode){
                mPresenter.choiceChanel("2",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_3==keyCode){
                mPresenter.choiceChanel("3",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_4==keyCode){
                mPresenter.choiceChanel("4",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_5==keyCode){
                mPresenter.choiceChanel("5",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_6==keyCode){
                mPresenter.choiceChanel("6",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_7==keyCode){
                mPresenter.choiceChanel("7",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_8==keyCode){
                mPresenter.choiceChanel("8",channelNumtxt);
            }else if ( KeyEvent.KEYCODE_9==keyCode){
                mPresenter.choiceChanel("9",channelNumtxt);
            }
            else {
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void clickSure() {

    }
}

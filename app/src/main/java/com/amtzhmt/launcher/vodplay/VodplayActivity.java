package com.amtzhmt.launcher.vodplay;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.customizeview.MyVideoView;

import java.text.NumberFormat;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class VodplayActivity extends MVPBaseActivity<VodplayContract.View, VodplayPresenter> implements VodplayContract.View,
        MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener,OnFocusChangeListener,View.OnClickListener {
    MyVideoView channelplay;
    boolean isinitSuccess =false;
    ImageView playpause;
    ImageView previous;
    ImageView next;
    RelativeLayout playcontrollView;
    TextView title;
    TextView time;
    ProgressBar progressbar;
    int PLAYSATTUS = Constant.UNINIT;
    int cur=0;
    int total=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodplay);
        channelplay = (MyVideoView)findViewById(R.id.channelplay);
        playcontrollView= (RelativeLayout) findViewById(R.id.playcontrollView);
        progressbar = (ProgressBar)findViewById(R.id.progressbar);
        playpause =(ImageView)findViewById(R.id.playpause);
        time = (TextView)findViewById(R.id.time);
        playpause.setOnFocusChangeListener(this);
        playpause.setOnClickListener(this);
        previous = (ImageView)findViewById(R.id.previous);
        previous.setOnFocusChangeListener(this);
        previous.setOnClickListener(this);
        next = (ImageView)findViewById(R.id.next);
        next.setOnFocusChangeListener(this);
        previous.setOnClickListener(this);
        title = (TextView)findViewById(R.id.title);
        channelplay.setOnErrorListener(this);
        channelplay.setOnPreparedListener(this);

        mPresenter.getvodData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        channelplay.setFocusable(false);
        playpause.requestFocus();
    }

    @Override
    public void getvoddataSuccess() {
        channelplay.setVideoURI(Uri.parse("http://192.168.2.40:9000/kzm.ts"));
    }

    @Override
    public void controllerView() {
          playcontrollView.setVisibility(View.GONE);
    }

    @Override
    public void updateView() {
         cur= channelplay.getCurrentPosition();
         total= channelplay.getDuration();
        time.setText(mPresenter.changeTimeFormat(cur)+"/"+mPresenter.changeTimeFormat(total));
       // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float)cur/(float)total*100);
        progressbar.setProgress(Integer.valueOf(result));
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        LogUtils.toast(this,"播放错误... ");
        LogUtils.i("-------:"+i +" "+i1);
        channelplay.stopPlayback();
        PLAYSATTUS=Constant.UNINIT;
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        LogUtils.i("准备完毕...");
        channelplay.start();
        mediaPlayer.setLooping(true);//设置循环播放
        isinitSuccess=true;
        PLAYSATTUS=Constant.PLAY;
        mPresenter.timeSend();
        mPresenter.timeUpdateview();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        channelplay.stopPlayback();
        mPresenter.timeremoveall();
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isinitSuccess){
             // 如果不是暂停状态才去设置3秒过后消失
            if (PLAYSATTUS!=Constant.PAUSE) {
                mPresenter.timeSend();
            }
            // 如果播控条隐藏了 那么给他显示出来的时候要手动给他吧焦点设置。否则不能进行焦点移动，焦点被videoview抢占了
            if (playcontrollView.getVisibility()==View.GONE){
                playcontrollView.setVisibility(View.VISIBLE);
                playpause.requestFocus();
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER||keyCode == KeyEvent.KEYCODE_ENTER) {
                return true;
            }else  if ( keyCode == KeyEvent.KEYCODE_DPAD_UP){
                return true;
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                return true;
            }
//            else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
//                return true;
//            }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
//                return true;
//            }
            else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.previous:
                if (b){
                    previous.setBackgroundResource(R.drawable.border_color);
                }else {
                    previous.setBackgroundResource(0);
                }
                break;
            case R.id.playpause:
                if (b){
                    playpause.setBackgroundResource(R.drawable.border_color);
                }else {
                    playpause.setBackgroundResource(0);
                }
                break;
            case R.id.next:
                if (b){
                    next.setBackgroundResource(R.drawable.border_color);
                }else {
                    next.setBackgroundResource(0);
                }
                break;
            case R.id.channelplay:
                if (b){
                    channelplay.setBackgroundResource(R.drawable.border_color);
                }else {
                    channelplay.setBackgroundResource(0);
                }
                break;
        }

    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.previous:
               break;
           case R.id.playpause:
               if (Constant.PLAY==PLAYSATTUS){
                   channelplay.pause();
                   playpause.setImageResource(R.mipmap.play);
                   PLAYSATTUS=Constant.PAUSE;
                   mPresenter.timeremove();
               }else {
                   channelplay.start();
                   playpause.setImageResource(R.mipmap.pause);
                   PLAYSATTUS=Constant.PLAY;
                   mPresenter.timeSend();
               }
               break;
           case R.id.next:
               break;
       }
    }
}

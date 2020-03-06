package com.amtzhmt.launcher.vodplay;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.customizeview.MySeekBar;
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
    MySeekBar progressbar;
    int PLAYSATTUS = Constant.UNINIT;
    int cur=0;
    int total=0;
    boolean isseekbarfocus =false;
    Rect oldRect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vodplay);
        channelplay = (MyVideoView)findViewById(R.id.channelplay);
        playcontrollView= (RelativeLayout) findViewById(R.id.playcontrollView);
        progressbar = (MySeekBar) findViewById(R.id.progressbar);
        progressbar.setOnFocusChangeListener(this);
        playpause =(ImageView)findViewById(R.id.playpause);
        time = (TextView)findViewById(R.id.time);
        playpause.setOnFocusChangeListener(this);
        playpause.setOnClickListener(this);
        previous = (ImageView)findViewById(R.id.previous);
        previous.setOnFocusChangeListener(this);
        previous.setOnClickListener(this);
        next = (ImageView)findViewById(R.id.next);
        next.setOnFocusChangeListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        previous.setOnClickListener(this);
        title = (TextView)findViewById(R.id.title);
        channelplay.setOnErrorListener(this);
        channelplay.setOnPreparedListener(this);
        oldRect = progressbar.getThumb().getBounds();//获取原来的Bounds构建一个Rect
        Drawable drawable = getResources().getDrawable(R.mipmap.thumbnf);//新的图片转成drawable对象
        drawable.setBounds(oldRect);//为新的图片对象添加Bounds
        progressbar.setThumb( drawable );
        //        获取播放数据
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
        String url = Constant.VIDEOEHTTP+getIntent().getStringExtra("url");
        channelplay.setVideoURI(Uri.parse(url));
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
        try {
            String result = numberFormat.format((float)cur/(float)total*100);
            progressbar.setProgress(Integer.valueOf(result));
        }catch (Exception e){
            LogUtils.d("设置时间出错："+channelplay.getDuration());
        }

    }

    @Override
    public void playvideo() {
        channelplay.start();
        playpause.setImageResource(R.mipmap.pause);
        PLAYSATTUS=Constant.PLAY;
        mPresenter.timeSend();
    }

    @Override
    public void pausevideo() {
        channelplay.pause();
        playpause.setImageResource(R.mipmap.play);
        PLAYSATTUS=Constant.PAUSE;
        mPresenter.playtimeremove();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        LogUtils.toast(this,"播放错误... ");
        LogUtils.i("播放错误:"+i +" "+i1);
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
        mPresenter.playtimeUpdateview();
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
        LogUtils.i("---->"+keyCode);
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
//            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER||keyCode == KeyEvent.KEYCODE_ENTER) {
//                return true;
//            }else  if ( keyCode == KeyEvent.KEYCODE_DPAD_UP){
//                return true;
//            }else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
//                return true;
//            }

             if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT&&isseekbarfocus){
                 cur = total*(progressbar.getProgress()-5)/100;
                 if (cur<=0){
                     cur=0;
                 }
                 channelplay.seekTo(cur);
                 if(PLAYSATTUS==Constant.PAUSE){
                     mPresenter.playvideo();
                 }
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT&&isseekbarfocus){
                 cur = total*(progressbar.getProgress()+5)/100;
                 if (cur>=total){
                     cur=total;
                 }
                 channelplay.seekTo(cur);
                 if(PLAYSATTUS==Constant.PAUSE){
                     mPresenter.playvideo();
                 }
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
            case R.id.progressbar:
                isseekbarfocus=b;
                if (b){
                    Drawable drawable = getResources().getDrawable(R.mipmap.thumbf);//新的图片转成drawable对象
                    drawable.setBounds(oldRect);//为新的图片对象添加Bounds
                    progressbar.setThumb( drawable );
                }else {
                    Drawable drawable = getResources().getDrawable(R.mipmap.thumbnf);//新的图片转成drawable对象
                    drawable.setBounds(oldRect);//为新的图片对象添加Bounds
                    progressbar.setThumb( drawable );
                }
                break;
        }

    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.previous:
               channelplay.setVideoURI(Uri.parse("http://www.tastyfit.vip:8080/test1.ts"));
               break;
           case R.id.playpause:
               if (Constant.PLAY==PLAYSATTUS){
                  mPresenter.pausevideo();
               }else {
                  mPresenter.playvideo();
               }
               break;
           case R.id.next:
               channelplay.setVideoURI(Uri.parse("http://124.116.129.62:8091/static_resource/hyresource/0003/video/8481565679653437.ts"));
               break;
       }
    }


}

package com.amtzhmt.launcher.vodplay;

import android.os.Handler;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;

import java.util.Formatter;
import java.util.Locale;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class VodplayPresenter extends BasePresenterImpl<VodplayContract.View> implements VodplayContract.Presenter{
    //将长度转换为时间
    StringBuilder mFormatBuilder = new StringBuilder();
    Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mView!=null) {
                mView.controllerView();
            }
        }
    };

    Handler playtimehandler = new Handler();
    Runnable playtimerunnable = new Runnable() {
        @Override
        public void run() {
            if (mView!=null) {
                mView.updateView();
                playtimehandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void getvodData() {
        mView.getvoddataSuccess();
    }

    @Override
    public void timeSend() {
        // 5秒过后隐藏 没有按键的话
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,5000);
    }

    @Override
    public void playtimeUpdateview() {
        playtimehandler.postDelayed(playtimerunnable,1);
    }

    @Override
    public void playtimeremove() {
        handler.removeCallbacks(runnable);
    }

    @Override
    public void timeremoveall() {
        handler.removeCallbacks(runnable);
        playtimehandler.removeCallbacks(playtimerunnable);
    }


    @Override
    public String changeTimeFormat(int time) {
        int totalSeconds = time / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public void playvideo() {
        mView.playvideo();
        playtimehandler.postDelayed(playtimerunnable,1);
    }

    @Override
    public void pausevideo() {
       mView.pausevideo();
        playtimehandler.removeCallbacks(playtimerunnable);
    }
}

package com.amtzhmt.launcher.vodplay;

import android.content.Context;

import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;

import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class VodplayContract {
    interface View extends BaseView {
        void getvoddataSuccess();
        void controllerView();
        void updateView();
    }

    interface  Presenter extends BasePresenter<View> {
        void  getvodData();
        //定时显示关闭的控件
        void   timeSend();
        void  timeUpdateview();
        void  timeremove();
        void timeremoveall();
        String changeTimeFormat(int time);
    }
}

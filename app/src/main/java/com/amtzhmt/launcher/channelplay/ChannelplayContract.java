package com.amtzhmt.launcher.channelplay;

import android.view.View;

import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;

import java.util.List;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class ChannelplayContract {
    interface View extends BaseView {
        void getChannelSuccess(String result, List<ChannelEntity> list);
        void controllerView();
    }

    interface  Presenter extends BasePresenter<View> {
      void  getChannelNumber();
      //定时显示关闭的控件
      void   timeSend( );
    }
}

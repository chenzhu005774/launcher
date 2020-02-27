package com.amtzhmt.launcher.channelplay;

import android.view.View;
import android.widget.TextView;

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
        void showchoiceChanel(int number);
        void setchoiceChanel(int number);
    }

    interface  Presenter extends BasePresenter<View> {
//    获取所有的直播频道
      void  getChannelNumber();
      //定时显示关闭的控件
      void   timeSend( android.view.View view);

      void  choiceChanel(String number, TextView view );
    }
}

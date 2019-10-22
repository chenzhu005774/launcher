package com.amtzhmt.launcher.home;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class HomeContract {
    interface View extends BaseView {
       void  inintSuccess();
       void initFail();
    }

    interface  Presenter extends BasePresenter<View> {
       void initData(String data, android.view.View.OnFocusChangeListener focusChangeListener, android.view.View.OnClickListener clickListener
       , RelativeLayout parentlayout);

        void  pause();
        void start();
        void startClien(Context context);

    }
}

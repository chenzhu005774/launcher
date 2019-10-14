package com.amtzhmt.launcher.main;


import android.app.Activity;
import android.view.View;
import android.widget.SeekBar;

import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class MainContract {
    interface View extends BaseView {
       void loginSuccess(String result);
       void loginFail(String result);
       void getTokenSuccess(String result);
       void getTokenFail(String result);
       void getpageSuccess(String result);
       void getpageFail(String result);
       void gotoNextActivity(String data,Class<? extends Activity> cls);
    }

    interface  Presenter extends BasePresenter<View> {
        void initScreen (android.view.View view,SeekBar seek);
        void leaveScreen (android.view.View view);
        void gotoNext();
    }
}

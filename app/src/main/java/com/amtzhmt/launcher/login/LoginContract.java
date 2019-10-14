package com.amtzhmt.launcher.login;



import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class LoginContract {
    interface View extends BaseView {
        void loginsuccess(String data);
        void loginfail();
        void binMacSuccess(String iptvaccoun);
        void binMacfail();

        void getdatasuccess(String data);
        void getdatafail();

    }

    interface  Presenter extends BasePresenter<View> {
       void login(String name,String pwd ,String mac);
       void bindMac();
    }
}

package com.amtzhmt.launcher.advert;
import com.amtzhmt.launcher.mvp.BasePresenter;
import com.amtzhmt.launcher.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class AdvertContract {
    interface View extends BaseView {
       void getAdvertSuccess(AdvertPresenter.AdvertInfo data);
       void getAdvertFail(String data);
       void showAdvertOver();


    }

    interface  Presenter extends BasePresenter<View> {

        void getAdvetrInfo();
        void showAdvert(android.view.View view, int type, AdvertPresenter.AdvertInfo advertInfo);
    }
}

package com.amtzhmt.launcher.main;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.amtzhmt.launcher.advert.AdvertActivity;
import com.amtzhmt.launcher.login.LoginActivity;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.annima.Rotate3dAnimation;
import com.amtzhmt.launcher.util.utils.bean.CustomerEntity;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.sqlite.CustomerInfoDB;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amtzhmt.launcher.util.utils.CheckNet.getNetMode;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class MainPresenter extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter,DialogCallback {
    SeekBar seek;
    int progress = 10;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (getNetMode(mView.getContext()) != 0) {
                handler.removeCallbacks(this);
                // handler.postDelayed(changePross, 1000);
                // 网络已经连接跳转 获取token 登录 如果上次已经登录的话那么在后台给他登录
                final CustomerInfoDB customerInfoDB = new CustomerInfoDB(mView.getContext());
                Api.getDefault().getLoginToken().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //注意这里用第一个Response参数的
                        try {
                            String result = response.body().string();
                            LogUtils.i("chenzhu--->token" + result);
                            JSONObject jSONObject = new JSONObject(result);
                            String token = jSONObject.getString("data");
                            Api.token = token;
                            mView.getTokenSuccess("get token success");

                            List<CustomerEntity> count = customerInfoDB.getAllObject();
                            if (count.size() != 0) {
                                //这里全部都去重新登录
                                customerInfoDB.deleteAll();
                            }
                            List<CustomerEntity> list = customerInfoDB.getAllObject();
                            if (list.size() == 0) {
                                mView.gotoNextActivity(null,LoginActivity.class);
                                handler.removeCallbacks(changePross);
                                return;
                            } else {
                                //登录
//                                CustomerEntity customerEntity = list.get(0);
//                                Map<String, String> hashMap = new HashMap();
//                                hashMap.put("iptvAccount", customerEntity.getName());
//                                hashMap.put("mac", customerEntity.getMac());
//                                hashMap.put("password", customerEntity.getPwd());
//                                Api.getDefault().login(hashMap).enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        String orgCode = "";
//                                        String customerCode = "";
//                                        try {
//                                            String result = response.body().string();
//                                            JSONObject jSONObject = new JSONObject(result);
//                                            orgCode = jSONObject.getJSONObject("data").getString("orgCode");
//                                            customerCode = jSONObject.getJSONObject("data").getString("orgCode");
//                                            mView.loginSuccess("login success");
//                                        } catch (Exception e) {
//                                            LogUtils.i("chenzhu--login fail->e"+e+" token" + Api.token);
//                                            mView.loginFail("login success getdata fail");
//                                            return;
//                                        }
//                                        Api.getDefault().getPage(orgCode, customerCode).enqueue(new Callback<ResponseBody>() {
//                                            @Override
//                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                try {
//                                                    String result = response.body().string();
//                                                    JSONObject jSONObject = new JSONObject(result);
//                                                    String data = jSONObject.getString("data");
//                                                    if (data==null||data.equals("")||data.equals("null")){
//                                                        mView.getpageFail("get page  fail");
//                                                    }else {
//                                                        mView.getpageSuccess("get page success");
//                                                        mView.gotoNextActivity(result,AdvertActivity.class);
//                                                    }
//                                                    return;
//                                                } catch (Exception e) {
//                                                    mView.getpageFail("get page  fail");
//                                                    e.printStackTrace();
//                                                    return;
//                                                }
//                                            }
//                                            @Override
//                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                                LogUtils.i(" --" + t);
//                                                mView.getpageFail("get page  fail");
//                                                return;
//                                            }
//                                        });
//                                    }
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                        mView.loginFail("login fail");
//                                        return;
//                                    }
//                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.getTokenFail("get token JSonException");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtils.i(" --" + t);
                        mView.getTokenFail("get token fail");
                        return;
                    }
                });

            } else {

                handler.postDelayed(changePross, 1000);
                LogUtils.showDialog(mView.getContext(),"当前网络连接异常是否跳转网络设置",MainPresenter.this);

            }
        }
    };

    // 10S 过后就直接去设置
    Runnable changePross = new Runnable() {
        @Override
        public void run() {
            progress += 10;
            if (progress > 90) {
                progress = 10;
//                一个是原生 一个是我们的apk
//                startAPP("com.android.smart.terminal.settings");
//                startAPP("com.android.settings");
                handler.removeCallbacks(changePross);
                handler.removeCallbacks(runnable);
                return;
            }
            handler.postDelayed(changePross, 1000);
            seek.setProgress(progress);
        }
    };



    @Override
    public void initScreen(View view ,SeekBar seek) {
        this.seek =seek;
        Rotate3dAnimation animation  = new Rotate3dAnimation(0,0,0,0,0,true);
        animation.setAnimateScale(view,2500);
    }

    @Override
    public void leaveScreen(View view) {
        handler.removeCallbacks(runnable);
        view.clearAnimation();
    }

    @Override
    public void gotoNext() {
        handler.postDelayed(runnable,1);
    }

    /**
     * @exception :启动apk
     * com.android.smart.terminal.settings/.LaunchActivity
     * @param ：String  包名
     **/
    public void startAPP(String appPackageName){
        try{
            Intent intent = mView.getContext().getPackageManager().getLaunchIntentForPackage(appPackageName);
            mView.getContext().startActivity(intent);
        }catch(Exception e){
            Toast.makeText(mView.getContext(), "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void clickSure() {
//        无网络提示按确认
        //                在这里提示一下 要去设置
        startAPP("com.android.smart.terminal.settings");

    }
}

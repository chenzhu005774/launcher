package com.amtzhmt.launcher.login;
import com.amtzhmt.launcher.util.utils.bean.CustomerEntity;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.sqlite.CustomerInfoDB;
import com.amtzhmt.launcher.util.utils.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amtzhmt.launcher.util.utils.CheckNet.getMacDefault;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void login(final String name, final String pwd,final String mac) {
        //登录
        Map<String, String> hashMap = new HashMap();
        hashMap.put("iptvAccount", name);
        hashMap.put("mac", mac);
        hashMap.put("password", pwd);
        Api.getDefault().login(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject jSONObject = new JSONObject(result);
                    String orgCode = jSONObject.getJSONObject("data").getString("orgCode");
                    String customerCode=jSONObject.getJSONObject("data").getString("customerCode");
                    //登录成功后 将他存入本地mysql
                    CustomerInfoDB customerInfoDB = new CustomerInfoDB(mView.getContext());
                    CustomerEntity customerEntity = new CustomerEntity();
                    customerEntity.setName(name);
                    customerEntity.setPwd(pwd);
                    customerEntity.setCode(customerCode);
                    customerEntity.setOrgcode(orgCode);
                    customerEntity.setMac(getMacDefault(mView.getContext()));
                    customerInfoDB.saveObject(customerEntity);
                    mView.loginsuccess(result);
                    Api.getDefault().getPage(orgCode, customerCode).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String result  = response.body().string();
                                JSONObject jSONObject = new JSONObject(result);
                                String data = jSONObject.getString("data");
                                if (data==null||data.equals("")||data.equals("null")){
                                    mView.getdatafail();
                                }else {
                                    mView.getdatasuccess(result);
                                }
                            } catch ( Exception e) {
                                e.printStackTrace();
                                mView.getdatafail();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mView.getdatafail();
                        }
                    });

                } catch ( Exception e) {
                    e.printStackTrace();
                    mView.loginfail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.loginfail();
            }
        });
    }

    @Override
    public void bindMac() {
        Api.getDefault().bindMacToAccount(getMacDefault(mView.getContext())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject jSONObject = new JSONObject(result);
                    String iptvaccount = jSONObject.getString("data");
                    mView.binMacSuccess(iptvaccount);
                } catch ( Exception e) {
                    e.printStackTrace();
                    mView.binMacfail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.loginfail();
            }

        });
    }
}

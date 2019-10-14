package com.amtzhmt.launcher;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.amtzhmt.launcher.home.HomeActivity;
import com.amtzhmt.launcher.login.LoginActivity;
import com.amtzhmt.launcher.util.utils.annima.Rotate3dAnimation;
import com.amtzhmt.launcher.util.utils.base.BaseActivity;
import com.amtzhmt.launcher.util.utils.bean.CustomerEntity;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.sqlite.CustomerInfoDB;
import com.amtzhmt.launcher.util.utils.LogUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.amtzhmt.launcher.util.utils.CheckNet.getNetMode;


public class MainActivity extends BaseActivity  {
//    ImageView icon;
//    SeekBar seek;
//    int progress = 10;
//    RelativeLayout iconview ;
//
//
//    Handler handler=new Handler();
//    Runnable runnable=new Runnable() {
//        @Override
//        public void run() {
//            if (getNetMode(MainActivity.this) != 0) {
//                handler.removeCallbacks(this);
//                handler.postDelayed(changePross, 1000);
//               // 网络已经连接跳转 获取token 登录 如果上次已经登录的话那么在后台给他登录
//                final CustomerInfoDB customerInfoDB = new CustomerInfoDB(MainActivity.this);
//                Api.getDefault().getLoginToken().enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        //注意这里用第一个Response参数的
//                        try {
//                            String result = response.body().string();
//                            LogUtils.i("chenzhu--->token" + result);
//                            JSONObject jSONObject = new JSONObject(result);
//                            String token = jSONObject.getString("data");
//                            Api.token = token;
//                            List<CustomerEntity> list = customerInfoDB.getAllObject();
//                            if (list.size() == 0) {
//                                skipAnotherActivity(MainActivity.this, LoginActivity.class);
//                                handler.removeCallbacks(changePross);
//                            } else {
//                                //登录
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
//                                            customerCode = jSONObject.getJSONObject("data").getString("customerCode");
//                                        } catch (Exception e) {
//                                            LogUtils.i("chenzhu--login fail->e"+e+" token" + Api.token);
//                                            Toast.makeText(MainActivity.this, "认证失败,请联系管理员", Toast.LENGTH_LONG).show();
//                                            return;
//                                        }
//                                        Api.getDefault().getPage(orgCode, customerCode).enqueue(new Callback<ResponseBody>() {
//                                            @Override
//                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                try {
//                                                    String result = response.body().string();
//                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                                    intent.putExtra("data", result);
//                                                    startActivity(intent);
//                                                    handler.removeCallbacks(changePross);
//                                                    return;
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                    Toast.makeText(MainActivity.this, "获取首页失败,请联系管理员", Toast.LENGTH_LONG).show();
//                                                    return;
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                                LogUtils.i(" --" + t);
//                                            }
//                                        });
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                    }
//
//                                });
//
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        LogUtils.i(" --" + t);
//                    }
//                });
//
//
//            } else {
//                handler.postDelayed(changePross, 1000);
//            }
//        }
//    };
//
//    Runnable changePross = new Runnable() {
//        @Override
//        public void run() {
//            progress += 10;
//            if (progress > 90) {
//                progress = 10;
//                startAPP("com.android.smart.terminal.settings");
//                handler.removeCallbacks(changePross);
//                handler.removeCallbacks(runnable);
//            }
//            handler.postDelayed(changePross, 1000);
//            seek.setProgress(progress);
//        }
//    };
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {
        skipAnotherActivity(this, com.amtzhmt.launcher.main.MainActivity.class);
        return;
//        icon = (ImageView)findViewById(R.id.icon);
//        seek = (SeekBar)findViewById(R.id.seek);
//        iconview= (RelativeLayout)findViewById(R.id.iconview);
//
//        Rotate3dAnimation animation  = new Rotate3dAnimation(0,0,0,0,0,true);
//        animation.setAnimateScale(icon,2500);
//        LogUtils.i("chenzhu--->token----------start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在这里检测网络 等待1.5s 发送消息检测网络状态
//        handler.postDelayed(runnable, 1 );
    }

    @Override
    public void initData() {


    }





}

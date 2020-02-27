package com.amtzhmt.launcher.channelplay;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.amtzhmt.launcher.mvp.BasePresenterImpl;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;
import com.amtzhmt.launcher.util.utils.bean.CustomerEntity;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.sqlite.CustomerInfoDB;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class ChannelplayPresenter extends BasePresenterImpl<ChannelplayContract.View> implements ChannelplayContract.Presenter{

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mView!=null) {
                mView.controllerView();
            }
        }
    };
//    频道号实时显示 ，频道延时显示
    String chanernumber="";
    Runnable chanelrun = new Runnable() {
        @Override
        public void run() {
            if (mView!=null) {
                mView.setchoiceChanel(Integer.valueOf(chanernumber));
                chanernumber="";
            }
        }
    };
    @Override
    public void getChannelNumber() {
        final CustomerInfoDB customerInfoDB = new CustomerInfoDB(mView.getContext());
        CustomerEntity customerEntity = customerInfoDB.getAllObject().get(0);
        Api.getDefault().getChannel(customerEntity.getCode(),1,100).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                JSONObject jSONObject = null;
                List<ChannelEntity> list = new ArrayList<ChannelEntity>();
                try {
                    result = response.body().string();
                    jSONObject = new JSONObject(result);
                    JSONArray data = jSONObject.getJSONObject("data").getJSONArray("pageRecords");
                    for (int a=0;a<data.length();a++){
                        ChannelEntity channelEntity = new ChannelEntity();
                        channelEntity.setName(data.getJSONObject(a).getString("name"));
                        channelEntity.setUrl(data.getJSONObject(a).getString("url"));
                        channelEntity.setNum(data.getJSONObject(a).getString("num"));
                        channelEntity.setLogo(data.getJSONObject(a).getString("logo"));
                        channelEntity.setRemark(data.getJSONObject(a).getString("remark"));
                        list.add(channelEntity);
                    }
                    mView.getChannelSuccess(result,list);
                } catch ( Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void timeSend(View view) {
        view.setVisibility(View.VISIBLE);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,3000);
    }



    @Override
    public void choiceChanel(String  number , TextView view) {
        if (chanernumber.length()==0&&Integer.valueOf(number)==0){
         // 开头就输入0 舍弃
            return;
        }
        if (chanernumber.length()>2){
            // 只能输入两位数
            return;
        }
        chanernumber+=number;
        mView.showchoiceChanel(Integer.valueOf(chanernumber));
        handler.removeCallbacks(chanelrun);
        handler.postDelayed(chanelrun,2000);
    }

}

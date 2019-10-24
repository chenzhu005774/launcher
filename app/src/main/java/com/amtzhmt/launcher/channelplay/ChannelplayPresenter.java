package com.amtzhmt.launcher.channelplay;
import android.os.Handler;
import android.view.View;

import com.amtzhmt.launcher.mvp.BasePresenterImpl;
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
    public void timeSend() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,3000);
    }

}

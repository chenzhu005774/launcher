package com.amtzhmt.launcher.advert;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.LogUtils;
import com.amtzhmt.launcher.util.utils.bean.CustomerEntity;
import com.amtzhmt.launcher.util.utils.customizeview.MyVideoView;
import com.amtzhmt.launcher.util.utils.image.MyLoader;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.sqlite.CustomerInfoDB;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

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
public class AdvertPresenter extends BasePresenterImpl<AdvertContract.View> implements AdvertContract.Presenter{
     MyVideoView videoView;
    @Override
    public void getAdvetrInfo() {
        final CustomerInfoDB customerInfoDB = new CustomerInfoDB(mView.getContext());
        List<CustomerEntity> list = customerInfoDB.getAllObject();
        if(list.size()==0){
            return;
        }
        CustomerEntity customerEntity =list.get(0);

        Api.getDefault().getAdvert( 1,20).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                JSONObject jSONObject = null;
                try {
                    result = response.body().string();
                    LogUtils.i("<<<<----->"+result);
                    jSONObject = new JSONObject(result);
                    JSONArray data = jSONObject.getJSONObject("data").getJSONArray("pageRecords");
                    if (data.length() == 0) {
                        mView.getAdvertFail("没有广告");
                        return;
                    }
                    jSONObject = data.getJSONObject(0);
                    AdvertInfo advertInfo = new AdvertInfo();
                    advertInfo.setDisplayStyle(jSONObject.getInt("displayStyle"));
                    try {
                        advertInfo.setLength(jSONObject.getInt("length"));
                    }catch (Exception e){
                        advertInfo.setLength(0);
                    }
                    try {
                        advertInfo.setDisplaySpace(jSONObject.getInt("displaySpace"));
                    }catch (Exception e){
                        advertInfo.setDisplaySpace(0);
                    }
                    try {
                        advertInfo.setDisplayMethod(jSONObject.getInt("displayMethod"));
                    }catch (Exception e){
                        advertInfo.setDisplayMethod(0);
                    }

                    if (advertInfo.getDisplayStyle()==0){
                        // 图片
                        if (  jSONObject.getJSONArray("url").length()==0){
                            mView.getAdvertFail("没有广告图片");
                        }else {
                            List<String> urls = new ArrayList<String>();
                            for (int a=0;a< jSONObject.getJSONArray("url").length();a++){
                                if (!jSONObject.getJSONArray("url").getJSONObject(a).isNull("diskPath")){
                                    urls.add(jSONObject.getJSONArray("url").getJSONObject(a).getString("diskPath"));
                                }
                            }
                            if (urls.size()==0){
                                mView.getAdvertFail("没有广告图片");
                            }
                            advertInfo.setPicUrls(urls);
                        }
                    }else {
                        //视频
                        if (jSONObject.getJSONArray("url").length()==0){
                            mView.getAdvertFail("没有广告视频");
                        }else {
                            if (!jSONObject.getJSONArray("url").getJSONObject(0).isNull("diskPath")) {
                                LogUtils.i("chenzhu--->这里这里 string:"+jSONObject.getJSONArray("url").getJSONObject(0).getString("diskPath"));
                                advertInfo.setVideoUrl(jSONObject.getJSONArray("url").getJSONObject(0).getString("diskPath"));
                            }else {
                                mView.getAdvertFail("没有广告视频");
                            }
                        }
                    }
                    mView.getAdvertSuccess(advertInfo);
                } catch ( Exception e) {
                    e.printStackTrace();
                    mView.getAdvertFail("获取广告异常1");
                    LogUtils.i("获取广告异常2:"+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.getAdvertFail("获取广告异常3");
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable =   new Runnable() {
        @Override
        public void run() {
            videoView.stopPlayback();
            mView.showAdvertOver();
        }
    };
    @Override
    public void showAdvert (final View view, int type, AdvertInfo advertInfo) {
        if (advertInfo.displayStyle==0){
            List<String> list_path = new ArrayList<>();
            List<String> list_title = new ArrayList<>();
            final Banner banner = (Banner)view;
            banner.setVisibility(View.VISIBLE);
            for (String url:advertInfo.getPicUrls()){
                list_path.add(Constant.pichttp+url);
                list_title.add("");
            }
             //给他算一下 图片总时长是多久
            advertInfo.length = advertInfo.getDisplaySpace()*list_path.size();
              //设置内置样式，共有六种可以点入方法内逐一体验使用。
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
              //设置图片加载器，图片加载器在下方
                banner.setImageLoader(new MyLoader());
             //设置图片网址或地址的集合
                banner.setImages(list_path);
                banner.setBannerTitles(list_title);
             //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                banner.setBannerAnimation(Transformer.Default);
              //设置轮播间隔时间 ms
                banner.setDelayTime(advertInfo.getDisplaySpace());
              //设置是否为自动轮播，默认是“是”。
                banner.isAutoPlay(true);
              //设置不能手动影响 默认是手指触摸 轮播图不能翻页
                 banner.setViewPagerIsScroll(false);
               //设置指示器的位置，小点点，左中右。
                banner.setIndicatorGravity(BannerConfig.NOT_INDICATOR)
               //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
               //.setOnBannerListener(this)
               //必须最后调用的方法，启动轮播图。
                .start();
              banner.updateBannerStyle(BannerConfig.NOT_INDICATOR);
             handler.postDelayed(
                       new Runnable() {
                           @Override
                           public void run() {
                            banner.stopAutoPlay();
                            mView.showAdvertOver();
                           }
                       },advertInfo.length );
        }else {
            videoView = (MyVideoView)view;
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(Constant.VIDEOEHTTP+advertInfo.getVideoUrl()));
            if (advertInfo.getDisplayMethod()==0){
                // 按照视频时长播放完成
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                      //  播放结束去首页
                        mp.stop();
                        mp.release();
                        mView.showAdvertOver();
                    }
                });
            }else {
                LogUtils.i("chenzhu----->这里这里");
                // 按照设置的时长播放完成，但是 如果已经视频不够长 还是就走了
                if (advertInfo.getVideoUrl()==null){
                    mView.showAdvertOver();
                    return;
                }
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //  播放结束去首页
                        mp.stop();
                        mp.release();
                        handler.removeCallbacks(runnable);
                        mView.showAdvertOver();
                    }
                });
                handler.postDelayed( runnable,advertInfo.length );
            }
        }
    }


    public class AdvertInfo{
        int displayStyle;     //   0图片 1视频
        int length;      //    展示时长
        int displayMethod ; // 0按视频/图片时长 1按展示时长
        int displaySpace ;     // 当以图片展示的时候 时长
        String videoUrl;//视频地址
        List<String> picUrls;//图片地址

        public int getDisplayStyle() {
            return displayStyle;
        }

        public void setDisplayStyle(int displayStyle) {
            this.displayStyle = displayStyle;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getDisplayMethod() {
            return displayMethod;
        }

        public void setDisplayMethod(int displayMethod) {
            this.displayMethod = displayMethod;
        }

        public int getDisplaySpace() {
            return displaySpace;
        }

        public void setDisplaySpace(int displaySpace) {
            this.displaySpace = displaySpace;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public List<String> getPicUrls() {
            return picUrls;
        }

        public void setPicUrls(List<String> picUrls) {
            this.picUrls = picUrls;
        }

        @Override
        public String toString() {
            return "displayStyle:"+displayStyle+"\n"+
                    "length:"+length+"\n"+
                    "displayMethod:"+displayMethod+"\n"+
                    "displaySpace:"+displaySpace+"\n"+
                    "videoUrl:"+videoUrl+"\n";
        }
    }

}

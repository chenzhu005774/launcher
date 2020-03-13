package com.amtzhmt.launcher.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.push.clientService;
import com.amtzhmt.launcher.util.utils.bean.ChannelEntity;
import com.amtzhmt.launcher.util.utils.bean.ListItemBean;
import com.amtzhmt.launcher.util.utils.commonbean.CommonBean;
import com.amtzhmt.launcher.util.utils.image.ViewbgLoader;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;
import com.amtzhmt.launcher.util.utils.net.Api;
import com.amtzhmt.launcher.util.utils.toolview.BannerViewTool;
import com.amtzhmt.launcher.util.utils.toolview.BannerViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewTool;
import com.amtzhmt.launcher.util.utils.toolview.ImageViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.ListViewTool;
import com.amtzhmt.launcher.util.utils.toolview.ListViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.MarqueeTextViewTool;
import com.amtzhmt.launcher.util.utils.toolview.MarqueeTextViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.TextClockViewTool;
import com.amtzhmt.launcher.util.utils.toolview.TextClockViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.TextViewTool;
import com.amtzhmt.launcher.util.utils.toolview.TextViewToolBean;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewTool;
import com.amtzhmt.launcher.util.utils.toolview.VideoViewToolBean;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class HomePresenter extends BasePresenterImpl<HomeContract.View> implements HomeContract.Presenter{

    RelativeLayout parentlayout;
    BannerViewTool bannerViewTool;

    CommonBean commonBean = new CommonBean();


    ImageViewTool imageViewTool = new ImageViewTool();//图片控件


    MarqueeTextViewTool marqueeTextViewTool = new MarqueeTextViewTool();//跑马灯控件


    TextViewTool textViewTool = new TextViewTool();  //文字控件


    TextClockViewTool textClockViewTool  = new TextClockViewTool ();//时间控件


    ListViewTool listViewTool = new ListViewTool();


    VideoViewToolBean videoViewToolBean = new VideoViewToolBean();
    VideoViewTool videoViewTool = new VideoViewTool();
    @Override
    public void initData(String data, View.OnFocusChangeListener onFocusChangeListener, View.OnClickListener onClickListener,RelativeLayout parentlayout) {
        this.parentlayout  =  parentlayout;
        // 公共属性
        commonBean .setContext(mView.getContext());
        commonBean.setFocusChangeListener(onFocusChangeListener);
        commonBean.setOnClickListener(onClickListener);
        commonBean.setLayout(parentlayout);

//        BannerViewToolBean bannerViewToolBean = new BannerViewToolBean();
//        bannerViewToolBean.setHeight(200);
//        bannerViewToolBean.setHeight(400);
//        bannerViewToolBean.setLeft(100);
//        bannerViewToolBean.setTop(50);
//        ArrayList<String> list = new ArrayList();
//        list.add("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%8C%BB%E9%99%A2&step_word=&hs=0&pn=0&spn=0&di=108020&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=3023154080%2C274986648&os=2473724681%2C863376530&simid=3448773794%2C476666588&adpicid=0&lpn=0&ln=1587&fr=&fmq=1562575452266_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic.qbaobei.com%2FUploads%2FPicture%2F2016-07-11%2F5782fa0f50f15.png&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bqkw5kjt_z%26e3Bv54AzdH3F3twghwg2AzdH3F9cnacl_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=&force=undefined");
//        list.add("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%8C%BB%E9%99%A2&step_word=&hs=0&pn=0&spn=0&di=108020&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=3023154080%2C274986648&os=2473724681%2C863376530&simid=3448773794%2C476666588&adpicid=0&lpn=0&ln=1587&fr=&fmq=1562575452266_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic.qbaobei.com%2FUploads%2FPicture%2F2016-07-11%2F5782fa0f50f15.png&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bqkw5kjt_z%26e3Bv54AzdH3F3twghwg2AzdH3F9cnacl_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=&force=undefined");
//
//        bannerViewToolBean.setList_path(list);
//        bannerViewToolBean.setList_title(list);
//        new BannerViewTool().creatview(bannerViewToolBean,commonBean);

        try {
            String result = data;
            JSONObject jSONObject = new JSONObject(result);
            JSONObject jsonData = jSONObject.getJSONObject("data");
            JSONArray jsonArray = jsonData.getJSONArray("components");


            if (!jsonData.getJSONObject("backgroundImage").isNull("diskPath")){
                String bgurl = Constant.pichttp+ jsonData.getJSONObject("backgroundImage").getString("diskPath");
                ViewbgLoader.getInstance().setBg(mView.getContext(), bgurl,parentlayout);
            }else {
                parentlayout.setBackground(mView.getContext().getResources().getDrawable(R.mipmap.bg));
            }



            for (int a =0 ;a<jsonArray.length();a++){
                String itemcomponents = jsonArray.getJSONObject(a).getString("properties").replaceAll("\\\\","");
                JSONObject itemcJson = new JSONObject(itemcomponents);
                String type =itemcJson.getString("type");
                LogUtils.i("chenzhu---->itemcJson" + type+"--"+itemcJson );
                switch (type){
                    case "s_img":
                        ImageViewToolBean imageViewToolBean_nf = new ImageViewToolBean();
                        //没有焦点图片
                        //  imageViewToolBean.setUrl(itemcJson.getString("url"));
                        if (!itemcJson.getJSONObject("url").isNull("diskPath")){
                            imageViewToolBean_nf.setUrl(itemcJson.getJSONObject("url").getString("diskPath"));
                        }else {
                            imageViewToolBean_nf.setUrl("");
                        }
                        //imageViewToolBean_nf.setUrl("http://192.168.2.201:1001/fileImage/imageShow/24da52d8da3442e5ad2209cee1762380");
                        imageViewToolBean_nf.setHeigh(itemcJson.getInt("height"));
                        imageViewToolBean_nf.setWidth(itemcJson.getInt("width"));
                        imageViewToolBean_nf.setMarleft( itemcJson.getInt("left"));
                        imageViewToolBean_nf.setMartop(itemcJson.getInt("top"));
                        imageViewToolBean_nf.setFocus(false);
                        imageViewTool.creatView(imageViewToolBean_nf,commonBean);
                        commonBean.setTag(imageViewToolBean_nf);
                        break;

                    case "s_text":
                        TextViewToolBean textViewToolBean = new TextViewToolBean();
                        //没有焦点文字
                        textViewToolBean.setFocus(false);
                        textViewToolBean.setHeigh(itemcJson.getInt("lineHeight"));
                        textViewToolBean.setWidth(itemcJson.getInt("width"));
                        textViewToolBean.setMarleft( itemcJson.getInt("left"));
                        textViewToolBean.setMartop(itemcJson.getInt("top"));
                        textViewToolBean.setTextsize(itemcJson.getInt("fontSize"));
                        textViewToolBean.setText (itemcJson.getString("text"));
                        //textViewToolBean.setTextCorol(itemcJson.getString("color")); textAlign
                        textViewTool.creatView(textViewToolBean,commonBean);
                        commonBean.setTag(textViewToolBean);
                        break;
                    case "s_time1":
                        //没有焦点
                        break;
                    case "s_time2":
                        TextClockViewToolBean textClockViewToolBean = new TextClockViewToolBean();
                        textClockViewToolBean.setFocus(false);
                        textClockViewToolBean.setHeigh(itemcJson.getInt("height"));
                        textClockViewToolBean.setWidth(itemcJson.getInt("width"));
                        textClockViewToolBean.setMarleft( itemcJson.getInt("left"));
                        textClockViewToolBean.setMartop(itemcJson.getInt("top"));
                        textClockViewToolBean.setTextsize(itemcJson.getInt("fontSize"));
                        textClockViewToolBean.setFormattype(itemcJson.getString("timeFormat"));
//                                textClockViewToolBean.setTextCorol(itemcJson.getInt("#FFFFFF"));
                        textClockViewTool.creatView(textClockViewToolBean,commonBean);
                        commonBean.setTag("s_time2");
                        //没有焦点

                        break;
                    case "s_vas_img":
                        //有焦点图片
                        ImageViewToolBean imageViewToolBean_f = new ImageViewToolBean();
                       if (!itemcJson.getJSONObject("iconPic").getJSONObject("url").isNull("diskPath")){
                           imageViewToolBean_f.setUrl(itemcJson.getJSONObject("iconPic").getJSONObject("url").getString("diskPath"));
                           LogUtils.i("----------->pic"+imageViewToolBean_f.getUrl());
                       }else {
                           imageViewToolBean_f.setUrl("");
                       }
//                        imageViewToolBean_f.setUrl(null);
                        imageViewToolBean_f.setHeigh(itemcJson.getInt("height"));
                        imageViewToolBean_f.setWidth(itemcJson.getInt("width"));
                        imageViewToolBean_f.setMarleft(itemcJson.getInt("left"));
                        imageViewToolBean_f.setMartop(itemcJson.getInt("top"));
                        imageViewToolBean_f.setFocustype(itemcJson.getInt("focusType"));
                        if (!itemcJson.getJSONObject("focusPic").getJSONObject("url").isNull("diskPath")){
                            imageViewToolBean_f.setFocuspicurl(itemcJson.getJSONObject("focusPic").getJSONObject("url").getString("diskPath"));
                        }else {
                            imageViewToolBean_f.setFocuspicurl("");
                        }
//
                        if (!itemcJson.getString("resource").equals("null")&&itemcJson.getString("resource")!=null
                            &&itemcJson.getJSONObject("resource").getJSONArray("resourceData").length()!=0) {
                            imageViewToolBean_f.setBindType(itemcJson.getJSONObject("resource").getInt("type"));
                            imageViewToolBean_f.setContentType(itemcJson.getJSONObject("resource").getJSONArray("resourceData").getJSONObject(0).getInt("type"));
                            String customercode = itemcJson.getJSONObject("resource").getJSONArray("resourceData").getJSONObject(0).getString("customerCode");
                            String parentcode = itemcJson.getJSONObject("resource").getJSONArray("resourceData").getJSONObject(0).getString("code");
                            String url = itemcJson.getJSONObject("data").getString("url");
                            if(url.isEmpty()){
                                imageViewToolBean_f.setJumpurl("");
                            }else {
                                if (imageViewToolBean_f.getBindType()==1){
                                    imageViewToolBean_f.setJumpurl(Constant.CATALOGEPGURL+"cpCode="+customercode+"&parentCode="+parentcode);
                                }else {
                                    switch (imageViewToolBean_f.getContentType()){
                                        case 0:
                                            imageViewToolBean_f.setJumpurl(Constant.TEXT+"code="+parentcode);
                                            break;
                                        case 1:
                                            imageViewToolBean_f.setJumpurl(Constant.PIC_TEXT+"code="+parentcode);
                                            break;
                                        case 2:
                                            imageViewToolBean_f.setJumpurl(Constant.PIC_TEXT+"code="+parentcode);
                                            break;
                                        case 3:
                                            imageViewToolBean_f.setJumpurl(Constant.PIC+"code="+parentcode);
                                            break;
                                        case 4:
//                                            视频fileVideo/VideoShow/4573f16ce00745d290e517f5e491a566 videoUrl
                                            String videovURl = itemcJson.getJSONObject("resource").getJSONArray("resourceData").getJSONObject(0).getString("videoUrl");
                                            imageViewToolBean_f.setJumpurl(videovURl);
                                            break;
                                    }

                                }

                            }

                        }else {
                            imageViewToolBean_f.setJumpurl("");
                        }

                        imageViewToolBean_f.setFocus(true);
                        commonBean.setTag(imageViewToolBean_f);
                        imageViewTool.creatView(imageViewToolBean_f,commonBean);
                        break;
                    case "s_marquee":
                        MarqueeTextViewToolBean marqueeTextViewToolBean = new MarqueeTextViewToolBean();
                        //跑马灯
                        //没有焦点
                        commonBean.setTag("s_marquee");
                        if (!itemcJson.getJSONObject("backgroundImage").isNull("diskPath")){
                            marqueeTextViewToolBean.setTextviewbgurl(itemcJson.getJSONObject("backgroundImage").getString("diskPath"));
                        }else {
                            marqueeTextViewToolBean.setTextviewbgurl("");
                        }

                        marqueeTextViewToolBean.setFocus(false);
                        marqueeTextViewToolBean.setHeigh(itemcJson.getInt("height"));
                        marqueeTextViewToolBean.setWidth(itemcJson.getInt("width"));
                        marqueeTextViewToolBean.setMarleft( itemcJson.getInt("left"));
                        marqueeTextViewToolBean.setMartop(itemcJson.getInt("top"));
                        marqueeTextViewToolBean.setTextsize(itemcJson.getInt("fontSize"));
                        marqueeTextViewToolBean.setText(itemcJson.getString("text"));
                        marqueeTextViewToolBean.setTextcolor(itemcJson.getString("color"));
                        marqueeTextViewTool.creatView(marqueeTextViewToolBean,commonBean);


                        break;
                    case "d_vas_list":
                        ListViewToolBean listViewToolBean = new ListViewToolBean();
                        List<ListItemBean> listItemBeen = new ArrayList<>();
                        //pagecode name  新闻标题
                        String pageCode = itemcJson.getString("pageCode");
                        commonBean.setTag("d_vas_list");
                        listViewToolBean.setTitlename(itemcJson.getString("name"));
                        listViewToolBean.setHeigh(itemcJson.getInt("height"));
                        listViewToolBean.setWidth(itemcJson.getInt("width"));
                        listViewToolBean.setMarleft( itemcJson.getInt("left"));
                        listViewToolBean.setMartop(itemcJson.getInt("top"));
                        listViewToolBean.setTitletextsize(itemcJson.getJSONObject("headLine").getInt("fontSize"));
                        listViewToolBean.setTitlehight(itemcJson.getJSONObject("headLine").getInt("height"));
                        listViewToolBean.setTitletextcolor(itemcJson.getJSONObject("headLine").getString("color"));
                        listViewToolBean.setContentlinehight(itemcJson.getJSONObject("contentList").getInt("lineHeight"));
                        listViewToolBean.setContenttextsize(itemcJson.getJSONObject("contentList").getInt("fontSize"));
                        listViewToolBean.setContenttextcolor(itemcJson.getJSONObject("contentList").getString("color"));

                        //  TODO 取到code再去获取数据
                        for(int i =0 ;i<10;i++){
                            ListItemBean listItemBean = new ListItemBean();
                            listItemBean.setIndex(a);
                            listItemBean.setText("测试你每天是是不是"+a);
                            listItemBeen.add(listItemBean);
                        }
                        listViewTool.creatView(listViewToolBean,commonBean,listItemBeen);

                        break;
                    case "s_video":
                        videoViewToolBean.setFocus(true);
                        videoViewToolBean.setHeigh(itemcJson.getInt("height"));
                        videoViewToolBean.setWidth(itemcJson.getInt("width"));
                        videoViewToolBean.setMarleft( itemcJson.getInt("left"));
                        videoViewToolBean.setMartop(itemcJson.getInt("top"));
                        if (itemcJson.getJSONObject("resource").getJSONArray("resourceData").length()!=0) {
                            videoViewToolBean.setUrl(itemcJson.getJSONObject("resource").getJSONArray("resourceData").getJSONObject(0).getString("url"));
                        }else {
                            videoViewToolBean.setUrl("http://192.168.2.170:9901/tsfile/live/0009_1.m3u8?key=txiptv&playlive=1&authid=0");
                        }
                        commonBean.setTag(videoViewToolBean);
                        videoViewTool.creatview(videoViewToolBean,commonBean);
                        break;
                }

            }
            mView.inintSuccess();
        } catch ( Exception e) {
            e.printStackTrace();
             mView.initFail();
        }

    }

    @Override
    public void jumpApk(String packgename, Context context) {
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packgename);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void pause() {
        videoViewTool.pause();
    }

    @Override
    public void start() {
        videoViewTool.start();
    }

    @Override
    public void startClien(final Context context) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.i("chenzhu--->Service  onStart will in  applocation");
                Intent actIntent = new Intent(context.getApplicationContext(), clientService.class);
                actIntent.setAction("android.intent.action.MAIN");
                actIntent.addCategory("android.intent.category.LAUNCHER");
                actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(actIntent);
            }
        },1* 1000);

    }

    @Override
    public void getUpdateApkinfo(final int version) {
        Api.getDefault().getApkInfo(1,100).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                JSONObject jSONObject = null;
                try {
                    result = response.body().string();
                    jSONObject = new JSONObject(result);
                    JSONArray data = jSONObject.getJSONObject("data").getJSONArray("pageRecords");
                    if (data.length()!=0){
                        JSONObject jsonObject = data.getJSONObject(0);
                        String onlineVersion = jsonObject.getString("version");
                        if (Integer.valueOf(onlineVersion)>Integer.valueOf(version)){
                           //  需要升级
                            mView.needUpdateApk(jsonObject.getJSONObject("url").getString("url"));
                        }else {
                            // 不需要升级
                            mView.unneedUpdateApk("当前launcher为最新桌面");
                        }
                    }
                } catch ( Exception e) {
                    e.printStackTrace();
                    mView.unneedUpdateApk("获取桌面版本信息解析失败");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.unneedUpdateApk("获取桌面版本失败");
            }
        });
    }

    @Override
    public void getUpdateSysinfo(final String localversion) {
        Api.getDefault().getSysInfo(1,100).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                JSONObject jSONObject = null;
                try {
                    result = response.body().string();
                    jSONObject = new JSONObject(result);
                    JSONArray data = jSONObject.getJSONObject("data").getJSONArray("pageRecords");
                    if (data.length()!=0){
                        JSONObject jsonObject = data.getJSONObject(0);
                        String onlineVersion = jsonObject.getString("version");

                         if (LogUtils.compare(localversion,onlineVersion)) {
                             //  需要升级
                             mView.needUpdateSys(jsonObject.getString("url"));
                         }else {
                             // 不需要升级
                             mView.unneedUpdateSys("当前系统为最新系统");
                         }
                    }
                } catch ( Exception e) {
                    e.printStackTrace();
                    mView.unneedUpdateSys("获取系统版本信息解析失败");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.unneedUpdateSys("获取系统版本失败");
            }
        });
    }



}

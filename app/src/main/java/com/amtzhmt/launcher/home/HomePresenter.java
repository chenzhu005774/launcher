package com.amtzhmt.launcher.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.push.clientService;
import com.amtzhmt.launcher.util.utils.bean.ListItemBean;
import com.amtzhmt.launcher.util.utils.commonbean.CommonBean;
import com.amtzhmt.launcher.util.utils.image.ViewbgLoader;
import com.amtzhmt.launcher.mvp.BasePresenterImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
                           System.out.println("----------->pic"+imageViewToolBean_f.getUrl());
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
                            videoViewToolBean.setUrl("http://192.168.2.120:9901/tsfile/live/0001_2.m3u8?key=txiptv&playlive=1&down=1");
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


}

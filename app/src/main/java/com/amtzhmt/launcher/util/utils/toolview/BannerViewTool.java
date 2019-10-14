package com.amtzhmt.launcher.util.utils.toolview;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.util.utils.commonbean.CommonBean;
import com.amtzhmt.launcher.util.utils.image.GlideImageLoader;
import com.amtzhmt.launcher.util.utils.image.MyLoader;
import com.amtzhmt.launcher.util.utils.Constant;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/3.
 */
final public class BannerViewTool  {
    public  Banner banner;
    public int  position;
    public void creatview(BannerViewToolBean bannerViewToolBean,CommonBean commonBean){

        RelativeLayout rootlayout = new RelativeLayout(commonBean.getContext());
        rootlayout.setOnFocusChangeListener(commonBean.getFocusChangeListener());
        rootlayout.setFocusable(bannerViewToolBean.focus);
        if (bannerViewToolBean.focus) {
            rootlayout.setBackgroundResource(R.drawable.bgseletor);
        }
        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (bannerViewToolBean.focus) {
            params.setMargins(bannerViewToolBean.getLeft()-Constant.margin, bannerViewToolBean.getTop()-Constant.margin, 0, 0);
        }else {
            params.setMargins(bannerViewToolBean.getLeft(), bannerViewToolBean.getTop(), 0, 0);
        }
        rootlayout.setLayoutParams(params);
        rootlayout.setTag(commonBean.getTag());
        rootlayout.setOnClickListener(commonBean.getOnClickListener());

        RelativeLayout.LayoutParams bannerpar =  new RelativeLayout.LayoutParams(bannerViewToolBean.getWidth(),bannerViewToolBean.getHeight());
//        bannerpar.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
         banner  = new Banner(commonBean.getContext());
        List<Integer> list_title_defult = new ArrayList<>();
        list_title_defult.add(R.mipmap.b1);
        banner.setImages(list_title_defult);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置网络图片集合
        banner.setImages(bannerViewToolBean.getList_path());
        banner.setBannerAnimation(Transformer.Default);
        banner.setImageLoader(new MyLoader());

        banner.setDelayTime(3000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);

//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
//        banner.setBannerAnimation(Transformer.CubeOut); //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(bannerViewToolBean.getList_title());

        banner.start();
        banner.setLayoutParams(bannerpar);
        rootlayout.addView(banner);
        commonBean.getLayout().addView(rootlayout);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position1) {
                position = position1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


}

package com.amtzhmt.launcher.catalog;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.mvp.MVPBaseActivity;
import com.amtzhmt.launcher.util.utils.LogUtils;

import java.util.Random;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class CatalogActivity extends MVPBaseActivity<CatalogContract.View, CatalogPresenter> implements CatalogContract.View {
    RelativeLayout progressout,progressin;
    RelativeLayout.LayoutParams params;
    LinearLayout root;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
//        progressout = (RelativeLayout)findViewById(R.id.progress_out);
//        progressin = (RelativeLayout)findViewById(R.id.progress_in);
//        params= (RelativeLayout.LayoutParams) progressin.getLayoutParams();
        root =(LinearLayout)findViewById(R.id.catalogroot);
//        params.height=progressout.getHeight()/3;//设置当前控件布局的高度  这个需要等获取到数据然后布局完成再去设置才有效
//        LogUtils.i("chenzhu----------height"+progressout.getHeight()/3);
//        progressin.setLayoutParams(params);//将设置好的布局参数应用到控件 中


    }


    @Override
    protected void onResume() {
        super.onResume();
        Random random = new Random();
        int pick = random.nextInt(5);
        switch (pick){
            case 1:
                root.setBackgroundResource(R.mipmap.t1);
                break;
            case 2:
                root.setBackgroundResource(R.mipmap.t2);
                break;
            case 3:
                root.setBackgroundResource(R.mipmap.t3);
                break;
            case 4:
                root.setBackgroundResource(R.mipmap.t4);
                break;
            case 5:
                root.setBackgroundResource(R.mipmap.t5);
                break;

            default:
                root.setBackgroundResource(R.mipmap.t1);
                break;
        }

    }
}

package com.amtzhmt.launcher.util.utils.toolview;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.util.utils.bean.ListItemBean;
import com.amtzhmt.launcher.util.utils.commonbean.CommonBean;
import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.MyAdapter;
import java.util.List;

/**
 * Created by Administrator on 2019/4/3.
 */
final public class ListViewTool {
    public void creatView(ListViewToolBean listViewToolBean , final CommonBean commonBean, final List<ListItemBean> list ){
        RelativeLayout rootlayout = new RelativeLayout(commonBean.getContext());
        rootlayout.setOnFocusChangeListener(commonBean.getFocusChangeListener());
        rootlayout.setFocusable(listViewToolBean.focus);
        rootlayout.requestFocus();
        if (listViewToolBean.focus) {
            rootlayout.setBackgroundResource(R.drawable.bgseletor);
        }

        RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if (listViewToolBean.focus) {
            //有背景框的话 就减去背景框的长度
            params.setMargins(listViewToolBean.getMarleft()- Constant.margin,listViewToolBean.getMartop()-Constant.margin,0,0);
        }else{
            params.setMargins(listViewToolBean.getMarleft() ,listViewToolBean.getMartop() ,0,0);
        }

        rootlayout.setLayoutParams(params);
        rootlayout.setTag(commonBean.getTag());
        rootlayout.setOnClickListener(commonBean.getOnClickListener());



        TextView textView = new TextView(commonBean.getContext());


        textView.setText(listViewToolBean.getTitlename());
        textView.setTextSize(listViewToolBean.getTitletextsize());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(android.graphics.Color.parseColor(listViewToolBean.getTitletextcolor()));
        textView.setHeight(listViewToolBean.getTitlehight());
        textView.setWidth(listViewToolBean.getWidth());
        textView.setId(View.generateViewId()); //此代码要在api level 17以上才能生效
        rootlayout.addView(textView);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
//        lp.setMargins(0, 5, 0, 5);
//        textView.setLayoutParams(lp);

         //这里减去  是为了title的高度
        RelativeLayout.LayoutParams imgparams =  new RelativeLayout.LayoutParams(
                listViewToolBean.getWidth(),listViewToolBean.getHeigh()-listViewToolBean.getTitlehight() );
        imgparams.addRule(RelativeLayout.BELOW,textView.getId());
        final ListView listView = new ListView(commonBean.getContext());
        listView.setLayoutParams(imgparams);
        listView.setSelector(R.drawable.bgseletor);
        listView.setDivider(null);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setAdapter(new MyAdapter(listViewToolBean,commonBean.getContext(),list));
        listView.setVerticalScrollBarEnabled(false);
//        listView.setMinimumHeight(listViewToolBean.getContentlinehight());
        rootlayout.setBackgroundColor(commonBean.getContext().getResources().getColor(R.color.fouhalfwhite));
        rootlayout.addView(listView);



        commonBean.getLayout().addView(rootlayout);

    }

}

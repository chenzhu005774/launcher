package com.amtzhmt.launcher.util.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.amtzhmt.launcher.R;
import com.amtzhmt.launcher.util.utils.bean.ListItemBean;
import com.amtzhmt.launcher.util.utils.toolview.ListViewToolBean;

import java.util.List;

/**
 * 自定义适配器
 * 
 * @author qiangzi
 * 
 */
public class MyAdapter extends BaseAdapter {
    private List<ListItemBean> dataList;
    private Context context;
    private  ListViewToolBean listViewToolBean;

    /**
     * 有参构造
     * 
     * @param context
     *            界面
     * @param dataList
     *            数据
     *            列表项资源文件
     */
    public MyAdapter(ListViewToolBean listViewToolBean,Context context, List<ListItemBean> dataList ) {
        this.context = context;
        this.dataList = dataList;
        this.listViewToolBean =listViewToolBean;

    }

    @Override
    public int getCount() {

        return dataList.size();
    }

    @Override
    public Object getItem(int index) {

        return dataList.get(index);
    }

    @Override
    public long getItemId(int index) {

        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        // 声明内部类
        Util util = null;
        // 中间变量
        final int flag = index;
        /**
         * 根据listView工作原理，列表项的个数只创建屏幕第一次显示的个数。
         * 之后就不会再创建列表项xml文件的对象，以及xml内部的组件，优化内存，性能效率
         */
        if (view == null) {
            util = new Util();
            // 给xml布局文件创建java对象
            LayoutInflater inflater = LayoutInflater.from(context);
//            view = inflater.inflate(resource, null);
            view = inflater.inflate(R.layout.list_view, null);
            // 指向布局文件内部组件
            util.contentTextView = (TextView) view .findViewById(R.id.item_content);

            // 增加额外变量
            view.setTag(util);



        } else {
            util = (Util) view.getTag();
        }
        // 获取数据显示在各组件
        ListItemBean listItemBean = dataList.get(index);
        util.contentTextView.setText(listItemBean.getText());
        util.contentTextView.setTextSize(listViewToolBean.getContenttextsize());
        util.contentTextView.setTextColor(android.graphics.Color.parseColor(listViewToolBean.getContenttextcolor()));
        if (listItemBean.getIndex()%2==0){
            view.setBackgroundColor(context.getResources().getColor(R.color.halfwhite));
        }
        return view;
    }

  class Util {
        TextView contentTextView;

    }
}

/**
 * 内部类，用于辅助适配
 * 
 * @author qiangzi
 * 
 */

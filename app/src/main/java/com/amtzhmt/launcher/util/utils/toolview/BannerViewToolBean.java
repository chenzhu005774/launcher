package com.amtzhmt.launcher.util.utils.toolview;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/4/3.
 */
final public class BannerViewToolBean {
//    public Context context ;
    public ArrayList<String> list_path;
    public ArrayList<String> list_title;
    public boolean focus;
    public int width ;
    public int height;
    public int left ;
    public int top;



    public ArrayList<String> getList_path() {
        return list_path;
    }

    public void setList_path(ArrayList<String> list_path) {
        this.list_path = list_path;
    }

    public ArrayList<String> getList_title() {
        return list_title;
    }

    public void setList_title(ArrayList<String> list_title) {
        this.list_title = list_title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}

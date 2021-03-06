package com.amtzhmt.launcher.util.utils.toolview;

import com.amtzhmt.launcher.util.utils.Constant;

/**
 * Created by Administrator on 2019/4/3.
 * attuibute
 * attuibute
 * 对应的属性值
 */
public class ImageViewToolBean {
    int width;
    int heigh;
    int martop;
    int marleft;

    boolean focus; //是否获取焦点
    String url;//图片地址
    boolean requesfocus =false;
    int focustype = 0;
    String focuspicurl;

    // 跳转url
    String jumpurl;
    // 内容类型   资源类型（0：文字，1：医师图文，2：常规图文，3：图片详情，4：视频
    int contentType;
    //  绑定的是栏目还是 内容 1 是栏目 2是内容
    int bindType;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigh() {
        return heigh;
    }

    public void setHeigh(int heigh) {
        this.heigh = heigh;
    }

    public int getMartop() {
        return martop;
    }

    public void setMartop(int martop) {
        this.martop = martop;
    }

    public int getMarleft() {
        return marleft;
    }

    public void setMarleft(int marleft) {
        this.marleft = marleft;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = Constant.pichttp+ url;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public boolean isRequesfocus() {
        return requesfocus;
    }

    public void setRequesfocus(boolean requesfocus) {
        this.requesfocus = requesfocus;
    }

    public int getFocustype() {
        return focustype;
    }

    public void setFocustype(int focustype) {
        this.focustype = focustype;
    }

    public String getFocuspicurl() {
        return focuspicurl;
    }

    public void setFocuspicurl(String focuspicurl) {
        this.focuspicurl = Constant.pichttp+focuspicurl;
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getBindType() {
        return bindType;
    }

    public void setBindType(int bindType) {
        this.bindType = bindType;
    }
}

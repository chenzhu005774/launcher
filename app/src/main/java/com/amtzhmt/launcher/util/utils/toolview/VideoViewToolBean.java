package com.amtzhmt.launcher.util.utils.toolview;

/**
 * Created by Administrator on 2019/4/10.
 */
public class VideoViewToolBean {
    int width;
    int heigh;
    int martop;
    int marleft;
    boolean focus;
    String url;//视屏地址

    int type;//1 是直播 2是点播放

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
        this.url = url;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

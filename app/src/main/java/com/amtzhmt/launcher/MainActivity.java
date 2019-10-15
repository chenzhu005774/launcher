package com.amtzhmt.launcher;
import com.amtzhmt.launcher.util.utils.base.BaseActivity;


public class MainActivity extends BaseActivity  {
    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void initView() {
        skipAnotherActivity(this, com.amtzhmt.launcher.main.MainActivity.class);
        return;
//        icon = (ImageView)findViewById(R.id.icon);
//        seek = (SeekBar)findViewById(R.id.seek);
//        iconview= (RelativeLayout)findViewById(R.id.iconview);
//
//        Rotate3dAnimation animation  = new Rotate3dAnimation(0,0,0,0,0,true);
//        animation.setAnimateScale(icon,2500);
//        LogUtils.i("chenzhu--->token----------start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在这里检测网络 等待1.5s 发送消息检测网络状态
//        handler.postDelayed(runnable, 1 );
    }

    @Override
    public void initData() {

    }




}

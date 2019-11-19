package com.amtzhmt.launcher.push;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amtzhmt.launcher.util.utils.Constant;
import com.amtzhmt.launcher.util.utils.LogUtils;

/**
 * Created by Administrator on 2018/1/12.
 */

public class clientService extends Service {
    final Context context  = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("chenzhu--->Service onDestroy");
    }

    private final static int SERVICE_ID=-5121;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("chenzhu--->Service  onStartCommand");
        startForeground(SERVICE_ID,new Notification());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String username = "12345567890";
                    LogUtils.i("username = "+username);
//                    MyUdpClient myUdpClient = new MyUdpClient(OriginalUtil.MD5(username),1,"192.168.2.40",8092,context);
                    MyUdpClient myUdpClient = new MyUdpClient("12345567890"+Constant.KEY,1, Constant.serviceIP,Constant.POTR,context);
                    myUdpClient.setHeartbeatInterval(30);
                    myUdpClient.start();
                    synchronized(myUdpClient){
                        myUdpClient.wait();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;

    }

    public class MyBinder extends Binder {

    }
}

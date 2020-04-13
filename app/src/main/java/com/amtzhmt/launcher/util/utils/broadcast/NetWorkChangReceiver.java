package com.amtzhmt.launcher.util.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amtzhmt.launcher.App;
import com.amtzhmt.launcher.util.utils.DialogCallback;
import com.amtzhmt.launcher.util.utils.LogUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 监听网络状态变化
 */
 
public class NetWorkChangReceiver extends BroadcastReceiver implements DialogCallback{
 Context context;
 boolean IsInterrupt = false; //是否断网中
     View view ;
 Handler handler = new Handler();
    Runnable success =  new Runnable() {
        @Override
        public void run() {
            try {
                if (view!=null){
                    LogUtils.wm.removeView(view);
                }
            }catch (Exception e){

            }

            view=  LogUtils.showWindowManagerDialog1(context, "网络连接成功");
        }
    };
    Runnable fail =  new Runnable() {
        @Override
        public void run() {
            try {
                if (view!=null){
                    LogUtils.wm.removeView(view);
                }
            }catch (Exception e){

            }

            view=  LogUtils.showWindowManagerDialog1(context, "网络断开,请检查网络设置,点击设置跳转网络设置");
        }
    };
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if ("android.net.ethernet.ETHERNET_STATE_CHANGE".equals(intent.getAction())) {
            if (isWirePluggedIn()) {
                LogUtils.i("收到插上线的广播");
                if (IsInterrupt) {
                    handler.removeCallbacks(success);
                    handler.postDelayed(success,1500);
                }
            } else {
                IsInterrupt = true;
                handler.removeCallbacks(fail);
                handler.postDelayed(fail,1500);
            }
        }
    }
    @Override
    public void clickSure() {
        startAPP("com.android.smart.terminal.settings");
    }
    /**
     * @exception :启动apk
     * com.android.smart.terminal.settings/.LaunchActivity
     * @param ：String  包名
     **/
    public void startAPP(String appPackageName){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }
    //判断网线拔插状态
    //通过命令cat /sys/class/net/eth0/carrier，如果插有网线的话，读取到的值是1，否则为0
    public boolean isWirePluggedIn(){
        String state= execCommand("cat /sys/class/net/eth0/carrier");
        if(state.trim().equals("1")){  //有网线插入时返回1，拔出时返回0
            return true;
        }
        return false;
    }
    public String execCommand(String command) {
        Runtime runtime;
        Process proc = null;
        StringBuffer stringBuffer = null;
        try {
            runtime = Runtime.getRuntime();
            proc = runtime.exec(command);
            stringBuffer = new StringBuffer();
            if (proc.waitFor() != 0) {
                System.err.println("exit value = " + proc.exitValue());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));

            String line = null;
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line + " ");
            }

        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                proc.destroy();
            } catch (Exception e2) {
            }
        }
        return stringBuffer.toString();
    }
}
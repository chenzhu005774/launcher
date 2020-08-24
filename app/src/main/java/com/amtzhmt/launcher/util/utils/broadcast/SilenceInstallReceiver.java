package com.amtzhmt.launcher.util.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SilenceInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")||
                intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            startApp("com.amtzhmt.launcher",context);
        }
    }
    public void startApp(String appPackageName,Context context){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "安装完成自启失败", Toast.LENGTH_LONG).show();
        }
    }
}

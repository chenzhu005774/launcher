package com.amtzhmt.launcher.util.utils;
 
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amtzhmt.launcher.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

/**
 *更新工具类
 */
public class UpdateManager {

//   Environment.getDataDirectory() = /data
//   Environment.getDownloadCacheDirectory() = /cache
//   Environment.getExternalStorageDirectory() = /mnt/sdcard
//   Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
//   Environment.getRootDirectory() = /system
//   getPackageCodePath() = /data/app/com.my.app-1.apk
//   getPackageResourcePath() = /data/app/com.my.app-1.apk
//   getCacheDir() = /data/data/com.my.app/cache
//   getDatabasePath(“test”) = /data/data/com.my.app/databases/test
//   getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
//   getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
//   getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
//   getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
//   getFilesDir() = /data/data/com.my.app/files
//            RecoverySystem.installPackage(context,packageFile);
//            RecoverySystem.verifyPackage(packageFile,listener,deviceCertsZipFile);
//            其中，第三个参数deviceCertsZipFile可以为null，默认使用”/system/etc/security/otacerts.zip”文件。
//        在AndroidManifest.xml文件中加入以下两行权限：
//        <uses-permission android:name="android.permission.ACCESS_CACHE_FILESYSTEM" />
//        <uses-permission android:name="android.permission.REBOOT" />

   private static final String syssavePath = Environment.getDownloadCacheDirectory().getPath()+"/recovery/";
   private static final String syssaveFileName =  "update.zip";

   private static final String savePath = Environment.getExternalStorageDirectory().getPath() + "/apkupdatefile/";
private static final String saveFileName ="launcher.apk";
//   private static final String savePath = Environment.getRootDirectory() + "/app/";
//   private static final String saveFileName =  "launcher.apk";
   private ProgressBar mProgress;
   private boolean interceptFlag = false;
   private String loadApkUrl;
   private int progress;
   private static final int DOWN_UPDATE = 1;
   private static final int APK_DOWN_OVER = 2;
   private static final int SYS_DOWN_OVER = 3;
   private  Context mContext;
   private Dialog downloadDialog;
   private TextView tvProgress;
   private int updatetype=1;
   AlertDialog.Builder builder;
   public UpdateManager(Context mContext,int updatetype ) {
      this.mContext = mContext;
      this.updatetype =updatetype;
   }

   private Handler mHandler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
         super.handleMessage(msg);
         switch (msg.what)
         {
            case DOWN_UPDATE:
               mProgress.setProgress(progress);
               tvProgress.setText(progress+"%");
               break;
            case APK_DOWN_OVER:
               installApk();
               break;
            case SYS_DOWN_OVER:
               builder.setMessage("正在验证系统软件包");
               verifyPackageRom();
               break;
         }
      }
   };

   /**
    * 验证系统包
    */
   private void verifyPackageRom() {
    File  packfile = new File(syssavePath, syssaveFileName);
      try {
         RecoverySystem.verifyPackage(packfile, new RecoverySystem.ProgressListener() {
            //该listener只接受一个抽象方法，参数为验证进度
            @Override
            public void onProgress(int progressnum) {
              LogUtils.i("verifyPackageRom progress = " + progressnum);
              progress= progressnum;
               //TODO 实时的将验证进度显示到进度条上。这里的dialog已经消失了。在这里应该重新建一个dialog
               mHandler.sendEmptyMessage(DOWN_UPDATE);
            }
         } , null);
         LogUtils.i("verifyPackage is completed and it ok");
         try {
            LogUtils.i("will install");
            RecoverySystem.installPackage(mContext, packfile);
         }catch (Exception e){
            LogUtils.i("system install Exception:"+e);
            e.printStackTrace();
            builder.setMessage("系统验证安装失败");
         }
      } catch (Exception e) {
         LogUtils.i("verifyPackage　or install Exception:"+e);
         e.printStackTrace();
         builder.setMessage("系统验证不通过");
      }
   }

   /**
    *覆盖安装apk
    */
   private void installApk() {
      File apkfile = new File(savePath,saveFileName);
      if (!apkfile.exists()) {
         return;
      }
     LogUtils.i("apk path:"+saveFileName);
      Intent intent = new Intent();
      intent.setAction("android.intent.action.VIEW");
      intent.addCategory("android.intent.category.DEFAULT");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
      mContext.startActivity(intent);
      downloadDialog.dismiss();

   }
   /**
    *升级对话框
    **/
   public  void showDialog(final String downApkUrl,String message) {
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
      builder.setTitle("新版本升级").
            setIcon(R.mipmap.update). // 设置提示框的图标
            setMessage("新版本主要包含以下更新:\n"+message).// 设置要显示的信息
            setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
         @Override
         public void onClick(DialogInterface dialog, int which) {
            startUpload(downApkUrl);//下载最新的版本程序
         }
      }).setNegativeButton("取消", null);//设置取消按钮,null是什么都不做，并关闭对话框
      AlertDialog alertDialog = builder.create();
      // 显示对话框
      alertDialog.show();
   }

  /**
   *开始下载文件
   **/
   private  void  startUpload(String downApkUrl){
      builder = new AlertDialog.Builder(mContext);
      builder.setTitle("文件下载").
      setIcon(R.mipmap.update).
      setMessage("文件正在下载,请勿断电!!!");
      final LayoutInflater inflater = LayoutInflater.from(mContext);
      View v = inflater.inflate(R.layout.progress, null);
      mProgress = (ProgressBar) v.findViewById(R.id.progress);
      tvProgress = (TextView) v.findViewById(R.id.tv_progress);
      builder.setView(v);
      downloadDialog = builder.create();
      downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
         @Override
         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return true;
         }
      });
      downloadDialog.show();
      loadApkUrl = downApkUrl;
      DownApkorSysrom();
   }
   /**
    *下载文件
    *这里我是用的子线程
    **/
   public  void DownApkorSysrom(){
      Thread thread=new Thread(new Runnable() {
         @Override
         public void run() {
            try {
               URL url = new URL(loadApkUrl);
               LogUtils.i("updateManager download file url:"+loadApkUrl);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.connect();
               int length = conn.getContentLength();
               InputStream is = conn.getInputStream();
               File file;
               String filename;
               File downFile;
               if(updatetype==1) {//类型为1 表示apk更新
                  file = new File(savePath);
                  if (!file.exists()) {
                     file.mkdir();
                  }
                  filename = saveFileName;
                  downFile = new File(file,filename);
               }else {//否则类型为2 表示系统更新
                   file = new File(syssavePath);
                  if (!file.exists()) {
                     file.mkdir();
                  }
                  filename = syssaveFileName;
                  downFile = new File(file,filename);
               }

               FileOutputStream fos = new FileOutputStream(downFile);
               int count = 0;
               byte buf[] = new byte[1024];
               do {
                  int numread = is.read(buf);
                  count += numread;
                  progress = (int) (((float) count / length) * 100);
                  // 更新进度
                  mHandler.sendEmptyMessage(DOWN_UPDATE);
                  if (numread <= 0) {
                     // 下载完成通知安装
                     if (updatetype==2) {
                        mHandler.sendEmptyMessage(SYS_DOWN_OVER);
                     }else {
                        mHandler.sendEmptyMessage(APK_DOWN_OVER);
                     }
                     break;
                  }
                  fos.write(buf, 0, numread);
               } while (!interceptFlag);// 点击取消就停止下载.
            } catch (MalformedURLException e) {
               LogUtils.i("down load MalformedURLException:"+e.toString());
               builder.setMessage("文件下载异常....");
               e.printStackTrace();
            } catch (IOException e) {
               LogUtils.i("down load IOException TTTT:"+e.toString());
               builder.setMessage("文件下载异常....");
               e.printStackTrace();
            }
         }
      });
      thread.start();
   }

}

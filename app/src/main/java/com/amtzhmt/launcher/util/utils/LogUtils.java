package com.amtzhmt.launcher.util.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amtzhmt.launcher.R;

/**
 * 打印log信息类
 * @author zxy
 */
public class LogUtils {
    /**
     * 是否允许显示log
     */
    public static final boolean isLogEnabled = true;
    private static final String TAG_APP = "chenzhu";

    /**
     * 显示调试log信息
     *
     * @param here
     * @param paramString
     */
    public static void d(String here, String paramString) {
        if (isLogEnabled) {
            Log.d("[" + here + "]", paramString);
        }
    }

    /**
     * 显示错误log信息
     *
     * @param here
     * @param paramString
     */
    public static void e(String here, String paramString) {
        if (isLogEnabled) {
            Log.d("[" + here + "]", paramString);
        }
    }

    /**
     * 显示info log信息
     *
     * @param here
     * @param paramString
     */
    public static void i(String here, String paramString) {
        if (isLogEnabled) {
            Log.d("[" + here + "]", paramString);
        }
    }

    /**
     * 显示VERBOSE log信息
     *
     * @param here
     * @param paramString
     */
    public static void v(String here, String paramString) {
        if (isLogEnabled) {
            Log.d("[" + here + "]", paramString);
        }
    }

    /**
     * 显示warm log信息
     *
     * @param here
     * @param paramString
     */
    public static void w(String here, String paramString) {
        if (isLogEnabled) {
            Log.d("[" + here + "]", paramString);
        }
    }


    /**
     * 显示info log信息
     *
     * @param paramString
     */
    public static void i(String paramString) {
        if (isLogEnabled) {
            Log.d("[" + TAG_APP + "]", paramString);
        }
    }



    //日志打印不全
    public static void longd(  String msg) {  //信息太长,分段打印
       String tag =TAG_APP;
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，

        //  把4*1024的MAX字节打印长度改为2001字符数

        int max_str_length = 2001 - tag.length();

        //大于4000时

        while (msg.length() > max_str_length) {

             i(tag, msg.substring(0, max_str_length));

            msg = msg.substring(max_str_length);

        }

        //剩余部分

        i(tag, msg);

    }


    /**
     * 显示VERBOSE log信息
     *
     * @param paramString
     */
    public static void v(String paramString) {
        if (isLogEnabled) {
            Log.d("[" + TAG_APP + "]", paramString);
        }
    }

    /**
     * 显示调试log信息
     *
     * @param paramString
     */
    public static void d(String paramString) {
        if (isLogEnabled) {
            Log.d("[" + TAG_APP + "]", paramString);
        }
    }
    public static void toast(Context context,String paramString) {
        if (isLogEnabled) {
            Toast.makeText(context, "[" + TAG_APP + "]" +paramString, Toast.LENGTH_LONG).show();
        }
    }


    public static void showDialog(Context context,String paramString) {
        if (isLogEnabled) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(context);
            builder.setTitle("提示" ) ;
            builder.setMessage(paramString ) ;
            builder.setPositiveButton("确认" ,  null );
            builder.show();
        }
    }

    /**
     * 显示错误log信息
     * @param paramString
     */
    public static void e(String paramString) {
        if (isLogEnabled) {
            Log.d("[" + TAG_APP + "]", paramString);
        }
    }
    /**
     * 显示系统级别弹窗
     * @param msg
     */
    public static void  showsystemDialog(String  msg, Context context , final Handler handler){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // to do
            }
        });
        final android.app.AlertDialog dialog = builder.create();
        //在dialog show前添加此代码，表示该dialog属于系统dialog。
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        View view = View.inflate(context, R.layout.system_dialog_view, null);
        TextView tvMsg = (TextView) dialog.findViewById( R.id.dialog_text);
        tvMsg.setText(msg);
        dialog.setView(view);
        new Thread() {
            public void run() {
                SystemClock.sleep(2000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            };
        }.start();
    }


    /**
     * 显示各种log信息
     * @param logType= Log.DEBUG/Log.ERROR...
     * @param content
     */
    public static void log(int logType, String content) {
        switch (logType) {
            case Log.DEBUG: {
                d(TAG_APP, content);
                break;
            }
            case Log.ERROR: {
                e(TAG_APP, content);
                break;
            }
            case Log.INFO: {
                i(TAG_APP, content);
                break;
            }
            case Log.VERBOSE: {
                v(TAG_APP, content);
                break;
            }
            case Log.WARN: {
                w(TAG_APP, content);
                break;
            }
            default: {
                break;
            }
        }
    }
}

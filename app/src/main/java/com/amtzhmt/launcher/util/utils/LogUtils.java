package com.amtzhmt.launcher.util.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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
     *
     * @param paramString
     */
    public static void e(String paramString) {
        if (isLogEnabled) {
            Log.d("[" + TAG_APP + "]", paramString);
        }
    }

    /**
     * 显示各种log信息
     *
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

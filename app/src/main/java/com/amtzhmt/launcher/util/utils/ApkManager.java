package com.amtzhmt.launcher.util.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 静默安装
 */
public class ApkManager {

    private static final String TAG = "chenzhu_ApkManager";
    private static final String INSTALL_CMD = "install";
    private static final String UNINSTALL_CMD = "uninstall";
    private Context context;

    public ApkManager(Context context){
        this.context=context;
    }

    /**
     * APK
     * @param apkPath
     *            APK安装包路径
     * @return true 静默安装成功 false 静默安装失败
     */
    public  boolean install(String apkPath) {
        String[] args = { "pm", INSTALL_CMD, "-r", apkPath };

        try {
            Toast.makeText(context,"开始安装",Toast.LENGTH_LONG).show();
            String result = apkProcess(args);
            LogUtils.i(TAG, "install log:"+result);

            if (result != null  && (result.endsWith("Success") || result.endsWith("Success\n"))) {
                Toast.makeText(context,"安装成功",Toast.LENGTH_LONG).show();
                return true;
            }
            Toast.makeText(context,"安装失败",Toast.LENGTH_LONG).show();
            return false;
        }catch (Exception e){
            Toast.makeText(context,"安装异常"+e,Toast.LENGTH_LONG).show();
            return false;
        }


    }



    /**
     * APK静默安装
     * 
     * @param packageName
     *            需要卸载应用的包名
     * @return true 静默卸载成功 false 静默卸载失败
     */
    public  boolean uninstall(String packageName) {
        String[] args = { "pm", UNINSTALL_CMD, packageName };
        String result = apkProcess(args);
       LogUtils.i(TAG, "uninstall log:"+result);
        if (result != null
                && (result.endsWith("Success") || result.endsWith("Success\n"))) {
            return true;
        }
        return false;
    }

    /**
     * 应用安装、卸载处理
     * 
     * @param args 安装、卸载参数
     * @return Apk安装、卸载结果
     */
    public  String apkProcess(String[] args) {
        String result = null;
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }


    /**
     * The install command installs a package to the system. Options:
     *
     * @command -l: install the package with FORWARD_LOCK.
     * @command -r: reinstall an existing app, keeping its data.
     * @command -t: allow test .apks to be installed.
     * @command -i: specify the installer package name.
     * @command -s: install package on sdcard.
     * @command -f: install package on internal flash.
     */
    /**
     * The uninstall command removes a package from the system. Options:
     *
     * @command -k: keep the data and cache directories around. after the
     *          package removal.
     */
    private String installSilently(String path) {

        // 通过命令行来安装APK
        String[] args = { "pm", "install", "-r", path };
        String result = "";
        // 创建一个操作系统进程并执行命令行操作
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    /**
     * install slient
     *
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
    public static int installSilent(String filePath) {
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return 1;
        }

        String[] args = { "pm", "install", "-r", filePath };
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        java.lang.Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.d(TAG, "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }

}
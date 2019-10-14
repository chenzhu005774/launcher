package com.amtzhmt.launcher.util.utils.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.amtzhmt.launcher.R;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.PromptEntity;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;
import com.xuexiang.xupdate.proxy.IUpdatePrompter;
import com.xuexiang.xupdate.proxy.IUpdateProxy;
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateChecker;
import com.xuexiang.xupdate.service.OnFileDownloadListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2019/6/5.
 * https://github.com/xuexiangjys/XUpdate
 */
public class UpdateUtil {
    public  void  updateApk(final Context context , final Activity activity,String url){
        XUpdate.newBuild(context)
                .updateUrl("http://192.168.2.207:1790/industry-iptv-api/apk/template/getPage?dataCode=002001&orgCode=002")
                .themeColor(context.getResources().getColor(R.color.update_theme_color))
                .updateParser(new CustomUpdateParser()) //设置自定义的版本更新解析器
                .topResId(R.mipmap.bg_update_top)
                .update();
    }


    public class CustomUpdatePrompter implements IUpdatePrompter {

        private Context mContext;

        public CustomUpdatePrompter(Context context) {
            mContext = context;
        }

        @Override
        public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
            showUpdatePrompt(updateEntity, updateProxy);
        }

        /**
         * 显示自定义提示
         *
         * @param updateEntity
         * @param updateProxy
         */
        private void showUpdatePrompt(final @NonNull UpdateEntity updateEntity, final @NonNull IUpdateProxy updateProxy) {
            String updateInfo = UpdateUtils.getDisplayUpdateInfo(mContext, updateEntity);

            new AlertDialog.Builder(mContext)
                    .setTitle(String.format("是否升级到%s版本？", updateEntity.getVersionName()))
                    .setMessage(updateInfo)
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateProxy.startDownload(updateEntity, new OnFileDownloadListener() {
                                @Override
                                public void onStart() {
                                    HProgressDialogUtils.showHorizontalProgressDialog(mContext, "下载进度", false);
                                }

                                @Override
                                public void onProgress(float progress, long total) {
                                    HProgressDialogUtils.setProgress(Math.round(progress * 100));
                                }

                                @Override
                                public boolean onCompleted(File file) {
                                    HProgressDialogUtils.cancel();
                                    return true;
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    HProgressDialogUtils.cancel();
                                }
                            });
                        }
                    })
                    .setNegativeButton("暂不升级", null)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }
    public class CustomUpdateParser implements IUpdateParser {
        @Override
        public UpdateEntity parseJson(String json) throws Exception {
            UpdateInfoBean result = new UpdateInfoBean();
            JSONObject itemcJson = new JSONObject(json);
            if (result != null) {
                return new UpdateEntity()
                        .setHasUpdate(result.getHasUpdate())
                        .setIsIgnorable(result.getIsIgnorable())
                        .setVersionCode(result.getVersionCode())
                        .setVersionName(result.getVersionName())
                        .setUpdateContent(result.getUpdateContent())
                        .setDownloadUrl(result.getDownloadUrl())
                        .setSize(result.getSize());
            }
            return null;
        }
    }
}

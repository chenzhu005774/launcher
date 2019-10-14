package com.amtzhmt.launcher.util.utils.update;

/**
 * Created by Administrator on 2019/6/5.
 */
public class UpdateInfoBean {
    boolean HasUpdate;
    boolean IsIgnorable;
    int VersionCode;
    String VersionName;
    String UpdateContent;
    String DownloadUrl;
    long Size;

    public boolean getHasUpdate() {
        return HasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        HasUpdate = hasUpdate;
    }

    public boolean getIsIgnorable() {
        return IsIgnorable;
    }

    public void setIsIgnorable(boolean isIgnorable) {
        IsIgnorable = isIgnorable;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getUpdateContent() {
        return UpdateContent;
    }

    public void setUpdateContent(String updateContent) {
        UpdateContent = updateContent;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }
}

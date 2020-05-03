package com.shrey.kc.kcui.entities;

import android.util.Log;

import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.io.File;

public class KCDriveFileDownloadRequest extends KCAccessRequest {

    private String remoteId;

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public File getLocalFile() {
        return localFile;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    private File localFile;

    public static KCDriveFileDownloadRequest getDownloadRequest(String remoteId, File localFile) {
        KCDriveFileDownloadRequest req = new KCDriveFileDownloadRequest();
        req.setLocalFile(localFile);
        req.setRemoteId(remoteId);
        Log.i("SIGNIN", "instance " + CurrentUserInfo.getUserInfo());
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        return req;
    }
}

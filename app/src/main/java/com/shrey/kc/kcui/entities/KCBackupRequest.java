package com.shrey.kc.kcui.entities;

import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.io.File;

public class KCBackupRequest extends KCAccessRequest {
    java.io.File theHolyBackup;

    public static KCBackupRequest getBackupRequest(java.io.File theHolyBackup) {
        KCBackupRequest req = new KCBackupRequest();
        req.setTheHolyBackup(theHolyBackup);
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        return req;
    }

    public File getTheHolyBackup() {
        return theHolyBackup;
    }

    public void setTheHolyBackup(File theHolyBackup) {
        this.theHolyBackup = theHolyBackup;
    }

}

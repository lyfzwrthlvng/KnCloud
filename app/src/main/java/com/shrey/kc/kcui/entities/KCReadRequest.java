package com.shrey.kc.kcui.entities;

import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.util.ArrayList;

public class KCReadRequest extends KCAccessRequest {
    public static KCReadRequest constructRequest(ArrayList<String> keywordList) {
        KCReadRequest req = new KCReadRequest();
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        req.setKeywordList(keywordList);
        return req;
    }

    ArrayList<String> keywordList;

    public ArrayList<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(ArrayList<String> list) {
        this.keywordList = list;
    }
}

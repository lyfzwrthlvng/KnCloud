package com.shrey.kc.kcui.entities;

import com.shrey.kc.kcui.objects.CurrentUserInfo;

public class KCWriteRequest extends KCAccessRequest {

    public static KCWriteRequest constructRequest(String value, String keyword) {
        KCWriteRequest req = new KCWriteRequest();
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        req.setValue(value);
        req.setKeyword(keyword);
        return req;
    }

    String keyword;

    String value;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

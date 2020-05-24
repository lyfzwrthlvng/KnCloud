package com.shrey.kc.kcui.entities;

import com.shrey.kc.kcui.objects.CurrentUserInfo;

public class KCUpdateRequest extends KCAccessRequest {
    String keyword;
    String value;
    String newValue;

    public static KCUpdateRequest constructRequest(String value, String keyword, String newValue) {
        KCUpdateRequest req = new KCUpdateRequest();
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        req.setValue(value);
        req.setKeyword(keyword);
        req.setNewValue(newValue);
        return req;
    }

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

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}

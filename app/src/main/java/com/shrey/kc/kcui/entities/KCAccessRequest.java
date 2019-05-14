package com.shrey.kc.kcui.entities;


import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.io.Serializable;

public class KCAccessRequest implements Serializable {
    public static KCAccessRequest constructRequest() {
        KCAccessRequest req = new KCAccessRequest();
        req.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        req.setPassKey("dummy");
        req.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
        return req;
    }
    String userKey;

    String passKey;

    Integer userId;

    public String getUserkey() {
        return userKey;
    }

    public void setUserkey(String key) {
        this.userKey = key;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String key){
        this.passKey = passKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

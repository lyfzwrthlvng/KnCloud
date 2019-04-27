package com.shrey.kc.kcui.entities;


import java.io.Serializable;

public class KCAccessRequest implements Serializable {
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

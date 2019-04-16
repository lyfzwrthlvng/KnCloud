package com.shrey.kc.kcui.objects;

import com.shrey.kc.kcui.entities.User;

public enum CurrentUserInfo {
    INSTANCE;

    User user = null;
    public static CurrentUserInfo getUserInfo() {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

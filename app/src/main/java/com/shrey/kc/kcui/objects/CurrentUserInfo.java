package com.shrey.kc.kcui.objects;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.shrey.kc.kcui.entities.User;

public enum CurrentUserInfo {
    INSTANCE;

    User user = null;
    GoogleAccountCredential authAccountCreds = null;

    public static CurrentUserInfo getUserInfo() {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoogleAccountCredential getAuthAccount() {
        return authAccountCreds;
    }

    public void setAuthAccount(GoogleAccountCredential authAccount) {
        this.authAccountCreds = authAccount;
        authAccountCreds.setSelectedAccount(getUser().getAccountInfo().getAccount());
    }
}

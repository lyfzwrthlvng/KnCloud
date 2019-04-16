package com.shrey.kc.kcui.entities;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class User {
    private String uniqueId;
    private GoogleSignInAccount accountInfo;

    public User(String uniqueId, GoogleSignInAccount gsa) {
        this.uniqueId = uniqueId;
        this.accountInfo = gsa;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public GoogleSignInAccount getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(GoogleSignInAccount accountInfo) {
        this.accountInfo = accountInfo;
    }
}

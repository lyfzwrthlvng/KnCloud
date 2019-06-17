package com.shrey.kc.kcui.objects;

import java.util.ArrayList;

public enum RuntimeDynamicDataHolder {
    INSTANCE;

    ArrayList<String> userTags = null;

    public static RuntimeDynamicDataHolder getRuntimeData() {
        return INSTANCE;
    }

    public ArrayList<String> getUserTags() {
        return userTags;
    }

    public void setUserTags(ArrayList<String> userTags) {
        this.userTags = userTags;
    }

}

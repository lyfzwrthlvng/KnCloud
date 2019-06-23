package com.shrey.kc.kcui.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public ArrayList<String> getUserTagsSorted() {
        /* in background, start constructing a trie of tags here so we can efficiently implement
        *  search as you type? we can add a couple or just 1 letter and show next few matching tags
        *  better than no help \m/
        *  */
        Collections.<String>sort(userTags);
        return userTags;
    }

}

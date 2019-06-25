package com.shrey.kc.kcui.objects;

import com.shrey.kc.kcui.algos.TrieForSearch;

import java.util.ArrayList;
import java.util.Collections;

public enum RuntimeDynamicDataHolder {
    INSTANCE;

    ArrayList<String> userTags;
    TrieForSearch trieForSearch = null;

    public static RuntimeDynamicDataHolder getRuntimeData() {
        return INSTANCE;
    }

    public ArrayList<String> getUserTags() {
        return userTags;
    }

    public void setUserTags(ArrayList<String> userTags) {
        this.userTags = userTags;
        final ArrayList<String> utf = userTags;
        // this perhaps should happen in bg
        // TODO do it via workerActivity and bcast listener etc
        this.trieForSearch = new TrieForSearch(userTags);
    }

    public ArrayList<String> getUserTagsSorted() {
        /* in background, start constructing a trie of tags here so we can efficiently implement
        *  search as you type? we can add a couple or just 1 letter and show next few matching tags
        *  better than no help \m/
        *  */
        Collections.<String>sort(userTags);
        return userTags;
    }

    public ArrayList<String> getAutocompleteWords(String partial) {
        return this.trieForSearch.suggest(partial);
    }

}

package com.shrey.kc.kcui.entities;

import java.util.ArrayList;

public class KCReadRequest extends KCAccessRequest {
    ArrayList<String> keywordList;

    public ArrayList<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(ArrayList<String> list) {
        this.keywordList = list;
    }
}

package com.shrey.kc.kcui.entities;

public class KCWriteRequest extends KCAccessRequest {

    String keyword;

    String value;

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
}

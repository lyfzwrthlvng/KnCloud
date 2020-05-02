package com.shrey.kc.kcui.entities;

import java.io.Serializable;
import java.util.HashMap;

public class NodeResult implements Serializable {

    HashMap<String, Object> result;
    public HashMap<String, Object> getResult() {
        return result;
    }
    public void setResult(HashMap<String, Object> result) {
        this.result = result;
    }
}

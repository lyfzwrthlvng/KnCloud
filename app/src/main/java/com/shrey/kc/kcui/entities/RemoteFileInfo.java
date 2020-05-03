package com.shrey.kc.kcui.entities;

import java.io.Serializable;

public class RemoteFileInfo implements Serializable {

    String name;
    String createdTime;
    String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public RemoteFileInfo(String name, String createdTime, String size, String id) {
        this.name=name;
        this.createdTime=createdTime;
        this.size=size;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

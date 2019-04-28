package com.shrey.kc.kcui.localdb;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Knowledge {

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    @PrimaryKey(autoGenerate = true)
    public long uid;

    @ColumnInfo(name = "knowledge")
    String knowledge;
}

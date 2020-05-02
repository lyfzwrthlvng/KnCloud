package com.shrey.kc.kcui.localdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MetaEntity extends DbEntity {

    @PrimaryKey(autoGenerate = true)
    long uid;

    @ColumnInfo(name = "metaInf")
    String metaInf;

    public String getMetaInf() {
        return this.metaInf;
    }

    public void setMetaInf(String metaInf) {
        this.metaInf = metaInf;
    }

}

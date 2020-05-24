package com.shrey.kc.kcui.localdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

package com.shrey.kc.kcui.localdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        indices = @Index(name = "indexTag", value = {"tag"})
)
public class Tag extends DbEntity {

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @PrimaryKey(autoGenerate = true)
    long uid;

    @ColumnInfo(name = "tag")
    String tag;
}

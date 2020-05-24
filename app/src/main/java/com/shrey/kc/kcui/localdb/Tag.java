package com.shrey.kc.kcui.localdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

package com.shrey.kc.kcui.localdb;

import androidx.room.ColumnInfo;

public class DbEntity {

    public DbEntity() {
        created = System.currentTimeMillis();
        updated = created;
    }

    @ColumnInfo(name = "created", index = true)
    Long created;

    @ColumnInfo(name = "updated", index = true)
    Long updated;

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}

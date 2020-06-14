package com.shrey.kc.kcui.localdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UpdateTrack extends DbEntity {

    public long getUid() {
        return uid;
    }

    public void setKnowledge(long uid) {
        this.uid = uid;
    }

    @PrimaryKey(autoGenerate = true)
    public long uid;
}

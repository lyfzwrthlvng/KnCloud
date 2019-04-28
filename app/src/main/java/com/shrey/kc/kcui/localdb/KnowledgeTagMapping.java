package com.shrey.kc.kcui.localdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(
        indices = {
                @Index(name = "index_tagId", value = {"tagId"}),
                @Index(name = "index_knowledgeId", value = {"knowledgeId"})
        }
)
public class KnowledgeTagMapping {
    @PrimaryKey(autoGenerate = true)
    long uid;

    @ColumnInfo(name = "tagId")
    long tagId;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    @ColumnInfo(name = "knowledgeId")
    long knowledgeId;
}

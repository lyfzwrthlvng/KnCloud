package com.shrey.kc.kcui.localdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        indices = {
                @Index(name = "index_tagId", value = {"tagId"}),
                @Index(name = "index_knowledgeId", value = {"knowledgeId"})
        }
)
public class KnowledgeTagMapping extends DbEntity {
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

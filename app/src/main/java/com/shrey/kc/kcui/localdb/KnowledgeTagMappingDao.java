package com.shrey.kc.kcui.localdb;

import java.util.ArrayList;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

@Dao
public interface KnowledgeTagMappingDao {
    @Query("select knowledgeId from KnowledgeTagMapping where tagId in (:tagIds)")
    public long[] findKnowledgesForTag(long[] tagIds);

    @Query("select updated from KnowledgeTagMapping order by uid desc limit 1")
    public long[] findLatestUpdate();

    @Query("select tagId from KnowledgeTagMapping where knowledgeId in (:knowledgeIds)")
    public long[] findTagsForKnowledge(long[] knowledgeIds);

    @Query("select uid from KnowledgeTagMapping where knowledgeId = :kid and tagId = :tagId")
    public long findMappingIdByTagKnowledge(long kid, long tagId);

    @Insert
    public long[] insertAll(KnowledgeTagMapping... mappings);

    @Query("delete from KnowledgeTagMapping where uid =:mapping")
    public void deleteById(long mapping);

}

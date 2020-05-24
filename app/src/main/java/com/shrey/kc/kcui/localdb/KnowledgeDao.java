package com.shrey.kc.kcui.localdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;


@Dao
public interface KnowledgeDao {
    @Query("select knowledge from Knowledge where uid in (:uids)")
    public String[] getById(long[] uids);

    @Query("select uid from Knowledge where knowledge in (:knowledge)")
    public long[] findKnowledgeIds(String[] knowledge);

    @Insert
    public long[] insertAll(Knowledge... knowledges);

    @Query("delete from Knowledge where uid = :knowledgeId")
    public void deleteById(long knowledgeId);

    @Query("update Knowledge set knowledge = :newString where uid = :uid")
    public void updateKnowledgeString(long uid, String newString);
}

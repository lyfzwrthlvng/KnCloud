package com.shrey.kc.kcui.localdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;


@Dao
public interface KnowledgeDao {
    @Query("select knowledge from Knowledge where uid in (:uids)")
    public String[] getById(long[] uids);

    @Query("select uid from Knowledge where knowledge in (:knowledge)")
    public long[] findKnowledgeIds(String[] knowledge);

    @Insert
    public long[] insertAll(Knowledge... knowledges);

    @Delete
    public void deleteById(long knowledgeId);
}

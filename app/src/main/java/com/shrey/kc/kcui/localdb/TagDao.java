package com.shrey.kc.kcui.localdb;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TagDao {
    @Query("select uid from Tag where tag in (:tags)")
    public long[] findTagIds(String[] tags);

    @Insert
    public long[] insertAll(Tag... tags);
}

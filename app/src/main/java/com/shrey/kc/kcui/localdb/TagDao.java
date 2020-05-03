package com.shrey.kc.kcui.localdb;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TagDao {
    @Query("select uid from Tag where tag in (:tags)")
    public long[] findTagIds(String[] tags);

    @Query("select tag from Tag")
    public String[] findTags();

    @Query("select tag from Tag where uid in (:uids)")
    public String[] findTagsForIds(long[] uids);

    @Insert
    public long[] insertAll(Tag... tags);

    @Query("delete from Tag where uid = :tagId")
    public void deleteById(long tagId);
}

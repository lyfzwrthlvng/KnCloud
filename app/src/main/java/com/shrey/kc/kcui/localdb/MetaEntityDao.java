package com.shrey.kc.kcui.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MetaEntityDao {
    @Query("select * from MetaEntity")
    public MetaEntity[] getLatest();

    @Insert
    public long[] insertAll(MetaEntity... entity);

    @Update
    public int update(MetaEntity... entity);
}

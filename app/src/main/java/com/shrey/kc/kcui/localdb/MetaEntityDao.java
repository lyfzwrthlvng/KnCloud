package com.shrey.kc.kcui.localdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface MetaEntityDao {
    @Query("select * from MetaEntity")
    public MetaEntity[] getLatest();

    @Insert
    public long[] insertAll(MetaEntity... entity);

    @Update
    public int update(MetaEntity... entity);
}
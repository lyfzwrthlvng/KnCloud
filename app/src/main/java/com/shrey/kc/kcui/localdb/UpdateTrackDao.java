package com.shrey.kc.kcui.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UpdateTrackDao {
    @Insert
    public long[] insertAll(UpdateTrack... updateTracks);

    @Query("select created from UpdateTrack order by created desc limit 1")
    public long[] getLatest();
}

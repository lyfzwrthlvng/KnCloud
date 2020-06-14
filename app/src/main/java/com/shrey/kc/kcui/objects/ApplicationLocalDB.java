package com.shrey.kc.kcui.objects;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.shrey.kc.kcui.localdb.Knowledge;
import com.shrey.kc.kcui.localdb.KnowledgeDao;
import com.shrey.kc.kcui.localdb.KnowledgeTagMapping;
import com.shrey.kc.kcui.localdb.KnowledgeTagMappingDao;
import com.shrey.kc.kcui.localdb.MetaEntity;
import com.shrey.kc.kcui.localdb.MetaEntityDao;
import com.shrey.kc.kcui.localdb.Tag;
import com.shrey.kc.kcui.localdb.TagDao;
import com.shrey.kc.kcui.localdb.UpdateTrack;
import com.shrey.kc.kcui.localdb.UpdateTrackDao;


@Database(
        entities = {Tag.class, Knowledge.class, KnowledgeTagMapping.class, MetaEntity.class, UpdateTrack.class},
        version = 4
)
public abstract class ApplicationLocalDB extends RoomDatabase {

    public abstract TagDao tagDao();
    public abstract KnowledgeDao knowledgeDao();
    public abstract KnowledgeTagMappingDao knowledgeTagMappingDao();
    public abstract MetaEntityDao metaEntityDao();
    public abstract UpdateTrackDao updateTrackDao();
}

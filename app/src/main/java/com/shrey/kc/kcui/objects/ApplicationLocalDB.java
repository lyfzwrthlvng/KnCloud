package com.shrey.kc.kcui.objects;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.shrey.kc.kcui.localdb.Knowledge;
import com.shrey.kc.kcui.localdb.KnowledgeDao;
import com.shrey.kc.kcui.localdb.KnowledgeTagMapping;
import com.shrey.kc.kcui.localdb.KnowledgeTagMappingDao;
import com.shrey.kc.kcui.localdb.Tag;
import com.shrey.kc.kcui.localdb.TagDao;


@Database(
        entities = {Tag.class, Knowledge.class, KnowledgeTagMapping.class},
        version = 2
)
public abstract class ApplicationLocalDB extends RoomDatabase {

    public abstract TagDao tagDao();
    public abstract KnowledgeDao knowledgeDao();
    public abstract KnowledgeTagMappingDao knowledgeTagMappingDao();
}

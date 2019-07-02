package com.shrey.kc.kcui.objects;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;


public enum LocalDBHolder {
    INSTANCE;

    ApplicationLocalDB localDB;
    File databasePath;

    public ApplicationLocalDB getSetLocalDB(Context applicationContext) {
        if(localDB == null) {
            /*
            localDB = Room.databaseBuilder(applicationContext, ApplicationLocalDB.class,
                    "local-kc-db").build();
                    */
            Migration roomMigration = new Migration(1,2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("alter table Knowledge add column created INTEGER default 1561314899");
                    database.execSQL("alter table Knowledge add column updated INTEGER default 1561314899");

                    database.execSQL("alter table KnowledgeTagMapping add column created INTEGER default 1561314899");
                    database.execSQL("alter table KnowledgeTagMapping add column updated INTEGER default 1561314899");

                    database.execSQL("alter table Tag add column created INTEGER default 1561314899");
                    database.execSQL("alter table Tag add column updated INTEGER default 1561314899");

                }
            };

            Migration roomMigration23 = new Migration(2,3) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                   /*
                    database.execSQL("create index created_index on Knowledge (created)");
                    database.execSQL("create index updated_index on Knowledge (updated)");

                    database.execSQL("create index created_index on KnowledgeTagMapping (created)");
                    database.execSQL("create index updated_index on KnowledgeTagMapping (updated)");

                    database.execSQL("create index created_index on Tag (created)");
                    database.execSQL("create index updated_index on Tag (updated)");
                    */
                   // room takes care of the above, below, creating a new table
                    database.execSQL("CREATE TABLE IF NOT EXISTS MetaEntity(uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "created INTEGER default 1561314899," +
                            "updated INTEGER default 1561314899," +
                            "metaInf TEXT)");
                }
            };
            localDB = Room.databaseBuilder(applicationContext,ApplicationLocalDB.class,"local-kc-db").addMigrations(roomMigration, roomMigration23).build();
        }
        return localDB;
    }

    public void setDatabasePath(File path) {
        this.databasePath = path;
    }

    public ApplicationLocalDB getLocalDB() {
        return localDB;
    }

    public File getDatabasePath() {
        return databasePath;
    }

}

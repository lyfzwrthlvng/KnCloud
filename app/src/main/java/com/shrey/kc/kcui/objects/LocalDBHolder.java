package com.shrey.kc.kcui.objects;

import androidx.room.Room;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.util.Log;

import com.shrey.kc.kcui.adaptors.DriveBackup;
import com.shrey.kc.kcui.executors.DownloadDriveBackupExecutor;

import java.io.File;


public enum LocalDBHolder {
    INSTANCE;

    ApplicationLocalDB localDB;
    File databasePath;
    boolean initWell;

    public ApplicationLocalDB getSetLocalDB(Context applicationContext, boolean update) {
        if(localDB == null || update==true) {
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

            if(update==true) {
                Log.d(LocalDBHolder.class.getName(), "updating...");
                localDB.close();
            }
            localDB = Room.databaseBuilder(applicationContext,ApplicationLocalDB.class,"local-kc-db").addMigrations(roomMigration, roomMigration23).build();

            //new DownloadDriveBackupExecutor().executeRequest();
        }
        return localDB;
    }

    public void setDatabasePath(File path) {
        this.databasePath = path;
    }

    public ApplicationLocalDB getLocalDB() {
        return localDB;
    }

    public void setInitWell(boolean initWell) {
        this.initWell = initWell;
    }

    public boolean getInitWell() {
        return this.initWell;
    }

    public File getDatabasePath() {
        return databasePath;
    }

}

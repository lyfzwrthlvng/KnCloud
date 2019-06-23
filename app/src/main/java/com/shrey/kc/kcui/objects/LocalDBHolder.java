package com.shrey.kc.kcui.objects;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;


public enum LocalDBHolder {
    INSTANCE;

    ApplicationLocalDB localDB;

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
            localDB = Room.databaseBuilder(applicationContext,ApplicationLocalDB.class,"local-kc-db").addMigrations(roomMigration).build();
        }
        return localDB;
    }

    public ApplicationLocalDB getLocalDB() {
        return localDB;
    }

}

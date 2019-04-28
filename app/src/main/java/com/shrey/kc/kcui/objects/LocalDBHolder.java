package com.shrey.kc.kcui.objects;

import android.arch.persistence.room.Room;
import android.content.Context;


public enum LocalDBHolder {
    INSTANCE;

    ApplicationLocalDB localDB;

    public ApplicationLocalDB getSetLocalDB(Context applicationContext) {
        if(localDB == null) {
            localDB = Room.databaseBuilder(applicationContext, ApplicationLocalDB.class,
                    "local-kc-db").build();
        }
        return localDB;
    }

    public ApplicationLocalDB getLocalDB() {
        return localDB;
    }

}

package com.blinkfast.drivedatastore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractDriveDao {

    private ConnectionKeeper connectionKeeper;
    Gson mapper;

    public AbstractDriveDao(ConnectionKeeper connectionKeeper) {
        this.connectionKeeper = connectionKeeper;
        this.mapper = new GsonBuilder().create();
    }

    public boolean persist(DriveStoreEntity entity) {
        if(connectionKeeper != null) {
            entity.getEntityName();
            String jsonEntity = mapper.toJson(entity);
        }
    return false;
    }

    public void sync(DriveStoreEntity entity) {
        if(connectionKeeper != null) {
            entity.getEntityName();
            String jsonEntity = mapper.toJson(entity);

        }
    }

}

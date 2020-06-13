package com.blinkfast.drivedatastore;

import java.io.Serializable;

/*
* An entity that is stored on the drive datastore
* extends Serializable since we'll serialize and store
* */
public interface DriveStoreEntity extends Serializable {
    public String getEntityName();
}

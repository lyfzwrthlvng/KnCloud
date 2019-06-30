package com.shrey.kc.kcui.adaptors;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.shrey.kc.kcui.objects.LocalDBHolder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class DriveBackup {
    private final Drive mDriveService;

    public DriveBackup(Drive driveService) {
        mDriveService = driveService;
    }

    public boolean createFileOnDrive(java.io.File localDb, String remoteName) {

        boolean backedUp = false;
        try {
            FileInputStream stream = new FileInputStream(localDb);
            File theHolyBackup = new File()
                    .setParents(Collections.singletonList("root"))
                    .setName(remoteName);
            File onlineFile = mDriveService.files().create(theHolyBackup).execute();
            String fileId = onlineFile.getId();
            //AbstractInputStreamContent sc =
            FileContent fc = new FileContent("notsure", localDb);
            mDriveService.files().update(fileId, theHolyBackup, fc);
            backedUp = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return backedUp;

    }
}

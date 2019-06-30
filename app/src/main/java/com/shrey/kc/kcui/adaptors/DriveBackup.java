package com.shrey.kc.kcui.adaptors;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.FileInputStream;
import java.io.IOException;
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
                    .setName(remoteName)
                    .setMimeType("application/octet-stream");
            File onlineFile = mDriveService.files().create(theHolyBackup).execute();
            String fileId = onlineFile.getId();
            //AbstractInputStreamContent sc =
            FileContent fc = new FileContent("application/octet-stream", localDb);
            mDriveService.files().update(fileId, theHolyBackup, fc);
            backedUp = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return backedUp;

    }
}

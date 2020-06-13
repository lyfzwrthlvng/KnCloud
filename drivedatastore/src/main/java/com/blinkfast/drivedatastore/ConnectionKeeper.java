package com.blinkfast.drivedatastore;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class ConnectionKeeper {

    private static Drive driveService;
    private static ConnectionKeeper keeperInstance;
    private static String DB_FILE_PREFIX = "DDSFILE-";
    private FileContent localFileContent;
    private com.google.api.services.drive.model.File remoteFile;

    public static ConnectionKeeper getConnection(String applicationName,
                                                 GoogleAccountCredential googleAccountCredential) {
        if(keeperInstance == null) {
            keeperInstance = new ConnectionKeeper(applicationName, googleAccountCredential);
        }
        return keeperInstance;
    }

    private ConnectionKeeper(String applicationName, GoogleAccountCredential googleAccountCredential) {
        if(driveService == null) {
            driveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(), googleAccountCredential)
                    .setApplicationName("knowledgeCloud")
                    .build();
        }
    }

    public static void setDriveService(Drive driveService) {
        //ConnectionKeeper.driveService = driveService;
    }

    public boolean createOrGetDbFile(String dbName) throws IOException {
        String fullFileName = DB_FILE_PREFIX + dbName;
        if(localFileContent ==null) {
            File localFile = new File(fullFileName);
            localFileContent = new FileContent("application/json", localFile);
            com.google.api.services.drive.model.File remoteFileT =
                    new com.google.api.services.drive.model.File()
                    .setParents(Collections.singletonList("root"))
                    .setName(fullFileName)
                    .setMimeType("application/json");
            remoteFile = driveService.files().create(remoteFileT, localFileContent).execute();
            Log.d(ConnectionKeeper.class.getName(), "Created remote and local files");
        }
        return true;
    }

}

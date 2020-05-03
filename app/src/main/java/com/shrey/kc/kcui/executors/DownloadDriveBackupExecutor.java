package com.shrey.kc.kcui.executors;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.shrey.kc.kcui.adaptors.DriveBackup;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCDriveFileDownloadRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.entities.RemoteFileInfo;
import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class DownloadDriveBackupExecutor implements GenericExecutor {
    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        KCDriveFileDownloadRequest drequest = (KCDriveFileDownloadRequest)request;
        DriveBackup backupAdaptor = null;
        if(backupAdaptor == null) {
            /*
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            this, Collections.singleton(DriveScopes.DRIVE_FILE));
                            */
            Drive googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(), CurrentUserInfo.INSTANCE.getAuthAccount()).setApplicationName("knowledgeCloud").build();
            backupAdaptor = new DriveBackup(googleDriveService);
        }
        String remoteName = "knowledgeCloud.sqlitedb";
        List<RemoteFileInfo> rmi = backupAdaptor.downloadRemoteFileList(remoteName);
        RemoteFileInfo largestFile = null;
        long largestSize = 0;
        for(RemoteFileInfo rmi1: rmi) {
            if(Long.parseLong(rmi1.getSize()) > largestSize) {
                largestSize = Long.parseLong(rmi1.getSize());
                largestFile = rmi1;
                Log.d("yoyo ", rmi1.getName());
            }
        }

        backupAdaptor.overwriteLocally(largestFile.getId(), drequest.getLocalFile());
        //Room.databaseBuilder()
        return null;
    }
}

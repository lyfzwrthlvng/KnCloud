package com.shrey.kc.kcui.executors;

import androidx.room.Room;
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
        RemoteFileInfo validFile = pickMostEligibleFile(rmi);

        if(validFile!=null) {
            backupAdaptor.overwriteLocally(validFile.getId(), drequest.getLocalFile());
        } else {
            Log.d("yoyo", "No valid backup file found!");
        }
        return null;
    }

    private RemoteFileInfo pickMostEligibleFile(List<RemoteFileInfo> list) {
        String latestCreatedTime = "0";
        RemoteFileInfo latestValidFile = null; // valid => non zero size
        for(RemoteFileInfo rf: list) {
            Log.d("yoyo" , "this time " + rf.getCreatedTime());
            if(rf.getCreatedTime().compareTo(latestCreatedTime) > 0) {
                if(Long.parseLong(rf.getSize()) > 0) {
                    latestCreatedTime = rf.getCreatedTime();
                    latestValidFile = rf;
                    Log.d("yoyo ", rf.getName() + " size " + rf.getSize());
                }
            }
        }
        return latestValidFile;
    }
}

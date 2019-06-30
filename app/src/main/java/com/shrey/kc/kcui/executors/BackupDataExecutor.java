package com.shrey.kc.kcui.executors;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.shrey.kc.kcui.adaptors.DriveBackup;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCBackupRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;

import org.w3c.dom.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

public class BackupDataExecutor implements GenericExecutor {

    DriveBackup backupAdaptor = null;

    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        if(backupAdaptor == null) {
            /*
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(
                            this, Collections.singleton(DriveScopes.DRIVE_FILE));
                            */
            Drive googleDriveService = new Drive(AndroidHttp.newCompatibleTransport(),
            JacksonFactory.getDefaultInstance(),CurrentUserInfo.INSTANCE.getAuthAccount());
            backupAdaptor = new DriveBackup(googleDriveService);
        }
        // expecting it to be set init
        java.io.File localDb = LocalDBHolder.INSTANCE.getDatabasePath();
        String remoteName = "knowledgeCloud.backup";
        KCBackupRequest kcBackupRequest = (KCBackupRequest) request;
        boolean backupResult = backupAdaptor.createFileOnDrive(kcBackupRequest.getTheHolyBackup(), remoteName);
        NodeResult result = new NodeResult();
        HashMap<String, Object> bingo = null;
        if(backupResult) {
            bingo = new HashMap<>();
            bingo.put("backupResult", "true");
        }
        result.setResult(bingo);
        return result;
    }
}

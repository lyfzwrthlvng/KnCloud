package com.shrey.kc.kcui.executors;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.shrey.kc.kcui.adaptors.DriveBackup;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCBackupRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.localdb.MetaEntity;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
            Drive googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),CurrentUserInfo.INSTANCE.getAuthAccount()).setApplicationName("knowledgeCloud").build();
            backupAdaptor = new DriveBackup(googleDriveService);
        }
        //just before we start upload, let's get time of update
        //inside upload, we check if uploaded time was greated than this for idempotency

        long[] updates = LocalDBHolder.INSTANCE.getLocalDB().knowledgeTagMappingDao().findLatestUpdate();
        if(updates == null || updates.length == 0) {
            NodeResult nr = new NodeResult();
            nr.setResult(new HashMap<String, Object>());
            nr.getResult().put("backupResult", "noop");
            return nr;
        }
        long backupDataTime = updates[0];
        // expecting it to be set init
        java.io.File localDb = LocalDBHolder.INSTANCE.getDatabasePath();
        String remoteName = "knowledgeCloud.sqlitedb";
        KCBackupRequest kcBackupRequest = (KCBackupRequest) request;
        boolean backupResult = backupAdaptor.uploadResumable(kcBackupRequest.getTheHolyBackup(), remoteName, backupDataTime);
        NodeResult result = new NodeResult();
        HashMap<String, Object> bingo = null;
        if(backupResult) {
            bingo = new HashMap<>();
            bingo.put("backupResult", "true");
        }
        result.setResult(bingo);
        if(backupResult) {
            // update in db
            long[] prev = LocalDBHolder.INSTANCE.getLocalDB().metaEntityDao().getLatest();
            if(prev == null || prev.length == 0) {
                // never uploaded!
                MetaEntity entity = new MetaEntity();
                entity.setMetaInf("doesn't really matter as of now");
                entity.setUpdated(backupDataTime);
                entity.setCreated(backupDataTime);
                prev = LocalDBHolder.INSTANCE.getLocalDB().metaEntityDao().insertAll(entity);
                if(prev == null) {
                    result.getResult().put("backupResult", "failed while inserting locally");
                }
            } else {
                if(prev[0] < backupDataTime) {
                    MetaEntity entity = new MetaEntity();
                    entity.setMetaInf("doesn't really matter as of now");
                    entity.setUpdated(backupDataTime);
                    entity.setCreated(backupDataTime);
                    int updated = LocalDBHolder.INSTANCE.getLocalDB().metaEntityDao().update(entity);
                    if(updated == 0) {
                        result.getResult().put("backupResult", "failed while updating locally");
                    }
                }
            }
        }
        return result;
    }
}

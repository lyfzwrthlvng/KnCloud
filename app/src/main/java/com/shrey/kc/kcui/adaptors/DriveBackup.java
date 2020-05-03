package com.shrey.kc.kcui.adaptors;

import android.util.Log;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.shrey.kc.kcui.entities.RemoteFileInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            FileContent fc = new FileContent("application/octet-stream", localDb);

            File onlineFile = mDriveService.files().create(theHolyBackup, fc).execute();
            Log.i(DriveBackup.class.getName(), "fc length: " + fc.getLength() + " : of size: " + onlineFile.getSize());
            String fileId = onlineFile.getId();
            //AbstractInputStreamContent sc =

            //mDriveService.files().update(fileId, theHolyBackup, fc);
            backedUp = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return backedUp;
    }

    public List<RemoteFileInfo> downloadRemoteFileList(String remoteName) {
        List<RemoteFileInfo> infos = new ArrayList<>();
        try {
            FileList checkFile = mDriveService.files().list()
                    .setFields("files(id,name,modifiedTime)")
                    .execute();

            File remoteFile = null;
            for (File dfile : checkFile.getFiles()) {
                if(dfile.getName().equalsIgnoreCase(remoteName)) {
                    infos.add(new RemoteFileInfo(dfile.getName(), dfile.getCreatedTime().toString(), dfile.getSize().toString(), dfile.getId()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    public void overwriteLocally(String id) {
        try {
            InputStream stream = mDriveService.files().get(id).executeMediaAsInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            java.io.File localDbFile = LocalDBHolder.INSTANCE.getDatabasePath();
            OutputStream os = new FileOutputStream(localDbFile);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(reader.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean uploadResumable(java.io.File localDb, String remoteName, long dataUpdateTime) {

        final ArrayList<Boolean> uploadStatus = new ArrayList<>();
        try {
            String remoteDoneName = remoteName;
            FileList checkFile = mDriveService.files().list()
                    .setFields("files(id,name,modifiedTime)")
                    .execute();
            File remoteFile = null;
            for (File dfile : checkFile.getFiles()) {
                if (dfile.getName().equalsIgnoreCase(remoteDoneName)) {
                    remoteFile = dfile;
                    break;
                }
            }

            FileContent fc = new FileContent("application/octet-stream", localDb);
            if (remoteFile == null) {
                Log.i(DriveBackup.class.getName(), "Uploading file first time....");
                File theHolyBackup = new File()
                        .setParents(Collections.singletonList("root"))
                        .setName(remoteName)
                        .setMimeType("application/octet-stream"); // in bytes
                Drive.Files.Create request = mDriveService.files().create(theHolyBackup, fc);
                request.getMediaHttpUploader().setProgressListener(new MediaHttpUploaderProgressListener() {
                    @Override
                    public void progressChanged(MediaHttpUploader uploader) throws IOException {
                        switch (uploader.getUploadState()) {
                            case MEDIA_COMPLETE:
                                uploadStatus.add(true);
                                break;
                            case NOT_STARTED:
                                break;
                            case MEDIA_IN_PROGRESS:
                                Log.d(DriveBackup.class.getName(), "backup in progress...");
                                break;
                            default:
                                break;
                        }
                        Log.d(DriveBackup.class.getName(),
                                (uploader.getUploadState() == MediaHttpUploader.UploadState.MEDIA_COMPLETE) ? "complete" : "in progress...");
                    }
                });
                File uploadedFile = request.execute();
                //uploadedFile = mDriveService.files().update(uploadedFile.getId(), uploadedFile).execute();

            } else if(remoteFile.getModifiedTime().getValue() < dataUpdateTime /*upload is older than data*/) {
                Log.i(DriveBackup.class.getName(), "Uploading file umpteenth time....");
                File dummy = new File();
                //dummy.setId(remoteFile.getId());
                Drive.Files.Update request = mDriveService.files().update(remoteFile.getId(), dummy, fc);
                request.getMediaHttpUploader().setProgressListener(new MediaHttpUploaderProgressListener() {
                    @Override
                    public void progressChanged(MediaHttpUploader uploader) throws IOException {
                        switch (uploader.getUploadState()) {
                            case MEDIA_COMPLETE:
                                uploadStatus.add(true);
                                break;
                            case NOT_STARTED:
                                break;
                            case MEDIA_IN_PROGRESS:
                                Log.d(DriveBackup.class.getName(), "backup in progress...");
                                break;
                            default:
                                break;
                        }
                        Log.d(DriveBackup.class.getName(),
                                (uploader.getUploadState() == MediaHttpUploader.UploadState.MEDIA_COMPLETE) ? "complete" : "in progress...");
                    }
                });
                File uploadedFile = request.execute();
            }else {
                uploadStatus.add(false);
                Log.i(DriveBackup.class.getName(), "latest file already uploaded!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploadStatus.size() == 1;
    }
}

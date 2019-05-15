package com.shrey.kc.kcui.executors;

import android.util.Log;

import com.shrey.kc.kcui.adaptors.OfflineDBAccessor;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class GetAllTagsExecutorLocal implements GenericExecutor {
    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Log.i(GetAllTagsExecutorLocal.class.getName(),"fetching all tags for " + request.getUserkey());
        return OfflineDBAccessor.getAllTags(request);
    }
}

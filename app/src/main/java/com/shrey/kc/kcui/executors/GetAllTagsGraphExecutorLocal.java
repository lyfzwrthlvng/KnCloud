package com.shrey.kc.kcui.executors;

import android.util.Log;

import com.shrey.kc.kcui.adaptors.OfflineDBAccessor;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.blox.graphview.Graph;

public class GetAllTagsGraphExecutorLocal implements GenericExecutor {
    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Log.i(GetAllTagsGraphExecutorLocal.class.getName(),"fetching all tags graph for " + request.getUserkey());
        NodeResult result = OfflineDBAccessor.getAllTagsGraph(request);
        return result;
    }
}

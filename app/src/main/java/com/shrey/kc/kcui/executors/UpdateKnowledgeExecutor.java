package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.adaptors.OfflineDBAccessor;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCUpdateRequest;
import com.shrey.kc.kcui.entities.NodeResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class UpdateKnowledgeExecutor implements GenericExecutor {
    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        OfflineDBAccessor.updateKnowledge((KCUpdateRequest) request);
        return new NodeResult();
    }
}

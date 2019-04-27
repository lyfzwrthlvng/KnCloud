package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.adaptors.ServerCaller;
import com.shrey.kc.kcui.adaptors.ServerCallerProvider;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.workerActivities.GenericCallbackHelper;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class GetKnowledgeExecutor implements GenericExecutor {

    private ServerCaller serverCaller;
    private String endpoint;

    @Inject
    public GetKnowledgeExecutor(ServerCaller serverCaller, String endpoint) {
        this.serverCaller = serverCaller;
        this.endpoint = endpoint;
    }

    public ServerCaller getServerCaller() {
        return serverCaller;
    }

    @Inject
    public void setServerCaller(ServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException {
        URL backend = new URL(endpoint);
        return serverCaller.nonBlockingServerCall(backend, "POST", request);
    }
}

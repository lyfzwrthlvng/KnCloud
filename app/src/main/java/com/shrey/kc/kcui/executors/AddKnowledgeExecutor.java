package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.adaptors.ServerCaller;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class AddKnowledgeExecutor implements GenericExecutor {

    private ServerCaller serverCaller;
    private String endpoint;

    @Inject
    public AddKnowledgeExecutor(ServerCaller serverCaller, String endpoint) {
        this.serverCaller = serverCaller;
        this.endpoint = endpoint;
    }

    @Override
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException {
        URL url = new URL(endpoint);
        return serverCaller.nonBlockingServerCall(url, "POST", request);
    }
}

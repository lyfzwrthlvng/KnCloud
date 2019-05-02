package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.adaptors.ServerCaller;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

public class FetchSuggestedTagsExecutor implements GenericExecutor {
    private ServerCaller serverCaller;
    private String endpoint;

    @Inject
    public FetchSuggestedTagsExecutor(ServerCaller serverCaller, String endpoint) {
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
    public NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        URL backend = new URL(endpoint);
        return serverCaller.nonBlockingServerCall(backend, "POST", request);
    }
}

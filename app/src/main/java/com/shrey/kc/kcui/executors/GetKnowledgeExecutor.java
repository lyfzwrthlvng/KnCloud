package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.adaptors.ServerCaller;
import com.shrey.kc.kcui.adaptors.ServerCallerProvider;

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
    public Map<String, Object> executeRequest(Map<String, Object> request) throws IOException, ExecutionException, InterruptedException {
        URL backend = new URL(endpoint);
        return serverCaller.nonBlockingServerCall(backend, "POST", request);
    }
}

package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.adaptors.ServerCaller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
    public Map<String, Object> executeRequest(Map<String, Object> request) throws IOException, ExecutionException, InterruptedException {
        URL url = new URL(endpoint);
        Map<String, Object> response = serverCaller.nonBlockingServerCall(url, "POST", request);
        // if above works, we've written!
        return null;
    }
}

package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.adaptors.ServerCaller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class AddKnowledgeExecutor implements GenericExecutor {

    private ServerCaller serverCaller;

    @Inject
    public AddKnowledgeExecutor(ServerCaller serverCaller) {
        this.serverCaller = serverCaller;
    }

    @Override
    public Map<String, Object> executeRequest(Map<String, Object> request) throws IOException, ExecutionException, InterruptedException {
        URL url = new URL("http://192.168.0.3:9090/write/kc");
        Map<String, Object> response = serverCaller.nonBlockingServerCall(url, "POST", request);
        // if above works, we've written!
        return null;
    }
}

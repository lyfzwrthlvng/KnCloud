package com.shrey.kc.kcui.executors;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.workerActivities.GenericCallbackHelper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface GenericExecutor {
    NodeResult executeRequest(KCAccessRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException;
}

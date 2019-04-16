package com.shrey.kc.kcui.executors;

import java.io.IOException;
import java.util.Map;

public interface GenericExecutor {
    Map<String, Object> executeRequest(Map<String, Object> request) throws IOException;
}

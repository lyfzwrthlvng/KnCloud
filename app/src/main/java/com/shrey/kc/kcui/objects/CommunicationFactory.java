package com.shrey.kc.kcui.objects;

import com.shrey.kc.kcui.executors.GenericExecutor;

import java.util.HashMap;

public enum CommunicationFactory {
    INSTANCE;
    public static CommunicationFactory getInstance() {
        return INSTANCE;
    }

    private final HashMap<String, GenericExecutor> registry = new HashMap<>();
    public void register(String task, GenericExecutor executor) {
        registry.put(task, executor);
    }

    public GenericExecutor getExecutor(String task) {
        return registry.get(task);
    }
}

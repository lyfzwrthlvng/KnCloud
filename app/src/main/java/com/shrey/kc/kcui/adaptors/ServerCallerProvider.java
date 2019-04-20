package com.shrey.kc.kcui.adaptors;

import javax.inject.Provider;

public class ServerCallerProvider implements Provider {
    @Override
    public Object get() {
        return new ServerCaller();
    }
}

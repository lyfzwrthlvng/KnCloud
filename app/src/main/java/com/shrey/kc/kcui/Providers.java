package com.shrey.kc.kcui;

import com.shrey.kc.kcui.adaptors.ServerCaller;

import dagger.Module;
import dagger.Provides;

@Module
public class Providers {

    @Provides
    static ServerCaller getServerCaller() {
        return new ServerCaller();
    }
}

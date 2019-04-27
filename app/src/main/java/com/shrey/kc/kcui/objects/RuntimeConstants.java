package com.shrey.kc.kcui.objects;

import android.os.Build;

public enum RuntimeConstants {
    INSTANCE;
    public final int START_ACTIVITY_FOR_TAGS = 99;
    public final int START_ACTIVITY_FOR_KNOWLEDGE = 98;

    public final int STARTED_ACTIVITY_RESULT_NOOP = 68;
    public final int STARTED_ACTIVITY_RESULT_GOOD = 69;

    public final boolean IS_EMULATOR = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);

}

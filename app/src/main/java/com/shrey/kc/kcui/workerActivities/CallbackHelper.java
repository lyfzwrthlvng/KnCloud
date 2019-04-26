package com.shrey.kc.kcui.workerActivities;

import android.content.Context;
import android.view.View;

import java.util.HashMap;

public interface CallbackHelper {
    public void callbackWithResult(HashMap<String, Object> result, View viewToUpdate);
}

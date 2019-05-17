package com.shrey.kc.kcui.activities;

import android.support.v7.app.AppCompatActivity;

import com.shrey.kc.kcui.entities.NodeResult;

public abstract class KCUIActivity extends AppCompatActivity {

    // to be implemented to do something with the result
    public abstract void handleBroadcastResult(NodeResult result, String action);
}

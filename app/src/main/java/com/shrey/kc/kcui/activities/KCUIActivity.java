package com.shrey.kc.kcui.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shrey.kc.kcui.entities.NodeResult;

public abstract class KCUIActivity extends AppCompatActivity {

    // to be implemented to do something with the result
    public abstract void handleBroadcastResult(NodeResult result, String action);

    protected void makeToastOfFailure() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Com breakdown with server :( \nTry in a little while maybe?",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.e("ServiceBroadcastListener", "FAILED COM WITH SERVER!");
    }

    protected void makeToastOfSuccess(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}

package com.shrey.kc.kcui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

public class KnowledgeDetails extends KCUIActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_DELETE_KNOWLEDGE);
        registerReceiver(serviceBcastReceiver, intentFilter);
        fillUpText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d(LoggedInAddKnowledge.class.getName(), "back");
        // finish without anything
        //Intent knowledgeResultIntent = new Intent();
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    private void fillUpText() {
        Intent intent = getIntent();
        String knowledge = intent.getStringExtra("knowledge");
        String tag = intent.getStringExtra("tag");
        TextView tv = findViewById(R.id.text_knowledge_details);
        tv.setText(knowledge);

        Button deleteButton = findViewById(R.id.delete_knowledge_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(KnowledgeDetails.class.getName(), "clicked on delete!");
                KCWriteRequest deleteRequest = new KCWriteRequest();
                // deleteRequest.setKeyword(); TODO, pass tag info as well to this page
                deleteRequest.setValue(knowledge);
                deleteRequest.setKeyword(tag);
                AsyncCall.startActionDelete(getApplicationContext(), deleteRequest);
            }
        });
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {
        // go back to the home page?!
        Log.d(KnowledgeDetails.class.getName(), "deletion done, going back!");
        makeToastOfSuccess("Knowledge deleted!");
        Intent homeIntent = new Intent(this, LoggedInHomeOne.class);
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(serviceBcastReceiver);
        super.onStop();
    }
}

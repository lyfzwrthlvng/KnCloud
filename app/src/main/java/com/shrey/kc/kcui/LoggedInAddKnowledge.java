package com.shrey.kc.kcui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;

public class LoggedInAddKnowledge extends KCUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logged_in_add_knowledge);
        setListeners();

        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void setListeners() {
        Button atb = findViewById(R.id.add_tag_text_button);
        final EditText kt = findViewById(R.id.add_tag_text);
        atb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddKnowledge", kt.getText().toString());
                // Send it to tag suggestions, we'll receive broadcast on next activity
                KCWriteRequest writeRequest = new KCWriteRequest();
                writeRequest.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
                writeRequest.setPassKey("dummy");
                writeRequest.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
                //AsyncCall.startActionFetchTags(getApplicationContext(),
                //        KCWriteRequest.constructRequest(kt.getText().toString(), ""));
                // start an activity to tags for this knowledge
                Intent collectTagsIntent = new Intent(LoggedInAddKnowledge.this, LoggedInAddKnowledgeAddTags.class);
                startActivityForResult(collectTagsIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_TAGS);
            }
        });

        kt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    kt.setText(null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_TAGS &&
                resultCode == RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD) {
            final EditText kt = findViewById(R.id.add_tag_text);
            Intent knowledgeResultIntent = new Intent(data);
            Log.d("knowLedgeAdd", knowledgeResultIntent.getStringArrayListExtra("tagsForKnowledge").get(0));
            knowledgeResultIntent.putExtra("knowledge",kt.getText().toString());
            setResult(resultCode,knowledgeResultIntent);
            finish();
        }
    }

    private void finishActivityWithResult(int resultCode, Intent returnInfo) {

    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {

    }

    private void performFetchAllTags(View v) {
        Log.i(LoggedInHome.class.getName(), "Fetching all tags for user");
        KCAccessRequest accessRequest = KCAccessRequest.constructRequest();
        AsyncCall.startActionFetchTags(getApplicationContext(), accessRequest);
    }

}

package com.shrey.kc.kcui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_SPACE;
import static android.view.View.GONE;

public class LoggedInAddKnowledgeAddTags extends KCUIActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_add_knowledge_add_tags);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(getParent() == null) {
            // err, maybe goto home
            //startActivity(new Intent(null, LoggedInHome.class));

        }
        setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_FETCH_TAGS);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
        setListeners();
    }

    private void setListeners() {
        final EditText et = findViewById(R.id.add_tag_text_tags_text);
        TextView tv = findViewById(R.id.text_view_suggestions_heading);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText et = (EditText)v;
                if(keyCode == KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // return this result to previous activity
                    // put result in intent and finish!
                    String tagsGlued = et.getText().toString();
                    if(tagsGlued.trim().isEmpty()) {
                        return true;
                    }
                    ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsGlued.split(" ")));
                    Intent resultIntent = new Intent();
                    resultIntent.putStringArrayListExtra("tagsForKnowledge", tags);
                    //setResult(2,intent);
                    setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD,resultIntent);
                    finish();
                } /*else  {
                    // add this to list of tags
                    String tagsGlued = et.getText().toString();
                    if(tagsGlued.trim().isEmpty()) {
                        return true;
                    }
                    ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsGlued.split(" ")));
                    if(tags.size()>=1) {
                        tv.setText("Add more tags or press enter to go");
                    }
                }*/
                return true;
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tagsGlued = s.toString();
                if(tagsGlued.trim().isEmpty()) {
                    tv.setText("Enter at least one tag and press enter");
                    return;
                }
                ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsGlued.split(" ")));
                if(tags.size()>=1) {
                    tv.setText("Add more tags or press enter to go");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    et.setText(null);
                }
            }
        });

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText(null);
            }
        });
    }

    @Override
    protected void onStop() {
        unregisterReceiver(serviceBcastReceiver);
        super.onStop();
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {

    }

    private void performFetchAllTags() {
        Log.i(LoggedInAddKnowledgeAddTags.class.getName(), "Fetching all tags for user");
        KCAccessRequest accessRequest = KCAccessRequest.constructRequest();
        AsyncCall.startActionFetchTags(getApplicationContext(), accessRequest);
    }

}

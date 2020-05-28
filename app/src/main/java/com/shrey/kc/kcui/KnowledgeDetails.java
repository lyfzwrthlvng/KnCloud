package com.shrey.kc.kcui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCUpdateRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class KnowledgeDetails extends KCUIActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        fillUpText();
    }

    @Override
    public void onStart() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_DELETE_KNOWLEDGE);
        intentFilter.addAction(AsyncCall.ACTION_UPDATE_KNOWLEDGE);
        intentFilter.addAction(AsyncCall.ACTION_FETCH_RELATED_TAGS);
        intentFilter.addAction(AsyncCall.ACTION_READ);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
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
        EditText tv = findViewById(R.id.text_knowledge_details);
        Button updateButton = findViewById(R.id.update_knowledge_button);
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

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncCall.startActionUpdateKnowledge(getApplicationContext(),
                        KCUpdateRequest.constructRequest(knowledge, tag,  tv.getText().toString()));
            }
        });

        AsyncCall.startActionFetchRelatedTags(getApplicationContext(), KCWriteRequest.constructRequest(knowledge, tag));
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {
        if(action.equals(AsyncCall.ACTION_DELETE_KNOWLEDGE)) {
            // go back to the home page?!
            Log.d(KnowledgeDetails.class.getName(), "deletion done, going back!");
            makeToastOfSuccess("Knowledge deleted!");
            Intent homeIntent = new Intent(this, LoggedInHomeOne.class);
            setResult(RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_NOOP);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        } else if(action.equals(AsyncCall.ACTION_UPDATE_KNOWLEDGE)) {
            Log.d(KnowledgeDetails.class.getName(), "updated knowledge");
            makeToastOfSuccess("Knowledge updated!");
        } else if(action.equals(AsyncCall.ACTION_FETCH_RELATED_TAGS)) {
            Log.d(KnowledgeDetails.class.getName(), "fetched tags related to this knowledge");
            String[] tags = (String[]) result.getResult().get("Tags");
            LinearLayout ll = findViewById(R.id.layout_other_tags);
            for(String tag: tags) {
                TextView tv  = new TextView(getApplicationContext());
                tv.setText("#"+tag+" ");
                tv.setTextSize(30);
                tv.setTextColor(getColor(R.color.color1v3));
                tv.setFocusable(true);
                tv.setZ(100);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(KnowledgeDetails.class.getName(), "clicked on one tag");
                        ArrayList<String> al = new ArrayList<String>();
                        al.add(tag);
                        KCReadRequest readRequest = KCReadRequest.constructRequest(al);
                        AsyncCall.startActionRead(getApplicationContext(), readRequest);
                    }
                });
                ll.addView(tv);
            }
        } else if(action.equals(AsyncCall.ACTION_READ)) {
            fillUpKnowledgeCards(result);
        }
    }

    private void fillUpKnowledgeCards(NodeResult result) {
        //TODO: bad coding assuming activity, get this from activity itself
        if(result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        ArrayList<LinkedHashMap> knows = (ArrayList<LinkedHashMap>) resp.get("Knowledge");
        String currentTag = ((String[]) resp.get("tag"))[0];
        int id = 999;
        ArrayList<String> knowledges = new ArrayList<>();
        for(LinkedHashMap param: knows) {
            if(param.get("cloud") == null) {
                continue;
            }
            knowledges.add(param.get("cloud").toString());
        }
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.toggleSoftInput(0, 0);
        Intent showKnowledgeIntent = new Intent(KnowledgeDetails.this, ViewKnowledge.class);
        showKnowledgeIntent.putStringArrayListExtra("knowledges", knowledges);
        showKnowledgeIntent.putExtra("tag",currentTag);
        Log.d("apicall", currentTag  + " << fetched knowledges for");
        startActivityForResult(showKnowledgeIntent, 0);
        return;
    }

    @Override
    protected void onStop() {
        if(serviceBcastReceiver != null) {
            unregisterReceiver(serviceBcastReceiver);
        }
        super.onStop();
    }
}

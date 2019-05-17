package com.shrey.kc.kcui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;
import com.shrey.kc.kcui.workerActivities.ServiceBcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_ESCAPE;

public class LoggedInHome extends KCUIActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_home);
        LocalDBHolder.INSTANCE.getSetLocalDB(getApplicationContext());
        setupListeners();
        loadRealUI();

        // handle if we were tasked with something in particular
        handleIntentRequest();
    }

    private void setupListeners() {
        final ConstraintLayout cl = findViewById(R.id.constraint_root);
        cl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KEYCODE_ESCAPE && event.getAction() == KeyEvent.ACTION_UP) {
                    LinearLayout ll = findViewById(R.id.root_vertical_container);
                    ll.setFocusable(true);
                    ll.requestFocus();
                }
                return true;
            }
        });

        final EditText et = findViewById(R.id.text_search);

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)v;
                et.setText(null);
                //et.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
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

        // ---- listeners for the add button
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start add tag/knowledge activity
                v.requestFocus();
                Intent initiateAddKnowledgeIntent = new Intent(LoggedInHome.this, LoggedInAddKnowledge.class);
                startActivityForResult(initiateAddKnowledgeIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_KNOWLEDGE);
            }
        });

        // ---- button search
        Button searchButton = findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.text_search);
                performSearchAction(findViewById(R.id.text_search));
            }
        });

        final Button listButton = findViewById(R.id.button_list);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start a new activity that lists all the tags
                performFetchAllTags(v);
            }
        });

    }

    private void loadRealUI() {
        findViewById(R.id.text_search).setEnabled(true);

    }

    private void fillbackgroundWithTags() {
        //TextView tv = findViewById(R.id.text_view_tags_background);
    }

    protected void fillUpCards(Map<String, Object> response) {
        ArrayList<Object> knowledges = (ArrayList<Object>) response.get("Knowledge");

    }

    @Override
    protected void onStart() {
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AsyncCall.ACTION_ADD);
        intentFilter.addAction(AsyncCall.ACTION_READ);
        intentFilter.addAction(AsyncCall.ACTION_FETCH_TAGS);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
        Log.i("LOGGED_IN_HOME", getApplicationContext().getPackageName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_KNOWLEDGE &&
                resultCode == RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD) {
            String knowledge = data.getStringExtra("knowledge");
            for(String tag: data.getStringArrayListExtra("tagsForKnowledge")) {
                KCWriteRequest request = new KCWriteRequest();
                request.setKeyword(tag);
                request.setValue(knowledge);
                request.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
                request.setPassKey("dummy");
                request.setUserkey(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
                // send request to server
                AsyncCall.startActionAdd(getApplicationContext(), request);
            }
        }
    }

    private void performSearchAction(View v) {
        Log.i(LoggedInHome.class.getName(), "Performing search action");
        final EditText editText = (EditText) v;
        //editText.setBackground(getDrawable(R.drawable.rounded_corners_activity));
        //editText.setBackgroundResource(R.drawable.rounded_corners_activity);

        String userKey = CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail();
        String keyword = editText.getText().toString();

        KCReadRequest request = new KCReadRequest();
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        request.setKeywordList(keywords);
        request.setUserkey(userKey);
        request.setPassKey("dummy");
        request.setUserId(CurrentUserInfo.getUserInfo().getUser().getAccountInfo().hashCode());
        AsyncCall.startActionRead(getApplicationContext(), request);
    }

    private void performFetchAllTags(View v) {
        Log.i(LoggedInHome.class.getName(), "Fetching all tags for user");
        KCAccessRequest accessRequest = KCAccessRequest.constructRequest();
        AsyncCall.startActionFetchTags(getApplicationContext(), accessRequest);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Log.i("QQQ", "hey starting new activity!!!");
        super.startActivityForResult(intent, requestCode);
    }

    private void handleIntentRequest() {
        Intent requestIntent = getIntent();
        if(requestIntent.getIntExtra("requestCode",-1) == RuntimeConstants.INSTANCE.START_FROM_WIDGET_FOR_ADD) {
            // better get going with adding a new knowledge + tags
            Intent initiateAddKnowledgeIntent = new Intent(LoggedInHome.this, LoggedInAddKnowledge.class);
            startActivityForResult(initiateAddKnowledgeIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_KNOWLEDGE);
        }

    }

    @Override
    protected void onStop() {
        unregisterReceiver(serviceBcastReceiver);
        super.onStop();
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {
        if(action.equalsIgnoreCase(AsyncCall.ACTION_FETCH_TAGS)) {
            startActivityAndFillAllTags(result);
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_READ)) {
            fillUpKnowledgeCards(result);
        } else if(action.equalsIgnoreCase(AsyncCall.ACTION_ADD)) {
            if(result == null) {
                makeToastOfFailure();
            } else {
                makeToastOfSuccess();
            }
            findViewById(R.id.add_button).requestFocus();
        }

    }

    public void startActivityAndFillAllTags(NodeResult result) {
        if(result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        String[] tags = (String[]) resp.get("Tags");
        ArrayList<String> tagsArray = new ArrayList<>();
        for(String tag: tags) {
            tagsArray.add(tag);
        }
        Intent showAllTagsIntent = new Intent(LoggedInHome.this, ViewTags.class);
        showAllTagsIntent.putStringArrayListExtra("Tags", tagsArray);
        startActivityForResult(showAllTagsIntent, 0);
        return;
    }

    private void makeToastOfFailure() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Com breakdown with server :( \nTry in a little while maybe?",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.e("ServiceBroadcastListener", "FAILED COM WITH SERVER!");
    }

    private void makeToastOfSuccess() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "note saved",
                Toast.LENGTH_SHORT);
        toast.show();
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
        Intent showKnowledgeIntent = new Intent(LoggedInHome.this, ViewKnowledge.class);
        showKnowledgeIntent.putStringArrayListExtra("knowledges", knowledges);
        startActivityForResult(showKnowledgeIntent, 0);
        return;
    }
}

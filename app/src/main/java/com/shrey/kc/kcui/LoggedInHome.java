package com.shrey.kc.kcui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.LocalDBHolder;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.ServerCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_ESCAPE;

public class LoggedInHome extends AppCompatActivity {

    ServiceBcastReceiver serviceBcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_home);
        LocalDBHolder.INSTANCE.getSetLocalDB(getApplicationContext());
        setupListeners();
        loadRealUI();
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

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    performSearchAction(v);
                }
                return true;
            }
        });
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
                    et.setLayoutParams(findViewById(R.id.text_search_reference).getLayoutParams());
                    //et.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                } else {
                    et.setText(null);
                    et.setLayoutParams(findViewById(R.id.text_search_reference_tiny).getLayoutParams());
                    //et.setWidth(R.dimen.round_button_radius);
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
                int refWidth = (int)(getResources().getDimension(R.dimen.round_button_radius));
                if(et.getWidth() == refWidth) {
                    et.setLayoutParams(findViewById(R.id.text_search_reference).getLayoutParams());
                    et.requestFocus();
                } else {
                    performSearchAction(findViewById(R.id.text_search));
                }
            }
        });

    }

    private void loadRealUI() {
        findViewById(R.id.text_search).setEnabled(true);
    }

    protected void fillUpCards(Map<String, Object> response) {
        ArrayList<Object> knowledges = (ArrayList<Object>) response.get("Knowledge");

    }

    @Override
    protected void onStart() {
        serviceBcastReceiver = new ServiceBcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServerCall.ACTION_ADD);
        intentFilter.addAction(ServerCall.ACTION_READ);
        registerReceiver(serviceBcastReceiver, intentFilter);
        super.onStart();
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
                ServerCall.startActionAdd(getApplicationContext(), request);
            }
        }
    }

    private void performSearchAction(View v) {
        final EditText editText = (EditText) v;
        editText.setBackground(getDrawable(R.drawable.rounded_corners_activity));
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
        ServerCall.startActionRead(getApplicationContext(), request);
    }

}

class ServiceBcastReceiver extends BroadcastReceiver {

    LoggedInHome activityRef;

    public ServiceBcastReceiver(LoggedInHome activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == ServerCall.ACTION_READ) {
            fillUpKnowledgeCards((NodeResult) intent.getSerializableExtra("result"));
        } else if(action == ServerCall.ACTION_ADD && intent.getSerializableExtra("result") == null) {
            makeToastOfFailure();
        }
    }

    private void fillUpKnowledgeCards(NodeResult result) {
        EditText et = activityRef.findViewById(R.id.text_search);
        et.setBackground(activityRef.getDrawable(R.drawable.rounded_corners));
        et.setAlpha(1);
        et.setText(null);
        et.setLayoutParams(activityRef.findViewById(R.id.text_search_reference_tiny).getLayoutParams());
        if(result == null || result.getResult() == null) {
            makeToastOfFailure();
            return;
        }
        HashMap<String, Object> resp = result.getResult();
        Log.d("apicall", resp.toString());
        ArrayList<LinkedHashMap> knows = (ArrayList<LinkedHashMap>) resp.get("Knowledge");
        LinearLayout ll = activityRef.findViewById(R.id.root_vertical_container);
        ll.removeAllViews();
        int id = 999;
        for(LinkedHashMap param: knows) {
            if(param.get("cloud") == null) {
                continue;
            }
            Log.d("asd", param.get("cloud").toString());
            CardView cardView = (CardView) activityRef.getLayoutInflater().inflate(R.layout.knowledge_card,null);
            id += 1;
            cardView.setId(id);
            TextView tv = cardView.findViewById(R.id.text_view_in_card);
            tv.setText(param.get("cloud").toString());
            ll.addView(cardView);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView thisTv = (TextView)v;
                    Intent detailsOfKnowledge = new Intent(activityRef, KnowledgeDetails.class);
                    detailsOfKnowledge.putExtra("knowledge", thisTv.getText());
                    activityRef.startActivity(detailsOfKnowledge);
                }
            });

        }
        InputMethodManager inputManager = (InputMethodManager) activityRef.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
        ll.setFocusable(true);
        ll.requestFocus();
    }

    private void makeToastOfFailure() {
        Toast toast = Toast.makeText(activityRef.getApplicationContext(),
                "Com breakdown with server :( \nTry in a little while maybe?",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.e("ServiceBroadcastListener", "FAILED COM WITH SERVER!");
    }
}

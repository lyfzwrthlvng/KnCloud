package com.shrey.kc.kcui;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.shrey.kc.kcui.objects.CommunicationFactory;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.RuntimeConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.view.KeyEvent.*;

public class LoggedInHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
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

        final EditText et = findViewById(R.id.textTag);

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
                et.setAlpha(1);
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    et.setText(null);
                } else {
                    et.setAlpha(0.8f);
                }
            }
        });

        // ---- listeners for the add button
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start add tag/knowledge activity
                Intent initiateAddKnowledgeIntent = new Intent(LoggedInHome.this, LoggedInAddKnowledge.class);
                startActivityForResult(initiateAddKnowledgeIntent, RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_KNOWLEDGE);
            }
        });

        // ---- button search
        Button searchButton = findViewById(R.id.button_seach);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearchAction(findViewById(R.id.textTag));
            }
        });

    }

    private void loadRealUI() {
        //findViewById(R.id.sign_in_button).setEnabled(false);
        //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        //setContentView(R.layout.activity_dashboard);
        findViewById(R.id.textTag).setEnabled(true);
    }

    protected void fillUpCards(Map<String, Object> response) {
        ArrayList<Object> knowledges = (ArrayList<Object>) response.get("Knowledge");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RuntimeConstants.INSTANCE.START_ACTIVITY_FOR_KNOWLEDGE &&
                resultCode == RuntimeConstants.INSTANCE.STARTED_ACTIVITY_RESULT_GOOD) {
            String knowledge = data.getStringExtra("knowledge");
            for(String tag: data.getStringArrayListExtra("tagsForKnowledge")) {
                HashMap<String, Object> writeRequest = new HashMap<>();
                writeRequest.put("keyword", tag);
                writeRequest.put("value", knowledge);
                writeRequest.put("userKey", CurrentUserInfo.getUserInfo().getUser().getAccountInfo().getEmail());
                writeRequest.put("userId", 1);
                writeRequest.put("passKey","dummy");
                Log.d(LoggedInHome.class.getName(), writeRequest.toString());

                // send request to server
                try {
                    CommunicationFactory.getInstance().getExecutor("ADD").executeRequest(writeRequest);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void performSearchAction(View v) {
        final EditText editText = (EditText) v;
        editText.setBackground(getResources().getDrawable(R.drawable.rounded_corners_activity));
        //editText.setBackgroundResource(R.drawable.rounded_corners_activity);

        HashMap<String, Object> req = new HashMap<>();
        String userKey = CurrentUserInfo.getUserInfo().getUser().getUniqueId();
        String keyword = editText.getText().toString();
        req.put("keywordList", Arrays.asList(keyword));
        req.put("userKey",userKey);
        req.put("userId",1);
        req.put("passKey","dummy");
        try {
            Map<String, Object> resp = null;
            resp = CommunicationFactory.getInstance().getExecutor("FIND").executeRequest(req);
            Log.d("apicall", resp.toString());
            ArrayList<LinkedTreeMap> knows = (ArrayList<LinkedTreeMap>) resp.get("Knowledge");
            LinearLayout ll = findViewById(R.id.root_vertical_container);
            ll.removeAllViews();
            for(LinkedTreeMap param: knows) {
                if(param.get("cloud") == null) {
                    continue;
                }
                Log.d("asd", param.get("cloud").toString());
                CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.knowledge_card,null);
                int ids = 78;
                cardView.setId(ids);
                cardView.setCardElevation(10);
                cardView.setRadius(10);
                CardView.LayoutParams lp = new CardView.LayoutParams(1000, 300);
                lp.setMargins(20,16,20,20);
                cardView.setLayoutParams(lp);
                TextView tv = cardView.findViewById(R.id.textView);
                tv.setText(param.get("cloud").toString());
                ll.addView(cardView);

            }
            editText.setBackground(getDrawable(R.drawable.rounded_corners));
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
            ll.setFocusable(true);
            ll.requestFocus();

            //CardView cardView = findViewById(R.id.card_view_for_knowledge);
            //lv.setBackgroundColor(Color.BLACK);
            //lv.addView(getLayoutInflater().inflate(R.layout.knowledge_card, null));

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            editText.setBackground(getDrawable(R.drawable.rounded_corners));
        } finally {
            //editText.setBackground(getDrawable(R.drawable.rounded_corners));
        }
    }

    /*

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        super.dispatchKeyEvent(event);
        boolean someAction = false;
        if(event.getKeyCode() == KEYCODE_ESCAPE && event.getAction() == KeyEvent.ACTION_UP) {
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, 0);
            LinearLayout ll = findViewById(R.id.root_vertical_container);
            ll.setFocusable(true);
            ll.requestFocus();
            someAction = true;
        }
        return someAction;
    }
    */
}

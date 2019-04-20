package com.shrey.kc.kcui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.shrey.kc.kcui.objects.CommunicationFactory;
import com.shrey.kc.kcui.objects.CurrentUserInfo;

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
                    performAction(v);
                }
                return true;
            }

            private void performAction(View v) {
                final EditText editText = (EditText) v;

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
                    for(LinkedTreeMap param: knows) {
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
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);
                    ll.setFocusable(true);
                    ll.requestFocus();

                    //CardView cardView = findViewById(R.id.card_view_for_knowledge);
                    //lv.setBackgroundColor(Color.BLACK);
                    //lv.addView(getLayoutInflater().inflate(R.layout.knowledge_card, null));

                } catch (IOException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText)v;
                et.setText(null);
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

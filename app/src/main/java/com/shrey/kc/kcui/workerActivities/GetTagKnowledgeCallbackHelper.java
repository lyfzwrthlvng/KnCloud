package com.shrey.kc.kcui.workerActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.shrey.kc.kcui.KnowledgeDetails;
import com.shrey.kc.kcui.LoggedInHome;
import com.shrey.kc.kcui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetTagKnowledgeCallbackHelper implements GenericCallbackHelper {

    private LoggedInHome activityRef;

    public GetTagKnowledgeCallbackHelper(){}

    public GetTagKnowledgeCallbackHelper(LoggedInHome activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void callbackWithResult(HashMap<String, Object> result) {
        if(activityRef == null) {
            return;
        }
        fillUpKnowledgeCards(result);
    }

    private void fillUpKnowledgeCards(Map<String, Object> resp) {
        if(resp == null) {
            Toast toast = Toast.makeText(activityRef.getApplicationContext(),
                    "Com breakdown with server :( \nTry in a little while maybe?",
                    Toast.LENGTH_SHORT);
            return;
        }
        Log.d("apicall", resp.toString());
        ArrayList<LinkedTreeMap> knows = (ArrayList<LinkedTreeMap>) resp.get("Knowledge");
        LinearLayout ll = activityRef.findViewById(R.id.root_vertical_container);
        ll.removeAllViews();
        for(LinkedTreeMap param: knows) {
            if(param.get("cloud") == null) {
                continue;
            }
            Log.d("asd", param.get("cloud").toString());
            CardView cardView = (CardView) activityRef.getLayoutInflater().inflate(R.layout.knowledge_card,null);
            int ids = 78;
            cardView.setId(ids);
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
        //editText.setBackground(getDrawable(R.drawable.rounded_corners));
        InputMethodManager inputManager = (InputMethodManager) activityRef.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
        ll.setFocusable(true);
        ll.requestFocus();
    }
}

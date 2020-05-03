package com.shrey.kc.kcui.workerActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shrey.kc.kcui.R;
import com.shrey.kc.kcui.ViewKnowledge;
import com.shrey.kc.kcui.ViewTags;
import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.entities.NodeResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServiceBcastReceiver extends BroadcastReceiver {

    KCUIActivity activityRef;

    public ServiceBcastReceiver(KCUIActivity activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == AsyncCall.ACTION_READ && !ViewKnowledge.isActive) {
            ViewKnowledge.isActive = true;
            activityRef.handleBroadcastResult((NodeResult)intent.getSerializableExtra("result"), AsyncCall.ACTION_READ);
            //activityRef.findViewById(R.id.add_button).requestFocus();
        } else if(action == AsyncCall.ACTION_ADD) {
            activityRef.handleBroadcastResult((NodeResult)intent.getSerializableExtra("result"), AsyncCall.ACTION_ADD);
        } else if(action == AsyncCall.ACTION_FETCH_TAGS && !ViewTags.isActive) {
            //Log.i("SERVICE: ", intent.getSerializableExtra("result").toString());
            NodeResult result = (NodeResult)intent.getSerializableExtra("result");
            activityRef.handleBroadcastResult(result, AsyncCall.ACTION_FETCH_TAGS);
        } else if(action == AsyncCall.ACTION_SUGGEST) {
            NodeResult result = (NodeResult)intent.getSerializableExtra("result");
            activityRef.handleBroadcastResult(result, AsyncCall.ACTION_SUGGEST);
        } else if(action == AsyncCall.ACTION_BACKUP) {
            NodeResult result = (NodeResult)intent.getSerializableExtra("result");
            activityRef.handleBroadcastResult(result, AsyncCall.ACTION_BACKUP);
        } else if(action == AsyncCall.ACTION_DELETE_KNOWLEDGE) {
            Log.d("ddd","received braodcast for deletion");
            activityRef.handleBroadcastResult((NodeResult)intent.getSerializableExtra("result"), AsyncCall.ACTION_DELETE_KNOWLEDGE);
        }

    }

    private void makeToastOfFailure() {
        Toast toast = Toast.makeText(activityRef.getApplicationContext(),
                "Com breakdown with server :( \nTry in a little while maybe?",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.e("ServiceBroadcastListener", "FAILED COM WITH SERVER!");
    }

    private void makeToastOfSuccess() {
        Toast toast = Toast.makeText(activityRef.getApplicationContext(),
                "note saved",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void suggestTags(NodeResult result) {
        LinkedHashMap<String, String> resultHash = (LinkedHashMap<String, String>) result.getResult().get("Tags");
        LinearLayout ll = activityRef.findViewById(R.id.layout_for_tag_suggestions);
        int id = 786;
        for(String tag: resultHash.keySet()) {
            RelativeLayout irl = (RelativeLayout) activityRef.getLayoutInflater().inflate(R.layout.tag_suggestion, null);
            irl.setId(id++);
            TextView tv = irl.findViewById(R.id.text_view_suggestion_sample);
            tv.setText("#" + tag);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = activityRef.findViewById(R.id.add_tag_text_tags_text);
                    TextView tvIn = (TextView) v;
                    String existing = et.getText().toString();
                    if(existing.equalsIgnoreCase(activityRef.getString(R.string.tags_here_space_separated_will_do))) {
                        existing = "";
                    }
                    existing += tvIn.getText().toString().replace("#"," ");
                    et.setText(existing);
                }
            });
            ll.addView(irl);
        }
    }

    private void startActivityAndFillAllTags(NodeResult result) {
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
        Intent showAllTagsIntent = new Intent(activityRef, ViewTags.class);
        showAllTagsIntent.putStringArrayListExtra("Tags", tagsArray);
        activityRef.startActivityForResult(showAllTagsIntent, 0);
        return;
    }
}

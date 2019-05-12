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
import com.shrey.kc.kcui.entities.NodeResult;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ServiceBcastReceiver extends BroadcastReceiver {

    AppCompatActivity activityRef;

    public ServiceBcastReceiver(AppCompatActivity activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == AsyncCall.ACTION_READ && !ViewKnowledge.isActive) {
            ViewKnowledge.isActive = true;
            fillUpKnowledgeCards((NodeResult) intent.getSerializableExtra("result"));
            activityRef.findViewById(R.id.add_button).requestFocus();
        } else if(action == AsyncCall.ACTION_ADD) {
            if(intent.getSerializableExtra("result") == null) {
                makeToastOfFailure();
            } else {
                makeToastOfSuccess();
            }
            activityRef.findViewById(R.id.add_button).requestFocus();
        } else if(action == AsyncCall.ACTION_FETCH_TAGS) {
            //Log.i("SERVICE: ", intent.getSerializableExtra("result").toString());
            NodeResult result = (NodeResult)intent.getSerializableExtra("result");
            //Log.i(ServiceBcastReceiver.class.getName(), result.getResult().get("Tags").toString());
            suggestTags(result);
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
        int id = 999;
        ArrayList<String> knowledges = new ArrayList<>();
        for(LinkedHashMap param: knows) {
            if(param.get("cloud") == null) {
                continue;
            }
            knowledges.add(param.get("cloud").toString());
        }
        InputMethodManager inputManager = (InputMethodManager) activityRef.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);
        Intent showKnowledgeIntent = new Intent(activityRef, ViewKnowledge.class);
        showKnowledgeIntent.putStringArrayListExtra("knowledges", knowledges);
        activityRef.startActivityForResult(showKnowledgeIntent, 0);
        return;
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
}

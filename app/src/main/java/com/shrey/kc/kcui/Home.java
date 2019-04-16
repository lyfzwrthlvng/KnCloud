package com.shrey.kc.kcui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shrey.kc.kcui.entities.User;
import com.shrey.kc.kcui.executors.GetKnowledgeExecutor;
import com.shrey.kc.kcui.objects.CommunicationFactory;
import com.shrey.kc.kcui.objects.CurrentUserInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private TextView mTextMessage;
    private final int RC_SIGN_IN = 9001;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(" 436356672313-2ckrm0rp57du8fh8il6ics8lq3rufqrf.apps.googleusercontent.com ")
                .requestEmail()
                //.requestServerAuthCode("436356672313-eaohphn8igpjvo3trjab35ulto79n7q5.apps.googleusercontent.com")
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // register listeners
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                    default:
                        break;
                }

            }

            private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                Log.i("SIGNIN", "step1");
                startActivityForResult(signInIntent, RC_SIGN_IN);
                Log.i("SIGNIN", "intent activity for result started");
            }

        });

        CommunicationFactory.getInstance().register("FIND", new GetKnowledgeExecutor());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // if account is null, need to sign in

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("SIGNIN", "intent activity for signin result is here");

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        // task contains info about signed in user
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            Log.i("SIGNIN", "sign in done for: " + account.getEmail());
            CurrentUserInfo.getUserInfo().setUser(new User(account.getId(), account));
            loadRealUI();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGNIN", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void loadRealUI() {
        findViewById(R.id.sign_in_button).setEnabled(false);
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        setContentView(R.layout.activity_dashboard);
        findViewById(R.id.find_tag_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> req = new HashMap<>();
                req.put("keywordList", Arrays.asList("neo"));
                req.put("userKey","shrey");
                req.put("userId",1);
                req.put("passKey","dummy");
                try {
                    // network on main thread bad bad bad TODO
                    Map<String, Object> resp = CommunicationFactory.getInstance().getExecutor("FIND").executeRequest(req);
                    Log.i("comm", resp.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.shrey.kc.kcui;

import androidx.room.Update;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.shrey.kc.kcui.activities.KCUIActivity;
import com.shrey.kc.kcui.adaptors.DriveBackup;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.entities.User;
import com.shrey.kc.kcui.executors.AddKnowledgeExecutorLocal;
import com.shrey.kc.kcui.executors.BackupDataExecutor;
import com.shrey.kc.kcui.executors.DeleteKnowledgeExecutor;
import com.shrey.kc.kcui.executors.FetchRelatedTagsExecutorLocal;
import com.shrey.kc.kcui.executors.GetAllTagsExecutorLocal;
import com.shrey.kc.kcui.executors.GetAllTagsGraphExecutorLocal;
import com.shrey.kc.kcui.executors.GetKnowledgeExecutorLocal;
import com.shrey.kc.kcui.executors.UpdateKnowledgeExecutor;
import com.shrey.kc.kcui.objects.CommunicationFactory;
import com.shrey.kc.kcui.objects.CurrentUserInfo;
import com.shrey.kc.kcui.objects.RuntimeConstants;
import com.shrey.kc.kcui.workerActivities.AsyncCall;

import java.util.Collections;


public class Home extends KCUIActivity {

    private final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(" 436356672313-2ckrm0rp57du8fh8il6ics8lq3rufqrf.apps.googleusercontent.com ")
                .requestEmail()
                // requesting to
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                //.requestServerAuthCode("436356672313-eaohphn8igpjvo3trjab35ulto79n7q5.apps.googleusercontent.com")
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        CurrentUserInfo.INSTANCE.setSignInClient(mGoogleSignInClient);

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


        String serverIp = null;
        if(RuntimeConstants.INSTANCE.IS_EMULATOR){
            serverIp = getString(R.string.serverIpQEMU);
        } else {
            serverIp = getString(R.string.serverIpPrivate);
        }
        /*
        CommunicationFactory.getInstance().register("FIND",
                new GetKnowledgeExecutor(new ServerCaller(),
                        serverIp + getString(R.string.serverEndpointGet) ));
        CommunicationFactory.getInstance().register("ADD",
                new AddKnowledgeExecutor(new ServerCaller(),
                        serverIp + getString(R.string.serverEndpointAdd)));
                        */
        Log.i(this.getClass().getName(), "server-ip: " + serverIp);
        CommunicationFactory.getInstance().register("FIND", new GetKnowledgeExecutorLocal());
        CommunicationFactory.getInstance().register("ADD", new AddKnowledgeExecutorLocal());
        CommunicationFactory.getInstance().register("USER_TAGS", new GetAllTagsExecutorLocal());
        CommunicationFactory.getInstance().register("BACKUP", new BackupDataExecutor());
        CommunicationFactory.getInstance().register("USER_TAGS_GRAPH", new GetAllTagsGraphExecutorLocal());
        CommunicationFactory.getInstance().register("DELETE_KNOWLEDGE", new DeleteKnowledgeExecutor());
        CommunicationFactory.getInstance().register("UPDATE_KNOWLEDGE", new UpdateKnowledgeExecutor());
        CommunicationFactory.getInstance().register(AsyncCall.ACTION_FETCH_RELATED_TAGS, new FetchRelatedTagsExecutorLocal());
        /*
        CommunicationFactory.getInstance().register("FETCH_TAGS",
                new FetchSuggestedTagsExecutor(new ServerCaller(),
                        serverIp + getString(R.string.serverEndpointTags)));
                        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("HOME", getApplicationContext().getPackageName());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // if account is null, need to sign in
        if(account == null) {
            SignInButton sib = findViewById(R.id.sign_in_button);
            sib.setVisibility(View.VISIBLE);
        } else {
            // below works!!!
            //GoogleSignIn.requestPermissions(Home.this, RC_DRIVE_PERM, account, new Scope(DriveScopes.DRIVE_FILE));
            startLoggedInActivity(account, false);

        }
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
            startLoggedInActivity(account, true);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGNIN", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void startLoggedInActivity(GoogleSignInAccount account, boolean justSignedIn) {
        // Signed in successfully, show authenticated UI.
        //updateUI(account);
        Log.i("SIGNIN", "sign in done for: " + account.getEmail());
        Log.i("SIGNIN", "instance " + CurrentUserInfo.getUserInfo());
        CurrentUserInfo.getUserInfo().setUser(new User(account.getId(), account));
        //loadRealUI();
        Intent loggedIntent = new Intent(this, LoggedInHomeOne.class);
        loggedIntent.putExtra("justSignedIn", justSignedIn);
        startActivity(loggedIntent);
    }

    @Override
    public void handleBroadcastResult(NodeResult result, String action) {

    }
}

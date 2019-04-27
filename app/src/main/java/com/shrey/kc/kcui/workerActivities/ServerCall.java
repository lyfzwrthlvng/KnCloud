package com.shrey.kc.kcui.workerActivities;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import com.shrey.kc.kcui.entities.KCAccessRequest;
import com.shrey.kc.kcui.entities.KCReadRequest;
import com.shrey.kc.kcui.entities.KCWriteRequest;
import com.shrey.kc.kcui.entities.NodeResult;
import com.shrey.kc.kcui.objects.CommunicationFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ServerCall extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_READ = "com.shrey.kc.kcui.workerActivities.action.READ";
    public static final String ACTION_ADD = "com.shrey.kc.kcui.workerActivities.action.ADD";

    public ServerCall() {
        super("ServerCall");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionRead(Context context, KCReadRequest readRequest) {
        Intent intent = new Intent(context, ServerCall.class);
        intent.setAction(ACTION_READ);
        intent.putExtra("request", readRequest);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionAdd(Context context, KCWriteRequest writeRequest) {
        Intent intent = new Intent(context, ServerCall.class);
        intent.setAction(ACTION_ADD);
        intent.putExtra("request", writeRequest);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        KCAccessRequest request = (KCAccessRequest)intent.getSerializableExtra("request");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_READ.equals(action)) {
                handleActionFind((KCReadRequest)request);
            } else if (ACTION_ADD.equals(action)) {
                handleActionAdd((KCWriteRequest)request);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param readRequest
     */
    private void handleActionFind(KCReadRequest readRequest) {
        try {
            NodeResult result = CommunicationFactory.getInstance().getExecutor("FIND").executeRequest(readRequest);
            Intent intent = new Intent();
            intent.setAction(ACTION_READ);
            intent.putExtra("result", result);
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAdd(KCWriteRequest writeRequest) {
        try {
            NodeResult result = CommunicationFactory.getInstance().getExecutor("ADD").executeRequest(writeRequest);
            Intent intent = new Intent();
            intent.setAction(ACTION_ADD);
            intent.putExtra("result", result);
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

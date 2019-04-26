package com.shrey.kc.kcui.workerActivities;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchTagKnowledge extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FETCH = "com.shrey.kc.kcui.workerActivities.action.FETCH";
    private static final String ACTION_BAZ = "com.shrey.kc.kcui.workerActivities.action.BAZ";

    // TODO: Rename parameters
    private static final String TAG = "com.shrey.kc.kcui.workerActivities.extra.TAG";
    private static final String USER_NAME = "com.shrey.kc.kcui.workerActivities.extra.USER_NAME";
    private static final String USER_ID = "com.shrey.kc.kcui.workerActivities.extra.USER_ID";

    public FetchTagKnowledge() {
        super("FetchTagKnowledge");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String tag, String userName, String userId) {
        Intent intent = new Intent(context, FetchTagKnowledge.class);
        intent.setAction(ACTION_FETCH);
        intent.putExtra(TAG, tag);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(USER_ID, userId);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FetchTagKnowledge.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(TAG, param1);
        intent.putExtra(USER_NAME, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH.equals(action)) {
                final String tag = intent.getStringExtra(TAG);
                final String userName = intent.getStringExtra(USER_NAME);
                final String userId = intent.getStringExtra(USER_ID);
                handleActionFoo(tag, userName, userId);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(TAG);
                final String param2 = intent.getStringExtra(USER_NAME);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * @param tag
     * @param userName
     * @param userId
     */
    private void handleActionFoo(String tag, String userName, String userId) {

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

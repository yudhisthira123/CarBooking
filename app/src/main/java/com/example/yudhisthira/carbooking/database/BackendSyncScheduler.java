package com.example.yudhisthira.carbooking.database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by yudhisthira on 18/05/17.
 */
public class BackendSyncScheduler extends BroadcastReceiver{
    /**
     * The constant TAG.
     */
    public static final String TAG          = BackendSyncScheduler.class.getSimpleName();
    /**
     * The constant PERIOD.
     */
    public static final int PERIOD          = 60000 * 2; // 2 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive ++");

        scheduleBackendSyncService(context, PERIOD);

        Log.d(TAG, "onReceive --");
    }

    /**
     * Schedule backend sync service.
     *
     * @param context the context
     * @param period  the period
     */
    public static void scheduleBackendSyncService(Context context, int period) {
        Log.d(TAG, "scheduleBackendSyncService ++");

        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, BackendSyncManager.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + period, pi);

        Log.d(TAG, "scheduleBackendSyncService --");
    }

    /**
     * Cancel backend sync service.
     *
     * @param context the context
     */
    public static void cancelBackendSyncService(Context context) {
        Log.d(TAG, "cancelBackendSyncService ++");

        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BackendSyncManager.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        mgr.cancel(pendingIntent);

        Log.d(TAG, "cancelBackendSyncService --");
    }
}

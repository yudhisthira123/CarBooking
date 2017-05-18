package com.example.yudhisthira.carbooking.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yudhisthira.carbooking.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 18/05/17.
 */
public class BackendSyncManager extends IntentService {
    private final static String TAG         = BackendSyncManager.class.getSimpleName();

    /**
     * Instantiates a new Backend sync manager.
     */
    public BackendSyncManager() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent ++");

        checkPendingSyncEvents(getApplicationContext());

        Log.d(TAG, "onHandleIntent --");
    }

    /**
     *
     * @param context
     */
    private void checkPendingSyncEvents(Context context) {
        List<Car> carList = DatabaseWrapper.getPendingSyncEvents(context);

        for(Car car: carList) {
            //TODO
            //Add code to update actual backend on cloud

            //
            DatabaseWrapper.updateBackendSyncStatus(context, car, BackendSyncState.SYNC_STATE_COMPLETE);
        }

        BackendSyncScheduler.scheduleBackendSyncService(context, BackendSyncScheduler.PERIOD);
    }
}

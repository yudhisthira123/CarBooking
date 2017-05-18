package com.example.yudhisthira.carbooking.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.yudhisthira.carbooking.data.CommonConstants;

/**
 * Created by yudhisthira on 17/05/17.
 */
public class BookingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String carName = intent.getStringExtra(CommonConstants.CAR_NAME);
        String bookingTime = intent.getStringExtra(CommonConstants.CAR_BOOKING_TIME);
        String bookingID = intent.getStringExtra(CommonConstants.CAR_BOOKING_ID);
        int notificationID = intent.getIntExtra(CommonConstants.NOTIFICATION_ID, CommonConstants.DEFAULT_NOTIFICATION_ID);

        Log.d("BookingReceiver1", "bookingTime = " + bookingTime);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(carName)
                .setContentText(bookingTime)
                .setSmallIcon(R.drawable.notification_icon)
                .setSound(soundUri)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Intent i = new Intent(context, MainActivity.class);
        i.setAction(CommonConstants.LAUNCH_BOOKING_DETAIL);

        Uri uri = Uri.parse(bookingID);
        i.setData(uri);

        i.putExtra(CommonConstants.CAR_BOOKING_TIME, bookingTime);
        i.putExtra(CommonConstants.CAR_BOOKING_ID, bookingID);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.contentIntent = pendingIntent;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }
}

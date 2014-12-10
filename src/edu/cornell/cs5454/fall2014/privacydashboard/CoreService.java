package edu.cornell.cs5454.fall2014.privacydashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;


/**
 * Update Service 
 * 1. This service starts when the user enter Cornell Tech and ends when the user leaves.
 *    Start and stop by GeofenceTransitionIntentService.
 * 2. The service uses AlarmManager to send interval broadcast
 *    and it also registers a receiver to catch the broadcast to update the user's status to the server
 * @author Cary Zeyue Chen
 */
public class CoreService extends Service
{
    private final static String     BROADCAST_REPEAT_ACTION = "repeat_action";
    private final static String     DEFAULT_PROCESS_NAME = "com.google.android.googlequicksearchbox";

    private final static int        EXEC_INTERVAL_SECOND = 10;
    private RepeatTaskReceiver      repeatTaskReceiver;
    private TimeRecordModel         timeRecordModel;

    private final static String DEBUG_TAG = "Core";

    // Service callbacks
    @Override public void onCreate() {
        super.onCreate();
        initVal();
        initMain();
        start();
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    private void initVal() {
        // register repeat call
        this.repeatTaskReceiver = new RepeatTaskReceiver();
        registerReceiver(this.repeatTaskReceiver,
                new IntentFilter(BROADCAST_REPEAT_ACTION)
        );

        timeRecordModel = new TimeRecordModel(this);
    }

    private void initMain() {

    }

    public void start() {
        Log.v(DEBUG_TAG, "start");
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 3);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(this, 0,
                new Intent(BROADCAST_REPEAT_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                now.getTimeInMillis(),
                EXEC_INTERVAL_SECOND*1000,
                pendingIntent);
    }

    private void sendBroadcastNextTime() {
        // send broadcast
        Calendar nextTime = Calendar.getInstance();
        nextTime.add(Calendar.SECOND, EXEC_INTERVAL_SECOND);
        int hour = nextTime.get(Calendar.HOUR_OF_DAY);
        int minute = nextTime.get(Calendar.MINUTE);
        int second = nextTime.get(Calendar.SECOND);
        String nextTimeStr = hour+":"+minute+":"+second;
        Log.v(DEBUG_TAG, nextTimeStr);

        Intent nextTimeIntent = new Intent();
        Bundle data = new Bundle();
        nextTimeIntent.putExtras(data);
        sendBroadcast(nextTimeIntent);
    }

    private class RepeatTaskReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context c, Intent i) {

            // send broadcast to change next time
            sendBroadcastNextTime();

            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> apps = am.getRunningAppProcesses();

            String name = apps.get(0).processName;
            Log.v(DEBUG_TAG, "top process name: " + name);
            long currentTimeMillis = System.currentTimeMillis();
            Log.v(DEBUG_TAG, "current time: " + String.valueOf(currentTimeMillis));

            if (!name.equals(DEFAULT_PROCESS_NAME)) {
                timeRecordModel.setLastRunning(name, currentTimeMillis, EXEC_INTERVAL_SECOND*1000);
                timeRecordModel.addToList(name);
            }
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (repeatTaskReceiver != null) {
            unregisterReceiver(repeatTaskReceiver);
        }
    }

}

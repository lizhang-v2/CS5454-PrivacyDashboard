package edu.cornell.cs5454.fall2014.privacydashboard;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zeyuec on 12/9/14.
 */
public class TimeRecordModel {
    private static final String DEBUG_TAG = "TimeRecord";

    public static final String RECORD_NAME = "TimeRecord";

    public static final String SINCE_TIME_TAG = "SinceTime";
    public static final String APP_LIST_TAG = "AppList";

    public static final String LAST_RUNNING_TAG = "LastRunning";
    public static final String TOTAL_TIME_TAG = "TotalTime";

    private SharedPreferences sp_;
    private Service service_;
    private Activity activity_;

    public TimeRecordModel(Activity activity) {
        activity_ = activity;
        sp_ = activity_.getSharedPreferences(RECORD_NAME, 0);
    }

    public TimeRecordModel(Service activity) {
        service_ = activity;
        sp_ = service_.getSharedPreferences(RECORD_NAME, 0);
    }

    public long getSinceTime() {
        return sp_.getLong(SINCE_TIME_TAG, -1);
    }

    public void setSinceTime(long currentTime) {
        SharedPreferences.Editor editor = sp_.edit();
        editor.putLong(SINCE_TIME_TAG, currentTime);
        editor.commit();
    }

    public long getLastRunning(String name) {
        return sp_.getLong(name+LAST_RUNNING_TAG, -1);
    }

    public void setLastRunning(String name, long currentTime, long addedTime) {
        SharedPreferences.Editor editor = sp_.edit();

        // get last
        long last = getLastRunning(name);
        if (last == -1) {
            last = currentTime;
            editor.putLong(name+TOTAL_TIME_TAG, 0);
        }

        // put last
        editor.putLong(name+LAST_RUNNING_TAG, currentTime);

        // add to total
        long total = getTotal(name);
        editor.putLong(name+TOTAL_TIME_TAG, total + addedTime);

        // commit
        editor.commit();
    }

    public long getTotal(String name) {
        return sp_.getLong(name+TOTAL_TIME_TAG, 0);
    }

    public void addToList(String name) {
        Set set = sp_.getStringSet(APP_LIST_TAG, null);
        if (set == null) {
            set = new HashSet();
        }

        set.add(name);
        SharedPreferences.Editor editor = sp_.edit();
        editor.putStringSet(APP_LIST_TAG, set);
        editor.commit();
    }

    public ArrayList<BoardCell> getList() {
        // TODO: sort and return
        Set<String> set = sp_.getStringSet(APP_LIST_TAG, null);
        ArrayList<BoardCell> ret = new ArrayList<BoardCell>();
        if (set == null || set.isEmpty()) {
            return ret;
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            long total = getTotal(name);
            long last = getLastRunning(name);
            ret.add(new BoardCell(name, total, last));
        }
        return ret;
    }
}

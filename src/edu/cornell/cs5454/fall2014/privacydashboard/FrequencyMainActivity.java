package edu.cornell.cs5454.fall2014.privacydashboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;



public class FrequencyMainActivity extends Activity {

    private static final String DEBUG_TAG = "Frequency Main";
    private TextView textViewBoard;
    private Button buttonRefresh;
    private TimeRecordModel timeRecordModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frequency);
        initVal();
        initMain();
    }

    private void initVal() {
        Log.v(DEBUG_TAG, "initVal");
        textViewBoard = (TextView) findViewById(R.id.board);
        buttonRefresh = (Button) findViewById(R.id.refresh);
        buttonRefresh.setOnClickListener(new ButtonRefreshClickListener());

        timeRecordModel = new TimeRecordModel(this);
    }

    private class ButtonRefreshClickListener implements View.OnClickListener {
        @Override public void onClick(View v) {

            ArrayList<BoardCell> boardData = timeRecordModel.getList();
            Collections.sort(boardData);
            String text = "";
            for (int i=0; i<boardData.size(); i++) {
                BoardCell cell = (BoardCell) boardData.get(i);
                text += friendlyName(cell.name) + " " + String.valueOf(cell.total) + " " + friendlyTime(cell.lastRunning) + "\n";
            }
            textViewBoard.setText(text);
        }
    }

    private String friendlyTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd kk:mm:ss");
        String timeString = sdf.format(time);
        return timeString;
    }

    private String friendlyName(String name) {
        if (name.contains("facebook")) {
            return "facebook";
        } else if (name.contains("privacydashboard")) {
            return "privacydashboard";
        } else if (name.contains("netease.newsreader")) {
            return "netease.newsreader";
        } else if (name.contains("twitter")) {
            return "twitter";
        } else if (name.contains("evernote")) {
            return "evernote";
        } else {
            return name;
        }
    }

    public void initMain() {
        // start service
        Intent coreServiceIntent = new Intent(FrequencyMainActivity.this, CoreService.class);
        startService(coreServiceIntent);
        Log.v(DEBUG_TAG, "initMain");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

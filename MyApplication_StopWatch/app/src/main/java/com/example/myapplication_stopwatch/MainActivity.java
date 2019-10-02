package com.example.myapplication_stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //properties
    private Handler timerHandler;
    private ArrayAdapter<String> itemAdaptor;
    private TextView txtTimer;
    private Button btnStartPause, btnLapReset;

    //user to keep track of time
    private long millisecondTime, pausedTime, startTime, updateTime = 0;

    //used to display time
    private int seconds, minutes, milliseconds;

    private boolean stopWatchStarted, stopWatchPaused = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //only used in one place, so should not be global variable
        ListView lvLaps;

        //timeHnadler is bound to thread
        timerHandler = new Handler();

        //sets the layout for each item of the list view
        itemAdaptor = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        txtTimer = findViewById(R.id.txt_timer);
        btnStartPause = findViewById(R.id.btn_start_pause);
        btnLapReset = findViewById(R.id.btn_lap_reset);
        lvLaps = findViewById(R.id.lv_laps);

        //binds data from Adapter to the ListView
        lvLaps.setAdapter(itemAdaptor);
        btnStartPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!stopWatchStarted||stopWatchPaused)
                {
                    stopWatchStarted=true;
                    stopWatchPaused = false;
                    startTime = SystemClock.uptimeMillis();

                    //enqueues the runnable to be called by the message queue
                    timerHandler.postDelayed(timerRunnable, 0);

                    //switch label strings
                    btnStartPause.setText(R.string.lblPause);
                    btnLapReset.setText(R.string.btnLap);
                }
                else {
                    pausedTime += millisecondTime;
                    stopWatchPaused=true;

                    //remove pending posts of timeRunnable in message queue
                    timerHandler.removeCallbacks(timerRunnable);

                    //switch label strings
                    btnStartPause.setText(R.string.lblStart);
                    btnLapReset.setText(R.string.lblReset);
                }
            }
        });

        btnLapReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stopWatchStarted && !stopWatchPaused) {
                    String lapTime = minutes + ":"
                            + String.format("%02d", seconds) + ":"
                            + String.format("%03d", milliseconds);
                    itemAdaptor.add(lapTime);
                }
                else if(stopWatchStarted) {
                    stopWatchStarted = false;
                    stopWatchPaused = false;

                    //remove pending posts of timerRunnable in message queue
                    timerHandler.removeCallbacks(timerRunnable);

                    //reset all values
                    millisecondTime = 0;
                    startTime = 0;
                    pausedTime = 0;
                    updateTime = 0;
                    seconds = 0;
                    minutes = 0;
                    milliseconds = 0;

                    //switch label strings
                    txtTimer.setText(R.string.lblTimer);
                    btnLapReset.setText(R.string.btnLap);

                    //wipe resources
                    itemAdaptor.clear();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Timer hasn't started yet!", Toast.LENGTH_SHORT).show();
                    }

                }

        });
    }
        public Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {

                millisecondTime = SystemClock.uptimeMillis() -startTime;

                //values used to keep track of where the stowatch timer left off
                updateTime=pausedTime+millisecondTime;
                milliseconds=(int)(updateTime%1000);
                seconds=(int) (updateTime/1000);

                //convert values to display
                minutes=seconds/60;
                seconds=seconds%60;
                String updatedTime=minutes+":"
                        + String.format("%02d",seconds)+":"
                        +String.format("%03d",milliseconds);

                txtTimer.setText(updatedTime);

                timerHandler.postDelayed( this,0);
            }
        };
}

package org.knockout.paxmantimerproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class TimeSetupActivity extends Activity {

    TextView dateTextView;
    TextView timeTextView;
    private String currentTimeString;
    private String currentDateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final List<Button> buttons = new ArrayList<>();
        List<String> times = Arrays.asList("00:15:00", "00:30:00", "00:45:00", "01:00:00", "01:30:00", "02:00:00");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setup);
        dateTextView = (TextView) findViewById(R.id.dateTextViewSetup);
        timeTextView = (TextView) findViewById(R.id.timeTextViewSetup);

        Button headerButton = (Button) findViewById(R.id.headerButton);
        buttons.add((Button) findViewById(R.id.buttonTimer1));
        buttons.add((Button) findViewById(R.id.buttonTimer2));
        buttons.add((Button) findViewById(R.id.buttonTimer3));
        buttons.add((Button) findViewById(R.id.buttonTimer4));
        buttons.add((Button) findViewById(R.id.buttonTimer5));
        buttons.add((Button) findViewById(R.id.buttonTimer6));
        headerButton.setText(getString(R.string.setTime, ""));
        final Handler timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTimeString = new SimpleDateFormat("HH:mm").format(new Date());
                currentDateString = new SimpleDateFormat("DD.mm.yyyy").format(new Date());
                dateTextView.setText(getString(R.string.time) + " " + currentTimeString);
                timeTextView.setText(getString(R.string.date) + " " + currentDateString);
                timeHandler.postDelayed(this, 10000);
            }
        }, 10);
        int i = 0;

        for (Button but : buttons) {
            but.setText(times.get(i));
            final int j = i;

            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("time", buttons.get(j).getText());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
            i++;
        }
    }
}
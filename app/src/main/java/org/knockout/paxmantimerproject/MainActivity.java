package org.knockout.paxmantimerproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity {
    Button timerButton;
    Button resetButton;
    ImageView progressBar;
    TextView dateTextView;
    TextView timeTextView;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    Integer SET_TIME = 1;
    private PaxmanTimer paxmanTimer;
    private String currentTimeString;
    private String currentDateString;

    private static final long MILISECONDS_IN_SECOND = 1000;
    private Long timerStartValue = 900L * MILISECONDS_IN_SECOND;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerButton = (Button) findViewById(R.id.timerButton);
        resetButton = (Button) findViewById(R.id.setupButton);
        progressBar = (ImageView) findViewById(R.id.progressbar);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);

        timerButton.setText(convertMilisecToTime(timerStartValue));
        resetButton.setText(R.string.action_settings);

        final Handler timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTimeString = new SimpleDateFormat("HH:mm").format(new Date());
                currentDateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
                dateTextView.setText(getString(R.string.time) + " " + currentTimeString);
                timeTextView.setText(getString(R.string.date) + " " + currentDateString);
                timeHandler.postDelayed(this, 10000);
            }
        }, 10);

        timerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (paxmanTimer == null || !paxmanTimer.isStarted()) {
                    paxmanTimer = new PaxmanTimer(timerStartValue, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timerButton.setText(convertMilisecToTime(millisUntilFinished));
                        }

                        public void onFinish() {
                            finishTimer();
                        }
                    }.start();
                    startTimer();
                } else if (paxmanTimer != null && !paxmanTimer.isPaused()) {
                    pauseTimer();
                } else if (paxmanTimer != null && paxmanTimer.isPaused()) {
                    resumeTimer();
                }
            }
        });

        timerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetTimer();
                return true;
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerButton.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), TimeSetupActivity.class);
                startActivityForResult(intent, SET_TIME);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                timerButton.setText(data.getStringExtra("time"));
                timerStartValue = convertTimetoMilisec(data.getStringExtra("time"));
                cancelTimer();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private void startTimer() {
        progressBar.setVisibility(View.VISIBLE);
        startAnimation(progressBar);
    }

    private void resetTimer() {
        stopAnimation(progressBar);
        //progressBar.setVisibility(View.INVISIBLE);
        paxmanTimer.cancel();
        timerButton.setText(convertMilisecToTime(timerStartValue));
    }

    public void cancelTimer() {
        stopAnimation(progressBar);
        //progressBar.setVisibility(View.INVISIBLE);
        if (paxmanTimer != null) {
            paxmanTimer.cancel();
        }
    }

    private void resumeTimer() {
        paxmanTimer.resume();
        timerButton.setText(R.string.pause);
        progressBar.setVisibility(View.VISIBLE);
        startAnimation(progressBar);
    }

    private void pauseTimer() {
        paxmanTimer.pause();
        stopAnimation(progressBar);
    }

    private void finishTimer() {
        timerButton.setEnabled(false);
        timerButton.setText("00:00:00");
        stopAnimation(progressBar);
        vibrator.vibrate(500);
    }

    private void startAnimation(ImageView imageView) {
        RotateAnimation anim = new RotateAnimation(0.0f, -360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1500);
        imageView.startAnimation(anim);
    }

    private void stopAnimation(ImageView imageView) {
        imageView.setAnimation(null);
    }

    private String convertMilisecToTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    private long convertTimetoMilisec(String millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = sdf.parse("1970-01-01 " + millis);
            return (date.getTime());
        } catch (Exception e) {
            return 0L;
        }

    }

}


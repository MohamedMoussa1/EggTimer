package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.Format;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    SeekBar valueBar;
    TextView timerDigitalDisplay;
    CountDownTimer eggTimer;
    Button button;
    MediaPlayer mediaPlayer;
    boolean go = false;
    public void startStopTimer(View view) {
        button = findViewById(R.id.button);
        if (!go) {
            eggTimer.start();
            button.setText("Stop");
            go = true;
            valueBar.setEnabled(false);
        } else {
            eggTimer.cancel();
            button.setText("Go");
            go = false;
            valueBar.setEnabled(true);
            resetTimer();
        }
    }

    public void operateEggTimer(long timeSeconds) {
        eggTimer =  new CountDownTimer(timeSeconds* 1000 + 100, 1000) {
            public void onTick(long millisUntilFinish) {
                long ms = millisUntilFinish;
                String display = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
                        TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
                timerDigitalDisplay.setText(display);
            }

            @Override
            public void onFinish() {
                button.setText("Go");
                go = false;
                valueBar.setEnabled(true);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                mediaPlayer.start();
                resetTimer();
            }
        };
    }

    public void resetTimer() {
        int startTimeMinutes = 0;
        int startTimeSeconds = 30;
        valueBar.setProgress(30);
        String startTime = String.format("%02d:%02d", startTimeMinutes, startTimeSeconds);
        timerDigitalDisplay.setText(startTime);
        operateEggTimer(startTimeSeconds);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerDigitalDisplay = findViewById(R.id.textView);
        valueBar = findViewById(R.id.seekBar);
        int max = 3599; // Seconds
        valueBar.setMax(max);
        resetTimer();

        valueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1; // Seconds
                long timeSeconds = progress;
                if (progress < min) {
                    timeSeconds = min;
                    valueBar.setProgress(min);
                }
                String currentTime = String.format("%02d:%02d",
                        TimeUnit.SECONDS.toMinutes(timeSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(timeSeconds)),
                        timeSeconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeSeconds)));
                timerDigitalDisplay.setText(currentTime);
                operateEggTimer(timeSeconds);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

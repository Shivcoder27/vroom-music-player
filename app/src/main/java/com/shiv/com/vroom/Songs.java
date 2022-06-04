package com.shiv.com.vroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Songs extends AppCompatActivity {
    // control + O to override method
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
     ImageView play,next,previous;
     MediaPlayer mediaPlayer;
     String textContent;
     ArrayList<File> songs;
     SeekBar seekBar;
     int position;
     Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList)bundle.getParcelableArrayList("songlist");
        textView.setSelected(true);
        textContent= intent.getStringExtra("currentSong");
        textView.setText(textContent);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
                int CurrentPosition =0;
                super.run();
                try {
                    while (CurrentPosition<mediaPlayer.getDuration()){
                        CurrentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(CurrentPosition);
                        sleep(900);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

     play.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(mediaPlayer.isPlaying()){
                 play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                 mediaPlayer.pause();
             }
             else{
                 play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                 mediaPlayer.start();
             }
         }
     });
     previous.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             mediaPlayer.pause();
             mediaPlayer.release();
             if(position!=0){
                 position= position-1;
             }
             else{
                 position = songs.size()-1;
             }
             Uri uri = Uri.parse(songs.get(position).toString());
             mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
             mediaPlayer.start();
             seekBar.setMax(mediaPlayer.getDuration());
             textContent= songs.get(position).getName().toString();
             textView.setText(textContent);
         }
     });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position= position+1;
                }
                else{
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textContent= songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });


    }
}
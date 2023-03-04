package com.example.geetkheladi;

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

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        musicPlayer.release();
        updateSeekbar.interrupt();
    }

    TextView songName,playTimer,songLength;
    ImageView play_pause,prev,next;
    SeekBar seekBar;
    ArrayList<File> songs;
    MediaPlayer musicPlayer;
    String geetkoName;
    int position;

    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        songName = findViewById(R.id.songName);
        play_pause = findViewById(R.id.play_pause);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        playTimer = findViewById(R.id.playTimer);
        songLength = findViewById(R.id.songLength);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        geetkoName = intent.getStringExtra("currentSong");
        songName.setText(geetkoName);
        songName.setSelected(true);

        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        musicPlayer = MediaPlayer.create(this, uri);
        musicPlayer.start();
        int songDuration = musicPlayer.getDuration()/1000;
        songLength.setText(songDuration/60 + "m : "+songDuration%60+"s");

        seekBar.setMax(musicPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int timer = seekBar.getProgress()/1000;
                playTimer.setText(timer/60 + "m : "+ timer%60 + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekbar = new Thread(){
            @Override
            public void run(){
                int currentPosition = 0;
                try {
                    while(currentPosition < musicPlayer.getDuration()){
                        currentPosition = musicPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(1000);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        updateSeekbar.start();

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicPlayer.isPlaying()){
                    musicPlayer.pause();
                    play_pause.setImageResource(R.drawable.play);
                }
                else{
                    musicPlayer.start();
                    play_pause.setImageResource(R.drawable.pause);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.stop();
                musicPlayer.release();
                if(position!=0){
                    position = position-1;

                }
                else{
                    position = songs.size()-1;

                }
                Uri uri = Uri.parse(songs.get(position).toString());
                musicPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(musicPlayer.getDuration());
                songName.setText(songs.get(position).getName().toString());
                musicPlayer.start();
                play_pause.setImageResource(R.drawable.pause);
                seekBar.setProgress(musicPlayer.getCurrentPosition());
                int songDuration = musicPlayer.getDuration()/1000;
                songLength.setText(songDuration/60 + "m : "+songDuration%60+"s");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.stop();
                musicPlayer.release();
                if(position!=songs.size()-1){
                    position = position+1;

                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                musicPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(musicPlayer.getDuration());
                songName.setText(songs.get(position).getName().toString());
                musicPlayer.start();
                play_pause.setImageResource(R.drawable.pause);
                seekBar.setProgress(musicPlayer.getCurrentPosition());
                int songDuration = musicPlayer.getDuration()/1000;
                songLength.setText(songDuration/60 + "m : "+songDuration%60+"s");
            }
        });
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicPlayer.stop();
                musicPlayer.release();
                if(position!=songs.size()-1){
                    position = position+1;

                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                musicPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(musicPlayer.getDuration());
                songName.setText(songs.get(position).getName().toString());
                musicPlayer.start();
                play_pause.setImageResource(R.drawable.pause);
                seekBar.setProgress(musicPlayer.getCurrentPosition());
                int songDuration = musicPlayer.getDuration()/1000;
                songLength.setText(songDuration/60 + "m : "+songDuration%60+"s");
            }
        });

    }
}
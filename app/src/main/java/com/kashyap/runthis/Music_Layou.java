package com.kashyap.runthis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Music_Layou extends AppCompatActivity {
MediaPlayer mediaPlayer;
Uri uri;
TextView tv1;
Button play,next,prev;
    int position;
    Thread updateseekBar;
    ArrayList<File> al1;
    SeekBar seekbar;
    int i;
 boolean stopThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music__layou);
        tv1=findViewById(R.id.artist_name);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        prev=findViewById(R.id.prev);
       seekbar=findViewById(R.id.seekbar);
       getSupportActionBar().setTitle("Now Playing");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
         al1= (ArrayList) bundle.getParcelableArrayList("arraylist");
        String song_name=bundle.getString("songname");
          tv1.setText(song_name);
          position=bundle.getInt("position");
          uri=Uri.parse(al1.get(position).toString());

            if(mediaPlayer==null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        releas();
                    }
                });
            }
        mediaPlayer.start();
        seekbar.setMax(mediaPlayer.getDuration());
             updateSeekbar.start();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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




    }
    Thread updateSeekbar=new Thread()  {
        @Override
        public void run()  {
            try {
                     // stopThread=true;
                int totalduration = mediaPlayer.getDuration(); //give maximum duration of the song
                int currentduration = 0;
                while (currentduration < totalduration) {

                    Thread.sleep(500);
                    try {
                        currentduration = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentduration);
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    };

    public void play(View view){
  seekbar.setMax(mediaPlayer.getDuration());
        if(mediaPlayer.isPlaying()){
            play.setBackgroundResource(R.drawable.ic_play);
            mediaPlayer.pause();
        }
        else{
            play.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();

        }

}


    public void prev(View view){ //pause
        mediaPlayer.release();
        if(position==0){
            Toast.makeText(this, "this is last song you can't go back", Toast.LENGTH_SHORT).show();
            mediaPlayer.release();
       //     position=((position+1)%al1.size());

            String songname=al1.get(position).getName().toString();
            tv1.setText(songname);
            Uri uri=Uri.parse(al1.get(position).toString());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        else{
            position=((position-1)%al1.size());

            String songname=al1.get(position).getName().toString();
            tv1.setText(songname);
            Uri uri=Uri.parse(al1.get(position).toString());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
    }
    public void next(View view){
             releas();
             position=((position+1)%al1.size());
             String songname=al1.get(position).getName().toString();
             tv1.setText(songname);
             Uri uri=Uri.parse(al1.get(position).toString());
             mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
             mediaPlayer.start();


    }
    public void releas(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
            Toast.makeText(this, "media player is released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){

            onBackPressed();
        }
        return  super.onOptionsItemSelected(item);
    }





    @Override
    protected void onStop() {
        super.onStop();
      //  updateSeekbar.stop();
       // updateSeekbar.interrupt();
       releas();

    }
}
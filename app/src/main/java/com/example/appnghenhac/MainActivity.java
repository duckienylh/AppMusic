package com.example.appnghenhac;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txtTile, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView imgHinh;
    ImageView btnPrev, btnPlay, btnStop, btnNext, btnRandom, btnRepeat;
    MediaPlayer mediaPlayer;
    Boolean repeatFlag = true;

    Boolean shuffle = false;
    Random random;

    ArrayList<Song> arraySong;
    int position = 0;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        AddSong();

        khoiTaoMediaPlayer();
        random = new Random();


        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffle){
                    btnRandom.setImageResource(R.drawable.ic_action_random);
                }else{
                    btnRandom.setImageResource(R.drawable.ic_action_random_on);
                }
                shuffle = !shuffle;
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeatFlag){
                    btnRepeat.setImageResource(R.drawable.ic_action_repeatone);
                }else{
                    btnRepeat.setImageResource(R.drawable.ic_action_repeat);
                }
                repeatFlag = !repeatFlag;

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position > arraySong.size()-1){
                    position = 0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                if(!shuffle){
                    khoiTaoMediaPlayer();
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_action_pause);
                    setTimeTotal();
                    updateTimeSong();

                }else{
                    int newSong =  position;
//                    while (newSong == position){
                        newSong = random.nextInt(arraySong.size());
//                    }
                    position = newSong;
                    khoiTaoMediaPlayer();
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_action_pause);
                    setTimeTotal();
                    updateTimeSong();
                }


            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0){
                    position = arraySong.size()-1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_action_pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.ic_action_play);
                khoiTaoMediaPlayer();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
//                    new dang het thi pause -> doi hinh play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_action_play);
                }else{
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_action_pause);
                }
                setTimeTotal();
                updateTimeSong();
                imgHinh.startAnimation(animation);
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void updateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
                // update progress sksong
                skSong.setProgress(mediaPlayer.getCurrentPosition());

                // kiểm tra thời gian bài hát => nếu kết thúc => next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position > arraySong.size()-1){
                            position = 0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        if(repeatFlag){
                            khoiTaoMediaPlayer();
                            mediaPlayer.start();
                            btnPlay.setImageResource(R.drawable.ic_action_pause);
                            setTimeTotal();
                            updateTimeSong();
                        }else{
                            mediaPlayer.start();
                            btnPlay.setImageResource(R.drawable.ic_action_pause);
                            setTimeTotal();
                            updateTimeSong();
                        }

                    }
                });
                handler.postDelayed(this, 500);
            }
        },100);

    }

    private void setTimeTotal(){
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        //gán max của sksong = meidaplayer.getduration()
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void khoiTaoMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTile.setText(arraySong.get(position).getTitle());
    }

    private void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("thuong em", R.raw.song1));
        arraySong.add(new Song("I do", R.raw.song2));
        arraySong.add(new Song("I do1", R.raw.song2));
        arraySong.add(new Song("I do2", R.raw.song2));
        arraySong.add(new Song("I do3", R.raw.song2));

    }

    private void AnhXa(){
        txtTimeSong = (TextView) findViewById(R.id.textviewTimeSong);
        txtTimeTotal = (TextView) findViewById(R.id.textviewTimeTotal);
        txtTile = (TextView) findViewById(R.id.textviewTile);
        skSong = (SeekBar) findViewById(R.id.seekBarSong);
        btnNext = (ImageView) findViewById(R.id.imageViewNext);
        btnPlay = (ImageView) findViewById(R.id.imageViewPlay);
        btnStop = (ImageView) findViewById(R.id.imageViewStop);
        btnPrev = (ImageView) findViewById(R.id.imageViewPrevious);
        btnRandom = (ImageView) findViewById(R.id.imageViewRandom);
        btnRepeat = (ImageView) findViewById(R.id.imageViewRepeat);
        imgHinh = (ImageView) findViewById(R.id.imageViewhing);
    }
}
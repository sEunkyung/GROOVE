package com.example.groove.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.groove.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Music_Player extends AppCompatActivity {
    private PlayerControlView pvc;
    private ExoPlayer player;
    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    int i = 0;

    // 추가
    int[] array = {R.raw.song2, R.raw.song4, R.raw.song3,
            R.raw.song0, R.raw.song1};
    int index = 0;
    long test = 0;
    SeekBar seekBar;
    PlayerControlView playerControlView;
    ImageButton music_play, music_next, music_pre;



    @Override
    protected void onStart() {
        super.onStart();
        if(i==1)
            initializePlayer(index);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (player != null){
            releasePlayer(player.getCurrentPosition());
            // ◁ : Stop
            // ○ : Pause
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (playWhenReady) {
            player.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!playWhenReady){
            player.play();
        }
    }


    private void releasePlayer(long current) {
        if(player != null){
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = current;
            currentWindow = player.getCurrentMediaItemIndex();
            player.release();
            player = null;
        }
    }

    private void newRelease(){
        player.pause();
        player = null;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_main);
        player = new ExoPlayer.Builder(this).build();
        pvc = findViewById(R.id.main_pcv);


        music_play = findViewById(R.id.music_play);
        music_pre = findViewById(R.id.music_pre);
        music_next = findViewById(R.id.music_next);

        // custom_control_layout.xml SeekBar
        seekBar = findViewById(R.id.seekBar_player);
        // 메인 xml exoplayer playControlView
        playerControlView = findViewById(R.id.main_pcv);
        music_play.setTag("재생");
        music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    if(player.isPlaying()){

                        test  = player.getCurrentPosition();
                        player.pause();
                    }else{
                        if(test != 0){
                            Log.d("왔따","Test");
                            playbackPosition =  test;
                        }

                        initializePlayer(index);
                        if(music_play.getTag().equals("재생")){
                            music_play.setImageResource(R.drawable.btn_pausexml);
                            music_play.setTag("일시정지");
                            i++;
                        }
                    }
                }
                else{
                    if(music_play.getTag().equals("일시정지")){
                        music_play.setImageResource(R.drawable.btn_playxml);
                        music_play.setTag("재생");
                        i--;
                    }
                    test  = player.getCurrentPosition();
                    player.pause();
                    Log.d("zzzzz", String.valueOf(i));
                }

                // 노래가 정지 돼있을때 시작
//                if(i==0){
////                    if(test==0){
////                        releasePlayer(0);
////                        test++;
////                    }else{
////                        releasePlayer(player.getCurrentPosition());
////                    }
//
//
//                    //Log.d("Test1",String.valueOf(player.getCurrentPosition()));
//                    initializePlayer(index);
//                    i=1;
//                } else if(i==1){
//                    Log.d("Test2",String.valueOf(player.getCurrentPosition()));
//                    player.pause();
//                    i=0;
//                }
            }
        });
        // 다음 곡
        music_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music_play.getTag().equals("재생")){
                    music_play.setImageResource(R.drawable.btn_pausexml);
                    music_play.setTag("일시정지");
                    i++;
                }
                newRelease();
                playbackPosition = 0;
                index++;
                if (index >= array.length){
                    index = 0;
                }
                initializePlayer(index);
//                releasePlayer(0);
//                index += 1;
//                if (index >= array.length){
//                    index = 0;
//                }
//                // playbackPosition = 0; // 다음 곡으로 넘어가면 처음부터 시작
//                initializePlayer(index);
            }
        });
        // 이전 곡
        music_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music_play.getTag().equals("재생")){
                    music_play.setImageResource(R.drawable.btn_pausexml);
                    music_play.setTag("일시정지");
                    i++;
                }
                newRelease();
                playbackPosition = 0;
                index--;
                if (index <0){
                    index = array.length-1;
                }
                initializePlayer(index);
//                releasePlayer(0);
//                index -= 1;
//                if (index < 0){
//                    index = array.length-1;
//                }
//                //playbackPosition = 0; // 다음 곡으로 넘어가면 처음부터 시작
//                initializePlayer(index);
            }
        });
        Log.d("ㅋㅋㅋgetPlayWhenReady", String.valueOf(player.getPlayWhenReady()));
    }

    private void initializePlayer(int pos){

        Log.d("TestPod", String.valueOf(pos));
        player = new ExoPlayer.Builder(Music_Player.this).build();
        pvc.setPlayer(player);

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//
//        // Create a reference with an initial file path and name
//        StorageReference pathReference = storageRef.child("song1114595.mp3");
//
//        // Create a reference to a file from a Cloud Storage URI
//        StorageReference gsReference = storage.getReferenceFromUrl("gs://groove-374902.appspot.com/song1114595.mp3");
//
//        // Create a reference from an HTTPS URL
//        player.setAudioAttributes(AudioAttributes.DEFAULT, false);
//        // Note that in the URL, characters are URL escaped!
//        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/song1114595.mp3");

        // 로컬에 있는 음악 파일 리스트에 넣기
        // com.example.프로젝트명.raw폴더의 mp3파일들의 리소스들을 찾는 것
        int musicID = this.getResources().getIdentifier("song1", "raw", this.getPackageName());
        // musicID의 리소스 데이터를 Uri로 바꿔주는 것
        Uri musicUri = RawResourceDataSource.buildRawResourceUri(musicID);
//        Uri musicUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//        Log.d("하ㅣ하하하하", String.valueOf(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)));
//        Log.d("하ㅣ하하하하", MediaStore.Audio.Media.ALBUM_ID);
        // 바꾼 Uri를 exoplayer에 mediaitems 통해 미디어 항목 생성
        MediaItem mediaItems = MediaItem.fromUri(musicUri);

        // player에 미디어 항목을 세팅
        player.setMediaItem(mediaItems);
        // player 준비
        player.prepare();
        // player가 준비가 됐고 playWhenReady가 true이면 재생, false면 정지
        player.setPlayWhenReady(playWhenReady);
        // Seek바의 현재위치
        player.seekTo(currentWindow, playbackPosition);

        Log.d("백포지션", String.valueOf(player.isPlaying()));
    }
}
package com.example.groove.activity;

import static com.example.groove.activity.MainActivity.song_list;
import static com.example.groove.activity.MainActivity.user_seq;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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
    AppCompatSeekBar seekBar;
    PlayerControlView playerControlView;
    ImageButton music_play, music_next, music_pre;
    TextView play_title, play_artist;
    ImageView img_player;
    AppCompatImageButton btn_down, btn_heart;
    RequestQueue requestQueue;

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

        btn_down = findViewById(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Main_Home에서 받아온 데이터
        Intent intent = getIntent();
        String song_id = intent.getStringExtra("song_id");
        String song_title = intent.getStringExtra("song_title");
        String artist_name = intent.getStringExtra("artist_name");
        String album_img = intent.getStringExtra("album_img");
        String song_lyrics = intent.getStringExtra("song_lyrics");
        String bool_heart = intent.getStringExtra("lkies_date");

        btn_heart = findViewById(R.id.btn_heart);
        play_title = (TextView) findViewById(R.id.play_title);
        play_artist = (TextView) findViewById(R.id.play_artist);
        img_player = (ImageView) findViewById(R.id.img_player);

        play_title.setText(song_title);
        play_artist.setText(artist_name);
        String imgfile = "album_" + album_img;
        img_player.setImageResource(getResources().getIdentifier(imgfile,"drawable",getApplication().getPackageName() ));

        player = new ExoPlayer.Builder(this).build();
        pvc = findViewById(R.id.main_pcv);

        music_play = findViewById(R.id.music_play);
        music_pre = findViewById(R.id.music_pre);
        music_next = findViewById(R.id.music_next);

        // custom_control_layout.xml SeekBar
        seekBar = findViewById(R.id.seekBar_player);
        // 메인 xml exoplayer playControlView
        playerControlView = findViewById(R.id.main_pcv);
        String mp3Name;
        music_play.setTag("재생");
        music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    if(player.isPlaying()){

                        test  = player.getCurrentPosition();
                        Log.d("지금포지션", String.valueOf(test));
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
            }
        });
        Log.d("ㅋㅋㅋgetPlayWhenReady", String.valueOf(player.getPlayWhenReady()));

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://172.30.1.49:3001/InsertList";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);

                            JSONArray song_list2 = json.getJSONArray("song_list");

                            btn_heart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(btn_heart.getTag().equals("heartoff")){
                                        btn_heart.setImageResource(R.drawable.img_heartxml);
                                        btn_heart.setTag("hearton");
                                    } else{
                                        btn_heart.setImageResource(R.drawable.img_heart_emptyxml);
                                        btn_heart.setTag("heartoff");
                                    }

                                }
                            });

                            if(bool_heart==""){
                                btn_heart.setImageResource(R.drawable.img_heartxml);
                                btn_heart.setTag("hearton");
                            } else{
                                btn_heart.setImageResource(R.drawable.img_heart_emptyxml);
                                btn_heart.setTag("heartoff");
                            }

                            if(!song_list.isEmpty()){
                                song_list.clear();
                            }
                            Log.d("왜요?", String.valueOf(song_list));
                            Log.d("왜요?", String.valueOf(song_list2));
                            for(int i=0; i<song_list2.length(); i++){
                                song_list.add(song_list2.getString(i));
                            }
                            Log.d("여기리스트는", String.valueOf(song_list.size()));
                            Log.d("왜요?", String.valueOf(song_list));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("통신", "실패");
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // getParams --> post 방식으로 데이터를 보낼 때 사용되는 메소드!
                // 데이터를 key - value 형태로 만들어서 보내겠습니다
                Map<String,String> params = new HashMap<String,String>();
                // params -> key-value 형태로 만들어줌
                params.put("user_seq", user_seq);
                params.put("song_id", song_id);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);


    }

    private void initializePlayer(int pos){

        Intent intent = getIntent();
        String song_id = intent.getStringExtra("song_id");

        Log.d("인덱스값", String.valueOf(pos));
        player = new ExoPlayer.Builder(Music_Player.this).build();
        pvc.setPlayer(player);

        Log.d("하하하하하",song_id);
        Log.d("하하하하하",song_list.get(0));
        String mp3Name = song_id;
        // com.example.프로젝트명.raw폴더의 mp3파일들의 리소스들을 찾는 것
//        int musicID = this.getResources().getIdentifier("song1", "raw", this.getPackageName());
        // 내부저장소에 있는 파일 uri
        File mp3File = new File(getFilesDir()+"/song_mp3/"+mp3Name+".mp3");
        // musicID의 리소스 데이터를 Uri로 바꿔주는 것
//        Uri musicUri = RawResourceDataSource.buildRawResourceUri(musicID);
        Uri musicUri = Uri.fromFile(mp3File);
        // 바꾼 Uri를 exoplayer에 mediaitems 통해 미디어 항목 생성
        MediaItem mediaItems = MediaItem.fromUri(musicUri);
        // player에 미디어 항목을 세팅
        player.setMediaItem(mediaItems);
        // player 준비
        player.prepare();
        // player가 준비가 됐고 playWhenReady가 true이면 재생, false면 정지
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Log.d("노래길이가 잘 나오냐..?", String.valueOf(player.getTotalBufferedDuration()));
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress((int) player.getContentPosition());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        Log.d("길이가..?", String.valueOf((float)player.getContentPosition()));
        long dur = player.getDuration();

        Log.d("길이가..?2", String.valueOf((int)dur/1000));

        Log.d("전체길이가..?2", String.valueOf(player.getCurrentTimeline()));

    }
}


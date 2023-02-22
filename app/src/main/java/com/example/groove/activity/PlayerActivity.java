package com.example.groove.activity;

import static com.example.groove.fragment.PlayList.frag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.groove.R;
import com.example.groove.fragment.Lyrics;
import com.example.groove.fragment.Music_Player;
import com.example.groove.fragment.PlayList;
import com.example.groove.fragment.Relative_Music;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.util.Log;

public class PlayerActivity extends AppCompatActivity {

    public static int index = 0;
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private Music_Player fragmentMusicPlayer;
    private Lyrics fragmentLyrics;
    private Relative_Music fragmentRalative;
    private PlayList fragmentPlaylist;
    Bundle bundle = new Bundle();
    AppCompatImageButton btn_down;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);

        // Main_Home, PlayList 에서 받아온 데이터
        Intent intent = getIntent();
        String song_id = intent.getStringExtra("song_id");
        String plindex = intent.getStringExtra("plindex");
        frag=1;

        bundle.putString("plindex", plindex);
        bundle.putString("song_id", song_id);

        btn_down = findViewById(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        bundle.putString("song_title", song_title);
//        bundle.putString("artist_name", artist_name);
//        bundle.putString("album_img", album_img);

        fragmentManager = getSupportFragmentManager();

        fragmentMusicPlayer = new Music_Player();
        fragmentRalative = new Relative_Music();
        fragmentLyrics = new Lyrics();
        fragmentPlaylist = new PlayList();

        // 받아온 데이터 홈 프래그먼트에 보내기
        fragmentMusicPlayer.setArguments(bundle);

        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.music_bottom, fragmentMusicPlayer).commitAllowingStateLoss();


    }


    public void clickHandler(View view){
        transaction = fragmentManager.beginTransaction();

        switch (view.getId()){

            case R.id.btn_related:
                transaction.add(R.id.music_bottom, fragmentRalative).commitAllowingStateLoss();
                break;
            case R.id.btn_lyrics:
                transaction.add(R.id.music_bottom, fragmentLyrics).commitAllowingStateLoss();
                break;
            case R.id.btn_list:
                transaction.add(R.id.music_bottom, fragmentPlaylist).commitAllowingStateLoss();
                break;
        }
    }


}


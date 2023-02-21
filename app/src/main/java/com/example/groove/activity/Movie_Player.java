package com.example.groove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Movie_Player extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    TextView tv_mvtitle, tv_lyrics;
    YouTubePlayer.OnInitializedListener listener;
    RequestQueue requestQueue;
    AppCompatImageButton img_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        // Main_Home에서 받아온 데이터
        Intent intent = getIntent();
        String art_id = intent.getStringExtra("art_id");


        youTubePlayerView = findViewById(R.id.pv_mv);
        tv_mvtitle = findViewById(R.id.tv_mvtitle);
        tv_lyrics = findViewById(R.id.tv_lyrics);
        img_down = findViewById(R.id.img_down);

        img_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 서버로부터 video_url 테이블에서 데이터 가져와
        // 데이터를 화면에 띄워.
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://172.30.1.31:3001/MVplayer";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String result = json.getString("result");
                            String lyrics = json.getString("lyrics");
                            String videoTitle = json.getString("videoTitle");
                            String videoUrl = json.getString("videoUrl");
                            Log.d("통신3", result);
                            Log.d("url주소", videoUrl);
                            // setText
                            videoUrl = videoUrl.replace("https://youtube.com/watch?v=", "");
                            tv_mvtitle.setText(videoTitle);
                            tv_lyrics.setText(lyrics);
                            // 유튜브 영상 띄우기
                            String finalVideoUrl = videoUrl;
                            listener = new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    youTubePlayer.loadVideo(finalVideoUrl); //
                                    //https://www.youtube.com/watch?v=bLoO0FSXncg 유투브에서 v="" 이부분이 키에 해당
                                    //https://www.youtube.com/watch?v=Fs_p7BHfzO0
                                }
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                }
                            };
                            youTubePlayerView.initialize("AIzaSyA9TRnQ1Lyszy2USJsb-1J4NujODTQZo7I", listener);
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
                params.put("artistid", art_id);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);

    }
}



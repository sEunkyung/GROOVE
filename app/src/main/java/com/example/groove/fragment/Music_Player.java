package com.example.groove.fragment;

import static com.example.groove.activity.MainActivity.user_seq;
import static com.example.groove.activity.PlayerActivity.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Music_Player extends Fragment {

    public static String playerSong_id;

    private PlayerControlView pvc;
    private MediaPlayer player = new MediaPlayer();
    String bool_heart = "0";
    AppCompatSeekBar seekBar_player;
    ImageButton music_play, music_next, music_pre, btn_shuffle, btn_repeat;
    TextView play_title, play_artist, time_start, time_end;
    ImageView img_player;
    AppCompatImageButton btn_heart;
    AppCompatButton btn_related, btn_lyrics, btn_list;
    RequestQueue requestQueue;
    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    // 곡 리스트 인덱스
    Bundle bundle;
    int i=0;
    ArrayList<String> song_list = new ArrayList<>();
    ArrayList<String> stitle_list = new ArrayList<>();
    ArrayList<String> aname_list = new ArrayList<>();
    ArrayList<String> salbum_list = new ArrayList<>();
//    @Override
//    public void onStart() {
//        super.onStart();
//        if(i==1)
//            player.start();
//    }
    @Override
    public void onStop() {
        super.onStop();
        if (player != null){
            player.release();
            // ◁ : Stop
            // ○ : Pause
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.pause();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!player.isPlaying()){
            player.start();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_music_player, container, false);
        View main  = inflater.inflate(R.layout.activity_playeractivity, container, false);

        btn_heart = rootView.findViewById(R.id.btn_heart);
        play_title = (TextView) rootView.findViewById(R.id.play_title);
        play_artist = (TextView) rootView.findViewById(R.id.play_artist);
        img_player = (ImageView) rootView.findViewById(R.id.img_player);
        music_play = (ImageButton) rootView.findViewById(R.id.music_play);
        music_pre = rootView.findViewById(R.id.music_pre);
        music_next = rootView.findViewById(R.id.music_next);
        btn_shuffle = rootView.findViewById(R.id.btn_shuffle);
        btn_shuffle.setTag("순차재생");
        btn_repeat = rootView.findViewById(R.id.btn_repeat);
        btn_repeat.setTag("반복재생");
        seekBar_player = rootView.findViewById(R.id.seekBar_player);
        time_start = rootView.findViewById(R.id.time_start);
        time_end = rootView.findViewById(R.id.time_end);

        bundle = getArguments();
        String song_id = bundle.getString("song_id");
//        Log.d("번들에서 song_id 잘왔어?", song_id);
        try{
            index = Integer.parseInt(bundle.getString("plindex"));
            Log.d("처음index", String.valueOf(index));
        } catch(Exception e){
            index = 0;

        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");


        music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPlaying()){
                    player.pause();
                    music_play.setImageResource(R.drawable.btn_playxml);
                } else {

                    time_end.setText(timeFormat.format(player.getDuration()));
                    playClicked(view);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            try {
                            music_play.setImageResource(R.drawable.btn_playxml);
                            if(index >= song_list.size()-1 && btn_repeat.getTag().equals("반복재생")){
                                index = 0;
                            }else if(index >= song_list.size()-1 && btn_repeat.getTag().equals("안반복재생")){
                                player.stop();
                            } else {
                                index++;
                            }
                            play_title.setText(stitle_list.get(index));
                            play_artist.setText(aname_list.get(index));
                            String imgfile = "album_" + salbum_list.get(index);
                            img_player.setImageResource(getResources().getIdentifier(imgfile,"drawable",getActivity().getPackageName() ));
                            player.release();
                            player = new MediaPlayer();
                            player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                            player.setDataSource(getActivity().getFilesDir().getAbsolutePath()+"/song_mp3/"+song_list.get(index)+".mp3");
                            player.prepare();
                            time_end.setText(timeFormat.format(player.getDuration()));
                            playClicked(view);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
                    music_play.setImageResource(R.drawable.btn_pausexml);
                }
            }
        });

        music_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(index >= song_list.size()-1 && btn_repeat.getTag().equals("반복재생")){
                        index = 0;
                    }else if(index >= song_list.size()-1 && btn_repeat.getTag().equals("안반복재생")){
                        player.stop();
                    } else {
                        index++;
                    }
                    play_title.setText(stitle_list.get(index));
                    play_artist.setText(aname_list.get(index));
                    String imgfile = "album_" + salbum_list.get(index);
                    img_player.setImageResource(getResources().getIdentifier(imgfile,"drawable",getActivity().getPackageName() ));
                    player.release();
                    player = new MediaPlayer();
                    player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                    player.setDataSource(getActivity().getFilesDir().getAbsolutePath()+"/song_mp3/"+song_list.get(index)+".mp3");
                    player.prepare();
                    time_end.setText(timeFormat.format(player.getDuration()));
                    playClicked(view);
                    music_play.setImageResource(R.drawable.btn_pausexml);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        });

        music_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(index <= 0 && btn_repeat.getTag().equals("반복재생")){
                        index = song_list.size()-1;
                    }else if(index <= 0 && btn_repeat.getTag().equals("안반복재생")){
                        player.stop();
                    } else{
                        index--;
                    }
                    play_title.setText(stitle_list.get(index));
                    play_artist.setText(aname_list.get(index));
                    String imgfile = "album_" + salbum_list.get(index);
                    img_player.setImageResource(getResources().getIdentifier(imgfile,"drawable",getActivity().getPackageName() ));
                    player.release();
                    player = new MediaPlayer();
                    player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                    player.setDataSource(getActivity().getFilesDir().getAbsolutePath()+"/song_mp3/"+song_list.get(index)+".mp3");
                    player.prepare();
                    time_end.setText(timeFormat.format(player.getDuration()));
                    playClicked(view);

                    music_play.setImageResource(R.drawable.btn_pausexml);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // 셔플재생 아직 덜함.
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btn_shuffle.getTag().equals("순차재생")){
                    btn_shuffle.setImageResource(R.drawable.img_heartxml);
                    index = (int)(Math.random() * song_list.size());
                    btn_shuffle.setTag("반복재생");
                } else{
                    btn_shuffle.setImageResource(R.drawable.img_heartxml);
                    index = (int)(Math.random() * song_list.size());
                    btn_shuffle.setTag("반복재생");
                }
            }
        });

        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_repeat.getTag().equals("반복재생")){
                    btn_repeat.setImageResource(R.drawable.img_heartxml);
                    btn_repeat.setTag("안반복재생");
                } else{
                    btn_repeat.setImageResource(R.drawable.btn_repeatxml);
                    btn_repeat.setTag("반복재생");
                }
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        String url = "http://192.168.0.2:3001/InsertList";

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
                            JSONArray stitle_list2 = json.getJSONArray("stitle_list");
                            JSONArray aname_list2 = json.getJSONArray("aname_list");
                            JSONArray salbum_list2 = json.getJSONArray("salbum_list");

                            bool_heart = json.getString("bool_likes");
                            Log.d("좋아요정보", String.valueOf(bool_heart));

                            if(bool_heart.equals("1")){
                                btn_heart.setImageResource(R.drawable.img_heartxml);
                                btn_heart.setTag("hearton");
                            } else{
                                btn_heart.setImageResource(R.drawable.img_heart_emptyxml);
                                btn_heart.setTag("heartoff");
                            }

                            if(!song_list.isEmpty()){
                                song_list.clear();
                                stitle_list.clear();
                                aname_list.clear();
                                salbum_list.clear();
                            }
                            for(int i=0; i<song_list2.length(); i++){
                                song_list.add(song_list2.getString(i));
                                stitle_list.add(stitle_list2.getString(i));
                                aname_list.add(aname_list2.getString(i));
                                salbum_list.add(salbum_list2.getString(i));
                            }


                            // 노래 세팅
                            play_title.setText(stitle_list.get(index));
                            play_artist.setText(aname_list.get(index));
                            String imgfile = "album_" + salbum_list.get(index);
                            img_player.setImageResource(getResources().getIdentifier(imgfile,"drawable",getActivity().getPackageName()));

                            player = new MediaPlayer();
                            player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                            player.setDataSource(getActivity().getFilesDir().getAbsolutePath()+"/song_mp3/"+song_list.get(index)+".mp3");
                            player.prepare();



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

                                    if (requestQueue == null) {
                                        requestQueue = Volley.newRequestQueue(getActivity());
                                    }
                                    String url = "http://192.168.0.2:3001/LikesAdd";

                                    StringRequest request = new StringRequest(
                                            Request.Method.POST,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d("통신", response);

                                                    try {
                                                        JSONObject json = new JSONObject(response);
                                                        String results = json.getString("results");
                                                        bool_heart = json.getString("bool_heart");
                                                        Log.d("결과", results);

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
                                            params.put("song_id", song_list.get(index));
                                            params.put("bool_heart", String.valueOf(bool_heart));

                                            // key-value 로 만들어진 params 객체를 전송!
                                            return params;
                                        }
                                    };
                                    requestQueue.add(request);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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
                params.put("bool_heart", String.valueOf(bool_heart));

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);

        return rootView;
    }

    Handler myHandler = new Handler(){
        // HandlerMessage로 Message객체를 통해서 요청을 받아옴!!
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.arg1==1){
                time_start.setText(""+timeFormat.format(msg.arg2));
            }
        }
    };

    public void playClicked(View v){
        seekBar_player.setMax(player.getDuration());
//        time_end.setText(timeFormat.format(player.getDuration()));
        seekBar_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    player.seekTo(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        player.start();
        Mythread mythread = new Mythread();
        mythread.start();
    }

    class Mythread extends Thread{
        // 새로운 작업 공간에서 실행시킬 로직 정의!! --> run 메소드 오버라이딩
        @Override
        public void run(){
            while(player.isPlaying()){
                try{
                    Thread.sleep(1000);

                    Message msg = myHandler.obtainMessage();
                    msg.arg1 =1;
                    msg.arg2 = player.getCurrentPosition();
                    myHandler.sendMessage(msg);

                } catch(Exception e){
                    e.printStackTrace();
                }
                seekBar_player.setProgress(player.getCurrentPosition());
                Log.d("잘되긴하냐", timeFormat.format(player.getCurrentPosition()));

            }
        }
    }

}


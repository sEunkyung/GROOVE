package com.example.groove.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.Fav_Songs_Selection_Adapter;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fav_Songs_Selection extends AppCompatActivity {
    AppCompatImageButton btn_pre1, btn_next;
    TextView tv_pl_title;
    ListView like_menuList;
    ArrayList<Main_Item> dataArray;
    Fav_Songs_Selection_Adapter adapter;
    ArrayList<String> fav_sel_art;
    String user_seq;
    ArrayList<String> selectSong;
    RequestQueue requestQueue;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);

        tv_pl_title = findViewById(R.id.tv_pl_title);
        like_menuList = findViewById(R.id.like_menuList);

        tv_pl_title.setText("아티스트 곡 선택");

        // 전달한 데이터를 받을 intent
        Intent intent = getIntent();
        fav_sel_art = intent.getStringArrayListExtra("fav_sel_art");
        user_seq = intent.getStringExtra("user_seq");
        Log.d("값 받아왔냐?", String.valueOf(fav_sel_art.size()));

        // 뒤로가기
        btn_pre1 = findViewById(R.id.btn_pre1);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);

        btn_pre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://172.30.1.31:3001/Choice_songs";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("키키", String.valueOf(response));
                            JSONArray song_id = json.getJSONArray("song_id");
                            JSONArray artist_name = json.getJSONArray("artist_name");
                            JSONArray song_title = json.getJSONArray("song_title");
                            JSONArray album_id = json.getJSONArray("album_id");


                            dataArray = new ArrayList<Main_Item>();
                            for (int i = 0; i < song_id.length(); i++) {
                                dataArray.add(new Main_Item(song_title.getString(i), artist_name.getString(i), getResources().getIdentifier("album_"+ album_id.getInt(i), "drawable", getApplicationContext().getPackageName())));
                            }

                            adapter = new Fav_Songs_Selection_Adapter(getApplicationContext().getApplicationContext(), R.layout.item_favsong, dataArray);
                            like_menuList.setAdapter(adapter);
                            selectSong = new ArrayList<>();

                            like_menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    try {
                                    Log.d("카카카카", "zzz");
                                    if(mSelectedItems.get(position)){
                                        mSelectedItems.put(position, false);
                                        view.setAlpha(1);

                                        selectSong.remove(String.valueOf(song_id.getInt(position)));


                                    } else{
                                        mSelectedItems.put(position, true);
                                        view.setAlpha((float)0.3);
                                        selectSong.add(String.valueOf(song_id.getInt(position)));
                                        Log.d("몇번째지", String.valueOf(song_id.getInt(position)));
                                        Log.d("하하", selectSong.toString());
                                    }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                            btn_next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (requestQueue == null) {
                                        requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    }

                                    String url = "http://172.30.1.31:3001/FavSongsInsert";

                                    StringRequest request = new StringRequest(
                                            Request.Method.POST,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.d("통신", response);

                                                    try {
                                                        JSONObject json = new JSONObject(response);
                                                        Log.d("키키", String.valueOf(response));
                                                        String nick = json.getString("userNick");
                                                        String favart = json.getString("favArt");
                                                        String favsong = json.getString("favSong");

                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent.putExtra("nick", nick);
                                                        intent.putExtra("favart", favart);
                                                        intent.putExtra("favsong", favsong);
                                                        intent.putExtra("user_seq", user_seq);
                                                        startActivity(intent);



                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
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
                                            params.put("favSongs", selectSong.toString());
                                            params.put("user_seq", user_seq);
                                            // key-value 로 만들어진 params 객체를 전송!
                                            return params;
                                        }
                                    };

                                    requestQueue.add(request);

                                }
                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("favArt", fav_sel_art.toString());
                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);




    }




}



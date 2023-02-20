package com.example.groove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagPlayList extends AppCompatActivity {

    AppCompatImageButton btn_pre; // 이전버튼
    ImageView img_playlist; // 플리 이미지
    TextView pl_title, pl_info; // 플리 이름 / 설명
    ListView menulist;  // 플리내 수록곡이 담길곳
    ArrayList<Main_Item> dataArray;  // 데이터셋
    PlayList_Adapter adapter; // 어댑터 사용!
    RequestQueue requestQueue;

    // DB에서 받아온 정보 넣는곳-
    String tit_arr[] = new String[10];
    String art_arr[] = new String[10];
    int img_arr[] = new int[10];

    String tagName;
    int tagImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglist);

        btn_pre = findViewById(R.id.btn_pre3);
        img_playlist = findViewById(R.id.img_playlist);
        pl_title = findViewById(R.id.pl_title);
        pl_info = findViewById(R.id.pl_info);
        menulist = findViewById(R.id.play_menuList);

        Intent intent = getIntent();
        tagName = intent.getStringExtra("tagName");
        pl_title.setText("# "+tagName);
        tagImg = intent.getIntExtra("tagImg",0);
//        Log.d("카카카", String.valueOf(arr));
//        String imgfile = "tag_" + album_img.getInt(i);
//        int a =  getResources().getIdentifier(imgfile, "drawable", getActivity().getPackageName());
        img_playlist.setImageResource(tagImg);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://192.168.0.2:3001/TagList";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray song_title = json.getJSONArray("song_title");
                            JSONArray artist_name = json.getJSONArray("artist_name");
                            JSONArray album_img = json.getJSONArray("album_img");
                            JSONArray song_id = json.getJSONArray("song_id");
                            JSONArray song_lyrics = json.getJSONArray("song_lyrics");


                            dataArray = new ArrayList<Main_Item>();
                            for (int i = 0; i < song_title.length(); i++) {
                                dataArray.add(new Main_Item(song_title.getString(i), artist_name.getString(i), getResources().getIdentifier("album_"+ album_img.get(i), "drawable", getApplicationContext().getPackageName())));
                            }
                            adapter = new PlayList_Adapter(getApplicationContext().getApplicationContext(), R.layout.item_playlist, dataArray);
                            menulist.setAdapter(adapter);

                            menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {

                                    Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                                    intent.putExtra("song_id", song_id.getString(i));
                                    intent.putExtra("song_title", song_title.getString(i));
                                    intent.putExtra("artist_name", artist_name.getString(i));
                                    intent.putExtra("album_img", album_img.getString(i));
                                    intent.putExtra("song_lyrics", song_lyrics.getString(i));
                                    startActivity(intent);

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

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
                params.put("tagName", tagName);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);
    }
}

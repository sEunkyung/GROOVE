package com.example.groove.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

public class LikeList extends AppCompatActivity {
    AppCompatImageButton btn_pre1;
    TextView tv_pl_title;
    ListView like_menuList;
    ArrayList<Main_Item> dataArray;
    PlayList_Adapter adapter;
    ArrayList<String> fav_sel_art;
    RequestQueue requestQueue;
    ArrayList<String> song_list = new ArrayList<>();
    ArrayList<String> stitle_list = new ArrayList<>();
    ArrayList<String> aname_list = new ArrayList<>();
    ArrayList<String> salbum_list = new ArrayList<>();
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);

        tv_pl_title = findViewById(R.id.tv_pl_title);
        like_menuList = findViewById(R.id.like_menuList);


        // 전달한 데이터를 받을 intent
        Intent intent = getIntent();
        String user_seq = intent.getStringExtra("user_seq");

        // 뒤로가기
        btn_pre1 = findViewById(R.id.btn_pre1);
        btn_pre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://172.30.1.31:3001/LikesSongs";

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

                            JSONArray song_list2 = json.getJSONArray("song_list");
                            JSONArray stitle_list2 = json.getJSONArray("stitle_list");
                            JSONArray aname_list2 = json.getJSONArray("aname_list");
                            JSONArray salbum_list2 = json.getJSONArray("salbum_list");

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

                            dataArray = new ArrayList<Main_Item>();
                            for (int i = 0; i < song_list.size(); i++) {
                                dataArray.add(new Main_Item(stitle_list.get(i), aname_list.get(i), getResources().getIdentifier("album_"+ salbum_list.get(i), "drawable", getApplicationContext().getPackageName())));
                            }

                            adapter = new PlayList_Adapter(getApplicationContext().getApplicationContext(), R.layout.item_likelist, dataArray);
                            like_menuList.setAdapter(adapter);

                            like_menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                                    intent.putExtra("song_id", song_list.get(i));
                                    startActivity(intent);

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
                params.put("user_seq", user_seq);
                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);




    }




}



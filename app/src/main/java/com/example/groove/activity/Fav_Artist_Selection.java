package com.example.groove.activity;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.Fav_Artist_Selection_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fav_Artist_Selection extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Main_Item> favItemList;
    private Fav_Artist_Selection_RecyclerView_Adapter mRecyclerViewAdapter;
    AppCompatImageButton btn_next2, btn_pre5;
    RequestQueue requestQueue;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    int index;

    // 가수 이름(MySQL에서 받아온 데이터)
    private String artistNameArr[] = new String[10];
    // 가수 이미지(MySQL에서 받아온 데이터)
    private int artistImgArr[] = new int[10];
    private CircleImageView civ;

    private String artistIdArr[] = new String[10];

    private ArrayList<String> selectart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_artist_selection);

        mRecyclerView = findViewById(R.id.fav_re);
        civ = findViewById(R.id.fav_artist_img);
        favItemList = new ArrayList<>();
        selectart = new ArrayList<>();
        btn_next2 = findViewById(R.id.btn_next2);
        btn_pre5 = findViewById(R.id.btn_pre5);

        btn_pre5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String user_seq = intent.getStringExtra("user_seq");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://172.30.1.42:3001/Choice_art";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("선호 아티스트 목록", String.valueOf(response));
                            JSONArray artist_id = json.getJSONArray("artist_id");
                            JSONArray artist_name = json.getJSONArray("artist_name");

//                          선호아티스트 리사이클러뷰
                            for(int i=0;i<10;i++){
                            favItemList.add(new Main_Item(artist_name.getString(i), getResources().getIdentifier("artist_" + artist_id.getInt(i), "drawable", getApplicationContext().getPackageName()), View_Type_Code.ViewType.FIRST_CONTENT));
                            Log.d("크크크", String.valueOf(favItemList.get(i).getSongName()));
                            }
                            mRecyclerViewAdapter = new Fav_Artist_Selection_RecyclerView_Adapter(favItemList);
                            mRecyclerView.setAdapter(mRecyclerViewAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));	// 가로
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            mRecyclerView.setLayoutManager(gridLayoutManager);

                            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {



                                @Override
                                public void onItemClick(View view, int position) throws JSONException {

//                                    Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
//                                    song_list.add(0, song_id.getString(position));
//                                    stitle_list.add(0, song_title.getString(position));
//                                    aname_list.add(0, artist_name.getString(position));
//                                    salbum_list.add(0, album_img.getInt(position));
//                                    startActivity(intent);
                                    if(mSelectedItems.get(position)){
                                        mSelectedItems.put(position, false);
                                        view.setAlpha(1);
                                        selectart.remove(String.valueOf(artist_id.getInt(position)));

                                    } else{
                                        mSelectedItems.put(position, true);
                                        view.setAlpha((float)0.3);
                                        selectart.add(String.valueOf(artist_id.getInt(position)));
                                        Log.d("몇번째지", String.valueOf(artist_id.getInt(position)));
                                        Log.d("하하", selectart.toString());
                                    }
                                }
                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));

                            btn_next2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (requestQueue == null) {
                                        requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    }

                                    String url = "http://172.30.1.42:3001/FavArtistInsert";

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

                                                        Intent intent = new Intent(getApplicationContext(), Fav_Songs_Selection.class);
                                                        intent.putExtra("fav_sel_art", selectart);
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
                                            params.put("favArt", selectart.toString());
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
//                params.put("recSong", recentsong);
//                params.put("favSong", favsong);
//                params.put("favArt", favart);



                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);




    }


}

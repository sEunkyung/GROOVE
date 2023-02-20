package com.example.groove.fragment;

import static com.example.groove.activity.MainActivity.song_list;
import static com.example.groove.activity.MainActivity.user_seq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.activity.MainActivity;
import com.example.groove.activity.Movie_Player;
import com.example.groove.activity.Music_Player;
import com.example.groove.adapter.Main_Home_RecyclerView_Adapter;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayList extends Fragment {

    AppCompatImageButton btn_pre; // 이전버튼
    TextView tv_pl_title;
    ListView playlist;  // 플리내 수록곡이 담길곳
    ArrayList<Main_Item> dataArray;  // 데이터셋
    PlayList_Adapter adapter; // 어댑터 사용!
    RequestQueue requestQueue;

    MainActivity mainActivity;

    // DB에서 받아온 정보 넣는곳-
    ArrayList<String> tit_arr = new ArrayList<>();
    ArrayList<String> art_arr = new ArrayList<>();
    ArrayList<Integer> img_arr = new ArrayList<>();;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_likelist, container, false);


        btn_pre = view.findViewById(R.id.btn_pre1);
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onChangeFragment(0);
            }
        });

        tv_pl_title = view.findViewById(R.id.tv_pl_title);

        playlist = view.findViewById(R.id.like_menuList);

        tv_pl_title.setText("재생목록");
        Log.d("하하하하하하하하하하하하하하하하하", String.valueOf(song_list));

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://172.30.1.31:3001/SongList";

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
                            JSONArray song_lyrics = json.getJSONArray("song_lyrics");

                            dataArray = new ArrayList<Main_Item>();
                            // 재생목록 리스트뷰
                            for (int i = 0; i < song_title.length(); i++) {
                                dataArray.add(new Main_Item(song_title.getString(i), artist_name.getString(i), getResources().getIdentifier("album_"+ album_img.getInt(i), "drawable", getActivity().getPackageName())));
                            }

                            adapter = new PlayList_Adapter(getActivity().getApplicationContext(), R.layout.item_playlist, dataArray);
                            playlist.setAdapter(adapter);

                            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {

                                        Log.d("카카카카", String.valueOf(adapterView.getAdapter().getItem(i)));
                                        Intent intent = new Intent(getActivity(), Music_Player.class);
                                        intent.putExtra("song_id", song_list.get(i));
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
                params.put("user_seq", user_seq);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };
        requestQueue.add(request);
        // Inflate the layout for this fragment
        return view;
    }

}

package com.example.groove.fragment;

import static com.example.groove.activity.MainActivity.user_seq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.activity.PlayerActivity;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayList extends Fragment {

    public static int frag = 0;
    AppCompatImageButton btn_pre; // 이전버튼
    TextView tv_pl_title;
    ListView playlist;  // 플리내 수록곡이 담길곳
    ArrayList<Main_Item> dataArray;  // 데이터셋
    PlayList_Adapter adapter; // 어댑터 사용!
    RequestQueue requestQueue;
    ArrayList<String> song_list = new ArrayList<>();
    ArrayList<String> stitle_list = new ArrayList<>();
    ArrayList<String> aname_list = new ArrayList<>();
    ArrayList<String> salbum_list = new ArrayList<>();

    // DB에서 받아온 정보 넣는곳-
    ArrayList<String> tit_arr = new ArrayList<>();
    ArrayList<String> art_arr = new ArrayList<>();
    ArrayList<Integer> img_arr = new ArrayList<>();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_likelist, container, false);


        btn_pre = view.findViewById(R.id.btn_pre1);

        tv_pl_title = view.findViewById(R.id.tv_pl_title);

        playlist = view.findViewById(R.id.like_menuList);

        tv_pl_title.setText("재생목록");

        if(frag == 0){
            btn_pre.setImageResource(R.drawable.img_playlistxml);
        } else if(frag==1){
            btn_pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().remove(PlayList.this).commit();
                    fragmentManager.popBackStack();
                }
            });
        }

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://172.30.1.49:3001/SongList";

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
                            // 재생목록 리스트뷰
//                            for (int i = 0; i < song_title.length(); i++) {
//                                dataArray.add(new Main_Item(song_title.getString(i), artist_name.getString(i), getResources().getIdentifier("album_"+ album_img.getInt(i), "drawable", getActivity().getPackageName())));
//                            }
                            for (int i = 0; i < stitle_list.size(); i++) {
                                dataArray.add(new Main_Item(stitle_list.get(i), aname_list.get(i), getResources().getIdentifier("album_"+ salbum_list.get(i), "drawable", getActivity().getPackageName())));
                            }

                            adapter = new PlayList_Adapter(getActivity().getApplicationContext(), R.layout.item_playlist, dataArray);
                            playlist.setAdapter(adapter);

                            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.d("카카카카", String.valueOf(adapterView.getAdapter().getItem(i)));
                                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                                    intent.putExtra("song_id",song_list.get(i));
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
        // Inflate the layout for this fragment
        return view;
    }

}

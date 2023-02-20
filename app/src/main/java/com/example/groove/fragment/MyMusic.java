package com.example.groove.fragment;

import static com.example.groove.activity.MainActivity.user_seq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.activity.Music_Player;
import com.example.groove.adapter.MyMusic_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MyMusic extends Fragment {

    private RecyclerView mRecentView;
    private RecyclerView mLikeView;
    private RecyclerView mFavArtistView;
    private ArrayList<Main_Item> mMusicItemList;
    private MyMusic_RecyclerView_Adapter mMusicListAdapter;
    GridLayoutManager gridLayoutManager;

    // 최근 정보(MySQL에서 받아온 데이터)
//    private String songNameArr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//    private int albumImgArr[] = {R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
//            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
//            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152};
    private String songNameArr[] = new String[4];
    private int albumImgArr[] = new int[4];


    // 좋아요 정보(MySQL에서 받아온 데이터)
    private String songNameArr2[] = {"11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private int albumImgArr2[] = {R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152};

    // 선호 아티스트 정보(MySQL에서 받아온 데이터, 유저정보)
    private String songNameArr3[] = {"21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
    private int albumImgArr3[] = {R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152};

    RequestQueue requestQueue;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mymusic, container, false);



        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://192.168.0.2:3001/RecentSong";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MyMusic",response);
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d("최근에 뭘 들었나", String.valueOf(response));
                            JSONArray song_title = json.getJSONArray("song_title");
                            JSONArray album_img = json.getJSONArray("album_id");

                            for (int i=0; i<4; i++){
                                songNameArr[i] = song_title.getString(i);
                                String imgfile = "album_" + album_img.getInt(i);
                                albumImgArr[i] = getResources().getIdentifier(imgfile, "drawable", getActivity().getPackageName());

                            }
                            Log.d("user_seq", String.valueOf(user_seq));

                            // 최근 곡 리사이클러뷰
                            mMusicItemList = new ArrayList<>();
                            for(int i=0;i<4;i++){
                                mMusicItemList.add(new Main_Item(songNameArr[i], albumImgArr[i], View_Type_Code.ViewType.FIRST_CONTENT));
                            }
                            mRecentView = rootView.findViewById(R.id.list_recent);
                            mMusicListAdapter = new MyMusic_RecyclerView_Adapter(mMusicItemList);
                            mRecentView.setAdapter(mMusicListAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mRecentView.setLayoutManager(gridLayoutManager);	// 가로

                            mRecentView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecentView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) throws JSONException {
                                    Intent intent = new Intent(getActivity(), Music_Player.class);
                                    intent.putExtra("song_title", song_title.getString(position));
                                    intent.putExtra("album_img", album_img.getString(position));
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));

                            // 좋아요한 곡 리사이클러뷰
                            mMusicItemList = new ArrayList<>();
                            for(int i=0;i<10;i++){
                                mMusicItemList.add(new Main_Item(songNameArr2[i], albumImgArr2[i], View_Type_Code.ViewType.SECOND_CONTENT));
                            }
                            mLikeView = rootView.findViewById(R.id.list_likesong);
                            mMusicListAdapter = new MyMusic_RecyclerView_Adapter(mMusicItemList);
                            mLikeView.setAdapter(mMusicListAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mLikeView.setLayoutManager(gridLayoutManager);	// 가로

                            // 선호 아티스트 선택 리사이클러뷰
                            mMusicItemList = new ArrayList<>();
                            for(int i=0;i<10;i++){
                                mMusicItemList.add(new Main_Item(songNameArr3[i], albumImgArr3[i], View_Type_Code.ViewType.THIRD_CONTENT));
                            }
                            mFavArtistView = rootView.findViewById(R.id.list_favartist);
                            mMusicListAdapter = new MyMusic_RecyclerView_Adapter(mMusicItemList);
                            mFavArtistView.setAdapter(mMusicListAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mFavArtistView.setLayoutManager(gridLayoutManager);	// 가로

                        }catch (JSONException e){
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MyMusic","실패");
                    }
                }
        ){
            //request객체에 데이터를 묶어서 보낼 곳
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // getParams ➡ POST 방식으로 데이터를 보내는 메소드
                // 데이터를 key - value 형태로 보냄
                Map<String, String> params = new HashMap<String, String>();
                // params ➡ key - value 형태로 만들어서 저장
                params.put("user_seq", user_seq);

                return params;
            }
        };
        requestQueue.add(request);

        return rootView;
    }

}

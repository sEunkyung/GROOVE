package com.example.groove.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.groove.adapter.Main_Home_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.View_Type_Code;
import com.google.common.io.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main_Home extends Fragment {
    private RecyclerView mSongView;
    private RecyclerView mTagView;
    private RecyclerView mMvView;
    private ArrayList<Main_Item> mMainItemList;
    private Main_Home_RecyclerView_Adapter mMainHomeRecyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    private TextView txt_nick;

    RequestQueue requestQueue;

    // 곡 정보(MySQL에서 받아온 데이터)
    String songNameArr[] = new String[9];
    String artistNameArr[] = new String[9];
    int albumImgArr[] = new int[9];
    // 태그 정보(MySQL에서 받아온 데이터)
    String tagNameArr[] = new String[6];
    int tagImgArr[] = new int[6];
    // 뮤비 정보(MySQL에서 받아온 데이터)
    String mvNameArr[] = new String[9];
    String mvUrlArr[] = new String[9];
    String artArr[] = new String[9];

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_home, container, false);
        View main  = inflater.inflate(R.layout.activity_navigation_main_menu, container, false);


        txt_nick = rootView.findViewById(R.id.txt_nick);
        Bundle bundle = getArguments();
        String text = bundle.getString("nick");
        String favart = bundle.getString("favart");
        String recentsong = bundle.getString("recentsong");
        String favsong = bundle.getString("favsong");
        txt_nick.setText(text+" 님을 위한,");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://172.30.1.42:3001/RecommendSong";

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
                            JSONArray song_title = json.getJSONArray("song_title");
                            JSONArray artist_name = json.getJSONArray("artist_name");
                            JSONArray album_img = json.getJSONArray("album_img");
                            JSONArray mv_url = json.getJSONArray("video_url");
                            JSONArray mv_tit = json.getJSONArray("video_title");
                            JSONArray art_id = json.getJSONArray("artist_id");

                            for(int i=0; i<9; i++){
                                songNameArr[i] = song_title.getString(i);
                                artistNameArr[i] = artist_name.getString(i);
                                String imgfile = "album_" + album_img.getInt(i);
                                albumImgArr[i] =  getResources().getIdentifier(imgfile, "drawable", getActivity().getPackageName());
                                String mvurl = mv_url.getString(i);
                                if(mvurl.contains("www")){
                                    mvurl = mvurl.replace("https://www.youtube.com/watch?v=","");
                                } else{
                                    mvurl = mvurl.replace("https://youtube.com/watch?v=","");
                                }
                                Log.d("유알엘", mvurl);
                                mvUrlArr[i] = "https://img.youtube.com/vi/"+mvurl+"/maxresdefault.jpg";
                                mvNameArr[i] = mv_tit.getString(i);
                                artArr[i] = art_id.getString(i);
                                Log.d("유알엘", mvUrlArr[i]);
                            }

                            // 추천 곡 리사이클러뷰
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<songNameArr.length;i++){
                                mMainItemList.add(new Main_Item(songNameArr[i], artistNameArr[i], albumImgArr[i], View_Type_Code.ViewType.FIRST_CONTENT));
                            }
                            mSongView = rootView.findViewById(R.id.list_recsong);
                            mMainHomeRecyclerViewAdapter = new Main_Home_RecyclerView_Adapter(mMainItemList);
                            mSongView.setAdapter(mMainHomeRecyclerViewAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 3, GridLayoutManager.HORIZONTAL, false);
                            mSongView.setLayoutManager(gridLayoutManager);	// 가로

                            // 태그 곡 리사이클러뷰
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<tagNameArr.length;i++){
                                mMainItemList.add(new Main_Item(tagNameArr[i], tagImgArr[i], View_Type_Code.ViewType.SECOND_CONTENT));
                            }
                            mTagView = rootView.findViewById(R.id.list_hashtag);
                            mMainHomeRecyclerViewAdapter = new Main_Home_RecyclerView_Adapter(mMainItemList);
                            mTagView.setAdapter(mMainHomeRecyclerViewAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mTagView.setLayoutManager(gridLayoutManager);	// 가로

                            // 추천 뮤비 리사이클러뷰
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<mvNameArr.length;i++){
                                mMainItemList.add(new Main_Item(mvNameArr[i], View_Type_Code.ViewType.THIRD_CONTENT, mvUrlArr[i]));
                            }
                            mMvView = rootView.findViewById(R.id.list_mv);
                            mMainHomeRecyclerViewAdapter = new Main_Home_RecyclerView_Adapter(mMainItemList);
                            mMvView.setAdapter(mMainHomeRecyclerViewAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mMvView.setLayoutManager(gridLayoutManager);	// 가로

                            mMvView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                @Override
                                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

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
                params.put("recSong", recentsong);
                params.put("favArt", favart);
                params.put("favSong", favsong);


                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);


        return rootView;
    }





}
package com.example.groove.fragment;

import static com.example.groove.activity.Login.my_url;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.example.groove.activity.Fav_Artist_Selection;
import com.example.groove.activity.Movie_Player;
import com.example.groove.activity.PlayerActivity;
import com.example.groove.activity.Search;
import com.example.groove.activity.TagPlayList;
import com.example.groove.adapter.Main_Home_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

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
    private RecyclerView mPickView;
    private ArrayList<Main_Item> mMainItemList;
    private Main_Home_RecyclerView_Adapter mMainHomeRecyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    private TextView txt_nick;

    AppCompatImageButton btn_search;
    RequestQueue requestQueue;

    // 곡 정보(MySQL에서 받아온 데이터)
    String songNameArr[] = new String[9];
    String artistNameArr[] = new String[9];
    int albumImgArr[] = new int[9];
    // 태그 정보(MySQL에서 받아온 데이터)
    String tagNameArr[] = {"드라이브", "노동요", "헬스, 런닝", "요가, 필라테스", "데이트", "이별"};
    int tagImgArr[] = {R.drawable.tag_driveicon, R.drawable.tag_workicon, R.drawable.tag_healthicon, R.drawable.tag_yogaicon, R.drawable.tag_dateicon, R.drawable.tag_sadicon};
    // 뮤비 정보(MySQL에서 받아온 데이터)
    String mvNameArr[] = new String[9];
    String mvUrlArr[] = new String[9];
    String artArr[] = new String[9];
    int editorImgArr[] = {R.drawable.editor1, R.drawable.editor2, R.drawable.editor3, R.drawable.editor4};
    String editorNameArr[] = {"멜로디가 기억에 남는 랩", "그루브 가득한 힙합", "여름에 듣는 시원한 힙합", "트렌디한 힙합"};


    String item_song_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_home, container, false);
        View main  = inflater.inflate(R.layout.activity_navigation_main_menu, container, false);

        txt_nick = rootView.findViewById(R.id.txt_nick);
        Bundle bundle = getArguments();
        String text = bundle.getString("nick");
        String favart = bundle.getString("favart");
        String favsong = bundle.getString("favsong");
        // 다음 과제로 해볼것 최근 들은 곡이 있다면 그 분위기에 맞는 곡 추천
        String recsong = "";

        btn_search = rootView.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);

            }
        });

        txt_nick.setText(text+" 님을 위한,");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://"+my_url+":3001/RecommendSong";

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
                            JSONArray song_id = json.getJSONArray("song_id");

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

                            mSongView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mSongView, new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) throws JSONException {

                                            item_song_id = song_id.getString(position);

                                            Intent intent = new Intent(getActivity(), PlayerActivity.class);
                                            intent.putExtra("song_id", item_song_id);
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onLongItemClick(View view, int position) {

                                        }
                                    }));

                            // 태그 곡 리사이클러뷰
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<tagNameArr.length;i++){
                                mMainItemList.add(new Main_Item("# " + tagNameArr[i], tagImgArr[i], View_Type_Code.ViewType.SECOND_CONTENT));
                            }
                            mTagView = rootView.findViewById(R.id.list_hashtag);
                            mMainHomeRecyclerViewAdapter = new Main_Home_RecyclerView_Adapter(mMainItemList);
                            mTagView.setAdapter(mMainHomeRecyclerViewAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mTagView.setLayoutManager(gridLayoutManager);	// 가로

                            mTagView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mTagView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) throws JSONException {

                                    Intent intent = new Intent(getActivity(), TagPlayList.class);
                                    intent.putExtra("tagName", tagNameArr[position]);
                                    Log.d("카카카", String.valueOf(tagImgArr[position]));
                                    Log.d("zzz카카", String.valueOf(R.drawable.tag_drive));
                                    intent.putExtra("tagImg", tagImgArr[position]);
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));

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

                            mMvView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mMvView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) throws JSONException {
                                    Intent intent = new Intent(getActivity(), Movie_Player.class);
                                    intent.putExtra("art_id", art_id.getString(position));
                                    startActivity(intent);

                                }
                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));

                            // 에디터픽 리사이클러뷰
                            mMainItemList = new ArrayList<>();
                            for(int i=0;i<editorNameArr.length;i++){
                                mMainItemList.add(new Main_Item(editorNameArr[i], editorImgArr[i], View_Type_Code.ViewType.FOURTH_CONTENT));
                            }

                            mPickView = rootView.findViewById(R.id.list_pick);
                            mMainHomeRecyclerViewAdapter = new Main_Home_RecyclerView_Adapter(mMainItemList);
                            mPickView.setAdapter(mMainHomeRecyclerViewAdapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            mPickView.setLayoutManager(gridLayoutManager);	// 가로

//                            mTagView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mTagView, new RecyclerItemClickListener.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) throws JSONException {
//
//                                    Intent intent = new Intent(getActivity(), TagPlayList.class);
//                                    intent.putExtra("tagName", tagNameArr[position]);
//                                    Log.d("카카카", String.valueOf(tagImgArr[position]));
//                                    Log.d("zzz카카", String.valueOf(R.drawable.tag_drive));
//                                    intent.putExtra("tagImg", tagImgArr[position]);
//                                    startActivity(intent);
//                                }
//
//                                @Override
//                                public void onLongItemClick(View view, int position) {
//
//                                }
//                            }));



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
                params.put("favSong", favsong);
                params.put("recSong", recsong);
                params.put("favArt", favart);



                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);


        return rootView;
    }





}
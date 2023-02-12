package com.example.groove.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groove.R;
import com.example.groove.adapter.MyMusic_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.View_Type_Code;

import java.util.ArrayList;

public class MyMusic extends Fragment {

    private RecyclerView mRecentView;
    private RecyclerView mLikeView;
    private RecyclerView mFavArtistView;
    private ArrayList<Main_Item> mMusicItemList;
    private MyMusic_RecyclerView_Adapter mMusicListAdapter;
    GridLayoutManager gridLayoutManager;

    // 최근 정보(MySQL에서 받아온 데이터)
    private String songNameArr[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private int albumImgArr[] = {R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152,
            R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152, R.drawable.album_745152};
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


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mymusic, container, false);

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

        return rootView;
    }

}

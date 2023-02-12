package com.example.groove.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.groove.R;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;

import java.util.ArrayList;

public class PlayList extends Fragment {

    AppCompatImageButton btn_pre; // 이전버튼
    TextView tv_pl_title;
    ListView playlist;  // 플리내 수록곡이 담길곳
    ArrayList<Main_Item> dataArray;  // 데이터셋
    PlayList_Adapter adapter; // 어댑터 사용!

    // DB에서 받아온 정보 넣는곳-
    String tit_arr[] = {"music1"};
    String art_arr[] = {"신은경"};
    int img_arr[] = {R.drawable.artist_739740};;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_likelist, container, false);


        btn_pre = view.findViewById(R.id.btn_pre1);

        tv_pl_title = view.findViewById(R.id.tv_pl_title);

        playlist = view.findViewById(R.id.like_menuList);
        dataArray = new ArrayList<Main_Item>();

        // 재생목록 리스트뷰
        tv_pl_title.setText("재생목록");
        for (int i = 0; i < 1; i++) {
            dataArray.add(new Main_Item(tit_arr[i], art_arr[i], img_arr[i]));
        }

        adapter = new PlayList_Adapter(getActivity().getApplicationContext(), R.layout.item_playlist, dataArray);

        playlist.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

}

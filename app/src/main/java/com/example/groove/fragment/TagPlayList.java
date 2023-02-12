package com.example.groove.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.example.groove.R;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;

import java.util.ArrayList;

public class TagPlayList extends Fragment {

    AppCompatImageButton btn_pre; // 이전버튼
    ImageView img_playlist; // 플리 이미지
    TextView pl_title, pl_info; // 플리 이름 / 설명
    ListView menulist;  // 플리내 수록곡이 담길곳
    ArrayList<Main_Item> dataArray;  // 데이터셋
    PlayList_Adapter adapter; // 어댑터 사용!

    // DB에서 받아온 정보 넣는곳-
    String tit_arr[] = {"music1"};
    String art_arr[] = {"신은경"};
    int img_arr[] = {R.drawable.album_745152};;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taglist, container, false);


        btn_pre = view.findViewById(R.id.btn_pre3);
        img_playlist = view.findViewById(R.id.img_playlist);
        pl_title = view.findViewById(R.id.pl_title);
        pl_info = view.findViewById(R.id.pl_info);
        menulist = view.findViewById(R.id.play_menuList);
        dataArray = new ArrayList<Main_Item>();

        //
        for (int i = 0; i < 1; i++) {
            dataArray.add(new Main_Item(tit_arr[i], art_arr[i], img_arr[i]));
        }

        adapter = new PlayList_Adapter(getActivity().getApplicationContext(), R.layout.item_playlist, dataArray);

        menulist.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

}

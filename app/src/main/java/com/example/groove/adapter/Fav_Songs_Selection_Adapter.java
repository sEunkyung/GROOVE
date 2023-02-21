package com.example.groove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.groove.R;
import com.example.groove.data.Main_Item;

import java.util.ArrayList;

public class Fav_Songs_Selection_Adapter extends BaseAdapter {

    private Context context; // 현재 액티비티의 정보
    private int layout; // 리스트뷰에 보여질 항목의 레이아웃에 대한 정보
    private ArrayList<Main_Item> dataArray; // 데이터셋
    private LayoutInflater inflater; // xml -> View 객체로 변환

    public Fav_Songs_Selection_Adapter(Context context, int layout, ArrayList<Main_Item> dataArray){
        this.context = context;
        this.layout = layout;
        this.dataArray = dataArray;
        // xml --> view 객체로 만들 수 있는 기능을 받아옴!
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Object getItem(int i) {
        return dataArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // my_item.xml 을 가져와 데이터를 담아준 다음에 ListView로 출력시켜야함!

        // layout 에 저장되어있는 my_item.xml 을 java에서 사용할 수 있도록 view 객체로 변환한다는 의미
        view = inflater.inflate(layout, null);

        ImageView img_album = view.findViewById(R.id.img_album_like);
        TextView tv_title = view.findViewById(R.id.tv_title_like);
        TextView tv_singer = view.findViewById(R.id.tv_singer_like);


        img_album.setImageResource(dataArray.get(i).getAlbumImg());
        tv_title.setText(dataArray.get(i).getSongName());
        tv_singer.setText(dataArray.get(i).getArtistName());
        return view;
    }

}

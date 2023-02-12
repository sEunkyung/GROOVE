package com.example.groove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groove.R;
import com.example.groove.data.Main_Item;
import com.example.groove.data.View_Type_Code;

import java.util.ArrayList;

public class MyMusic_RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Main_Item> mMainItemList = null;

    public MyMusic_RecyclerView_Adapter(ArrayList<Main_Item> dataList){
        this.mMainItemList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == View_Type_Code.ViewType.FIRST_CONTENT){
            view = inflater.inflate(R.layout.item_recentartist, parent, false);
            return new MyMusicRecentViewHolder(view);
        } else if(viewType == View_Type_Code.ViewType.SECOND_CONTENT){
            view = inflater.inflate(R.layout.item_storage, parent, false);
            return new MyMusicLikesViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_fav_artist, parent, false);
            return new MyMusicFavArtistViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof MyMusicRecentViewHolder){
            Main_Item item = mMainItemList.get(position);
            ((MyMusicRecentViewHolder) holder).songName.setText(item.getSongName());
            ((MyMusicRecentViewHolder) holder).albumImg.setImageResource(item.getAlbumImg());
        } else if(holder instanceof MyMusicLikesViewHolder){
            Main_Item item = mMainItemList.get(position);
            ((MyMusicLikesViewHolder) holder).songName2.setText(item.getSongName());
            ((MyMusicLikesViewHolder) holder).albumImg2.setImageResource(item.getAlbumImg());
        } else{
            Main_Item item = mMainItemList.get(position);
            ((MyMusicFavArtistViewHolder) holder).artistName.setText(item.getSongName());
            ((MyMusicFavArtistViewHolder) holder).artistImg.setImageResource(item.getAlbumImg());
        }

    }

    @Override
    public int getItemCount() {
        return mMainItemList.size();
    }

    public int getItemViewType(int position){
        return mMainItemList.get(position).getViewType();
    }
    public class MyMusicRecentViewHolder extends RecyclerView.ViewHolder{

        TextView songName;
        ImageView albumImg;


        // 각각의 item에 대한 뷰가 뷰홀더의 파라미터로 전달 됨.
        public MyMusicRecentViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.mymusic_name);
            albumImg = itemView.findViewById(R.id.mymusic_img);
        }
    }
    public class MyMusicLikesViewHolder extends RecyclerView.ViewHolder {

        TextView songName2;
        ImageView albumImg2;

        public MyMusicLikesViewHolder(@NonNull View itemView) {
            super(itemView);
            songName2 = itemView.findViewById(R.id.mymusic_name);
            albumImg2 = itemView.findViewById(R.id.mymusic_img);

        }
    }
    public class MyMusicFavArtistViewHolder extends RecyclerView.ViewHolder{

        TextView artistName;
        ImageView artistImg;

        public MyMusicFavArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.fav_artist);
            artistImg = itemView.findViewById(R.id.fav_img);

        }
    }


}

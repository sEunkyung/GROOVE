package com.example.groove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groove.R;
import com.example.groove.data.Main_Item;
import com.example.groove.data.View_Type_Code;

import java.util.ArrayList;

public class Main_Home_RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Main_Item> mMainItemList = null;

    public Main_Home_RecyclerView_Adapter(ArrayList<Main_Item> dataList){
        this.mMainItemList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == View_Type_Code.ViewType.FIRST_CONTENT){
            view = inflater.inflate(R.layout.item_main_song, parent, false);
            return new MainSongViewHolder(view);
        } else if(viewType == View_Type_Code.ViewType.SECOND_CONTENT){
            view = inflater.inflate(R.layout.item_main_tag, parent, false);
            return new MainTagViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_main_mv, parent, false);
            return new MainMvViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if(holder instanceof MainSongViewHolder){
            Main_Item item = mMainItemList.get(position);
            ((MainSongViewHolder) holder).songName.setText(item.getSongName());
            ((MainSongViewHolder) holder).artistName.setText(item.getArtistName());
            ((MainSongViewHolder) holder).albumImg.setImageResource(item.getAlbumImg());
        } else if(holder instanceof MainTagViewHolder){
            Main_Item item = mMainItemList.get(position);
            ((MainTagViewHolder) holder).tagName.setText(item.getSongName());
            ((MainTagViewHolder) holder).tagImg.setImageResource(item.getAlbumImg());
        } else{
            Main_Item item = mMainItemList.get(position);
            ((MainMvViewHolder) holder).mvName.setText(item.getSongName());
//            ((MainMvViewHolder) holder).mvImg.setImageResource(item.getAlbumImg());
            Glide.with(holder.itemView).load(item.getMvimg()).into(((MainMvViewHolder) holder).mvImg);
        }

    }

    @Override
    public int getItemCount() {
        return mMainItemList.size();
    }

    public int getItemViewType(int position){
        return mMainItemList.get(position).getViewType();
    }
    public class MainSongViewHolder extends RecyclerView.ViewHolder{

        TextView songName, artistName;
        ImageView albumImg;


        // 각각의 item에 대한 뷰가 뷰홀더의 파라미터로 전달 됨.
        public MainSongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songName);
            artistName = itemView.findViewById(R.id.artistName);
            albumImg = itemView.findViewById(R.id.albumImg);

        }
    }
    public class MainTagViewHolder extends RecyclerView.ViewHolder {

        TextView tagName;
        ImageView tagImg;

        public MainTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);
            tagImg = itemView.findViewById(R.id.tagImg);

        }
    }
    public class MainMvViewHolder extends RecyclerView.ViewHolder{

        TextView mvName;
        ImageView mvImg;

        public MainMvViewHolder(@NonNull View itemView) {
            super(itemView);
            mvName = itemView.findViewById(R.id.mvName);
            mvImg = itemView.findViewById(R.id.mvImg);

        }
    }


}

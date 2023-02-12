package com.example.groove.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groove.R;
import com.example.groove.data.Main_Item;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fav_Artist_Selection_RecyclerView_Adapter extends RecyclerView.Adapter<Fav_Artist_Selection_RecyclerView_Adapter.ViewHolder> {

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fav_artist_name;
        CircleImageView fav_artist_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fav_artist_name = (TextView)itemView.findViewById(R.id.fav_artist_name);
            fav_artist_img = (CircleImageView)itemView.findViewById(R.id.fav_artist_img);

            fav_artist_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();

                    if ( mSelectedItems.get(position, false) ){
                        mSelectedItems.put(position, false);
                        view.setAlpha(1);
                    } else {
                        mSelectedItems.put(position, true);
                        view.setAlpha((float) 0.3);
                    }
                }
            });

        }
    }

    private ArrayList<Main_Item> favList = null;

    public Fav_Artist_Selection_RecyclerView_Adapter(ArrayList<Main_Item> favList) {
        this.favList = favList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_fav_artist_choice, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Main_Item item = favList.get(position);
        holder.fav_artist_name.setText(item.getSongName());
        holder.fav_artist_img.setImageResource(item.getAlbumImg());
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

}

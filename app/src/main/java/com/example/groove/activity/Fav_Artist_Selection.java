package com.example.groove.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groove.R;
import com.example.groove.adapter.Fav_Artist_Selection_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.View_Type_Code;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fav_Artist_Selection extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Main_Item> favItemList;
    private Fav_Artist_Selection_RecyclerView_Adapter mRecyclerViewAdapter;

    ImageButton btn_next2;

    TextView fav_artist_name;

    int index;

    // 가수 이름(MySQL에서 받아온 데이터)
    private String artistNameArr[] = {"빈지노다이나믹듀오에픽하이기리보이", "다이나믹듀오","에픽하이","기리보이",
            "릴러말즈", "이영지", "박재범","사이먼도미닉"};
    // 가수 이미지(MySQL에서 받아온 데이터)
    private int artistImgArr[] = {R.drawable.album_5302, R.drawable.album_40134,
            R.drawable.album_40482, R.drawable.album_351473, R.drawable.album_325951,
            R.drawable.album_333190, R.drawable.album_684955, R.drawable.album_684955};
    private CircleImageView civ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_artist_selection);

        mRecyclerView = findViewById(R.id.fav_re);
        civ = findViewById(R.id.fav_artist_img);
        favItemList = new ArrayList<>();

        for(int i=0;i<8;i++){
            favItemList.add(new Main_Item(artistNameArr[i], artistImgArr[i], View_Type_Code.ViewType.FIRST_CONTENT));
            Log.d("크크크", String.valueOf(favItemList.get(i).getSongName()));
        }

        mRecyclerViewAdapter = new Fav_Artist_Selection_RecyclerView_Adapter(favItemList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));	// 가로
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        btn_next2 = findViewById(R.id.btn_next2);
//        fav_artist_name = findViewById(R.id.fav_artist_name);
//        String favArtist = fav_artist_name.getText().toString();
//        String url = "";

        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), LikeList.class);

                startActivity(intent);
                finish();
            }
        });
    }
}

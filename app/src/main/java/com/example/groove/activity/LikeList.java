package com.example.groove.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groove.R;

public class LikeList extends AppCompatActivity {
    ImageButton btn_pre1;
    TextView like_menuList;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);

        // 전달한 데이터를 받을 intent
        Intent intent = getIntent();
        String fav_sel_art = intent.getStringExtra("fav_sel_art");
        String fav_sel_name = intent.getStringExtra("fav_sel_name");

        //like_menuList = findViewById(R.id.like_menuList); //임시


        // 뒤로가기
        btn_pre1 = findViewById(R.id.btn_pre1);

        btn_pre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Fav_Artist_Selection.class);
                startActivity(intent);
                Log.d("결과값", String.valueOf(intent));
                finish();
            }



        });

        // 선택 아티스트 앨범, 아티스트 이름, 음원 제목



    }




}



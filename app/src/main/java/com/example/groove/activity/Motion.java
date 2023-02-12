package com.example.groove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groove.R;

public class Motion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motionlayout);

        View layout = findViewById(R.id.motionLayout);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // 온터치 메소드의 구분자역할을 함.
                int action = event.getAction();

                // layout의 가로 위치를 가져옴
                // float curX = event.getX();

                // layout의 세로 위치를 가져옴
                float curY = event.getY();
                // 손가락이 떼졌을 때 가로와 세로 위치를 가져옴--> 손가락 상황에 따라서 여러가지 효과를 줄 수 있음
                if (action==MotionEvent.ACTION_UP){

                    // 만약 세로를 얼마이상 움직인 후 화면에서 손가락을 뗐다면 인텐트 실행해서 다른 액티비티 실행
                    if (curY>300){
                        Intent intent = new Intent(Motion.this, Music_Player.class);
                        startActivity(intent);
                        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                    // Log.d("Test2123",curX +":"+curY);

                }
                return false;
            }
        });
    }
}
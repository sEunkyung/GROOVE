package com.example.groove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import static com.example.groove.fragment.PlayList.frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.groove.R;
import com.example.groove.fragment.Main_Home;
import com.example.groove.fragment.MyMusic;
import com.example.groove.fragment.PlayList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    public static ArrayList<String> song_list = new ArrayList<>();
//    public static ArrayList<String> stitle_list = new ArrayList<>();
//    public static ArrayList<String> aname_list = new ArrayList<>();
//    public static ArrayList<Integer> salbum_list = new ArrayList<>();
    public static String user_seq;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Main_Home fragmentHome = new Main_Home();
    private PlayList fragmentList = new PlayList();
    private MyMusic fragmentMyMusic = new MyMusic();

    // 로그인 창에서 받아온 데이터
    Bundle bundle = new Bundle();
    Intent intent;
    String nick, favart, favsong;
    Main_Home mainHome; // 메인 프래그먼트
    MyMusic myMusic; // 보관함 프래그먼트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main_menu);

        frag =0;
        intent = getIntent();
        nick = intent.getStringExtra("nick");
        favart = intent.getStringExtra("favart");
        favsong = intent.getStringExtra("favsong");
        user_seq = intent.getStringExtra("user_seq");

        bundle.putString("nick", nick);
        bundle.putString("favart", favart);
        bundle.putString("favsong", favsong);

        // 받아온 데이터 홈 프래그먼트에 보내기
        fragmentHome.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        // 프래그먼트
        mainHome = new Main_Home();
        myMusic = new MyMusic();

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.music_relative: // 메인홈 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.music_lyrics: // 플레이리스트 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentList).commitAllowingStateLoss();
                    break;
                case R.id.music_list: // 내음악 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentMyMusic).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
    // 프래그먼트 번호에 따라 페이지 이동
    public void onChangeFragment(int index){
        if (index == 0){// 메인 홈
            getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, fragmentHome).commit();
        }
    }
}

package com.example.groove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.fragment.Main_Home;
import com.example.groove.fragment.MyMusic;
import com.example.groove.fragment.PlayList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> song_list = new ArrayList<>();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Main_Home fragmentHome = new Main_Home();
    private PlayList fragmentList = new PlayList();
    private MyMusic fragmentMyMusic = new MyMusic();

    // 로그인 창에서 받아온 데이터
    Bundle bundle = new Bundle();
    Intent intent;
    String nick, favart, recentsong, favsong;
    public static String user_seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main_menu);

        intent = getIntent();
        nick = intent.getStringExtra("nick");
        favart = intent.getStringExtra("favart");
        recentsong = intent.getStringExtra("recentsong");
        favsong = intent.getStringExtra("favsong");
        user_seq = intent.getStringExtra("user_seq");

        song_list = intent.getStringArrayListExtra("song_list");
        Log.d("하하하", String.valueOf(song_list));

        bundle.putString("nick", nick);
        bundle.putString("favart", favart);
        bundle.putString("recentsong", recentsong);
        bundle.putString("favsong", favsong);
        bundle.putStringArrayList("song_list", song_list);
        bundle.putString("user_seq", user_seq);

        // 받아온 데이터 홈 프래그먼트에 보내기
        fragmentHome.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_home: // 메인홈 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.menu_list: // 플레이리스트 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentList).commitAllowingStateLoss();
                    break;
                case R.id.menu_mymusic: // 내음악 프래그먼트
                    transaction.replace(R.id.menu_frame_layout, fragmentMyMusic).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}

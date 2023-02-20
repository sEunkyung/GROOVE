package com.example.groove.activity;

import static android.app.PendingIntent.getActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.Fav_Artist_Selection_RecyclerView_Adapter;
import com.example.groove.adapter.Main_Home_RecyclerView_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;
import com.example.groove.fragment.Main_Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fav_Artist_Selection extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Main_Item> favItemList;
    private Fav_Artist_Selection_RecyclerView_Adapter mRecyclerViewAdapter;

    ImageButton btn_next2;
    RequestQueue requestQueue;



    int index;

    // 가수 이름(MySQL에서 받아온 데이터)
    private String artistNameArr[] = new String[8];
    // 가수 이미지(MySQL에서 받아온 데이터)
    private int artistImgArr[] = new int[8];
    private CircleImageView civ;

    private String artistIdArr[] = new String[8];

    private ArrayList<String> selectart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_artist_selection);

        mRecyclerView = findViewById(R.id.fav_re);
        civ = findViewById(R.id.fav_artist_img);
        favItemList = new ArrayList<>();
        selectart = new ArrayList<>();


//        for(int i=0;i<8;i++){
//            favItemList.add(new Main_Item(artistNameArr[i], artistImgArr[i], View_Type_Code.ViewType.FIRST_CONTENT));
//            Log.d("크크크", String.valueOf(favItemList.get(i).getSongName()));
//        }
        mRecyclerViewAdapter = new Fav_Artist_Selection_RecyclerView_Adapter(favItemList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));	// 가로
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        btn_next2 = findViewById(R.id.btn_next2);
//        fav_artist_name = findViewById(R.id.fav_artist_name);
//        String favArtist = fav_artist_name.getText().toString();
//        String url = "";


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://172.30.1.34:3001/Choice_art";


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        Log.d("통신",response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray fav_art = json.getJSONArray("fav_art");
                            JSONArray fav_name = json.getJSONArray("fav_name");

                            Random random = new Random();
                            int[] k = new int[8];
                            boolean check = true;
                            for(int i=0; i<8; i++) {
                                check = true;
                                // 랜덤으로 아티스트 불러오기 8개
                                //int I = random.nextInt(20) + 1; // 너무 많으면 렉걸려서 20개
                                int rnd = random.nextInt(100)+1;
                                k[i] = rnd;
                                artistIdArr[i] = fav_art.getString(rnd);
                                artistNameArr[i] = fav_name.getString(rnd);
                                String imgfile = "artist_" + fav_art.getString(rnd);
                                artistImgArr[i] =  getResources().getIdentifier(imgfile, "drawable", getApplicationContext().getPackageName());

                                for(int j=0; j<i; j++) {
                                    if(k[j] == k[i]) {
                                        i--;
                                        check = false;
                                    }
                                }
                                if(check == true) {
                                    favItemList.add(new Main_Item(artistNameArr[i], artistImgArr[i], View_Type_Code.ViewType.FIRST_CONTENT));
                                    //selectart.add(Integer.parseInt(artistIdArr[i]), artistNameArr[i]);
                                }
                            }

                            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) throws JSONException {
                               Intent intent = new Intent(getApplicationContext(), LikeList.class);
                                intent.putExtra("fav_sel_art", fav_art.getString(position));
                                intent.putExtra("fav_sel_name", fav_name.getString(position));
                                Log.d("클릭", String.valueOf(position));
                                Log.d("테스트1", String.valueOf(fav_name.get(position)));
                                Log.d("테스트2", String.valueOf(favItemList.get(position)));
                                Log.d("테스트2", String.valueOf(selectart));
                                // 다음 버튼을 눌러야 값이 보내지게 startActivity는 하단 버튼에 입력

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            }));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("통신", "실패");
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // getParams --> post 방식으로 데이터를 보낼 때 사용되는 메소드!
                // 데이터를 key - value 형태로 만들어서 보내겠습니다
                Map<String,String> params = new HashMap<String,String>();
                // params -> key-value 형태로 만들어줌


                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };

        requestQueue.add(request);




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

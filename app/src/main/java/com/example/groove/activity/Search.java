package com.example.groove.activity;

import static com.example.groove.activity.Login.my_url;
import static com.example.groove.activity.MainActivity.user_seq;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.adapter.Search_Adapter;
import com.example.groove.data.Main_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {

    EditText search_bar;
    ListView list_search;
    ArrayList<Main_Item> dataArray;  // 데이터셋
    Search_Adapter adapter; // 어댑터 사용!
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_bar = findViewById(R.id.search_bar);
        list_search = findViewById(R.id.list_search);

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        Log.d("엔터눌렀음","ㅋㅋ");

                        if (requestQueue == null) {
                            requestQueue = Volley.newRequestQueue(getApplicationContext());
                        }

                        String url = "http://"+my_url+":3001:3001/";

                        StringRequest request = new StringRequest(
                                Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("통신", response);

                                        try {
                                            JSONObject json = new JSONObject(response);
                                            JSONArray song_id = json.getJSONArray("song_id");
                                            JSONArray search_content = json.getJSONArray("search_content");



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
                                params.put("user_seq", user_seq);

                                // key-value 로 만들어진 params 객체를 전송!
                                return params;
                            }
                        };
                        requestQueue.add(request);

                        return true;
                }
                return false;
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        String url = "http://172.30.1.42:3001/Research";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray song_id = json.getJSONArray("song_id");
                            JSONArray search_content = json.getJSONArray("search_content");

                            // 리스트뷰 띄우기
                            dataArray = new ArrayList<Main_Item>();
                            for (int i = 0; i < search_content.length(); i++) {
                                dataArray.add(new Main_Item(search_content.getString(i)));
                            }
                            adapter = new Search_Adapter(getApplicationContext(),R.layout.item_search, dataArray);

                            list_search.setAdapter(adapter);

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
                params.put("user_seq", user_seq);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };
        requestQueue.add(request);
    }
}
package com.example.groove.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Relative_Music extends Fragment {


    ImageButton btn_down3;
    String song_id;
    RequestQueue requestQueue;
    Bundle bundle = new Bundle();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_relative__music, container, false);
        View main  = inflater.inflate(R.layout.activity_navigation_main_menu, container, false);



        btn_down3 = rootView.findViewById(R.id.btn_down3);

        btn_down3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(Relative_Music.this).commit();
                fragmentManager.popBackStack();
            }
        });

//
//        if (requestQueue == null) {
//            requestQueue = Volley.newRequestQueue(getContext());
//        }
//
//        String url = "http://192.168.0.2:3001/Lyrics";
//
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("통신", response);
//
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            Log.d("키키", String.valueOf(response));
//                            String lyrics = json.getString("song_lyrics");
//
//                            player_lyrics.setText(lyrics);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            throw new RuntimeException(e);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("통신", "실패");
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                // getParams --> post 방식으로 데이터를 보낼 때 사용되는 메소드!
//                // 데이터를 key - value 형태로 만들어서 보내겠습니다
//                Map<String,String> params = new HashMap<String,String>();
//                // params -> key-value 형태로 만들어줌
//                params.put("song_id", "30515340");
//                // key-value 로 만들어진 params 객체를 전송!
//                return params;
//            }
//        };
//
//        requestQueue.add(request);


        return rootView;
    }

}
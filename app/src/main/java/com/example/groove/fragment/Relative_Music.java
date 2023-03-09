package com.example.groove.fragment;

import static com.example.groove.activity.MainActivity.user_seq;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
import com.example.groove.activity.PlayerActivity;
import com.example.groove.adapter.MyMusic_RecyclerView_Adapter;
import com.example.groove.adapter.PlayList_Adapter;
import com.example.groove.data.Main_Item;
import com.example.groove.data.RecyclerItemClickListener;
import com.example.groove.data.View_Type_Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Relative_Music extends Fragment {


    ImageButton btn_down3;
    String song_id;
    RequestQueue requestQueue;
    Bundle bundle = new Bundle();
    ArrayList<String> song_list = new ArrayList<>();
    ArrayList<String> stitle_list = new ArrayList<>();
    ArrayList<String> salbum_list = new ArrayList<>();
    ArrayList<Main_Item> dataArray;
    MyMusic_RecyclerView_Adapter adapter;
    RecyclerView pl_relative;
    GridLayoutManager gridLayoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("result", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                song_id = result.getString("song_id");
                Log.d("과연..?", song_id);
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_relative__music, container, false);
        View main  = inflater.inflate(R.layout.activity_navigation_main_menu, container, false);

        btn_down3 = rootView.findViewById(R.id.btn_down3);
        pl_relative = rootView.findViewById(R.id.pl_relative);
        btn_down3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(Relative_Music.this).commit();
                fragmentManager.popBackStack();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }

        String url = "http://172.30.1.49:3001/RelativeSongs";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("통신", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray song_list2 = json.getJSONArray("song_list");
                            JSONArray stitle_list2 = json.getJSONArray("stitle_list");
                            JSONArray salbum_list2 = json.getJSONArray("salbum_list");

                            if(!song_list.isEmpty()){
                                song_list.clear();
                                stitle_list.clear();
                                salbum_list.clear();
                            }
                            for(int i=0; i<song_list2.length(); i++){
                                song_list.add(song_list2.getString(i));
                                stitle_list.add(stitle_list2.getString(i));
                                salbum_list.add(salbum_list2.getString(i));
                            }

                            dataArray = new ArrayList<>();
                            for (int i = 0; i < stitle_list.size(); i++) {
                                dataArray.add(new Main_Item(stitle_list.get(i), getResources().getIdentifier("album_"+ salbum_list.get(i), "drawable", getActivity().getPackageName()), View_Type_Code.ViewType.FIRST_CONTENT));
                            }
                            pl_relative = rootView.findViewById(R.id.pl_relative);
                            adapter = new MyMusic_RecyclerView_Adapter(dataArray);
                            pl_relative.setAdapter(adapter);
                            gridLayoutManager = new GridLayoutManager(rootView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
                            pl_relative.setLayoutManager(gridLayoutManager);	// 가로

                            pl_relative.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), pl_relative, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) throws JSONException {

                                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                                    intent.putExtra("song_id",song_list.get(position));
                                    startActivity(intent);
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
                params.put("song_id", song_id);

                // key-value 로 만들어진 params 객체를 전송!
                return params;
            }
        };
        requestQueue.add(request);


        return rootView;
    }

}
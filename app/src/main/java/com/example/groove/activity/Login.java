package com.example.groove.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.groove.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {
    public static String user_seq;
    EditText login_id, login_pw;
    AppCompatButton btn_joinform, btn_login;
    RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        btn_joinform = findViewById(R.id.btn_joinform);
        btn_login = findViewById(R.id.btn_login);


        btn_joinform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Join.class);

                startActivity(intent);
                finish();
            }
        });

        // Node.js와 통신
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputId = login_id.getText().toString();
                String inputPw = login_pw.getText().toString();

                String url = "http://172.30.1.42:3001/Login";

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("통신", response);



                                try {
                                    JSONObject json = new JSONObject(response);

                                    String result = json.getString("result");
                                    String nick = json.getString("userNick");
                                    String favart = json.getString("favArt");
                                    String favsong = json.getString("favSong");
                                    String user_seq = json.getString("user_seq");

                                    Log.d("로그인여부", result);
                                    Log.d("닉네임", nick);
                                    Log.d("선호 아티스트", favart);
                                    Log.d("선호 곡", String.valueOf(favsong.length()));


                                    // 로그인 성공했을 때 선호 아티스트가 null이면 선호 아티스트로 창으로 아니면 메인 창으로
                                    if(result.equals("로그인 성공")){
                                        Intent intent;
                                        if(favart.equals("null") || favsong.equals("null")){
                                            Log.d("여기드감?",favart);
                                            intent = new Intent(getApplicationContext(), Fav_Artist_Selection.class);
                                            intent.putExtra("user_seq", user_seq);
                                        } else{
                                            intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("nick", nick);
                                            intent.putExtra("favart", favart);
                                            intent.putExtra("favsong", favsong);
                                            intent.putExtra("user_seq", user_seq);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("통신", String.valueOf(error));
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
                        params.put("id", inputId);
                        params.put("pw", inputPw);

                        // key-value 로 만들어진 params 객체를 전송!
                        return params;
                    }
                };

                requestQueue.add(request);
            }
        });

    }
}
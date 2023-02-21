package com.example.groove.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

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

public class Join extends AppCompatActivity {

    AppCompatButton btn_join;
    AppCompatImageButton btn_pre4;
    EditText edit_joinId, edit_joinPw, edit_joinName, edit_joinNick, edit_joinEmail, edit_joinBirth;
    RadioGroup joinGender;
    int gender = 0;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btn_pre4 = findViewById(R.id.btn_pre4);
        btn_join = findViewById(R.id.btn_join);
        edit_joinId = findViewById(R.id.edit_joinId);
        edit_joinPw = findViewById(R.id.edit_joinPw);
        edit_joinName = findViewById(R.id.edit_joinName);
        edit_joinNick = findViewById(R.id.edit_joinNick);
        edit_joinEmail = findViewById(R.id.edit_joinEmail);
        edit_joinBirth = findViewById(R.id.edit_joinBirth);
        joinGender = findViewById(R.id.joinGender);

        joinGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.gender_male){
                    gender = 0;
                } else{
                    gender = 1;
                }
            }
        });

        btn_pre4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);

                startActivity(intent);
                finish();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String joinId = edit_joinId.getText().toString();
                String joinPw = edit_joinPw.getText().toString();
                String joinName = edit_joinName.getText().toString();
                String joinNick = edit_joinNick.getText().toString();
                String joinEmail = edit_joinEmail.getText().toString();
                String joinBirth = edit_joinBirth.getText().toString();

                String url = "http://172.30.1.49:3001/Join";    // 서버주소
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
                                    Log.d("회원가입", result);

                                    if (result.equals("회원가입 성공")) {
                                        Intent intent = new Intent(getApplicationContext(), Login.class);

                                        startActivity(intent);
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("통신", "실패");
                                Log.d("에러", String.valueOf(error));
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
                        params.put("id", joinId);
                        params.put("pw", joinPw);
                        params.put("name", joinName);
                        params.put("nick", joinNick);
                        params.put("email", joinEmail);
                        params.put("birth", joinBirth);
                        params.put("gender", String.valueOf(gender));

                        // key-value 로 만들어진 params 객체를 전송!
                        return params;
                    }
                };

                requestQueue.add(request);

            }
        });
    }
}
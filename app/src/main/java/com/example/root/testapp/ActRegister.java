package com.example.root.testapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActRegister extends AppCompatActivity {


    OkHttpClient client = new OkHttpClient();
    final ExecutorService service = Executors.newSingleThreadExecutor();
    final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");



    View.OnClickListener btnSubmit_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserFormData data = new UserFormData();
            data.email = etEmail.getText().toString();
            data.password = etPassword.getText().toString();
            data.confirmPwd = etConfirmPwd.getText().toString();

            Gson gson = new Gson();
            final String  json = gson.toJson(data);

            service.submit(new Runnable() {
                @Override
                public void run()
                {
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://jofriend.duckdns.org/api/user/register")
                            .post(body)
                            .build();
                    try
                    {
                        final Response response = client.newCall(request).execute();
                        final String resStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                String result = "";
                                UserFormMsg msg = gson.fromJson(resStr,UserFormMsg.class);

                                tvResult.setText((msg.equals(null))+"");
                                int cnt=0;

                                if(("1").equals(msg.emailBlankErr))
                                {
                                    result+="emailBlankErr\n";
                                    cnt++;
                                }
                                if(("1").equals(msg.emailExistErr))
                                {
                                    result+="emailExistErr\n";
                                    cnt++;

                                }
                                if(("1").equals(msg.emailFormatErr))
                                {
                                    result+="emailFormatErr\n";
                                    cnt++;

                                }
                                if(("1").equals(msg.passwordBlankErr))
                                {
                                    result+="passwordBlankErr\n";
                                    cnt++;

                                }
                                if(("1").equals(msg.passwordNotMatchErr))
                                {
                                    result+="passwordNotMatchErr\n";
                                    cnt++;
                                }

                                if(cnt==0)
                                {
                                    Users newOne = gson.fromJson(resStr,Users.class);
                                    result=String.format("帳號已建立.請至信箱啟用帳號.\nemail:\n%s,\n建立時間:\n%s",
                                            newOne.getEmail(),newOne.getCreateAt());
                                }
                                tvResult.setText(result);

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//end run
            });//end service.submit
        }//end OnClick
    };//end View.OnClickListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_register);
        InitiateComponent();

    }
    private void InitiateComponent() {
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etConfirmPwd = (EditText)findViewById(R.id.etConfirmPwd);
        tvResult = (TextView)findViewById(R.id.tvResult);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(btnSubmit_Click);
    }
    TextView tvResult;
    EditText etEmail,etPassword,etConfirmPwd;
    Button btnSubmit;
}

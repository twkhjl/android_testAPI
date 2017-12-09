package com.example.root.testapp;

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

public class ActLogin extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    final ExecutorService service = Executors.newSingleThreadExecutor();
    final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);

        InitiateComponent();
    }

    View.OnClickListener btnSubmit_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UserFormData data = new UserFormData();
            data.email = etEmail.getText().toString();
            data.password = etPassword.getText().toString();

            Gson gson = new Gson();
            final String  json = gson.toJson(data);

            service.submit(new Runnable() {
                @Override
                public void run()
                {
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://jofriend.duckdns.org/api/user/login")
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
                                UserFormMsg msg = gson.fromJson(resStr,UserFormMsg.class);
                                String result = msg.err;
                                if(result==null || result=="")
                                {
                                    Users user = gson.fromJson(resStr,Users.class);
                                    result =
                                            String.format("登入成功.\n帳號名稱:\n%s",
                                            user.getEmail());

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

    private void InitiateComponent() {
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        tvResult = (TextView)findViewById(R.id.tvResult);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(btnSubmit_Click);
    }
    TextView tvResult;
    EditText etEmail,etPassword;
    Button btnSubmit;

}

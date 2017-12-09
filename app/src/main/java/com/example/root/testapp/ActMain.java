package com.example.root.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ActMain extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main);
        InitiateComponent();
    }

    private void InitiateComponent() {
        tvTest = (TextView)findViewById(R.id.tvTest);
        btnGetAll = (Button)findViewById(R.id.btnGetAll);
        btnGetOne = (Button)findViewById(R.id.btnGetOne);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnGetAll.setOnClickListener(btnGetAll_Click);
        btnGetOne.setOnClickListener(btnGetOne_Click);
        btnLogin.setOnClickListener(btnLogin_Click);
        btnRegister.setOnClickListener(btnRegister_Click);

    }
    TextView tvTest;
    Button btnGetAll,btnGetOne,btnLogin,btnRegister;

    final ExecutorService service = Executors.newSingleThreadExecutor();
    View.OnClickListener btnGetAll_Click = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            service.submit(new Runnable() {
                @Override
                public void run()
                {
                    Request request = new Request.Builder()
                            .url("http://jofriend.duckdns.org/api/user")
                            .build();
                    try
                    {
                        final Response response = client.newCall(request).execute();
                        final String resStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                Users[] userArr = gson.fromJson(resStr, Users[].class);

                                tvTest.setText(userArr[0].getEmail().toString());
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//end run
            });//end service.submit
        }//end onClick
    };//end listener

    View.OnClickListener btnGetOne_Click=new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            service.submit(new Runnable() {
                @Override
                public void run()
                {
                    Request request = new Request.Builder()
                            .url("http://jofriend.duckdns.org/api/user/62")
                            .build();
                    try
                    {
                        final Response response = client.newCall(request).execute();
                        final String resStr = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                Users user = gson.fromJson(resStr, Users.class);

                                tvTest.setText(user.getEmail().toString());
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//end run
            });//end service.submit

        }//end onClick
    };//end listener

    View.OnClickListener btnLogin_Click= new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(ActMain.this,ActLogin.class);
            startActivity(i);

        }//end onClick
    };//end listener

    View.OnClickListener btnRegister_Click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ActMain.this,ActRegister.class);
            startActivity(i);

        }
    };//end listener
}


package com.cclx.mobile.b_client;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Client("192.168.128.97", 12345, new Client.OnMessageListener() {
                    @Override
                    public void onMessage(String str) {
                        showMessage(str);
                    }
                }).start();
            }
        }).start();
    }

    private void showMessage(final String str) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

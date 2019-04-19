package com.cclx.mobile.b_client;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        imageView = (ImageView) findViewById(R.id.imageView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Client("192.168.128.97", 12345, new Client.OnMessageListener() {
                    @Override
                    public void onMessage(String str) {
                        showMessage(str);
                    }

                    @Override
                    public void onImage(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
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

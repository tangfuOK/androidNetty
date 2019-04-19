package com.cclx.mobile.b_server;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cclx.mobile.b_server.utils.Server;
import com.cclx.mobile.b_server.utils.ServerClient;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server();
                server.createSocket(new ServerClient.OnMessageListener() {
                    @Override
                    public void onReceive(ServerClient client, String str) {
                        showMessage(str);
                    }
                });
            }
        }).start();

        handler.post(runnable);
    }

    private void showMessage(final String str) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            server.sendMsg("随机数字是" + new Random().nextInt(100) + "【服务端发送】");
            handler.postDelayed(runnable, 5000);
        }
    };
}

package com.cclx.mobile.b_client;

import android.text.TextUtils;

import com.cclx.mobile.b_client.utils.BytesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client extends Thread {

    private String ip;
    private int port;
    private OnMessageListener onMessageListener;

    private Socket socket;

    public Client(String ip, int port, OnMessageListener onMessageListener) {
        this.ip = ip;
        this.port = port;
        this.onMessageListener = onMessageListener;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            if (socket.isConnected()) {
                InputStream inputStream = socket.getInputStream();
                int len = 0;
                while (true) {
                    byte[] src = new byte[4];
                    inputStream.read(src);
                    len = BytesUtils.bytes2Int(src);

                    byte[] srcData = new byte[len];
                    inputStream.read(srcData);
                    String str = new String(srcData);
                    if (onMessageListener != null && !TextUtils.isEmpty(str)) {
                        onMessageListener.onMessage(str);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnMessageListener {
        void onMessage(String str);
    }
}

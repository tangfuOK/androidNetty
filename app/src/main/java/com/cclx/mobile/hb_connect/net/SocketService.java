package com.cclx.mobile.hb_connect.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SocketService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startService();
                } catch (Exception e) {
                    Log.e("SocketService", "启动socket服务器异常", e);
                }
            }
        }).start();
    }

    private void startService() throws Exception {
        new DiscardServer(12345).run();
    }
}

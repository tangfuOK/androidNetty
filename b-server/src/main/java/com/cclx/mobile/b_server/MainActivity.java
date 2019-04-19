package com.cclx.mobile.b_server;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cclx.mobile.b_server.camera.DoorbellCamera;
import com.cclx.mobile.b_server.utils.Server;
import com.cclx.mobile.b_server.utils.ServerClient;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Server server;

    private DoorbellCamera mCamera;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        imageView = (ImageView) findViewById(R.id.imageView);

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

        initCamera();
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

    /**
     * A {@link Handler} for running Camera tasks in the background.
     */
    private Handler mCameraHandler;

    /**
     * Listener for new camera images.
     */
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.d("123", "PhotoCamera OnImageAvailableListener");

                    Image image = reader.acquireLatestImage();
                    // get image bytes
                    ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
                    final byte[] imageBytes = new byte[imageBuf.remaining()];
                    imageBuf.get(imageBytes);
                    image.close();
                    onPictureTaken(imageBytes);
                }
            };

    /**
     * Handle image processing in Firebase and Cloud Vision.
     */
    private void onPictureTaken(final byte[] imageBytes) {
        Log.d("123", "PhotoCamera onPictureTaken");
        if (imageBytes != null) {
            String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
            Log.d("123", "imageBase64:" + imageStr);

            final Bitmap[] bitmap = {BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length)};
            if (bitmap[0] != null) {
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(getExternalFilesDir(null) + "pic.jpg"));// /sdcard/Android/data/com.things.thingssocket/filespic.jpg
                    bitmap[0].compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    bitmap[0].recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmaps = BitmapFactory.decodeFile(getExternalFilesDir(null) + "pic.jpg");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        server.sendImage(bitmaps);
                    }
                }).start();
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmaps);
                    }
                });
            }
        }
    }

    private HandlerThread mCameraThread;

    public void initCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        DoorbellCamera.dumpFormatInfo(this);

        // Creates new handlers and associated threads for camera and networking operations.
        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());//在mCameraThread这个线程中创建handler对象

        // Camera code is complicated, so we've shoved it all in this closet class for you.
        mCamera = DoorbellCamera.getInstance();
        mCamera.initializeCamera(this, mCameraHandler, mOnImageAvailableListener);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("takePicture", "click image to take picture");
                mCamera.takePicture();
            }
        });
    }
}

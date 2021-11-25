package id.hangga.gampilcamera;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import id.hangga.gampil.Facing;
import id.hangga.gampil.GampilPreview;
import id.hangga.gampil.TakePhotoListener;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    ImageView imgCaptured;
    Button btnTakePicture;
    private final String[] permissions = {/*
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,*/
            Manifest.permission.CAMERA
    };
    private boolean mPermissions;
    private GampilPreview gampilPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gampilPreview = findViewById(R.id.gampilPreview);
        gampilPreview.setFacing(Facing.FRONT_CAMERA);
        imgCaptured = findViewById(R.id.imgCaptured);
        btnTakePicture = findViewById(R.id.btnTakePicture);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gampilPreview.takePhoto(60, new TakePhotoListener() {
                    @Override
                    public void onPhotoTaken(Bitmap bitmap, File file) {
                        imgCaptured.setImageBitmap(bitmap);
                        gampilPreview.setVisibility(View.GONE);
                        btnTakePicture.setEnabled(false);
                    }

                    @Override
                    public void onPhotoError(String message) {

                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gampilPreview.stop();
    }

    void startPreview(){
        if (Utils.allPermissionsGranted(this, permissions)) {
            gampilPreview.start();
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Utils.allPermissionsGranted(this)) {
            startPreview();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
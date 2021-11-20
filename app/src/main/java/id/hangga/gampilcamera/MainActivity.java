package id.hangga.gampilcamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import id.hangga.gampil.GampilPreview;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private boolean mPermissions;
    private GampilPreview gampilPreview;
    ImageView imgCaptured;
    Button btnTakePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gampilPreview = findViewById(R.id.gampilPreview);
        imgCaptured = findViewById(R.id.imgCaptured);
        btnTakePicture = findViewById(R.id.btnTakePicture);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gampilPreview.takePicture(new GampilPreview.OnTakePicture() {
                    @Override
                    public void onPictureTaken(File file, Bitmap bitmap) {
                        imgCaptured.setImageBitmap(bitmap);
                        gampilPreview.setVisibility(View.GONE);
                        btnTakePicture.setEnabled(false);
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        verifyPermissions();
        //gampilPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gampilPreview.onPause();
    }


    public void verifyPermissions() {
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            gampilPreview.onResume();
        } else {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (mPermissions) {
                gampilPreview.onResume();
            } else {
                verifyPermissions();
            }
        }
    }
}
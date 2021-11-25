package id.hangga.gampil;

import android.graphics.Bitmap;

import java.io.File;

public interface TakePhotoListener {
    void onPhotoTaken(Bitmap bitmap, File file);
    void onPhotoError(String message);
}

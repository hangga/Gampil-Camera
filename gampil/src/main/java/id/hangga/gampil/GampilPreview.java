package id.hangga.gampil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Preview the camera image in the screen.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GampilPreview extends FrameLayout {

    private static final String TAG = "GampilPreview";
    private int facing = Facing.FRONT_CAMERA;
    private Context context;
    private SurfaceView surfaceView;
    private boolean startRequested;
    private boolean surfaceAvailable;
    private CameraSource cameraSource;
    private TakePhotoListener takePhotoListener;

    public GampilPreview(Context context) {
        super(context);
        init(context);
    }

    public GampilPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GampilPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void createBitmap(byte[] data, int compressQuality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            File dir = new File(context.getFilesDir(), "kredibel-sdk");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, "ocr" + System.currentTimeMillis() + ".jpg");

            //File imageFile = new File(getDefaultPath());
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream);
            outputStream.flush();
            outputStream.close();

            if (takePhotoListener != null) {
                takePhotoListener.onPhotoTaken(BitmapUtils.flip(bitmap), imageFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (takePhotoListener != null) {
                takePhotoListener.onPhotoError(e.getMessage());
            }
        }
    }

    /**
     * @return Get default file path from external storage
     */
    private String getDefaultPath() {
        String filename = "ocr" + System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + filename;
        } else {
            return Environment.getExternalStorageDirectory().toString() + "/" + filename;
        }
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
        if (cameraSource != null) {
            cameraSource.setFacing(facing);
        }
    }

    void init(Context context) {
        this.context = context;
        ScreenSize screenSize = Utility.getScreenSize((Activity) context);
        CameraSource.DEFAULT_REQUESTED_CAMERA_PREVIEW_WIDTH = screenSize.getWidth();
        CameraSource.DEFAULT_REQUESTED_CAMERA_PREVIEW_HEIGHT = screenSize.getHeight();

        startRequested = false;
        surfaceAvailable = false;

        surfaceView = new SurfaceView(context);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        surfaceView.setScaleX(1);
        surfaceView.setScaleY(1);
        addView(surfaceView);
        setBackgroundColor(Color.parseColor("#2B2B2B"));
    }

    private void createCameraSource() {
        try {
            if (cameraSource == null) {
                cameraSource = new CameraSource((Activity) context);
                cameraSource.setFacing(facing);
            }
            resume();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (this.cameraSource == null) createCameraSource();
    }

    public void stop() {
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @SuppressLint("MissingPermission")
    private void startIfReady() throws IOException, SecurityException {
        if (startRequested && surfaceAvailable) {
            cameraSource.start(surfaceView.getHolder());
            requestLayout();
            startRequested = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = 320;
        int height = 240;
        if (cameraSource != null) {
            Size size = cameraSource.getPreviewSize();
            if (size != null) {
                width = size.getWidth();
                height = size.getHeight();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        float previewAspectRatio = (float) width / height;
        int layoutWidth = right - left;
        int layoutHeight = bottom - top;
        float layoutAspectRatio = (float) layoutWidth / layoutHeight;
        if (previewAspectRatio > layoutAspectRatio) {
            // The preview input is wider than the layout area. Fit the layout height and crop
            // the preview input horizontally while keep the center.
            int horizontalOffset = (int) (previewAspectRatio * layoutHeight - layoutWidth) / 2;
            surfaceView.layout(-horizontalOffset, 0, layoutWidth + horizontalOffset, layoutHeight);
        } else {
            // The preview input is taller than the layout area. Fit the layout width and crop the preview
            // input vertically while keep the center.
            int verticalOffset = (int) (layoutWidth / previewAspectRatio - layoutHeight) / 2;
            surfaceView.layout(0, -verticalOffset, layoutWidth, layoutHeight + verticalOffset);
        }

        try {
            startIfReady();
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    private boolean isPortraitMode() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        Log.d(TAG, "isPortraitMode returning false by default");
        return false;
    }

    /**
     * @param quality           Compress bitmap quality
     * @param takePhotoListener CallBack after takePicture
     */
    public void takePhoto(int quality, TakePhotoListener takePhotoListener) {
        this.takePhotoListener = takePhotoListener;
        Camera.ShutterCallback shutterCallback = () -> Log.d(TAG, "onShutter: ");
        cameraSource.getCamera().takePicture(shutterCallback, null, (data, camera) -> {
            camera.startPreview();
            createBitmap(data, quality);
        });
    }


    public void resume() {
        if (this.cameraSource != null) {
            startRequested = true;
            try {
                startIfReady();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            surfaceAvailable = true;
            try {
                startIfReady();
            } catch (IOException e) {
                Log.e(TAG, "Could not start camera source.", e);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            surfaceAvailable = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    }
}

package id.hangga.gampil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScalingTextureView extends TextureView {
    private static final String TAG = "ScalingTextureView";

    public int mRatioWidth = 0;
    public int mRatioHeight = 0;
    // scaling
    public float mWidthCorrection = 0f;
    float mScreenAspectRatio = 1f;
    float mPreviewAspectRatio = 1f;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;

    public ScalingTextureView(@NonNull Context context) {
        super(context);
    }

    public ScalingTextureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalingTextureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScalingTextureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /*public ScalingTextureView(Context context) {
        this(context, null);
    }

    public ScalingTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalingTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/

    public void setAspectRatio(int width, int height, int screenWidth, int screenHeight) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        mScreenAspectRatio = (float) mScreenHeight / (float) mScreenWidth;
        mPreviewAspectRatio = (float) mRatioHeight / (float) mRatioWidth;
        getWidthCorrection();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        }
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    private void getWidthCorrection() {
        String roundedScreenAspectRatio = String.format("%.2f", mScreenAspectRatio);
        String roundedPreviewAspectRatio = String.format("%.2f", mPreviewAspectRatio);
        if (!roundedPreviewAspectRatio.equals(roundedScreenAspectRatio)) {

            float scaleFactor = (mScreenAspectRatio / mPreviewAspectRatio);
            Log.d(TAG, "configureTransform: scale factor: " + scaleFactor);

            mWidthCorrection = (((float) mScreenWidth * scaleFactor) - mScreenWidth) / 2;
            Log.d(TAG, "getWidthCorrection: width correction: " + mWidthCorrection);
        }
    }
}

package com.sen;

/**
 * Created by Adminsss on 15-02-2016.
 */


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class ScreenCaptureFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ScreenCaptureFragment";

    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    private static final int REQUEST_MEDIA_PROJECTION = 1;

    private int mScreenDensity;

    private int mResultCode;
    private Intent mResultData;

    private Surface mSurface;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionManager mMediaProjectionManager;
    private Button mButtonToggle;
    private SurfaceView mSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.act_spy_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
        mSurface = mSurfaceView.getHolder().getSurface();
        mButtonToggle = (Button) view.findViewById(R.id.toggle);
        mButtonToggle.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        //mMediaProjectionManager = (MediaProjectionManager)activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mResultData != null) {
            outState.putInt(STATE_RESULT_CODE, mResultCode);
            outState.putParcelable(STATE_RESULT_DATA, mResultData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                if (mVirtualDisplay == null) {
                    try {
                        startScreenCapture();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    stopScreenCapture();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(getActivity(), R.string.user_cancelled, Toast.LENGTH_SHORT).show();
                return;
            }
            Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            mResultCode = resultCode;
            mResultData = data;
            setUpMediaProjection();
            try {
                setUpVirtualDisplay();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScreenCapture();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDownMediaProjection();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startScreenCapture() throws IOException {
        Activity activity = getActivity();
        if (mSurface == null && activity == null){
            return;
        }
        if (mMediaProjection != null) {
            setUpVirtualDisplay();
        } else if (mResultCode != 0 && mResultData != null) {
            setUpMediaProjection();
            setUpVirtualDisplay();
        } else {
            startActivityForResult(
                    mMediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpVirtualDisplay() throws IOException {

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                mSurfaceView.getWidth(), mSurfaceView.getHeight(), mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mSurface, null, null);

        mButtonToggle.setText(R.string.stop);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        mButtonToggle.setText(R.string.start);
    }

/*

    public void takeScreenshot2(View v){
        2         MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        3         Intent intent = projectionManager.createScreenCaptureIntent();
        4         startActivity(intent);
        5
        6         int mWidth = mWindowManager.getDefaultDisplay().getWidth();
        7         int mHeight = mWindowManager.getDefaultDisplay().getHeight();
        8         ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.RGB_565, 2);
        9         DisplayMetrics metrics = new DisplayMetrics();
        10         mWindowManager.getDefaultDisplay().getMetrics(metrics);
        11         int mScreenDensity = metrics.densityDpi;
        12
        13         MediaProjection mProjection = projectionManager.getMediaProjection(1, intent);
        14         final VirtualDisplay virtualDisplay = mProjection.createVirtualDisplay("screen-mirror",
                15                 mWidth, mHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                16                 mImageReader.getSurface(), null, null);
        17         Image image = mImageReader.acquireLatestImage();
        18         final Image.Plane[] planes = image.getPlanes();
        19         final ByteBuffer buffer = planes[0].getBuffer();
        20         int offset = 0;
        21         int pixelStride = planes[0].getPixelStride();
        22         int rowStride = planes[0].getRowStride();
        23         int rowPadding = rowStride - pixelStride * mWidth;
        24         Bitmap bitmap = Bitmap.createBitmap(mWidth+rowPadding/pixelStride, mHeight, Bitmap.Config.RGB_565);
        25         bitmap.copyPixelsFromBuffer(buffer);
        26         image.close();
        27
        28         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        29         String strDate = dateFormat.format(new java.util.Date());
        30         String pathImage = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
        31         String nameImage = pathImage+strDate+".png";
        32         if(bitmap != null) {
            33             try{
                34                 File fileImage = new File(nameImage);
                35                 if(!fileImage.exists()){
                    36                     fileImage.createNewFile();
                    37                 }
                38                 FileOutputStream out = new FileOutputStream(fileImage);
                39                 if(out != null){
                    40                     bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    41                     out.flush();
                    42                     out.close();
                    43                     Toast.makeText(this,"get phone's screen succeed",Toast.LENGTH_SHORT).show();
                    44                     Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    45                     Uri contentUri = Uri.fromFile(fileImage);
                    46                     media.setData(contentUri);
                    47                     getApplicationContext().sendBroadcast(media);
                    48                 }
                49             }catch(FileNotFoundException e) {
                50                 e.printStackTrace();
                51             }catch (IOException e){
                52                 e.printStackTrace();
                53             }
            54         }
        55         else{
            56             Toast.makeText(this,"cannot get phone's screen",Toast.LENGTH_SHORT).show();
            57         }
        58     }*/
}
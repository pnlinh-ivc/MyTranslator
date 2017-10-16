package com.ndanh.mytranslator.screen.camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.NavigatorFooterAndPermissionActivity;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.model.OCRResult;
import com.ndanh.mytranslator.screen.camera.ui.MaskView;
import com.ndanh.mytranslator.util.IOUtil;
import com.ndanh.mytranslator.util.PermissionHelper;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class CameraActivity extends NavigatorFooterAndPermissionActivity implements
        SurfaceHolder.Callback, CameraContract.CameraView, MaskView.TranslateRequest {

    private static final String BUNDLE_SRC_LANG = "src_lang";
    private static final String BUNDLE_DEST_LANG = "dest_lang";


    @Inject
    CameraContract.CameraPresenter presenter;
    private Language srcLang = Language.ENGLISH, destLang = Language.JAPANESE;

    @BindView((R.id.preview))
    LinearLayout cameraView;
    @BindView((R.id.preview_mask))
    LinearLayout cameraViewMask;
    @BindView(R.id.navigation_footer_camera)
    RelativeLayout hiddenPanel;
    @BindView(R.id.action_choose_source)
    Button comboboxSource;
    @BindView(R.id.action_choose_dest)
    Button comboboxDest;
    @BindView(R.id.image)
    ImageView imageView;

    MaskView view_mask;

    private Camera mCamera;
    private SurfaceView surfaceView;
    private static final String TAG = "OcrCaptureActivity";
    private int orientation = 90;
    private int mWidth, mHeight;

    private String trainedDir = "tessdata/%s.traineddata";
    private String tessDir = "tesseract/tessdata";
    private String trainedName = "%s.traineddata";
    private String nm1Path, nm2Path;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);

        if (bundle != null) {
            srcLang = Language.fromShort(bundle.getString(BUNDLE_SRC_LANG));
            destLang = Language.fromShort(bundle.getString(BUNDLE_DEST_LANG));
        }

        updateLanguage();
        initData();
        presenter.startDetector(getFilesDir() + "/tesseract", srcLang.getShortName(), nm1Path, nm2Path);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (PermissionHelper.hasPermissions(this, Manifest.permission.CAMERA)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpCamera();
                }
            }, 200);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.removeView(surfaceView);
        cameraView.removeView(view_mask);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SRC_LANG, srcLang.getShortName());
        outState.putString(BUNDLE_DEST_LANG, destLang.getShortName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopRec();
    }

    @Override
    public void translate(List<String> request) {
        presenter.translate(request, srcLang.getShortName(), destLang.getShortName());
    }

    @Override
    public void displayResult(CameraFrame frame) {
        view_mask.drawFrame(frame.getData());

    }

    @Override
    public void onError(String msg) {
        showMessage(msg);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayResult(List<String> response, List<String> request) {
        view_mask.setData(response, request);
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public String getSrcLang() {
        return srcLang.getShortName();
    }

    @Override
    public String getDestLang() {
        return destLang.getShortName();
    }

    @Override
    public void invisibleView() {
        hiddenPanel.setVisibility(View.GONE);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void afterRequestPermission() {

    }

    @Override
    protected String[] setPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // Camera Open
            if (mCamera == null) {
                return;
            }
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCamera.startPreview();
        presenter.bufferCamera(mCamera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallbackWithBuffer(null);
            try {
                mCamera.setPreviewDisplay(null);
            } catch (Exception e) {
                Log.e(TAG, "Failed to clear camera preview: " + e);
            }
            mCamera.release();
            mCamera = null;
        }
    }

    @OnClick(R.id.btn_swap_language)
    public void changeLanguage(View v) {
        recreate();
    }

    @OnClick(R.id.action_choose_source)
    public void changeSource(View view) {
        showChangeLanguagePopup(view, true);
    }

    @OnClick(R.id.action_choose_dest)
    public void changeDest(View view) {
        showChangeLanguagePopup(view, false);
    }

    private void initData() {
        nm1Path = IOUtil.copyRawToFileInternal(this, R.raw.trained_classifier_nm1, "ocr_data", "nm1.xml", false);
        nm2Path = IOUtil.copyRawToFileInternal(this, R.raw.trained_classifier_nm1, "ocr_data", "nm2.xml", false);
        String lang = srcLang.getShortName();
        IOUtil.copyAssetToFileInternal(this, String.format(trainedDir, lang),
                tessDir, String.format(trainedName, lang), false);
    }

    private void setUpCamera() {
        mCamera = getCameraInstance();
        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(orientation);

        mWidth = cameraView.getWidth();
        mHeight = cameraView.getHeight();

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size mPreviewSize = previewSizes.get(0);
        for (Camera.Size determineSize : previewSizes) {
            int sizeFirst = determineSize.width * determineSize.height;
            if (mWidth * mHeight > sizeFirst) continue;
            if (mHeight > determineSize.width || mWidth > determineSize.height) continue;
            mPreviewSize = determineSize;
        }

        if (mPreviewSize != null) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        mPreviewSize = parameters.getPreviewSize();
        mCamera.setParameters(parameters);

        mWidth = mPreviewSize.height;
        mHeight = mPreviewSize.width;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mWidth, mHeight);

        view_mask = new MaskView(this, this);
        surfaceView = new SurfaceView(getApplicationContext());

        surfaceView.setLayoutParams(lp);
        view_mask.setLayoutParams(lp);
        cameraView.addView(surfaceView);
        cameraViewMask.addView(view_mask);
        surfaceView.getHolder().addCallback(CameraActivity.this);
        view_mask.bringToFront();
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void showChangeLanguagePopup(View view, final boolean isSource) {
        PopupMenu pum = new PopupMenu(this, view);
        List<Language> list = Language.getAll();
        for (Language language : list) {
            pum.getMenu().add(language.getNormalName());
        }
        pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String normalName = item.getTitle().toString();
                Language language = Language.fromNormal(normalName);

                if (isSource) {
                    if (!srcLang.getNormalName().equals(normalName)) {
                        srcLang = language;
                        postRecreate();
                    }
                } else {
                    if (!destLang.getNormalName().equals(normalName)) {
                        destLang = language;
                        updateLanguage();
                    }
                }
                return true;
            }
        });
        pum.show();
    }

    private void postRecreate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new ArrayList<OCRResult>());
                recreate();
            }
        }, 100);
    }

    private void updateLanguage() {
        comboboxSource.setText(srcLang.getNormalName());
        comboboxDest.setText(destLang.getNormalName());
    }


}

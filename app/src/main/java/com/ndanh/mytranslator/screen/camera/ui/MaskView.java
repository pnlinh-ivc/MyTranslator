package com.ndanh.mytranslator.screen.camera.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ndanh.mytranslator.model.OCRResult;
import com.ndanh.mytranslator.model.TranslateGCloud.Translation;
import com.ndanh.mytranslator.screen.camera.CameraFrame;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ndanh on 9/5/2017.
 */

public class MaskView extends View {
    private static final String TAG = MaskView.class.getSimpleName();
    private Paint mPaint;
    private Paint tPaint;
    private List<OCRResult> mListOCR;
    private TranslateRequest translateRequest;
    private Map<String, String> dictionary;
    private List<Data> dataList;

    public MaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dictionary = new HashMap<>();
        initPaint();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    public MaskView(Context context, TranslateRequest translateRequest) {
        super(context);
        dictionary = new HashMap<>();
        this.translateRequest = translateRequest;
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        if (dataList != null)
//            for (Data item : dataList) {
//                canvas.drawRect(item.rectangle, mPaint);
//                tPaint.setTextSize(item.rectangle.height());
//                canvas.drawText(getTranslateText(item.text), item.rectangle.left + 2, item.rectangle.top + item.rectangle.height() - 2, tPaint);
//            }

        if (mListOCR != null) {
            for (OCRResult ocrResult : mListOCR) {
                canvas.drawRect(ocrResult.x, ocrResult.y, ocrResult.x + ocrResult.width, ocrResult.y + ocrResult.height, mPaint);
                canvas.drawText(ocrResult.text, ocrResult.x, ocrResult.y, tPaint);
            }
        }
    }

    public void drawFrame(List<Data> dataList) {
        List<String> pendingTranslate = new ArrayList<>();
        if (this.dataList == null) this.dataList = new ArrayList<>();
        this.dataList.clear();
        this.dataList.addAll(dataList);
        for (Data item : dataList) {
            if (dictionary.containsKey(item.text)) {
                if (dictionary.get(item.text).equals("")) {
                    pendingTranslate.add(item.text);
                }
            } else {
                pendingTranslate.add(item.text);
            }
        }
        invalidate();
        if (pendingTranslate.size() > 0)
            translateRequest.translate(pendingTranslate);
    }


    public static class Data {
        private Rect rectangle;
        private String text;

        public Data(Rect rectangle, String text) {
            this.rectangle = rectangle;
            this.text = text;
        }
    }

    public String getTranslateText(String text) {
        if (dictionary.containsKey(text)) {
            return dictionary.get(text);
        } else {
            return text;
        }
    }

    public void setData(List<String> response, List<String> request) {
        boolean change = false;
        for (int i = 0; i < request.size(); i++) {
            if (dictionary.containsKey(request.get(i))) {
                if (!dictionary.get(request.get(i)).equals(response.get(i))) {
                    dictionary.put(request.get(i), response.get(i));
                    change = true;
                }
            } else {
                dictionary.put(request.get(i), response.get(i));
                change = true;
            }
        }
        if (change) invalidate();
    }


    private void initPaint() {
        mListOCR = new ArrayList<>();
        tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(25);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.argb(127, 0, 0, 0));
    }

    public interface TranslateRequest {
        void translate(List<String> request);
    }

    long time = System.currentTimeMillis();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<OCRResult> listOCR) {
        mListOCR.clear();
        mListOCR = new ArrayList<>(listOCR);
        invalidate();
        Log.e(TAG, String.format("onMessageEvent: %s", (System.currentTimeMillis() - time) / 1000));
        time = System.currentTimeMillis();
    }

}

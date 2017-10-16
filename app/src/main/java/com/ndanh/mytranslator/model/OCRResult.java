package com.ndanh.mytranslator.model;

/**
 * Created by pnlinh on 10/4/2017.
 */

public class OCRResult {
    public int x;
    public int y;
    public int width;
    public int height;
    public String text;

    public OCRResult() {
    }

    @Override
    public String toString() {
        return "OCRResult{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", text='" + text + '\'' +
                '}';
    }
}

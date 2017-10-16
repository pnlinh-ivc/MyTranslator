package com.ndanh.mytranslator.model;

import android.graphics.Rect;

import java.util.List;

/**
 * Created by dauda on 05/06/2017.
 */

public class DetectResult {
    private String srcText;
    private Rect position;
    private String translatedText;

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getSrcText() {
        return srcText;
    }

    public void setSrcText(String srcText) {
        this.srcText = srcText;
    }

    public Rect getPosition() {
        return position;
    }

    public void setPosition(Rect position) {
        this.position = position;
    }

    public void merge(Rect rect , String srcText){
        this.position.top = rect.top < this.position.top ? rect.top : this.position.top;
        this.position.bottom = rect.bottom > this.position.bottom ? rect.bottom : this.position.bottom;
        this.position.right = rect.right;
        this.srcText += " " + srcText;
    }


}

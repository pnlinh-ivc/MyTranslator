
package com.ndanh.mytranslator.model.TranslateGCloud;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("translations")
    @Expose
    private List<Translation> translations = null;

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public String megerAll() {
        String result = "";

        for (Translation translation : translations) {
            result += translation.getTranslatedText();
        }
        return result;
    }

}
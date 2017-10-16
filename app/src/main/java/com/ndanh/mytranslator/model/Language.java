package com.ndanh.mytranslator.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ndanh.mytranslator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ndanh on 8/31/2017.
 */

public enum Language {
    ENGLISH("English", "eng", "en_US"),
    JAPANESE("Japanese", "jpn", "ja_JP"),
    VIETNAMESE("Vietnamese", "vie", "vi_VN");

    String normalName;
    String shortName;
    String locateName;

    Language(String normalName, String shortName, String locateName) {
        this.normalName = normalName;
        this.shortName = shortName;
        this.locateName = locateName;
    }

    public String getNormalName() {
        return normalName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLocateName() {
        return locateName;
    }

    public static List<Language> getAll() {
        List<Language> list = new ArrayList<>();
        for (Language f : Arrays.asList(values())) {
            list.add(f);
        }
        return list;
    }

    public static Language fromNormal(String normal) {
        for (Language language : values()) {
            if (TextUtils.equals(language.getNormalName(), normal))
                return language;
        }
        return ENGLISH;
    }

    public static Language fromShort(String shortName) {
        for (Language language : values()) {
            if (TextUtils.equals(language.getShortName(), shortName))
                return language;
        }
        return ENGLISH;
    }
}

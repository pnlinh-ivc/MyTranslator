package com.ndanh.mytranslator.services;

import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.app.Config;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ndanh on 9/1/2017.
 */

public interface TranslateAPI {
    @FormUrlEncoded
    @POST("/language/translate/v2")
    Call<TranslatorResponse> getTranslateResult(@Query( value = Config.TRANSLATE_GCLOUD_QUERY )List<String> queries ,
                                                @FieldMap Map<String, String> options);
}

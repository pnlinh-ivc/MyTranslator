package com.ndanh.mytranslator.modulesimpl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ndanh.mytranslator.model.TranslateGCloud.Data;
import com.ndanh.mytranslator.model.TranslateGCloud.Translation;
import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.services.ITranslate;
import com.ndanh.mytranslator.app.Config;
import com.ndanh.mytranslator.services.TranslateAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ndanh on 3/31/2017.
 */

public class GoogleTranslateHelper implements ITranslate {
    private static final String TAG = GoogleTranslateHelper.class.getSimpleName();
    private TranslateAPI translateService;

    private OnTranslateListener listener;

    public GoogleTranslateHelper(TranslateAPI translateService) {
        this.translateService = translateService;
    }

    @Override
    public void setOnTranslateListener(OnTranslateListener listener) {
        this.listener = listener;
    }

    @Override
    public void translate(List<String> src, String srclang, String destLang) {
        Map<String, String> data = new HashMap<>();
        data.put(Config.TRANSLATE_GCLOUD_SOURCE, srclang);
        data.put(Config.TRANSLATE_GCLOUD_TARGET, destLang);
        data.put(Config.TRANSLATE_GCLOUD_FORMAT, Config.TRANSLATE_GCLOUD_FORMAT_TYPE);
        data.put(Config.TRANSLATE_GCLOUD_KEY, Config.TRANSLATE_GCLOUD_API_KEY);
        translateService.getTranslateResult(src, data).enqueue(new Callback<TranslatorResponse>() {
            @Override
            public void onResponse(Call<TranslatorResponse> call, Response<TranslatorResponse> response) {
                RequestModel model = RequestModel.parse(response);

                if (listener == null) return;
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body(), model);
                } else {
                    listener.onFailed("Failed");
                    Log.e(TAG, String.format("onResponse: %s", response.errorBody().toString()));
                }
            }

            @Override
            public void onFailure(Call<TranslatorResponse> call, Throwable t) {
                if (listener == null) return;
                listener.onFailed("Failed");
                t.printStackTrace();
            }
        });

    }

    public static class RequestModel {
        public String source, target;
        public List<String> query;

        public RequestModel() {
        }

        public RequestModel(List<String> query, String source, String target) {
            this.query = query;
            this.source = source;
            this.target = target;
        }

        static RequestModel parse(Response response) {
            RequestModel model = new RequestModel();
            try {
                model.query = response.raw().request().url().queryParameterValues(Config.TRANSLATE_GCLOUD_QUERY);
                FormBody formBody = ((FormBody) response.raw().request().body());

                for (int i = 0; i < formBody.size(); i++) {
                    if (formBody.name(i).equals(Config.TRANSLATE_GCLOUD_SOURCE))
                        model.source = formBody.encodedValue(i);
                    if (formBody.name(i).equals(Config.TRANSLATE_GCLOUD_TARGET))
                        model.target = formBody.encodedValue(i);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                model = null;
            }
            return model;
        }

        @Override
        public String toString() {
            return "{" +
                    "query='" + query + '\'' +
                    ", source='" + source + '\'' +
                    ", target='" + target + '\'' +
                    '}';
        }
    }
}

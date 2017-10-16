package com.ndanh.mytranslator.dagger;

import android.content.Context;

import com.ndanh.mytranslator.modulesimpl.HistoryDaoImpl;
import com.ndanh.mytranslator.services.HistoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ndanh on 9/1/2017.
 */
@Module
public class DatabaseModule {
    @Provides
    @Singleton
    HistoryDao provideHistoryDao(Context context) {
        return new HistoryDaoImpl (context);
    }
}

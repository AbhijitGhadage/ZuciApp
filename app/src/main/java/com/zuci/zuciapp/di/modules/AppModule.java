package com.zuci.zuciapp.di.modules;

import android.content.Context;

import com.zuci.zuciapp.App;
import com.zuci.zuciapp.di.utils.rx.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return new RxBus();
    }
}

package com.zuci.zuciapp.di;

import com.zuci.zuciapp.App;
import com.zuci.zuciapp.di.modules.ActivityBindingModule;
import com.zuci.zuciapp.di.modules.AppModule;
import com.zuci.zuciapp.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        /* Use AndroidInjectionModule.class if you're not using support library */
        AndroidSupportInjectionModule.class,
        AppModule.class,
        NetworkModule.class,
        ActivityBindingModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        AppComponent build();
    }

    void inject(App app);
}

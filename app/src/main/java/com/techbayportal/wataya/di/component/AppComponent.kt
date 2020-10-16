package com.techbayportal.wataya.di.component

import com.techbayportal.wataya.Application
import com.techbayportal.wataya.di.module.ActivityBuildersModule
import com.techbayportal.wataya.di.module.AppModule
import com.techbayportal.wataya.di.module.FragmentBuildersModule
import com.techbayportal.wataya.di.module.PresenterBuildersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AppModule::class,
        ActivityBuildersModule::class,
        FragmentBuildersModule::class,
        PresenterBuildersModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: Application)


}
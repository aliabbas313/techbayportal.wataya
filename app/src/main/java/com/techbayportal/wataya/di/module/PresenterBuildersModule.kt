package com.techbayportal.wataya.di.module

import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.view.splash.SplashActivityPresenter
import com.techbayportal.wataya.mvp.view.splash.SplashInterfaces
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class PresenterBuildersModule {

    @Provides
    fun provideSplashActivityPresenter(dataManager: DataManager,
                                       schedulerProvider: SchedulerProvider,
                                       disposable: CompositeDisposable): SplashActivityPresenter<SplashInterfaces.SplashView> {
        return SplashActivityPresenter(dataManager, schedulerProvider, disposable)
    }

}
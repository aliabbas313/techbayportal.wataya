package com.techbayportal.wataya.di.module

import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.view.main.MainActivityPresenter
import com.techbayportal.wataya.mvp.view.main.MainInterfaces
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

    @Provides
    fun provideMainActivityPresenter(dataManager: DataManager,
                                       schedulerProvider: SchedulerProvider,
                                       disposable: CompositeDisposable): MainActivityPresenter<MainInterfaces.MainView> {
        return MainActivityPresenter(dataManager, schedulerProvider, disposable)
    }

}
package com.techbayportal.wataya.mvp.view.splash

import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.di.module.SchedulerProvider
import com.techbayportal.wataya.mvp.view.base.BasePresenter

open class SplashActivityPresenter<V : SplashInterfaces.SplashView> @Inject
constructor(
    val mDataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    disposable: CompositeDisposable
) : BasePresenter<V>(schedulerProvider = schedulerProvider, compositeDisposable = disposable),
    SplashInterfaces.SplashPresenter<Any?> {

}

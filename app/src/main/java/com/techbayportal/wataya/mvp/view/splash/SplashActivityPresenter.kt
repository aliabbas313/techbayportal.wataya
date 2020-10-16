package com.techbayportal.wataya.mvp.view.splash

import com.techbayportal.wataya.di.module.SchedulerProvider
import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.view.base.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class SplashActivityPresenter<V : SplashInterfaces.SplashView> @Inject
constructor(
    val mDataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    disposable: CompositeDisposable
) : BasePresenter<V>(schedulerProvider = schedulerProvider, compositeDisposable = disposable),
    SplashInterfaces.SplashPresenter<Any?> {

}

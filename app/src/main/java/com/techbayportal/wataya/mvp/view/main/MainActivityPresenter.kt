package com.techbayportal.wataya.mvp.view.main

import com.techbayportal.wataya.di.module.SchedulerProvider
import com.techbayportal.wataya.helper.Constant
import com.techbayportal.wataya.helper.DeviceAvailabilityRetryWithDelay
import com.techbayportal.wataya.helper.ResponseCode
import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.view.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

open class MainActivityPresenter<V : MainInterfaces.MainView> @Inject
constructor(
    val mDataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    disposable: CompositeDisposable
) : BasePresenter<V>(schedulerProvider = schedulerProvider, compositeDisposable = disposable),
    MainInterfaces.MainPresenter<Any?> {

}

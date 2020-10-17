package com.techbayportal.wataya.mvp.view.base

import io.reactivex.disposables.CompositeDisposable
import com.techbayportal.wataya.di.module.SchedulerProvider
import com.techbayportal.wataya.mvp.view.base.BaseInterfaces.*

abstract class BasePresenter<V : BaseView> internal constructor(protected val schedulerProvider: SchedulerProvider, protected val compositeDisposable: CompositeDisposable) : BaseInterfaces.BaseMVPPresenter<V> {

    var mvpView: V? = null
        private set

    val isViewAttached: Boolean
        get() = mvpView != null

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        compositeDisposable.dispose()
        mvpView = null
    }

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.onAttach(MvpView) before" + " requesting data to the Presenter")

}

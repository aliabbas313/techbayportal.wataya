package com.techbayportal.wataya.mvp.view.splash

import com.techbayportal.wataya.mvp.view.base.BaseInterfaces

interface SplashInterfaces {

    interface SplashPresenter<T> {

    }

    interface SplashView : BaseInterfaces.BaseView {
        fun hideActionBar()
        fun delayedHide(delayMillis: Int)
    }
}
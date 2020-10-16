package com.techbayportal.wataya.mvp.view.base

interface BaseInterfaces {

    interface BaseView {
        fun showLoading()
        fun hideLoading()
    }

    interface onOptionMenuReady {
        fun menuReady()
    }
    interface BaseMVPPresenter<V : BaseView> {

        fun onAttach(mvpView: V)

        fun onDetach()

    }
}
package com.techbayportal.wataya.mvp.view.main

import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.mvp.view.base.BaseInterfaces

interface MainInterfaces {

    interface MainPresenter<T> {
    }

    interface MainView : BaseInterfaces.BaseView {
        fun showData(data: BaseModel<UserData>)
        fun showError(error: String)
    }
}
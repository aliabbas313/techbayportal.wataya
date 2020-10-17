package com.techbayportal.wataya.mvp.view.addaddress

import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.request.AddUserRequest
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.mvp.view.base.BaseInterfaces

interface AddAddressInterfaces {

    interface AddAddressPresenter<T> {
        fun getUserAddresses(device_id: Int, lang: String)
        fun addUserAddress(body: AddUserRequest)
    }

    interface AddAddressView : BaseInterfaces.BaseView {
        fun showData(data: BaseModel<UserData>)
        fun showError(error: String)
    }
}
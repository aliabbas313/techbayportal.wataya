package com.techbayportal.wataya.mvp.data

import com.techbayportal.wataya.mvp.data.local.PreferencesHelper
import com.techbayportal.wataya.mvp.data.remote.ApiService
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.request.AddUserRequest
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import io.reactivex.Observable
import javax.inject.Inject

class DataManager
@Inject
constructor(var mPreferencesHelper: PreferencesHelper, var iApiService: ApiService) {

    var data: String? = null

    fun getUserAddresses(device_id: Int, lang: String): Observable<BaseModel<UserData>> {

        return iApiService.getUserAddresses(device_id, lang)
    }

    fun addUserAddress(body: AddUserRequest): Observable<BaseModel<UserData>> {
        return iApiService.addUserAddress(body)
    }
}

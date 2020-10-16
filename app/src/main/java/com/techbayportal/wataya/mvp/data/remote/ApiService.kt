package com.techbayportal.wataya.mvp.data.remote

import com.techbayportal.wataya.BuildConfig
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import io.reactivex.Observable
import retrofit2.http.*

private const val BASE_URL = BuildConfig.URL_BASE + "public/api"

interface ApiService {

    @GET("$BASE_URL/production/v2/get-user-addresses")
    fun getUserAddresses(
        @Query("device_id") device_id: Int,
        @Query("lang") lang: String): Observable<BaseModel<UserData>>

}

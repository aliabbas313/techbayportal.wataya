package com.techbayportal.wataya.mvp.data.remote.model

import com.google.gson.annotations.SerializedName

data class BaseModel<T>(
    @SerializedName("status")
    var status: Boolean,
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: ArrayList<String>,
    @SerializedName("data")
    var data: T? = null
)
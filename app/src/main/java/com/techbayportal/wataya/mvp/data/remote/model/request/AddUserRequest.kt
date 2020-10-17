package com.techbayportal.wataya.mvp.data.remote.model.request


import com.google.gson.annotations.SerializedName

data class AddUserRequest(
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("long")
    var long: Double,
    @SerializedName("building_name")
    var buildingName: String,
    @SerializedName("apartment")
    var apartment: String,
    @SerializedName("street_address")
    var streetAddress: String,
    @SerializedName("area_id")
    var areaId: Int,
    @SerializedName("city_id")
    var cityId: Int,
    @SerializedName("device_id")
    var deviceId: String,
    @SerializedName("lang")
    var lang: String
)
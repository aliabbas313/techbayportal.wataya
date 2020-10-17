package com.techbayportal.wataya.mvp.data.remote.model.response


import com.google.gson.annotations.SerializedName

data class AddUserResponse(
    @SerializedName("address_id")
    val addressId: Int,
    @SerializedName("apartment")
    val apartment: String,
    @SerializedName("area")
    val area: String,
    @SerializedName("area_id")
    val areaId: Int,
    @SerializedName("building_name")
    val buildingName: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
    @SerializedName("street_address")
    val streetAddress: String,
    @SerializedName("user_id")
    val userId: String
)
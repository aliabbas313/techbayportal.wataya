package com.techbayportal.wataya.mvp.data.remote.model.response


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("areas_of_cities")
    val areasOfCities: List<AreasOfCity>,
    @SerializedName("cities")
    val cities: List<City>,
    @SerializedName("user_address")
    val userAddress: List<UserAddres>
) {
    data class AreasOfCity(
        @SerializedName("area_id")
        val areaId: Int,
        @SerializedName("city_id")
        val cityId: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("shipping_charges")
        val shippingCharges: Int
    )

    data class City(
        @SerializedName("city_id")
        val cityId: Int,
        @SerializedName("name")
        val name: String
    )


    data class UserAddres(
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
        val streetAddress: String
    )
}
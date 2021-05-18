package com.lukianbat.feature.city.common.data.remote.model

import com.google.gson.annotations.SerializedName

data class CitiesResponse(
    @SerializedName("cities")
    val cities: List<CityApiModel>
) {

    data class CityApiModel(
        @SerializedName("latinFullName") val latinFullName: String,
        @SerializedName("latinCity") val latinCity: String,
        @SerializedName("location") val location: LocationApiModel,
    )

    data class LocationApiModel(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lon: Double
    )
}

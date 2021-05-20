package com.lukianbat.feature.city.common.data.remote.mapper

import com.lukianbat.feature.city.common.data.remote.model.CitiesResponse
import com.lukianbat.core.common.model.CityModel

internal object ApiMapper {
    fun CitiesResponse.toDomain() = cities.map {
        CityModel(
            fullName = it.latinFullName,
            cityName = it.latinCity,
            latitude = it.location.lat,
            longitude = it.location.lon
        )
    }
}

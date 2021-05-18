package com.lukianbat.tickets.feature.loading.presentation

import com.google.android.gms.maps.model.LatLng
import com.lukianbat.core.common.ChosenCitiesModel
import com.lukianbat.core.common.CityModel

fun ChosenCitiesModel.toUi() = RouteUiModel(
    fromCity.toUi(),
    toCity.toUi()
)

private fun CityModel?.toUi(): RouteUiModel.CityUiModel {
    return RouteUiModel.CityUiModel(
        name = this?.cityName ?: "",
        latLng = LatLng(
            this?.latitude ?: 0.0,
            this?.longitude ?: 0.0
        )
    )
}

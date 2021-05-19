package com.lukianbat.tickets.feature.loading.presentation

import com.google.android.gms.maps.model.LatLng
import com.lukianbat.core.common.CityModel

fun CityModel?.toUi(): RouteUiModel.CityUiModel {
    return RouteUiModel.CityUiModel(
        name = this?.cityName?.substring(0..2) ?: "",
        latLng = LatLng(
            this?.latitude ?: 0.0,
            this?.longitude ?: 0.0
        )
    )
}

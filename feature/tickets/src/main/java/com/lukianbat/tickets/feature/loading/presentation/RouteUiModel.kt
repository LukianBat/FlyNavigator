package com.lukianbat.tickets.feature.loading.presentation

import com.google.android.gms.maps.model.LatLng

data class RouteUiModel(
    val firstPoint: CityUiModel,
    val secondPoint: CityUiModel
) {
    data class CityUiModel(
        val name: String,
        val latLng: LatLng
    )
}
package com.lukianbat.tickets.common.domain

import com.lukianbat.core.common.model.CityModel

data class RouteModel(
    val firstPoint: CityModel,
    val secondPoint: CityModel
)

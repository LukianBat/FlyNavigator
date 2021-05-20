package com.lukianbat.core.common.gateway

import com.lukianbat.core.common.model.ChosenCitiesModel
import com.lukianbat.core.common.model.CityModel
import io.reactivex.subjects.BehaviorSubject

interface ChosenCitiesGateway {
    fun getChosenCities(): BehaviorSubject<ChosenCitiesModel>

    fun setFromCity(cityModel: CityModel?)

    fun setToCity(cityModel: CityModel?)
}
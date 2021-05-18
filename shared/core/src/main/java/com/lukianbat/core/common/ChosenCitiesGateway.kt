package com.lukianbat.core.common

import io.reactivex.subjects.BehaviorSubject

interface ChosenCitiesGateway {
    fun getChosenCities(): BehaviorSubject<ChosenCitiesModel>

    fun setFromCity(cityModel: CityModel?)

    fun setToCity(cityModel: CityModel?)
}
package com.lukianbat.flynavigator.domain

import com.lukianbat.core.common.gateway.ChosenCitiesGateway
import com.lukianbat.core.common.model.ChosenCitiesModel
import com.lukianbat.core.common.model.CityModel
import io.reactivex.subjects.BehaviorSubject

class ChosenCitiesGatewayImpl : ChosenCitiesGateway {
    private val citiesBehaviorSubject = BehaviorSubject.createDefault(ChosenCitiesModel())

    override fun getChosenCities() = citiesBehaviorSubject

    override fun setFromCity(cityModel: CityModel?) {
        citiesBehaviorSubject.onNext(
            citiesBehaviorSubject.value!!.copy(fromCity = cityModel)
        )
    }

    override fun setToCity(cityModel: CityModel?) {
        citiesBehaviorSubject.onNext(
            citiesBehaviorSubject.value!!.copy(toCity = cityModel)
        )
    }
}

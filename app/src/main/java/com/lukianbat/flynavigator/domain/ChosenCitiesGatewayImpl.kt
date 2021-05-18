package com.lukianbat.flynavigator.domain

import com.lukianbat.core.common.ChosenCitiesGateway
import com.lukianbat.core.common.ChosenCitiesModel
import com.lukianbat.core.common.CityModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ChosenCitiesGatewayImpl @Inject constructor() : ChosenCitiesGateway {
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

package com.lukianbat.feature.city.common.domain.usecase

import com.lukianbat.core.common.gateway.ChosenCitiesGateway
import com.lukianbat.core.common.model.CityModel
import com.lukianbat.feature.city.common.data.remote.gateway.CitiesRemoteGateway
import com.lukianbat.feature.city.common.di.CityFlowScope
import javax.inject.Inject

@CityFlowScope
class CityInteractor @Inject constructor(
    private val remoteGateway: CitiesRemoteGateway,
    private val chosenCitiesGateway: ChosenCitiesGateway
) {

    fun searchCity(namePrefix: String) = remoteGateway.getCitiesList(namePrefix)

    fun getChosenCities() = chosenCitiesGateway.getChosenCities()

    fun setFromCity(cityModel: CityModel?) = chosenCitiesGateway.setFromCity(cityModel)

    fun setToCity(cityModel: CityModel?) = chosenCitiesGateway.setToCity(cityModel)
}

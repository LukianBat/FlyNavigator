package com.lukianbat.feature.city.common.domain.usecase

import com.lukianbat.core.common.ChosenCitiesGateway
import com.lukianbat.core.common.CityModel
import com.lukianbat.feature.city.common.data.remote.gateway.CitiesRemoteGateway
import javax.inject.Inject

class CityInteractor @Inject constructor(
    private val remoteGateway: CitiesRemoteGateway,
    private val chosenCitiesGateway: ChosenCitiesGateway
) {

    fun searchCity(namePrefix: String) = remoteGateway.getCitiesList(namePrefix)

    fun getChosenCities() = chosenCitiesGateway.getChosenCities()

    fun setFromCity(cityModel: CityModel?) = chosenCitiesGateway.setFromCity(cityModel)

    fun setToCity(cityModel: CityModel?) = chosenCitiesGateway.setToCity(cityModel)
}

package com.lukianbat.feature.city.common.data.remote.gateway

import com.lukianbat.core.common.model.CityModel
import com.lukianbat.feature.city.common.data.remote.CitiesApi
import com.lukianbat.feature.city.common.data.remote.mapper.ApiMapper.toDomain
import com.lukianbat.feature.city.common.di.CityFlowScope
import io.reactivex.Single
import javax.inject.Inject

@CityFlowScope
class CitiesRemoteGateway @Inject constructor(private val citiesApi: CitiesApi) {
    fun getCitiesList(namePrefix: String): Single<List<CityModel>> {
        return citiesApi.getCities(namePrefix)
            .map { it.toDomain() }
    }
}

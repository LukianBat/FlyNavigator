package com.lukianbat.feature.city.common.di.modules

import com.lukianbat.core.common.ChosenCitiesGateway
import com.lukianbat.feature.city.common.data.remote.gateway.CitiesRemoteGateway
import com.lukianbat.feature.city.common.domain.usecase.CityInteractor
import dagger.Module
import dagger.Provides

@Module
class CityDomainModule {

    @Provides
    fun providesCitiesUseCase(
        chosenCitiesGateway: ChosenCitiesGateway,
        remoteGateway: CitiesRemoteGateway,
    ): CityInteractor {
        return CityInteractor(remoteGateway, chosenCitiesGateway)
    }
}
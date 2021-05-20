package com.lukianbat.feature.city.common.di

import com.lukianbat.feature.city.common.di.modules.CitiesFlowModule
import com.lukianbat.feature.city.common.di.modules.NetworkModule
import com.lukianbat.feature.city.feature.CitiesFlowFragment
import com.lukianbat.feature.city.feature.city.di.ChooseCityComponent
import com.lukianbat.feature.city.feature.routes.di.RoutesComponent
import dagger.Module
import dagger.Subcomponent

@CityFlowScope
@Subcomponent(
    modules = [
        CitiesFlowSubcomponentsModule::class,
        NetworkModule::class,
        CitiesFlowModule::class
    ]
)
interface CitiesFlowComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CitiesFlowComponent
    }

    fun routesComponent(): RoutesComponent.Factory

    fun chooseCityComponent(): ChooseCityComponent.Factory

    fun inject(fragment: CitiesFlowFragment)
}

@Module(subcomponents = [ChooseCityComponent::class, RoutesComponent::class])
object CitiesFlowSubcomponentsModule
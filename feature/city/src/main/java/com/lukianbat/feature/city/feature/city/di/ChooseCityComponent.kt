package com.lukianbat.feature.city.feature.city.di

import com.lukianbat.feature.city.common.domain.model.ChooseCityType
import com.lukianbat.feature.city.feature.city.presentation.ChooseCityFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ChooseCityModule::class])
interface ChooseCityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance chooseCityType: ChooseCityType): ChooseCityComponent
    }

    fun inject(fragment: ChooseCityFragment)
}

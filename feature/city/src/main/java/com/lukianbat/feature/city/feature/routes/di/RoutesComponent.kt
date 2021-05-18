package com.lukianbat.feature.city.feature.routes.di

import com.lukianbat.feature.city.feature.routes.presentation.RoutesFragment
import dagger.Subcomponent

@Subcomponent(modules = [RoutesModule::class])
interface RoutesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RoutesComponent
    }

    fun inject(fragment: RoutesFragment)
}

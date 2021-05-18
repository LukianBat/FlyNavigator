package com.lukianbat.tickets.feature.loading.di

import com.lukianbat.tickets.feature.loading.presentation.LoadingTicketsFragment
import dagger.Subcomponent

@Subcomponent(modules = [LoadingTicketsModule::class])
interface LoadingTicketsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoadingTicketsComponent
    }

    fun inject(fragment: LoadingTicketsFragment)
}

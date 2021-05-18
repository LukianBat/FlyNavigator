package com.lukianbat.tickets.feature.tickets.di

import com.lukianbat.tickets.feature.loading.presentation.LoadingTicketsFragment
import dagger.Subcomponent

@Subcomponent(modules = [TicketsListModule::class])
interface TicketsListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TicketsListComponent
    }

    fun inject(fragment: LoadingTicketsFragment)
}

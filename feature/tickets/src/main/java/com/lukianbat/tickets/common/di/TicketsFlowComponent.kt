package com.lukianbat.tickets.common.di

import com.lukianbat.tickets.feature.TicketsFlowFragment
import com.lukianbat.tickets.feature.loading.di.LoadingTicketsComponent
import com.lukianbat.tickets.feature.tickets.di.TicketsListComponent
import dagger.Module
import dagger.Subcomponent

@TicketsFlowScope
@Subcomponent(modules = [TicketsModule::class, TicketsFlowSubcomponentsModule::class])
interface TicketsFlowComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TicketsFlowComponent
    }

    fun loadingTicketsComponent(): LoadingTicketsComponent

    fun ticketsListComponent(): TicketsListComponent

    fun inject(fragment: TicketsFlowFragment)
}

@Module(subcomponents = [LoadingTicketsComponent::class, TicketsListComponent::class])
object TicketsFlowSubcomponentsModule

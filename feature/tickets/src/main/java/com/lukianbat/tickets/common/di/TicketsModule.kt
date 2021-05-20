package com.lukianbat.tickets.common.di

import androidx.lifecycle.ViewModel
import com.lukianbat.core.di.ViewModelKey
import com.lukianbat.tickets.feature.TicketsFlowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TicketsModule {

    @Binds
    @IntoMap
    @TicketsFlowScope
    @ViewModelKey(TicketsFlowViewModel::class)
    abstract fun bindViewModel(viewModel: TicketsFlowViewModel): ViewModel
}

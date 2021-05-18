package com.lukianbat.tickets.feature.tickets.di

import androidx.lifecycle.ViewModel
import com.lukianbat.core.di.ViewModelKey
import com.lukianbat.tickets.feature.loading.presentation.LoadingTicketViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TicketsListModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoadingTicketViewModel::class)
    abstract fun bindViewModel(viewModel: LoadingTicketViewModel): ViewModel
}

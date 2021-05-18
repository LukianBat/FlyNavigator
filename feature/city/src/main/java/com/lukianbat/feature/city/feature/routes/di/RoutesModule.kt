package com.lukianbat.feature.city.feature.routes.di

import androidx.lifecycle.ViewModel
import com.lukianbat.core.di.ViewModelKey
import com.lukianbat.feature.city.feature.routes.presentation.RoutesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RoutesModule {

    @Binds
    @IntoMap
    @ViewModelKey(RoutesViewModel::class)
    abstract fun bindViewModel(viewModel: RoutesViewModel): ViewModel
}

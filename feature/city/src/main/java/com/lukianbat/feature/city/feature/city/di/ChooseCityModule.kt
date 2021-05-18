package com.lukianbat.feature.city.feature.city.di

import androidx.lifecycle.ViewModel
import com.lukianbat.core.di.ViewModelKey
import com.lukianbat.feature.city.feature.city.presentation.ChooseCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChooseCityModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChooseCityViewModel::class)
    abstract fun bindViewModel(viewModel: ChooseCityViewModel): ViewModel
}

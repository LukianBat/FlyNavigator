package com.lukianbat.feature.city.common.di.modules

import androidx.lifecycle.ViewModel
import com.lukianbat.core.di.ViewModelKey
import com.lukianbat.feature.city.common.di.CityFlowScope
import com.lukianbat.feature.city.feature.CitiesFlowViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CitiesFlowModule {

    @Binds
    @IntoMap
    @ViewModelKey(CitiesFlowViewModel::class)
    @CityFlowScope
    abstract fun bindViewModel(viewModel: CitiesFlowViewModel): ViewModel
}

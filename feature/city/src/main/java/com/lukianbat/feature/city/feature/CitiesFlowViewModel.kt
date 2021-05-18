package com.lukianbat.feature.city.feature

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import javax.inject.Inject

class CitiesFlowViewModel @Inject constructor(
    private val componentController: CitiesFlowComponentController
) : RxViewModel() {

    override fun onCleared() {
        super.onCleared()
        componentController.clearCitiesFlowComponent()
    }
}

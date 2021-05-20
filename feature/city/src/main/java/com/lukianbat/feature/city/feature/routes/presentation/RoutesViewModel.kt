package com.lukianbat.feature.city.feature.routes.presentation

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.architecture.mvvm.RxViewOutput
import com.lukianbat.core.common.model.ChosenCitiesModel
import com.lukianbat.feature.city.common.domain.model.ChooseCityType
import com.lukianbat.feature.city.common.domain.usecase.CityInteractor
import javax.inject.Inject

class RoutesViewModel @Inject constructor(
    cityInteractor: CityInteractor,
    errorAdapter: RxViewOutput.ErrorAdapter
) : RxViewModel() {

    private val chosenCities = RxViewOutput<ChosenCitiesModel>(this)
    private val onNext = RxViewOutput<Unit>(this, RxViewOutput.Strategy.ONCE)
    private val chooseCity = RxViewOutput<ChooseCityType>(this, RxViewOutput.Strategy.ONCE)

    init {
        chosenCities.source(cityInteractor.getChosenCities(), errorAdapter)
    }

    fun onNext() = onNext.asOutput()

    fun chooseCity() = chooseCity.asOutput()

    fun chosenCities() = chosenCities.asOutput()

    fun onFromCityClicked() {
        chooseCity.valueSource(ChooseCityType.CHOOSE_CITY_FROM)
    }

    fun onToCityClicked() {
        chooseCity.valueSource(ChooseCityType.CHOOSE_CITY_TO)
    }

    fun onNextClicked() {
        onNext.valueSource(Unit)
    }
}

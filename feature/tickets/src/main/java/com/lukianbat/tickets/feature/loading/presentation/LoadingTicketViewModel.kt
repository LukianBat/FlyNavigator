package com.lukianbat.tickets.feature.loading.presentation

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.architecture.mvvm.RxViewOutput
import com.lukianbat.core.common.ChosenCitiesGateway
import javax.inject.Inject

class LoadingTicketViewModel @Inject constructor(
    private val chosenCitiesGateway: ChosenCitiesGateway,
    private val errorAdapter: RxViewOutput.ErrorAdapter
) : RxViewModel() {

    private val routes = RxViewOutput<RouteUiModel>(this, RxViewOutput.Strategy.ONCE)

    fun routes() = routes.asOutput()

    fun onMapReady() {
        val sourceChosenCities = chosenCitiesGateway.getChosenCities()
            .map { it.toUi() }
        routes.source(sourceChosenCities, errorAdapter)
    }
}
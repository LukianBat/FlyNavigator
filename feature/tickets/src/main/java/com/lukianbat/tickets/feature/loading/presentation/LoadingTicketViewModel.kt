package com.lukianbat.tickets.feature.loading.presentation

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.architecture.mvvm.RxViewOutput
import com.lukianbat.core.common.ChosenCitiesGateway
import com.lukianbat.tickets.feature.loading.utils.BezierPathUtils
import javax.inject.Inject

class LoadingTicketViewModel @Inject constructor(
    private val chosenCitiesGateway: ChosenCitiesGateway,
    private val errorAdapter: RxViewOutput.ErrorAdapter
) : RxViewModel() {

    private val routes = RxViewOutput<RouteUiModel>(this, RxViewOutput.Strategy.ONCE)
    private var uiModel: RouteUiModel? = null

    fun routes() = routes.asOutput()

    fun onMapReady() {
        val sourceChosenCities = chosenCitiesGateway.getChosenCities()
            .map {
                val firstPoint = it.fromCity.toUi()
                val secondPoint = it.toCity.toUi()
                if (uiModel == null) {
                    uiModel = RouteUiModel(
                        firstPoint,
                        secondPoint,
                        BezierPathUtils.getBezierLinePoints(firstPoint.latLng, secondPoint.latLng)
                    )
                }
                requireNotNull(uiModel)
            }
        routes.source(sourceChosenCities, errorAdapter)
    }

    fun onStopAnimation(animationLastPointIndex: Int) {
        uiModel = uiModel?.copy(lastPointIndex = animationLastPointIndex)
    }
}

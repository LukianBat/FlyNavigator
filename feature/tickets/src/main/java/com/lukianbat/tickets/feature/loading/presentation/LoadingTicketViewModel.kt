package com.lukianbat.tickets.feature.loading.presentation

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.architecture.mvvm.RxViewOutput
import com.lukianbat.tickets.common.domain.ChosenRouteUseCase
import com.lukianbat.tickets.feature.loading.utils.BezierPathUtils
import javax.inject.Inject

class LoadingTicketViewModel @Inject constructor(
    private val routeUseCase: ChosenRouteUseCase,
    private val errorAdapter: RxViewOutput.ErrorAdapter
) : RxViewModel() {

    private val routes = RxViewOutput<RouteUiModel>(this, RxViewOutput.Strategy.ONCE)
    private var uiModel: RouteUiModel? = null

    fun routes() = routes.asOutput()

    fun onMapReady() {
        val sourceChosenCities = routeUseCase()
            .map {
                val firstPoint = it.firstPoint.toUi()
                val secondPoint = it.secondPoint.toUi()
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

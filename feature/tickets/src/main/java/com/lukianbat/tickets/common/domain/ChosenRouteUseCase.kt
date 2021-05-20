package com.lukianbat.tickets.common.domain

import com.lukianbat.core.common.gateway.ChosenCitiesGateway
import com.lukianbat.tickets.common.di.TicketsFlowScope
import io.reactivex.Observable
import javax.inject.Inject

@TicketsFlowScope
class ChosenRouteUseCase @Inject constructor(private val chosenCitiesGateway: ChosenCitiesGateway) {
    operator fun invoke(): Observable<RouteModel> = chosenCitiesGateway.getChosenCities()
        .map {
            RouteModel(
                it.fromCity!!,
                it.toCity!!
            )
        }
}

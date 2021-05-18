package com.lukianbat.tickets.feature

import com.lukianbat.architecture.mvvm.RxViewModel
import com.lukianbat.tickets.common.di.TicketsFlowComponentController
import javax.inject.Inject

class TicketsFlowViewModel @Inject constructor(
    private val componentController: TicketsFlowComponentController
) : RxViewModel() {

    override fun onCleared() {
        super.onCleared()
        componentController.clearTicketsFlowComponent()
    }
}

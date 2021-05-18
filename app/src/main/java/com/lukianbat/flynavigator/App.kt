package com.lukianbat.flynavigator

import android.app.Application
import com.lukianbat.core.utils.MutableLazy
import com.lukianbat.feature.city.common.di.CitiesFlowComponent
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import com.lukianbat.flynavigator.di.ApplicationComponent
import com.lukianbat.flynavigator.di.DaggerApplicationComponent
import com.lukianbat.tickets.common.di.TicketsFlowComponent
import com.lukianbat.tickets.common.di.TicketsFlowComponentController

class App : Application(), CitiesFlowComponentController, TicketsFlowComponentController {

    lateinit var appComponent: ApplicationComponent

    private val ticketsFlowComponent = MutableLazy.resettableLazy {
        appComponent.ticketsFlowComponent().create()
    }

    private val citiesFlowComponent = MutableLazy.resettableLazy {
        appComponent.citiesFlowComponent().create()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun provideCitiesFlowComponent(): CitiesFlowComponent {
        return requireNotNull(citiesFlowComponent.value)
    }

    override fun clearCitiesFlowComponent() {
        citiesFlowComponent.reset()
    }

    override fun provideTicketsFlowComponent(): TicketsFlowComponent {
        return requireNotNull(ticketsFlowComponent.value)
    }

    override fun clearTicketsFlowComponent() {
        ticketsFlowComponent.reset()
    }
}

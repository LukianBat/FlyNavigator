package com.lukianbat.flynavigator.di.module

import android.content.Context
import com.lukianbat.feature.city.common.di.CitiesFlowComponentController
import com.lukianbat.tickets.common.di.TicketsFlowComponentController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ComponentsControllerModule {

    @Singleton
    @Provides
    fun provideCitiesFlowComponentController(context: Context): CitiesFlowComponentController {
        return (context as CitiesFlowComponentController)
    }

    @Singleton
    @Provides
    fun provideFlyMapComponentController(context: Context): TicketsFlowComponentController {
        return (context as TicketsFlowComponentController)
    }
}

package com.lukianbat.flynavigator.di

import android.content.Context
import com.lukianbat.core.di.ViewModelBuilderModule
import com.lukianbat.feature.city.common.di.CitiesFlowComponent
import com.lukianbat.flynavigator.di.module.ApplicationModule
import com.lukianbat.flynavigator.di.module.ComponentsControllerModule
import com.lukianbat.tickets.common.di.TicketsFlowComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ComponentsControllerModule::class,
        ViewModelBuilderModule::class,
        SubcomponentsModule::class
    ]
)
interface ApplicationComponent {

    fun citiesFlowComponent(): CitiesFlowComponent.Factory

    fun ticketsFlowComponent(): TicketsFlowComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}

@Module(
    subcomponents = [
        CitiesFlowComponent::class,
        TicketsFlowComponent::class
    ]
)
object SubcomponentsModule
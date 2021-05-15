package com.lukianbat.flynavigator.di

import android.content.Context
import com.lukianbat.core.di.ViewModelBuilderModule
import com.lukianbat.flynavigator.di.module.ApplicationModule
import com.lukianbat.flynavigator.di.module.ComponentsControllerModule
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

//    fun startFragmentComponent(): StartFragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}

@Module(
    subcomponents = [
//        StartFragmentComponent::class
    ]
)
object SubcomponentsModule
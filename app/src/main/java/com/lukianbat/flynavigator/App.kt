package com.lukianbat.flynavigator

import android.app.Application
//import com.lukianbat.core.utils.MutableLazy
import com.lukianbat.flynavigator.di.ApplicationComponent
import com.lukianbat.flynavigator.di.DaggerApplicationComponent

class App : Application() {

    lateinit var appComponent: ApplicationComponent

//    private val startComponent = MutableLazy.resettableLazy {
//        appComponent.startFragmentComponent().create()
//    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(applicationContext)
    }

//    override fun provideStartFragmentComponent(): StartFragmentComponent {
//        return appComponent.startFragmentComponent().create()
//    }
//
//    override fun clearStartFragmentComponent() {
//        startComponent.reset()
//    }
}

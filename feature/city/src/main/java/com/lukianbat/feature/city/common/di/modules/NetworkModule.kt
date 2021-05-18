package com.lukianbat.feature.city.common.di.modules

import com.lukianbat.feature.city.common.data.remote.CitiesApi
import com.lukianbat.feature.city.common.data.remote.CitiesInterceptor
import com.lukianbat.feature.city.common.data.remote.gateway.CitiesRemoteGateway
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Named(CITIES_CLIENT)
    fun provideCitiesOkHttpClient(
        okHttpClient: OkHttpClient,
        citiesInterceptor: CitiesInterceptor
    ): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor(citiesInterceptor)
            .build()
    }

    @Provides
    @Named(CITIES_CLIENT)
    fun provideCitiesRetrofit(@Named(CITIES_CLIENT) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://yasen.hotellook.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    fun provideCitiesApi(@Named(CITIES_CLIENT) retrofit: Retrofit): CitiesApi =
        retrofit.create(CitiesApi::class.java)

    @Provides
    fun providesCitiesGateway(citiesApi: CitiesApi): CitiesRemoteGateway =
        CitiesRemoteGateway(citiesApi)

    companion object {
        private const val CITIES_CLIENT = "CITIES_CLIENT"
    }
}

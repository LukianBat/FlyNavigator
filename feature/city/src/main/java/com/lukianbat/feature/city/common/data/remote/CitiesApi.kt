package com.lukianbat.feature.city.common.data.remote

import com.lukianbat.feature.city.common.data.remote.model.CitiesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesApi {
    @GET("autocomplete")
    fun getCities(@Query("term") namePrefix: String): Single<CitiesResponse>
}

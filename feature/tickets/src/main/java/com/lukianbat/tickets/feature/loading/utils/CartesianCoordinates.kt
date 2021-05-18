package com.lukianbat.tickets.feature.loading.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CartesianCoordinates(latLng: LatLng) {

    private val lat = Math.toRadians(latLng.latitude)
    private val lon = Math.toRadians(latLng.longitude)
    val x: Double = R * cos(lat) * cos(lon)
    val y: Double = R * cos(lat) * sin(lon)
    val z: Double = R * sin(lat)

    companion object {
        private const val R = 6371 // approximate radius of earth

        fun toLatLng(x: Double, y: Double, z: Double): LatLng {
            val lat = if (z / R > 1) Math.toDegrees(asin(1.0)) else Math.toDegrees(asin(z / R))
            return LatLng(lat, Math.toDegrees(atan2(y, x)))
        }
    }
}

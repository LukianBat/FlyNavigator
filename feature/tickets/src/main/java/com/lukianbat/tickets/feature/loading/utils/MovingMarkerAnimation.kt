package com.lukianbat.tickets.feature.loading.utils

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MovingMarkerAnimation(private val marker: Marker) {

    private lateinit var prevLocation: LatLng
    private lateinit var currentLocation: LatLng

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun startAnimation(path: List<LatLng>, stepDuration: Long) {
        if (runnable != null) {
            stopAnimation()
        }
        var currentPointIndex = 0
        currentLocation = path.first()
        runnable = Runnable {
            run {
                if (currentPointIndex < path.size) {
                    prevLocation = currentLocation
                    currentLocation = path[currentPointIndex]
                    updateMarkerLocation(stepDuration)
                    runnable?.let { handler.postDelayed(it, stepDuration) }
                    ++currentPointIndex
                } else {
                    runnable?.let { handler.removeCallbacks(it) }
                    runnable = null
                    marker.isVisible = false
                }
            }
        }
        runnable?.let { handler.post(it) }
    }

    fun stopAnimation() {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = null
    }

    private fun updateMarkerLocation(stepDuration: Long) {
        val valueAnimator = markerAnimator(stepDuration)
        valueAnimator.addUpdateListener {
            val multiplier = it.animatedFraction
            val nextLocation = LatLng(
                multiplier * currentLocation.latitude + (1 - multiplier) * prevLocation.latitude,
                multiplier * currentLocation.longitude + (1 - multiplier) * prevLocation.longitude
            )
            marker.position = nextLocation
            if (prevLocation.latitude != nextLocation.latitude || prevLocation.longitude != prevLocation.longitude) {
                marker.rotation = getMarkerBearing(prevLocation, nextLocation)
            }
        }
        valueAnimator.start()
    }

    private fun markerAnimator(duration: Long): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }

    private fun getMarkerBearing(begin: LatLng, end: LatLng): Float {
        val lat1: Double = begin.latitude * PI / 180
        val long1: Double = begin.longitude * PI / 180
        val lat2: Double = end.latitude * PI / 180
        val long2: Double = end.longitude * PI / 180

        val dLon = long2 - long1

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - (sin(lat1) * cos(lat2) * cos(dLon))

        var bearing = atan2(y, x)

        bearing = Math.toDegrees(bearing)
        bearing = ((bearing + 360) % 360) - 90

        return bearing.toFloat()
    }
}

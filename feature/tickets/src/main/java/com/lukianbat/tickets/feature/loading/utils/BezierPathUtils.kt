package com.lukianbat.tickets.feature.loading.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

object BezierPathUtils {

    fun getBezierLinePoints(
        point1: LatLng,
        point2: LatLng,
    ): ArrayList<LatLng> {
        val (firstCenter, secondCenter) = midPoints(point1, point2)

        val coord1 = CartesianCoordinates(point1)
        val coord2 = CartesianCoordinates(firstCenter)
        val coord3 = CartesianCoordinates(secondCenter)
        val coord4 = CartesianCoordinates(point2)

        val list = ArrayList<LatLng>()

        //calculating points for bezier
        val tDelta = 1.0 / 500
        var t = 0.0
        while (t <= 1) {
            val oneMinusT = 1 - t

            val y = oneMinusT.pow(3) * coord1.y +
                    3 * oneMinusT.pow(2) * t * coord3.y +
                    3 * oneMinusT * t.pow(2) * coord2.y +
                    t.pow(3) * coord4.y

            val x = oneMinusT.pow(3) * coord1.x +
                    3 * oneMinusT.pow(2) * t * coord3.x +
                    3 * oneMinusT * t.pow(2) * coord2.x +
                    t.pow(3) * coord4.x

            val z = oneMinusT.pow(3) * coord1.z +
                    3 * oneMinusT.pow(2) * t * coord3.z +
                    3 * oneMinusT * t.pow(2) * coord2.z +
                    t.pow(3) * coord4.z

            val point = CartesianCoordinates.toLatLng(x, y, z)
            list.add(point)

            t += tDelta
        }
        return list
    }

    private fun midPoints(p1: LatLng, p2: LatLng): Pair<LatLng, LatLng> {

        val lat1: Double = Math.toRadians(p1.latitude)
        val lon1: Double = Math.toRadians(p1.longitude)
        val lat2: Double = Math.toRadians(p2.latitude)
        val lon2: Double = Math.toRadians(p2.longitude)

        val x1 = cos(lat1) * cos(lon1)
        val y1 = cos(lat1) * sin(lon1)
        val firstZ1 = sin(lat1)
        val secondZ1 = cos(lat1)

        val x2 = cos(lat2) * cos(lon2)
        val y2 = cos(lat2) * sin(lon2)
        val firstZ2 = sin(lat2)
        val secondZ2 = cos(lat2)

        val x = (x1 + x2) / 2
        val y = (y1 + y2) / 2

        val firstZ = (firstZ1 + firstZ2) / 2
        val secondZ = (secondZ1 + secondZ2) / 2

        val lon = atan2(y, x)
        val hyp = sqrt(x * x + y * y)

        var firstLat = atan2(.9 * firstZ, hyp)
        if (firstLat > 0) firstLat = atan2(1.1 * firstZ, hyp)

        var secondLat = atan2(.9 * secondZ, hyp)
        if (secondLat > 0) secondLat = atan2(1.1 * secondZ, hyp)

        return LatLng(Math.toDegrees(firstLat), Math.toDegrees(lon)) to
                LatLng(Math.toDegrees(secondLat), Math.toDegrees(lon))
    }
}

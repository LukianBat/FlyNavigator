package com.lukianbat.tickets.feature.loading.presentation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.lukianbat.feature.map.R
import kotlin.math.abs
import kotlin.math.atan2


class FlyAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint()
    private var path: Path = Path()

    private var inProgress = false

    private lateinit var bitmap: Bitmap
    private var bitmapOffsetX = 0F
    private var bitmapOffsetY = 0F

    private var pathMeasure: PathMeasure? = null
    private var pathLength = 0F
    private var distance = 0

    private var pos: FloatArray = FloatArray(2)
    private var tan: FloatArray = FloatArray(2)

    private var operationMatrix: Matrix = Matrix()

    init {
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(
            floatArrayOf(
                FIRST_DASH_PATH_EFFECT_INTERVAL,
                SECOND_DASH_PATH_EFFECT_INTERVAL
            ),
            DASH_PATH_EFFECT_PHASE
        )
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = ContextCompat.getColor(context, R.color.route_point_color)
        paint.strokeWidth = ROUTE_LINE_STROKE_WIDTH
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!inProgress) return
        canvas.drawPath(path, paint)
        if (distance < pathLength) {
            pathMeasure?.getPosTan(distance.toFloat(), pos, tan)

            operationMatrix.reset()

            val degrees = (atan2(tan[1].toDouble(), tan[0].toDouble()) * 180.0 / Math.PI).toFloat()
            operationMatrix.postRotate(degrees, bitmapOffsetX, bitmapOffsetY)
            operationMatrix.postTranslate(pos[0] - bitmapOffsetX, pos[1] - bitmapOffsetY)

            canvas.drawBitmap(bitmap, operationMatrix, null)
            distance++
        } else {
            distance = 0
        }
        invalidate()
    }

    fun startFlyAnimation(fromPoint: Point, toPoint: Point, @DrawableRes iconRes: Int) {
        bitmap = BitmapFactory.decodeResource(resources, iconRes)
        bitmapOffsetX = (bitmap.width / 2).toFloat()
        bitmapOffsetY = (bitmap.height / 2).toFloat()

        calculatePath(fromPoint, toPoint)

        pathMeasure = PathMeasure(path, false)
        pathLength = pathMeasure?.length ?: 0F

        distance = 0

        inProgress = true
        invalidate()
    }

    fun stopFlyAnimation() {
        inProgress = false
        invalidate()
    }

    private fun calculatePath(fromPoint: Point, toPoint: Point) {
        val (secondPoint, thirdPoint) = if (abs(fromPoint.x - toPoint.x) > abs(fromPoint.y - toPoint.y)) {
            val k = abs(fromPoint.x - toPoint.x) / 2
            Point(fromPoint.x, toPoint.y + k) to Point(toPoint.x, fromPoint.y - k)
        } else {
            val k = abs(fromPoint.y - toPoint.y) / 2
            Point(toPoint.x + k, fromPoint.y) to Point(fromPoint.x - k, toPoint.y)
        }

        path.moveTo(fromPoint.x.toFloat(), fromPoint.y.toFloat())
        path.cubicTo(
            secondPoint.x.toFloat(),
            secondPoint.y.toFloat(),
            thirdPoint.x.toFloat(),
            thirdPoint.y.toFloat(),
            toPoint.x.toFloat(),
            toPoint.y.toFloat()
        )
    }

    companion object {
        private const val ROUTE_LINE_STROKE_WIDTH = 15F
        private const val DASH_PATH_EFFECT_PHASE = 0F
        private const val FIRST_DASH_PATH_EFFECT_INTERVAL = 0F
        private const val SECOND_DASH_PATH_EFFECT_INTERVAL = 35F
    }
}

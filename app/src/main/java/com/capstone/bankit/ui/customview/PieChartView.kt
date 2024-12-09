package com.capstone.bankit.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sqrt

class PieChartView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val slices: MutableList<Slice> = ArrayList()
    private val pieBounds = RectF()
    private val tooltipPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var totalValue = 0f
    private var tooltipText: String? = null
    private var tooltipX = 0f
    private var tooltipY = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint.style = Paint.Style.FILL

        tooltipPaint.color = Color.BLACK
        tooltipPaint.style = Paint.Style.FILL
        tooltipPaint.alpha = 200

        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun setData(slices: List<Slice>) {
        this.slices.clear()
        this.slices.addAll(slices)
        totalValue = 0f
        for (slice in slices) {
            totalValue += slice.value
        }
        tooltipText = null
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (slices.isEmpty()) return

        var startAngle = 0f
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (min(centerX.toDouble(), centerY.toDouble()) * 0.8f).toFloat()
        pieBounds[centerX - radius, centerY - radius, centerX + radius] = centerY + radius

        for (slice in slices) {
            val sweepAngle = (slice.value / totalValue) * 360f

            paint.color = slice.color
            canvas.drawArc(pieBounds, startAngle, sweepAngle, true, paint)

            slice.startAngle = startAngle
            slice.sweepAngle = sweepAngle
            startAngle += sweepAngle
        }

        // Draw the tooltip if available
        if (tooltipText != null) {
            val tooltipWidth = textPaint.measureText(tooltipText) + 40
            val tooltipHeight = 80f

            val tooltipRect = RectF(
                tooltipX - tooltipWidth / 2,
                tooltipY - tooltipHeight - 20,
                tooltipX + tooltipWidth / 2,
                tooltipY - 20
            )

            canvas.drawRoundRect(tooltipRect, 20f, 20f, tooltipPaint)
            canvas.drawText(tooltipText!!, tooltipX, tooltipY - 50, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val centerX = width / 2f
        val centerY = height / 2f

        val dx = x - centerX
        val dy = y - centerY
        val distance = sqrt((dx * dx + dy * dy).toDouble()).toFloat()

        val radius = (min(centerX.toDouble(), centerY.toDouble()) * 0.8f).toFloat()

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (distance <= radius) {
                    var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                    angle = if (angle < 0) angle + 360 else angle

                    for (slice in slices) {
                        if (angle >= slice.startAngle && angle <= (slice.startAngle + slice.sweepAngle)) {
                            tooltipText =
                                slice.title + ": " + Math.round((slice.value / totalValue) * 100) + "%"
                            tooltipX = x
                            tooltipY = y
                            invalidate()
                            return true
                        }
                    }
                }
                // If touch is outside the pie chart, hide the tooltip
                tooltipText = null
                invalidate()
                return false
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Hide the tooltip when the user lifts their finger
                tooltipText = null
                invalidate()
                return false
            }

            else -> return super.onTouchEvent(event)
        }
    }

    class Slice(var title: String, var value: Float, var color: Int) {
        var startAngle: Float = 0f
        var sweepAngle: Float = 0f
    }
}
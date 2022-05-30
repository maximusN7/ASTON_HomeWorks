package com.example.aston_view_hw3

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CustomViewClock(context: Context, attributeSet: AttributeSet) : View (context, attributeSet){

    private val paint = Paint()

    var hourColor = R.color.black
    var minuteColor = R.color.red
    var secondColor = R.color.blue
    private var borderColor = Color.BLACK

    private var pointerWidth = 25.0f
    private var borderWidth = 20.0f
    var hourWidth = 20.0f
    var minuteWidth = 15.0f
    var secondWidth = 10.0f

    var hourLength = 150f
    var minuteLength = 200f
    var secondLength = 350f

    var hourModifier = 0
    var minuteModifier = 0
    var secondModifier = 0

    private var size = 360

    init {
        setupAttributes(attributeSet)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(size / 2f, size / 2f)
        drawClockFace(canvas)
        drawHourHand(canvas)
        drawMinuteHand(canvas)
        drawSecondHand(canvas)
    }

    private fun drawClockFace(canvas: Canvas) {
        val radius = size * 4f / 11f

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        canvas.drawCircle(0f, 0f, radius, paint)

        val mouthPath = Path()
        paint.strokeWidth = pointerWidth
        paint.style = Paint.Style.STROKE

        var angle = 0f
        for (i in 1..12) {
            mouthPath.moveTo(radius * cos(angle), radius * sin(angle))
            mouthPath.lineTo((radius - 50f) * cos(angle), (radius - 50f) * sin(angle))
            canvas.drawPath(mouthPath, paint)
            angle += (PI / 6 ).toFloat()
        }
    }

    private fun drawHourHand(canvas: Canvas) {
        paint.color = hourColor
        paint.strokeWidth = hourWidth
        paint.style = Paint.Style.STROKE
        val mouthPath = Path()

        mouthPath.moveTo(0f, 0f)
        mouthPath.lineTo(-(hourLength / 5) * cos(hoursToRadian()), (hourLength / 5) * sin(hoursToRadian()))
        mouthPath.lineTo(hourLength * cos(hoursToRadian()), -hourLength * sin(hoursToRadian()))
        canvas.drawPath(mouthPath, paint)

        invalidate()
    }

    private fun drawMinuteHand(canvas: Canvas) {
        paint.color = minuteColor
        paint.strokeWidth = minuteWidth
        paint.style = Paint.Style.STROKE
        val mouthPath = Path()

        mouthPath.moveTo(0f, 0f)
        mouthPath.lineTo(-(minuteLength / 5) * cos(minutesToRadian()), (minuteLength / 5) * sin(minutesToRadian()))
        mouthPath.lineTo(minuteLength * cos(minutesToRadian()), -minuteLength * sin(minutesToRadian()))
        canvas.drawPath(mouthPath, paint)

        invalidate()
    }

    private fun drawSecondHand(canvas: Canvas) {

        paint.color = secondColor
        paint.strokeWidth = secondWidth
        paint.style = Paint.Style.STROKE
        val mouthPath = Path()

        mouthPath.reset()
        mouthPath.moveTo(0f, 0f)
        mouthPath.lineTo(-(secondLength / 5) * cos(secondsToRadians()), (secondLength / 5) * sin(secondsToRadians()))
        mouthPath.lineTo(secondLength * cos(secondsToRadians()), -secondLength * sin(secondsToRadians()))
        canvas.drawPath(mouthPath, paint)

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    private fun setupAttributes(attributeSet: AttributeSet){
        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.CustomViewClock, 0, 0
        )
        secondColor = attributeArray.getColor(
            R.styleable.CustomViewClock_second_color,
            ContextCompat.getColor(context, secondColor)
        )
        secondModifier = attributeArray.getInt(R.styleable.CustomViewClock_second_modifier, secondModifier)
        secondLength = attributeArray.getFloat(R.styleable.CustomViewClock_second_length, secondLength)
        secondWidth = attributeArray.getFloat(R.styleable.CustomViewClock_second_width, secondWidth)

        minuteColor = attributeArray.getColor(
            R.styleable.CustomViewClock_minute_color,
            ContextCompat.getColor(context, minuteColor)
        )
        minuteModifier = attributeArray.getInt(R.styleable.CustomViewClock_minute_modifier, minuteModifier)
        minuteLength = attributeArray.getFloat(R.styleable.CustomViewClock_minute_length, minuteLength)
        minuteWidth = attributeArray.getFloat(R.styleable.CustomViewClock_minute_width, minuteWidth)

        hourColor = attributeArray.getColor(
            R.styleable.CustomViewClock_hour_color,
            ContextCompat.getColor(context, hourColor)
        )
        hourModifier = attributeArray.getInt(R.styleable.CustomViewClock_hour_modifier, hourModifier)
        hourLength = attributeArray.getFloat(R.styleable.CustomViewClock_hour_length, hourLength)
        hourWidth = attributeArray.getFloat(R.styleable.CustomViewClock_hour_width, hourWidth)

        attributeArray.recycle()
    }

    private fun secondsToRadians(): Float {
        return (PI / 2 - secondModifier * PI / 30).toFloat()
    }

    private fun minutesToRadian(): Float {
        return (PI / 2 - minuteModifier * PI / 30 - secondModifier * PI / 1800).toFloat()
    }

    private fun hoursToRadian(): Float {
        return (PI / 2 - hourModifier * PI / 6 - minuteModifier * PI / 360).toFloat()
    }
}
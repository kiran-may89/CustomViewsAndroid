package com.methods.customviews

import android.content.Context
import android.graphics.*
import android.icu.util.TimeUnit.values
import android.util.AttributeSet
import android.view.View
import java.lang.StrictMath.min
import java.lang.StrictMath.sin
import java.time.chrono.JapaneseEra.values
import kotlin.math.cos

private enum class Speed(val label: Int) {
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);
    fun next() = when (this) {
        OFF -> LOW
        LOW -> MEDIUM
        MEDIUM -> HIGH
        HIGH -> OFF
    }
}
private const val RADIUS_OFFSET_LABEL = 20
private const val RADIUS_OFFSET_INDICATOR = -35

class FanSpeed @JvmOverloads constructor(context: Context,attr: AttributeSet? = null,defStyleAttr: Int = 0) :   View(context,attr,defStyleAttr)  {

    private var radius = 0.0f
    private val position:PointF  = PointF(0.0f,0.0f)
    private var fanSpeed = Speed.OFF
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 20.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    private val fanSpeedLowColor:Int
    private val fanSpeedMediumColor:Int
    private val fanSpeedMaxColor:Int
    init {
        isClickable = true

        val typedArray = context.obtainStyledAttributes(attr,R.styleable.DialView)
        fanSpeedLowColor=typedArray.getColor(R.styleable.DialView_fanColor1,0)
        fanSpeedMediumColor = typedArray.getColor(R.styleable.DialView_fanColor2,0)
        fanSpeedMaxColor = typedArray.getColor(R.styleable.DialView_fanColor3,0)
        typedArray.recycle()
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w,h)/2.0*0.8).toFloat()

    }
    private fun PointF.computeXYPos(pos:Speed, radius:Float){
        var startAngle = Math.PI*(9/8.0)
        val angle  = startAngle + pos.ordinal * (Math.PI / 4)
        x  = (radius * cos(angle)).toFloat()+width/2
        y = (radius* sin(angle)).toFloat()+height/2

    }

/*    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize  = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }*/
    override fun performClick(): Boolean {

        if (super.performClick()) return true


        fanSpeed = fanSpeed.next()
        updateContentDescription()

        invalidate()
        return true
    }
    private fun updateContentDescription() {
        contentDescription = resources.getString(fanSpeed.label)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = when(fanSpeed){
            Speed.OFF -> Color.GRAY
            Speed.LOW -> fanSpeedLowColor
            Speed.MEDIUM -> fanSpeedMediumColor
            Speed.HIGH -> fanSpeedMaxColor
        }
        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(),radius,paint);
        val markerRadius = radius + RADIUS_OFFSET_INDICATOR
        position.computeXYPos(fanSpeed, markerRadius)
        paint.color = Color.BLACK
        canvas.drawCircle(position.x, position.y, radius/12, paint)

        val labelRadius = radius + RADIUS_OFFSET_LABEL
        for (i in Speed.values()) {
            position.computeXYPos(i, labelRadius)
            val label = resources.getString(i.label)
            canvas.drawText(label, position.x, position.y, paint)
        }
    }


}
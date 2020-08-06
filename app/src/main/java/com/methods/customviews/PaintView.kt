package com.methods.customviews

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat

class PaintView(ctx:Context):View(ctx) {
    private lateinit var extrBitmap:Bitmap
    private lateinit var extraCanvas:Canvas
    private val backGroundColor  = ResourcesCompat.getColor(resources,R.color.colorAccent,null)
    private val paintColor = Color.CYAN
    private val paint:Paint = Paint().apply {

     isAntiAlias = true
        color = paintColor
        isDither = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
        strokeWidth = 8.0F

    }
    private val touchTolerance = ViewConfiguration.get(ctx).scaledTouchSlop
    private val path:Path  = Path()
    private var touchx = 0f
    private  var touchY = 0f
    private  var currentX = 0f
    private  var currentY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if(::extrBitmap.isInitialized) extrBitmap.recycle()
        extrBitmap  = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extrBitmap)
        extraCanvas.drawColor(backGroundColor)
    }

    override fun onDraw(canvas: Canvas) {

       canvas.drawBitmap(extrBitmap,0.0f,0.0f,null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        touchx = event.x
        touchY = event.y
        when (event.action){
            MotionEvent.ACTION_DOWN-> touchDown()
            MotionEvent.ACTION_MOVE->touchMove()
            MotionEvent.ACTION_UP->touchUp()

        }
        return true
    }

    fun touchDown(){
        path.reset()
        path.moveTo(touchx,touchY)
        currentX = touchx
        currentY  = touchY

    }
    fun touchMove(){
        val dx = Math.abs(touchx - currentX)
        val dy = Math.abs(touchY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(currentX,currentY,(touchx+currentX)/2,(touchY+currentY)/2)
            currentX = touchx
            currentY = touchY
            extraCanvas.drawPath(path,paint)
            invalidate()
        }

    }
    fun touchUp(){
        path.reset()

    }
}
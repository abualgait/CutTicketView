package com.abualgait.msayed.cutticketview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout


class TopTicketView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.top_ticket_view_layout, this, true)
    }


    private val path = Path()

    override fun dispatchDraw(canvas: Canvas?) {
        val count = canvas!!.save()
        //arc path
        path.moveTo(0f, 0f)
        path.lineTo(0f, canvas.height.toFloat() - 50f)
        path.quadTo(
            50f,
            canvas.height.toFloat() - 50f,
            50f,
            canvas.height.toFloat()
        )
        path.lineTo(canvas.width.toFloat() - 50f, canvas.height.toFloat())
        path.quadTo(
            canvas.width.toFloat() - 50f,
            canvas.height.toFloat() - 50f,
            canvas.width.toFloat(),
            canvas.height.toFloat() - 50f
        )
        path.lineTo(canvas.width.toFloat(), 0f)
        canvas.clipPath(path, Region.Op.INTERSECT)
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(count)
    }


}
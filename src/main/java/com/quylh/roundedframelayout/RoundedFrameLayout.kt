package com.quylh.roundedframelayout

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import kotlin.math.min

/**
 * A lightweight rounded [FrameLayout] without CardView compatibility padding.
 *
 * Child views are clipped to the real bounds of this view, so their displayed
 * size is not reduced on older devices that add compatibility padding.
 */
class RoundedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var cornerRadius: Float
    private var fillColor: Int
    private var strokeColor: Int
    private var strokeWidth: Int
    private val shapeDrawable = GradientDrawable()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedFrameLayout,
            defStyleAttr,
            0
        )
        cornerRadius = typedArray.getDimension(
            R.styleable.RoundedFrameLayout_roundedCornerRadius,
            0f
        )
        fillColor = typedArray.getColor(
            R.styleable.RoundedFrameLayout_roundedBackgroundColor,
            Color.TRANSPARENT
        )
        strokeColor = typedArray.getColor(
            R.styleable.RoundedFrameLayout_roundedStrokeColor,
            Color.TRANSPARENT
        )
        strokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.RoundedFrameLayout_roundedStrokeWidth,
            0
        )
        typedArray.recycle()

        shapeDrawable.shape = GradientDrawable.RECTANGLE
        background = shapeDrawable
        updateShape()

        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (view.width <= 0 || view.height <= 0) return
                val outlineRadius = min(
                    cornerRadius,
                    min(view.width, view.height) / 2f
                )
                outline.setRoundRect(0, 0, view.width, view.height, outlineRadius)
            }
        }
    }

    fun setCornerRadius(radiusPx: Float) {
        val newRadius = radiusPx.coerceAtLeast(0f)
        if (cornerRadius == newRadius) return
        cornerRadius = newRadius
        updateShape()
        invalidateOutline()
    }

    fun setFillColor(color: Int) {
        if (fillColor == color) return
        fillColor = color
        updateShape()
    }

    fun setStrokeColor(color: Int) {
        if (strokeColor == color) return
        strokeColor = color
        updateShape()
    }

    fun setStrokeWidth(widthPx: Int) {
        val newWidth = widthPx.coerceAtLeast(0)
        if (strokeWidth == newWidth) return
        strokeWidth = newWidth
        updateShape()
    }

    fun setStroke(widthPx: Int, color: Int) {
        strokeWidth = widthPx.coerceAtLeast(0)
        strokeColor = color
        updateShape()
    }

    private fun updateShape() {
        shapeDrawable.cornerRadius = cornerRadius
        shapeDrawable.setColor(fillColor)
        shapeDrawable.setStroke(strokeWidth, strokeColor)
        clipToOutline = cornerRadius > 0f
        invalidate()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        invalidateOutline()
    }
}

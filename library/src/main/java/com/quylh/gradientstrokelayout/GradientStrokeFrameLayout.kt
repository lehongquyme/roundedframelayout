package com.quylh.gradientstrokelayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import kotlin.math.ceil
import kotlin.math.max

/**
 * A [FrameLayout] that draws a rounded gradient stroke without filling its center.
 *
 * The layout itself stays transparent. Add child views if content or an inner
 * background is needed.
 */
class GradientStrokeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class Orientation {
        LEFT_TO_RIGHT,
        TOP_TO_BOTTOM,
        DIAGONAL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val strokeBounds = RectF()
    private val contentBounds = RectF()
    private val contentPath = Path()

    private var originalPaddingLeft = paddingLeft
    private var originalPaddingTop = paddingTop
    private var originalPaddingRight = paddingRight
    private var originalPaddingBottom = paddingBottom

    private var strokeWidthPx: Float = dpToPx(DEFAULT_STROKE_WIDTH_DP)

    private var cornerRadiusPx: Float = 0f

    @ColorInt
    private var startColor: Int = DEFAULT_START_COLOR

    @ColorInt
    private var centerColor: Int = DEFAULT_CENTER_COLOR

    @ColorInt
    private var endColor: Int = DEFAULT_END_COLOR

    private var orientation: Orientation = Orientation.TOP_TO_BOTTOM

    private var shouldClipContent: Boolean = true

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.GradientStrokeFrameLayout,
            defStyleAttr,
            0
        ).apply {
            strokeWidthPx = getDimension(
                R.styleable.GradientStrokeFrameLayout_gslStrokeWidth,
                strokeWidthPx
            ).coerceAtLeast(0f)
            cornerRadiusPx = getDimension(
                R.styleable.GradientStrokeFrameLayout_gslCornerRadius,
                cornerRadiusPx
            ).coerceAtLeast(0f)
            startColor = getColor(
                R.styleable.GradientStrokeFrameLayout_gslStartColor,
                startColor
            )
            centerColor = getColor(
                R.styleable.GradientStrokeFrameLayout_gslCenterColor,
                centerColor
            )
            endColor = getColor(
                R.styleable.GradientStrokeFrameLayout_gslEndColor,
                endColor
            )
            orientation = Orientation.entries.getOrElse(
                getInt(R.styleable.GradientStrokeFrameLayout_gslOrientation, 1)
            ) { Orientation.TOP_TO_BOTTOM }
            shouldClipContent = getBoolean(
                R.styleable.GradientStrokeFrameLayout_gslClipContent,
                true
            )
            recycle()
        }

        strokePaint.strokeWidth = strokeWidthPx
        applyContentInset()
        setWillNotDraw(false)
    }

    fun setGradientStrokeWidth(width: Float) {
        strokeWidthPx = width.coerceAtLeast(0f)
        strokePaint.strokeWidth = strokeWidthPx
        applyContentInset()
        requestLayout()
        updateDrawingData()
    }

    fun setGradientCornerRadius(radius: Float) {
        cornerRadiusPx = radius.coerceAtLeast(0f)
        invalidate()
    }

    fun setGradientColors(
        @ColorInt startColor: Int,
        @ColorInt centerColor: Int,
        @ColorInt endColor: Int
    ) {
        this.startColor = startColor
        this.centerColor = centerColor
        this.endColor = endColor
        updateShader()
        invalidate()
    }

    fun setGradientOrientation(orientation: Orientation) {
        this.orientation = orientation
        updateShader()
        invalidate()
    }

    fun setClipContentToRoundedBounds(enabled: Boolean) {
        shouldClipContent = enabled
        invalidate()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateDrawingData()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (shouldClipContent && !contentBounds.isEmpty) {
            val contentRadius = (cornerRadiusPx - strokeWidthPx).coerceAtLeast(0f)
            contentPath.reset()
            contentPath.addRoundRect(
                contentBounds,
                contentRadius,
                contentRadius,
                Path.Direction.CW
            )

            val saveCount = canvas.save()
            canvas.clipPath(contentPath)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(saveCount)
        } else {
            super.dispatchDraw(canvas)
        }

        if (strokeWidthPx > 0f && !strokeBounds.isEmpty) {
            val strokeRadius =
                (cornerRadiusPx - strokeWidthPx / 2f).coerceAtLeast(0f)
            canvas.drawRoundRect(strokeBounds, strokeRadius, strokeRadius, strokePaint)
        }
    }

    private fun updateDrawingData() {
        if (width <= 0 || height <= 0) return

        updateShader()
        val halfStroke = strokeWidthPx / 2f
        strokeBounds.set(
            halfStroke,
            halfStroke,
            (width - halfStroke).coerceAtLeast(halfStroke),
            (height - halfStroke).coerceAtLeast(halfStroke)
        )
        contentBounds.set(
            strokeWidthPx,
            strokeWidthPx,
            (width - strokeWidthPx).coerceAtLeast(strokeWidthPx),
            (height - strokeWidthPx).coerceAtLeast(strokeWidthPx)
        )
        invalidate()
    }

    private fun updateShader() {
        if (width <= 0 || height <= 0) return

        val (endX, endY) = when (orientation) {
            Orientation.LEFT_TO_RIGHT -> width.toFloat() to 0f
            Orientation.TOP_TO_BOTTOM -> 0f to height.toFloat()
            Orientation.DIAGONAL -> width.toFloat() to height.toFloat()
        }
        strokePaint.shader = LinearGradient(
            0f,
            0f,
            endX,
            endY,
            intArrayOf(startColor, centerColor, endColor),
            null,
            Shader.TileMode.CLAMP
        )
    }

    private fun applyContentInset() {
        val inset = ceil(strokeWidthPx).toInt()
        super.setPadding(
            max(originalPaddingLeft, inset),
            max(originalPaddingTop, inset),
            max(originalPaddingRight, inset),
            max(originalPaddingBottom, inset)
        )
    }

    private fun dpToPx(dp: Float): Float = dp * resources.displayMetrics.density

    private companion object {
        const val DEFAULT_STROKE_WIDTH_DP = 1f
        val DEFAULT_START_COLOR = Color.parseColor("#FABB73")
        val DEFAULT_CENTER_COLOR = Color.parseColor("#FFA750")
        val DEFAULT_END_COLOR = Color.parseColor("#D27433")
    }
}

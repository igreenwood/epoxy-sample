package com.isseiaoki.epoxy.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.isseiaoki.epoxy.R

/**
 * https://github.com/JakeWharton/u2020/blob/master/src/main/java/com/jakewharton/u2020/ui/misc/AspectRatioImageView.java
 */
open class AspectRatioImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : android.support.v7.widget.AppCompatImageView(context, attrs, defStyleAttr) {

  private val widthRatio: Int
  private val heightRatio: Int

  init {
    val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
    widthRatio = a.getInteger(R.styleable.AspectRatioImageView_ariv_width_ratio, 1)
    heightRatio = a.getInteger(R.styleable.AspectRatioImageView_ariv_height_ratio, 1)
    a.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var widthSpec = widthMeasureSpec
    var heightSpec = heightMeasureSpec
    val widthMode = View.MeasureSpec.getMode(widthSpec)
    var widthSize = View.MeasureSpec.getSize(widthSpec)
    val heightMode = View.MeasureSpec.getMode(heightSpec)
    var heightSize = View.MeasureSpec.getSize(heightSpec)

    if (widthMode == MeasureSpec.EXACTLY) {
      if (heightMode != MeasureSpec.EXACTLY) {
        heightSize = (widthSize * 1f / widthRatio * heightRatio).toInt()
      }
    } else if (heightMode == MeasureSpec.EXACTLY) {
      widthSize = (heightSize * 1f / heightRatio * widthRatio).toInt()
    } else {
      throw IllegalStateException("Either width or height must be EXACTLY.")
    }

    widthSpec = View.MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
    heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)

    super.onMeasure(widthSpec, heightSpec)
  }
}
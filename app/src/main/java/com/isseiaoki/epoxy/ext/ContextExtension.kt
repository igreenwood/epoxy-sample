package com.isseiaoki.epoxy.ext

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun Context.getDisplayMetrics(): DisplayMetrics {
  val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
  return DisplayMetrics().apply {
    wm.defaultDisplay.getRealMetrics(this)
  }
}

fun Context.density() = getDisplayMetrics().density

fun Context.dpToPx(dp: Int) = Math.round(dp.toFloat() * density())
package com.isseiaoki.epoxy.ext

import android.content.ContentResolver
import android.content.res.Resources

fun Resources.getUrlFromDrawableResId(drawableResId: Int): String {
  return buildString {
    val resources = this@getUrlFromDrawableResId

    append(ContentResolver.SCHEME_ANDROID_RESOURCE)
    append("://")
    append(resources.getResourcePackageName(drawableResId))
    append("/")
    append(resources.getResourceTypeName(drawableResId))
    append("/")
    append(resources.getResourceEntryName(drawableResId))
  }
}
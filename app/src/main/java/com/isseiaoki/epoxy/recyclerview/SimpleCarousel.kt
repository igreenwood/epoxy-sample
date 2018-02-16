package com.isseiaoki.epoxy.recyclerview

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView

@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SimpleCarousel(context: Context) : Carousel(context) {

  init {
    clipToPadding = false
    setPadding(0, 0, 0, 0)
    setItemSpacingDp(0)
  }

  override fun getDefaultSpacingBetweenItemsDp(): Int = 0

}
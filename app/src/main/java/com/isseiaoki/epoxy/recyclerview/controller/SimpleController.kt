package com.isseiaoki.epoxy.recyclerview.controller

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.isseiaoki.epoxy.ext.dpToPx
import com.isseiaoki.epoxy.recyclerview.SimpleItem
import com.isseiaoki.epoxy.recyclerview.model.BannerModel_
import com.isseiaoki.epoxy.recyclerview.model.EmptyTextModel_
import com.isseiaoki.epoxy.recyclerview.model.ItemModel_
import com.isseiaoki.epoxy.recyclerview.model.LoadingFooterModel_
import timber.log.Timber

class SimpleController(
    var context: Context,
    private var onBannerClicked: (SimpleItem) -> Unit = {},
    private var onItemClicked: (SimpleItem) -> Unit = {},
    filterDuplicates: Boolean = true
) : EpoxyController() {

  init {
    setFilterDuplicates(filterDuplicates)
  }

  private var banners = mutableListOf<SimpleItem>()
  private var items = mutableListOf<SimpleItem>()
  private var showFooter = false

  override fun buildModels() {
    // empty view
    if (banners.isEmpty() || items.isEmpty()) {
      EmptyTextModel_()
          .id("empty")
          .spanSizeOverride { _, _, _ -> 2 }
          .emptyText("no item")
          .addTo(this)
      return
    }
    // carousel header
    if (banners.isNotEmpty()) {
      val spacing = context.dpToPx(8)
      CarouselModel_()
          .padding(Carousel.Padding(spacing, 0, 0, 0, spacing))
          .id("carousel")
          .spanSizeOverride { _, _, _ -> 2 }
          .models(
              banners.map {
                BannerModel_()
                    .id(it.id)
                    .banner(it)
                    .onBannerClicked { onBannerClicked(it) }
              }
          )
          .addTo(this)
    }
    // items
    items.forEach {
      ItemModel_()
          .id(it.id)
          .item(it)
          .onItemClicked(onItemClicked)
          .spanSizeOverride { _, _, _ -> 1 }
          .addTo(this)
    }
    // footer
    LoadingFooterModel_()
        .id("footer")
        .spanSizeOverride { _, _, _ -> 2 }
        .addIf(showFooter, this)
  }

  fun update(banners: List<SimpleItem>, items: List<SimpleItem>) {
    Timber.e("update")
    this.banners.clear()
    this.banners.addAll(banners)
    this.items.clear()
    this.items.addAll(items)
    showFooter = false
    requestModelBuild()
  }

  fun addAll(items: List<SimpleItem>) {
    this.items.addAll(items)
    showFooter = false
    requestModelBuild()
  }

  fun showFooter() {
    showFooter = true
    requestModelBuild()
  }

  fun showEmpty() {
    requestModelBuild()
  }
}
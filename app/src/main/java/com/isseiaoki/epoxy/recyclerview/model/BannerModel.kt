package com.isseiaoki.epoxy.recyclerview.model

import android.content.Context
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.isseiaoki.epoxy.R
import com.isseiaoki.epoxy.databinding.ItemBannerBinding
import com.isseiaoki.epoxy.recyclerview.SimpleItem
import com.squareup.picasso.Picasso

/**
 * model for banner
 */
@EpoxyModelClass(layout = R.layout.item_banner)
abstract class BannerModel : DataBindingModel<ItemBannerBinding>() {

  @EpoxyAttribute lateinit var banner: SimpleItem
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) lateinit var onBannerClicked: (SimpleItem) -> Unit

  override fun bind(binding: ItemBannerBinding, context: Context) {
    Picasso.with(context).load(banner.imageUrl).fit().into(binding.bannerImage)
    binding.root.setOnClickListener { onBannerClicked(banner) }
  }

  override fun unbind(binding: ItemBannerBinding) {
    binding.bannerImage.setImageDrawable(null)
    binding.root.setOnClickListener(null)
  }

}
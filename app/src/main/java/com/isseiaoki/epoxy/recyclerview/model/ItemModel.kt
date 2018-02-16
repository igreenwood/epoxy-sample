package com.isseiaoki.epoxy.recyclerview.model

import android.content.Context
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.isseiaoki.epoxy.R
import com.isseiaoki.epoxy.databinding.ItemImageBinding
import com.isseiaoki.epoxy.recyclerview.SimpleItem
import com.squareup.picasso.Picasso

/**
 * model for item
 */
@EpoxyModelClass(layout = R.layout.item_image)
abstract class ItemModel : DataBindingModel<ItemImageBinding>() {

  @EpoxyAttribute lateinit var item: SimpleItem
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) lateinit var onItemClicked: (SimpleItem) -> Unit

  override fun bind(binding: ItemImageBinding, context: Context) {
    Picasso.with(context).load(item.imageUrl).into(binding.itemImage)
    binding.itemTextView.text = "cat ${item.id + 1}"
    binding.root.setOnClickListener { onItemClicked(item) }
  }

  override fun unbind(binding: ItemImageBinding) {
    binding.itemImage.setImageDrawable(null)
    binding.root.setOnClickListener(null)
  }

}
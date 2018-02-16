package com.isseiaoki.epoxy.recyclerview.model

import android.content.Context
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.isseiaoki.epoxy.R
import com.isseiaoki.epoxy.databinding.ItemEmptyTextBinding

/**
 * model for empty view
 */
@EpoxyModelClass(layout = R.layout.item_empty_text)
abstract class EmptyTextModel : DataBindingModel<ItemEmptyTextBinding>() {

  @EpoxyAttribute lateinit var emptyText: String

  override fun bind(binding: ItemEmptyTextBinding, context: Context) {
    if (emptyText.isNotEmpty()) {
      binding.emptyTextView.text = emptyText
    }
  }

  override fun unbind(binding: ItemEmptyTextBinding) {
    // do nothing
  }

}
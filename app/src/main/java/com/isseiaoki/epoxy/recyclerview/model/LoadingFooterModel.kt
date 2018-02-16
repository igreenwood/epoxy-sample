package com.isseiaoki.epoxy.recyclerview.model

import android.content.Context
import com.airbnb.epoxy.EpoxyModelClass
import com.isseiaoki.epoxy.R
import com.isseiaoki.epoxy.databinding.ItemLoadingFooterBinding

/**
 * model for footer
 */
@EpoxyModelClass(layout = R.layout.item_loading_footer)
abstract class LoadingFooterModel : DataBindingModel<ItemLoadingFooterBinding>() {

  override fun bind(binding: ItemLoadingFooterBinding, context: Context) {
    // do nothing
  }

  override fun unbind(binding: ItemLoadingFooterBinding) {
    // do nothing
  }

}
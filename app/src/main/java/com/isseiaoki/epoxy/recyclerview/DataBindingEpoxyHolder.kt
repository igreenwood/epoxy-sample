package com.isseiaoki.epoxy.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.CallSuper
import android.view.View
import com.airbnb.epoxy.EpoxyHolder

class DataBindingEpoxyHolder : EpoxyHolder() {

  @CallSuper
  override fun bindView(itemView: View) {
    binding = DataBindingUtil.bind(itemView)
  }

  lateinit var binding: ViewDataBinding

}
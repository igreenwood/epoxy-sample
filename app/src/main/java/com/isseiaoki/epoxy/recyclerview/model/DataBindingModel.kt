package com.isseiaoki.epoxy.recyclerview.model

import android.content.Context
import android.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.isseiaoki.epoxy.recyclerview.DataBindingEpoxyHolder

abstract class DataBindingModel<in T : ViewDataBinding> : EpoxyModelWithHolder<DataBindingEpoxyHolder>() {

  abstract fun bind(binding: T, context: Context)

  abstract fun unbind(binding: T)

  @Suppress("UNCHECKED_CAST")
  override fun bind(holder: DataBindingEpoxyHolder) {
    val binding = holder.binding as? T ?: return
    val context = binding.root.context
    bind(binding, context)
  }

  @Suppress("UNCHECKED_CAST")
  override fun unbind(holder: DataBindingEpoxyHolder) {
    val binding = holder.binding as? T ?: return
    unbind(binding)
  }

}
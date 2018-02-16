package com.isseiaoki.epoxy.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.util.concurrent.atomic.AtomicBoolean

abstract class OnLoadMoreListener(private val layoutManager: LinearLayoutManager)
  : RecyclerView.OnScrollListener() {

  companion object {
    private val LOAD_MINIMUM_MILLIS = 1000L
  }

  private var isLoading = AtomicBoolean(false)
  private var previousTotal = 0
  private var loadStartMillis = 0L

  override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    if (dy <= 0 || layoutManager.itemCount == 0) return

    recyclerView?.layoutManager?.let {
      val firstVisibleItem = (it as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: return
      val visibleItemCount = recyclerView.childCount
      val totalItemCount = layoutManager.itemCount
      val now = System.currentTimeMillis()

      if (now - loadStartMillis < LOAD_MINIMUM_MILLIS) return

      if (isLoading.get()) {
        if (totalItemCount > previousTotal || totalItemCount == 0) {
          isLoading.set(false)
          previousTotal = totalItemCount
        }
      }

      if (!isLoading.get() && totalItemCount <= firstVisibleItem + visibleItemCount) {
        recyclerView.post { onLoadMore() }
        loadStartMillis = System.currentTimeMillis()
        isLoading.set(true)
      }
    }
  }

  abstract fun onLoadMore()

}
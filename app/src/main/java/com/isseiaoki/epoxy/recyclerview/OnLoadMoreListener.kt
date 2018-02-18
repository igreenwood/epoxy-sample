package com.isseiaoki.epoxy.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class OnLoadMoreListener(private val layoutManager: LinearLayoutManager)
  : RecyclerView.OnScrollListener() {

  abstract fun onLoadMore()

  private var isLoading = false
  private var previousTotal = 0
  private var loadStartMillis = 0L

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    if (dy <= 0 || layoutManager.itemCount == 0) return

    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
    val visibleItemCount = recyclerView.childCount
    val totalItemCount = layoutManager.itemCount
    val now = System.currentTimeMillis()

    if (now - loadStartMillis < LOAD_MINIMUM_MILLIS) return

    if (isLoading) {
      if (totalItemCount > previousTotal || totalItemCount == 0) {
        isLoading = false
        previousTotal = totalItemCount
      }
    }

    if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem) {
      recyclerView.post { onLoadMore() }
      loadStartMillis = System.currentTimeMillis()
      isLoading = true
    }
  }

  companion object {
    private val LOAD_MINIMUM_MILLIS = 1000L
  }

}
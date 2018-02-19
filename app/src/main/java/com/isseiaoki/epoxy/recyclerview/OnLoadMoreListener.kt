package co.monomy.presentation.view.component.recyclerview

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class OnLoadMoreListener(private val layoutManager: LinearLayoutManager)
  : RecyclerView.OnScrollListener() {

  companion object {
    private val LOAD_MINIMUM_MILLIS = 500L
  }

  private var loadStartMillis = 0L

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
    val visibleItemCount = recyclerView.childCount
    val allItem = layoutManager.itemCount
    if (allItem <= firstVisibleItem + visibleItemCount) {
      val now = System.currentTimeMillis()
      if (now - loadStartMillis < LOAD_MINIMUM_MILLIS) return
      loadStartMillis = now
      onLoadMore()
    }
  }

  abstract fun onLoadMore()
}
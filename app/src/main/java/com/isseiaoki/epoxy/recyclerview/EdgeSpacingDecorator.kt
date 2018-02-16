package com.isseiaoki.epoxy.recyclerview

import android.graphics.Rect
import android.support.annotation.Px
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.LayoutManager
import android.support.v7.widget.RecyclerView.State
import android.view.View

/**
 * Custom spacing decoration
 */
class EdgeSpacingDecorator @JvmOverloads constructor(
    @Px private var spacingPx: Int = 0,
    private var ignoreHeader: Boolean = false) : ItemDecoration() {
  private var verticallyScrolling: Boolean = false
  private var horizontallyScrolling: Boolean = false
  private var firstItem: Boolean = false
  private var lastItem: Boolean = false
  private var grid: Boolean = false

  private var isFirstItemInRow: Boolean = false
  private var fillsLastSpan: Boolean = false
  private var isInFirstRow: Boolean = false
  private var isInLastRow: Boolean = false

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State?) {
    // Zero everything out for the common case
    outRect.setEmpty()

    val position = parent.getChildAdapterPosition(view)
    if (position == RecyclerView.NO_POSITION) {
      // View is not shown
      return
    }

    if (ignoreHeader && position == 0) {
      // ignore header
      return
    }

    val layout = parent.layoutManager
    calculatePositionDetails(parent, position, layout)

    var left = useLeftPadding()
    var right = useRightPadding()
    var top = useTopPadding()
    var bottom = useBottomPadding()

    if (shouldReverseLayout(layout, horizontallyScrolling)) {
      if (horizontallyScrolling) {
        val temp = left
        left = right
        right = temp
      } else {
        val temp = top
        top = bottom
        bottom = temp
      }
    }

    // Divided by two because it is applied to the left side of one item and the right of another
    // to add up to the total desired space
    val padding = spacingPx
    outRect.right = if (right) padding else 0
    outRect.left = if (left) padding else 0
    outRect.top = if (top) padding else 0
    outRect.bottom = if (bottom) padding else 0
  }

  private fun calculatePositionDetails(parent: RecyclerView, position: Int, layout: LayoutManager) {
    val itemCount = parent.adapter.itemCount
    firstItem = position == 0
    lastItem = position == itemCount - 1
    horizontallyScrolling = layout.canScrollHorizontally()
    verticallyScrolling = layout.canScrollVertically()
    grid = layout is GridLayoutManager

    if (grid) {
      val grid = layout as GridLayoutManager
      val spanSizeLookup = grid.spanSizeLookup
      val spanSize = spanSizeLookup.getSpanSize(position)
      val spanCount = grid.spanCount
      val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
      isFirstItemInRow = spanIndex == 0
      fillsLastSpan = spanIndex + spanSize == spanCount
      isInFirstRow = isInFirstRow(position, spanSizeLookup, spanCount)
      isInLastRow = !isInFirstRow && isInLastRow(position, itemCount, spanSizeLookup, spanCount)
    }
  }

  private fun shouldReverseLayout(layout: LayoutManager, horizontallyScrolling: Boolean): Boolean {
    var reverseLayout = layout is LinearLayoutManager && layout.reverseLayout
    val rtl = layout.layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
    if (horizontallyScrolling && rtl) {
      // This is how linearlayout checks if it should reverse layout in #resolveShouldLayoutReverse
      reverseLayout = !reverseLayout
    }

    return reverseLayout
  }

  private fun useBottomPadding(): Boolean {
    return if (grid) {
      horizontallyScrolling && fillsLastSpan
    } else verticallyScrolling && !lastItem

  }

  private fun useTopPadding(): Boolean {
    return if (grid) {
      horizontallyScrolling || verticallyScrolling && !isInFirstRow
    } else verticallyScrolling && !firstItem

  }

  private fun useRightPadding(): Boolean {
    return if (grid) {
      verticallyScrolling && fillsLastSpan
    } else horizontallyScrolling && !lastItem

  }

  private fun useLeftPadding(): Boolean {
    return if (grid) {
      horizontallyScrolling && !isInFirstRow || verticallyScrolling
    } else horizontallyScrolling && !firstItem

  }

  private fun isInFirstRow(position: Int, spanSizeLookup: SpanSizeLookup, spanCount: Int): Boolean {
    var totalSpan = 0
    for (i in 0..position) {
      totalSpan += spanSizeLookup.getSpanSize(i)
      if (totalSpan > spanCount) {
        return false
      }
    }

    return true
  }

  private fun isInLastRow(position: Int, itemCount: Int, spanSizeLookup: SpanSizeLookup,
      spanCount: Int): Boolean {
    var totalSpan = 0
    for (i in itemCount - 1 downTo position) {
      totalSpan += spanSizeLookup.getSpanSize(i)
      if (totalSpan > spanCount) {
        return false
      }
    }

    return true
  }
}

package com.algostack.nir.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlinx.coroutines.NonDisposableHandle.parent

class HorizontalSpace : ItemDecoration() {



    private val size = 16
    private val edgeEnabled = true
    private val NO_SPACING = 20
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // LayoutManager properties
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val orientation = layoutManager.orientation



        // Basic item positioning
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        val isLastPosition = position == (itemCount - 1)
        val isFirstPosition = position == 0

        // Saved size
        val sizeBasedOnEdge = if (edgeEnabled) size else NO_SPACING
        val sizeBasedOnFirstPosition = if (isFirstPosition) sizeBasedOnEdge else size
        val sizeBasedOnLastPosition = if (isLastPosition) sizeBasedOnEdge else NO_SPACING // NO_SPACING means zero

        // Update properties
        when (orientation) {
            RecyclerView.HORIZONTAL -> {
                with(outRect) {
                    left = sizeBasedOnFirstPosition
                    top = sizeBasedOnEdge
                    right = sizeBasedOnLastPosition
                    bottom = sizeBasedOnEdge
                }
            }
            RecyclerView.VERTICAL -> {
                with(outRect) {
                    left = sizeBasedOnEdge
                    top = sizeBasedOnFirstPosition
                    right = sizeBasedOnEdge
                    bottom = sizeBasedOnLastPosition
                }
            }
        }
    }


}
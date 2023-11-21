package com.algostack.nir.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpace : ItemDecoration() {

    // Define constants for spacing
    private val size = 16
    private val edgeEnabled = true
    private val NO_SPACING = 20
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
        with(outRect) {
            left = sizeBasedOnEdge
            top = sizeBasedOnFirstPosition
            right = sizeBasedOnEdge
            bottom = sizeBasedOnLastPosition
        }
    }
}